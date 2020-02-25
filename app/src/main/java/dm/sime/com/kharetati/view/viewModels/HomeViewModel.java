package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.DynamicLayout;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.AppSessionResponse;
import dm.sime.com.kharetati.datas.models.Applications;
import dm.sime.com.kharetati.datas.models.GetAppResponse;
import dm.sime.com.kharetati.datas.models.GetAreaNamesResponse;
import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.InAppNotifications;
import dm.sime.com.kharetati.datas.models.InAppNotificationsModel;
import dm.sime.com.kharetati.datas.models.LookupInput;
import dm.sime.com.kharetati.datas.models.LookupParameterModel;
import dm.sime.com.kharetati.datas.models.LookupResponseModel;
import dm.sime.com.kharetati.datas.models.MakaniToDLTMResponse;
import dm.sime.com.kharetati.datas.models.ParcelResponse;
import dm.sime.com.kharetati.datas.models.Parcels;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.models.SearchForm;
import dm.sime.com.kharetati.datas.models.SearchParameterInput;
import dm.sime.com.kharetati.datas.models.SearchParameterModel;
import dm.sime.com.kharetati.datas.models.SearchResult;
import dm.sime.com.kharetati.datas.models.SerializeGetAppInputRequestModel;
import dm.sime.com.kharetati.datas.models.SerializeGetAppRequestModel;
import dm.sime.com.kharetati.datas.models.Tabs;
import dm.sime.com.kharetati.datas.models.WebSearchResult;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.MyApiService;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.HomeRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.adapters.InAppNotificationAdapter;
import dm.sime.com.kharetati.view.customview.CleanableEditText;
import dm.sime.com.kharetati.view.fragments.DeliveryFragment;
import dm.sime.com.kharetati.view.fragments.HomeFragment;
import dm.sime.com.kharetati.view.navigators.FragmentNavigator;
import dm.sime.com.kharetati.view.navigators.HomeNavigator;
import dm.sime.com.kharetati.view.navigators.MainNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class HomeViewModel extends ViewModel {

    public boolean isExists =false;
    Activity activity;

    InAppNotificationAdapter adapterNotification;
    InAppNotificationsModel model;
    FragmentNavigator navigator;
    public HomeNavigator homeNavigator;
    private Applications selectedApplication;
    private Tabs selectedTab;

    MutableLiveData<List<InAppNotifications>> mutableInAppNotifications = new MutableLiveData<>();
    MutableLiveData<List<Applications>> mutableHomeGridMenu = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    int lastIndex = 0;
    private MyApiService apiService;
    private HomeRepository repository;
    private KharetatiApp kharetatiApp;
    private Parcels[] lstParcels;

    /*public HomeViewModel(){

    }*/
    public HomeViewModel(Activity context, HomeRepository repository){
        this.activity = context;
        this.repository = repository;

        kharetatiApp = KharetatiApp.create(activity);
    }

    public void initializeHomeVM(Context context){
        model = new InAppNotificationsModel();
        mutableInAppNotifications = new MutableLiveData<>();
        mutableInAppNotifications.setValue(model.getLstInAppNotifications());
        adapterNotification = new InAppNotificationAdapter(R.layout.adapter_in_app_notifications, this, context);

        getSession();
    }

    public void manageAppBar(Context ctx, boolean status){
        navigator = (FragmentNavigator) ctx;
        navigator.manageActionBar(status);
    }

    public void manageAppBottomBAtr(Context ctx, boolean status){
        navigator = (FragmentNavigator) ctx;
        navigator.manageBottomBar(status);
    }

    public void navigate(Context ctx, String fragmentTag){
        lastIndex = 0;
        navigator = (FragmentNavigator) ctx;
        navigator.fragmentNavigator(fragmentTag, true, null);
    }

    public void navigateWithParam(Context ctx, String fragmentTag, List<Object> param){
        lastIndex = 0;
        navigator = (FragmentNavigator) ctx;
        navigator.fragmentNavigator(fragmentTag, true, param);
    }

    /// Home Grid Menu///

    public void initializeHomeGridMenu(Context context, int position){
        //mutableHomeGridMenuItems.setValue(mutableHomeGridMenu.getValue());
    }

    public int getGridPagerSize(){
        if(mutableHomeGridMenu.getValue().size() < 7){
            return 1;
        } else {
            return ((mutableHomeGridMenu.getValue().size() / 6) + 1);
        }
    }

    public MutableLiveData<List<Applications>> getMutableHomeGridMenu(){
        return mutableHomeGridMenu;
    }

    public List<Applications> getListHomeGridMenuItems(int position) {
        List<Applications> temp;
        int x = mutableHomeGridMenu.getValue().subList(lastIndex, mutableHomeGridMenu.getValue().size()).size();



        if(x > 6){
            temp = mutableHomeGridMenu.getValue().subList(lastIndex, lastIndex + 6);
        } else {
            temp = mutableHomeGridMenu.getValue().subList(lastIndex, mutableHomeGridMenu.getValue().size());

        }
        //lastIndex = lastIndex + 6;


        return temp;
    }

    public Applications getCurrentHomeGridMenuItems(int position){
        if (mutableHomeGridMenu.getValue() != null ) {
            return mutableHomeGridMenu.getValue().get(position);
        }
        return null;
    }

    public Applications getApplication(String appID){
        for (Applications app: mutableHomeGridMenu.getValue()) {
            if(app.getId().equals(appID)){
                return app;
            }
        }
        return null;
    }

    public List<SearchForm> getSearchForm(String appID){
        for (Applications app: mutableHomeGridMenu.getValue()) {
            if(app.getId().equals(appID)){
                return app.getSearchForm();
            }
        }
        return null;
    }

    /// Home Grid Menu End...///

    /// Notifications///
    public MutableLiveData<List<InAppNotifications>> getMutableInAppNotifications(){
        return mutableInAppNotifications;
    }

    public List<InAppNotifications> getInAppNotifications(){
        return mutableInAppNotifications.getValue();
    }

    public InAppNotifications getCurrentInAppNotifications(int  position){
        if (mutableInAppNotifications.getValue() != null ) {
            return mutableInAppNotifications.getValue().get(position);
        }
        return null;
    }

    public void  setInAppNotificationsAdapter(List<InAppNotifications> lstInAppNotifications) {
        this.adapterNotification.setInAppNotifications(lstInAppNotifications);
        this.adapterNotification.notifyDataSetChanged();
    }

    public InAppNotificationAdapter getInAppNotificationAdapter() {
        return adapterNotification;
    }
    /// Notifications End...///



    public void getMakaniToDLTM(String makani){

        homeNavigator.onStarted();

        //Global.makani ="3003295320";

        HTTPRequestBody.MakaniBody makaniBody = new HTTPRequestBody.MakaniBody();
        makaniBody.MAKANI =makani;

        Disposable disposable = repository.getMakaniToDLTM(makaniBody)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MakaniToDLTMResponse>() {
                    @Override public void accept(MakaniToDLTMResponse makaniToDLTMResponse) throws Exception {
                        gotoMakani(makaniToDLTMResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        homeNavigator.onFailure(throwable.getMessage());
                    }
                });

        compositeDisposable.add(disposable);

    }

    private void gotoMakani(MakaniToDLTMResponse makaniToDLTMResponse) {

        //Global.makani = getMakaniNumber();

        if (!Global.isConnected(activity)) {

            if(Global.appMsg!=null)
                AlertDialogUtil.errorAlertDialog(activity.getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , activity.getResources().getString(R.string.ok), activity);
            else
                AlertDialogUtil.errorAlertDialog(activity.getResources().getString(R.string.lbl_warning), activity.getResources().getString(R.string.internet_connection_problem1), activity.getResources().getString(R.string.ok), activity);
            return;
        }
        else{

            if(makaniToDLTMResponse!=null){

                if (makaniToDLTMResponse != null && !Boolean.valueOf(makaniToDLTMResponse.getIs_exception())) {
                    Global.dltm = makaniToDLTMResponse.getDLTMContainer().getDLTM();
                    String parcelId = makaniToDLTMResponse.getDLTMContainer().getPARCEL_ID();
                    PlotDetails.parcelNo=parcelId;


                    if(Global.isValidTrimedString(Global.dltm)){
                        Global.landNumber = null;
                        Global.area = null;
                        Global.area_ar = null;
                        Global.isMakani =true;
                        //homeNavigator.onSuccess();
                        if(DeliveryFragment.isDeliveryFragment)
                            return;
                        else
                            navigate(activity, FragmentTAGS.FR_MAP);

                    } else {
                        homeNavigator.onFailure(Global.CURRENT_LOCALE.equals("en")? Global.appMsg.getInvalidmakaniEn():Global.appMsg.getInvalidmakaniAr());

                    }
                }
                else
                    homeNavigator.onFailure(Global.CURRENT_LOCALE.equals("en")? Global.appMsg.getInvalidmakaniEn():Global.appMsg.getInvalidmakaniAr());

            }
            else
                homeNavigator.onFailure(Global.CURRENT_LOCALE.equals("en")? Global.appMsg.getErrorFetchingDataEn():Global.appMsg.getErrorFetchingDataAr());

        }

    }

    public void getSession() {

        homeNavigator.onStarted();
        String url = AppUrls.BASE_AUXULARY_URL + "getsession/" + Global.accessToken + "/" + "AndroidV8.0";

        Disposable disposable = repository.getSession(url)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AppSessionResponse>() {
                    @Override public void accept(AppSessionResponse appResponse) throws Exception {
                        getAppSession(appResponse);

                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        homeNavigator.onFailure("Unable to connect the remote server");
                    }
                });

        compositeDisposable.add(disposable);
    }

    private void getAppSession(AppSessionResponse appResponse){
        if (appResponse != null) {
            homeNavigator.onSuccess();
            if (appResponse != null && appResponse.getService_response() != null) {
                Global.app_session_token = appResponse.getService_response().getToken();
                getApps();
            } else {
                AlertDialogUtil.errorAlertDialog("", activity.getResources().getString(R.string.community_error),
                        activity.getResources().getString(R.string.ok), activity);
            }
        } else {
            AlertDialogUtil.errorAlertDialog("", activity.getResources().getString(R.string.community_error),
                    activity.getResources().getString(R.string.ok), activity);
        }
    }


    public void getApps() {

        homeNavigator.onStarted();

        String url = AppUrls.BASE_AUXULARY_URL + "/getapps";

        SerializeGetAppRequestModel model = new SerializeGetAppRequestModel();

        SerializeGetAppInputRequestModel inputModel = new SerializeGetAppInputRequestModel();
        inputModel.setTOKEN(Global.app_session_token);
        inputModel.setREMARKS("AndroidV8.0");

        model.setInputJson(inputModel);

        Disposable disposable = repository.getAppResponse(url, model)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetAppResponse>() {
                    @Override public void accept(GetAppResponse appResponse) throws Exception {
                        getAppResponse(appResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        homeNavigator.onFailure("Unable to connect the remote server");
                    }
                });

        compositeDisposable.add(disposable);
    }

    public void getAppResponse(GetAppResponse appResponse){
        if (appResponse != null) {
            homeNavigator.onSuccess();
            if (appResponse != null && appResponse.getService_response() != null
            && appResponse.getService_response().getApplications() != null) {
                mutableHomeGridMenu = new MutableLiveData<>();
                mutableHomeGridMenu.setValue(appResponse.getService_response().getApplications());
                homeNavigator.populateGridMenu();
            } else {
                AlertDialogUtil.errorAlertDialog("", activity.getResources().getString(R.string.community_error),
                        activity.getResources().getString(R.string.ok), activity);
            }
        } else {
            AlertDialogUtil.errorAlertDialog("", activity.getResources().getString(R.string.community_error),
                    activity.getResources().getString(R.string.ok), activity);
        }


    }

    public Applications getSelectedApplication() {
        return selectedApplication;
    }

    public void setSelectedApplication(Applications selectedApplication) {
        this.selectedApplication = selectedApplication;
    }

    public Tabs getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(Tabs selectedTab) {
        this.selectedTab = selectedTab;
    }

    public void getAppsSearchResult(String searchText) {

        homeNavigator.onStarted();

        String url = getSelectedApplication().getSearchUrl();

        SearchParameterModel searchModel = new SearchParameterModel();

        SearchParameterInput inputModel = new SearchParameterInput();
        inputModel.setApplicationId(getSelectedApplication().getId());
        inputModel.setSearchValue(searchText);
        inputModel.setTabId(getSelectedTab().getId());
        inputModel.setTOKEN(Global.app_session_token);
        inputModel.setREMARKS("AndroidV8.0");

        searchModel.setInputJson(inputModel);

        if(getSelectedApplication().getHasMap()) {
            Disposable disposable = repository.getMapBasedSearchResult(url, searchModel)
                    .subscribeOn(kharetatiApp.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<SearchResult>() {
                        @Override
                        public void accept(SearchResult response) throws Exception {
                            getMapBasedSearchResult(response);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            homeNavigator.onFailure("Unable to connect the remote server");
                        }
                    });
            compositeDisposable.add(disposable);
        } else {
            Disposable disposable = repository.getWebBasedSearchResult(url, searchModel)
                    .subscribeOn(kharetatiApp.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<WebSearchResult>() {
                        @Override
                        public void accept(WebSearchResult response) throws Exception {
                            getWebBasedSearchResult(response);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            homeNavigator.onFailure("Unable to connect the remote server");
                        }
                    });
            compositeDisposable.add(disposable);
        }
    }

    public void getMapBasedSearchResult(SearchResult result){
        if (result != null) {
            homeNavigator.onSuccess();
            if (result != null && result.getService_response() != null) {
                Global.mapSearchResult = result;
                navigate(activity, FragmentTAGS.FR_MAP);
            } else {
                AlertDialogUtil.errorAlertDialog("", activity.getResources().getString(R.string.community_error),
                        activity.getResources().getString(R.string.ok), activity);
            }
        } else {
            AlertDialogUtil.errorAlertDialog("", activity.getResources().getString(R.string.community_error),
                    activity.getResources().getString(R.string.ok), activity);
        }
    }

    public void getWebBasedSearchResult(WebSearchResult result){
        if (result != null) {
            homeNavigator.onSuccess();
            if (result != null && result.getService_response() != null) {
                ArrayList param = new ArrayList<>();
                param.add(result.getService_response().getDisplayInWebViewPage());
                navigateWithParam(activity, FragmentTAGS.FR_WEBVIEW, param);
            } else {
                AlertDialogUtil.errorAlertDialog("", activity.getResources().getString(R.string.community_error),
                        activity.getResources().getString(R.string.ok), activity);
            }
        } else {
            AlertDialogUtil.errorAlertDialog("", activity.getResources().getString(R.string.community_error),
                    activity.getResources().getString(R.string.ok), activity);
        }
    }

    public void getLookUp(String lookupId, String lookupValue) {

        homeNavigator.onStarted();

        String url = AppUrls.LOOKUP_URL;

        LookupParameterModel model = new LookupParameterModel();

        LookupInput inputModel = new LookupInput();
        inputModel.setLkpId(lookupId);
        inputModel.setLkpValue(lookupValue);
        inputModel.setTOKEN(Global.app_session_token);
        inputModel.setREMARKS("AndroidV8.0");

        model.setInputJson(inputModel);

        Disposable disposable = repository.getLookupResult(url, model)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LookupResponseModel>() {
                    @Override public void accept(LookupResponseModel responseModel) throws Exception {
                        getLookupResponse(responseModel);
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        homeNavigator.onFailure("Unable to connect the remote server");
                    }
                });

        compositeDisposable.add(disposable);
    }

    public void getLookupResponse(LookupResponseModel responseModel){
        if (responseModel != null) {
            homeNavigator.onSuccess();
            if (responseModel != null && responseModel.getService_response() != null) {
                Global.lookupResponse = responseModel.getService_response();
                homeNavigator.populateAreaNames();
            } else {
                AlertDialogUtil.errorAlertDialog("", activity.getResources().getString(R.string.community_error),
                        activity.getResources().getString(R.string.ok), activity);
            }
        } else {
            AlertDialogUtil.errorAlertDialog("", activity.getResources().getString(R.string.community_error),
                    activity.getResources().getString(R.string.ok), activity);
        }


    }

}
