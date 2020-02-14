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

import java.util.HashMap;
import java.util.List;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.Applications;
import dm.sime.com.kharetati.datas.models.GetAreaNamesResponse;
import dm.sime.com.kharetati.datas.models.GridMenuModel;
import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.InAppNotifications;
import dm.sime.com.kharetati.datas.models.InAppNotificationsModel;
import dm.sime.com.kharetati.datas.models.MakaniToDLTMResponse;
import dm.sime.com.kharetati.datas.models.ParcelResponse;
import dm.sime.com.kharetati.datas.models.Parcels;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.models.SearchForm;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.MyApiService;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.HomeRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.adapters.InAppNotificationAdapter;
import dm.sime.com.kharetati.view.customview.CleanableEditText;
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
    GridMenuModel modelGridMenu;
    FragmentNavigator navigator;
    public HomeNavigator homeNavigator;

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

        modelGridMenu = new GridMenuModel();
        mutableHomeGridMenu = new MutableLiveData<>();
        mutableHomeGridMenu.setValue(modelGridMenu.getKharetati().getApplications());
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

    /// Home Grid Menu///

    public void initializeHomeGridMenu(Context context, int position){
        //mutableHomeGridMenuItems.setValue(mutableHomeGridMenu.getValue());
    }

    public int getGridPagerSize(){
        return ((mutableHomeGridMenu.getValue().size()/6) + 1);
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



    public void getMakaniToDLTM(){

        homeNavigator.onStarted();

        //Global.makani ="3003295320";

        HTTPRequestBody.MakaniBody makaniBody = new HTTPRequestBody.MakaniBody();

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



    public void getParcelID() {

        homeNavigator.onStarted();

        HTTPRequestBody.ParcelBody parcelBody = new HTTPRequestBody.ParcelBody();

        Disposable disposable = repository.getParcelID(parcelBody)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ParcelResponse>() {
                    @Override public void accept(ParcelResponse parcelResponse) throws Exception {
                        getParcelDetails(parcelResponse);

                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        homeNavigator.onFailure("Unable to connect the remote server");
                    }
                });

        compositeDisposable.add(disposable);


    }

    private void getParcelDetails(ParcelResponse parcelResponse) {
        try {

            if (parcelResponse != null) {
                homeNavigator.onSuccess();

                if (parcelResponse != null && !Boolean.valueOf(parcelResponse.getIs_exception())) {
                    lstParcels = parcelResponse.getParcelContainer().getParcels();
                    if(lstParcels != null && lstParcels.length > 0){
                        Global.landNumber=Global.LandNo+"/"+(Global.subNo.isEmpty()? "0":Global.subNo);
                        PlotDetails.parcelNo =lstParcels[0].getParcelId();
                        PlotDetails.isOwner =false;
                        PlotDetails.clearCommunity();

                        navigate(activity, FragmentTAGS.FR_MAP);
                    }
                } else {
                    AlertDialogUtil.errorAlertDialog("", activity.getResources().getString(R.string.no_valid_land),
                            activity.getResources().getString(R.string.ok), activity);
                }
            } else {
                AlertDialogUtil.errorAlertDialog("", activity.getResources().getString(R.string.no_valid_land),
                        activity.getResources().getString(R.string.ok), activity);
            }
        } catch (Exception e) {
            homeNavigator.onFailure(e.getMessage());
            e.printStackTrace();
        }

    }

    public void getAreaNames() {

        homeNavigator.onStarted();

        HTTPRequestBody.AreaBody areaBody = new HTTPRequestBody.AreaBody();

        Disposable disposable = repository.getAreaNames(areaBody)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetAreaNamesResponse>() {
                    @Override public void accept(GetAreaNamesResponse areaNamesResponse) throws Exception {
                        getAreaNamesDetails(areaNamesResponse);

                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        homeNavigator.onFailure("Unable to connect the remote server");
                    }
                });

        compositeDisposable.add(disposable);


    }

    private void getAreaNamesDetails(GetAreaNamesResponse areaNamesResponse) {


        if (areaNamesResponse != null) {
            homeNavigator.onSuccess();
            if (areaNamesResponse != null && areaNamesResponse.getAreaResponse() != null) {
                Global.areaResponse = areaNamesResponse.getAreaResponse();
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
