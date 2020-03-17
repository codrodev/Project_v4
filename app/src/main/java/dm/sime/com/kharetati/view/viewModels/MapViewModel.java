package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import org.json.JSONObject;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.BaseResponseModel;
import dm.sime.com.kharetati.datas.models.GetAppResponse;
import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.MakaniToDLTMResponse;
import dm.sime.com.kharetati.datas.models.ParcelDetails;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.models.SerializableParcelDetails;
import dm.sime.com.kharetati.datas.models.SerializableSaveBookMarks;
import dm.sime.com.kharetati.datas.models.SerializeBookMarksModel;
import dm.sime.com.kharetati.datas.models.SerializeGetAppInputRequestModel;
import dm.sime.com.kharetati.datas.models.SerializeGetAppRequestModel;
import dm.sime.com.kharetati.datas.models.SerializedValidateParcelModel;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.MyApiService;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.HomeRepository;
import dm.sime.com.kharetati.datas.repositories.MapRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.fragments.ParentSiteplanFragment;
import dm.sime.com.kharetati.view.navigators.FragmentNavigator;
import dm.sime.com.kharetati.view.navigators.MapNavigator;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static dm.sime.com.kharetati.utility.Global.isBookmarks;

public class MapViewModel extends ViewModel {

    FragmentNavigator frNavigator;
    private Activity activity;
    public MapNavigator mapNavigator;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    int lastIndex = 0;
    private MyApiService apiService;
    private MapRepository repository;
    private KharetatiApp kharetatiApp;

    public MapViewModel(){

    }
    public MapViewModel(Activity context, MapRepository repository){
        this.activity = context;
        this.repository = repository;
        kharetatiApp = KharetatiApp.create(activity);
    }

    public void manageAppBar(Context ctx, boolean status){
        frNavigator = (FragmentNavigator) ctx;
        frNavigator.manageActionBar(status);
    }

    public void manageAppBottomBAtr(Context ctx, boolean status){
        frNavigator = (FragmentNavigator) ctx;
        frNavigator.manageBottomBar(status);
    }

    public void navigate(Context ctx, String fragmentTag){
        frNavigator = (FragmentNavigator) ctx;
        frNavigator.fragmentNavigator(fragmentTag, true, null);
    }

    public void getParceldetails(){
        mapNavigator.onStarted();
        String url = AppUrls.BASE_AUXULARY_URL + "/getparceldetails";

        SerializeGetAppRequestModel model = new SerializeGetAppRequestModel();

        SerializeGetAppInputRequestModel inputModel = new SerializeGetAppInputRequestModel();
        inputModel.setParcel_id(Integer.parseInt(PlotDetails.parcelNo));
        inputModel.setTOKEN(Global.app_session_token == null ? "" : Global.app_session_token);
        inputModel.setREMARKS(Global.getPlatformRemark());
        inputModel.setGuest(!Global.isUserLoggedIn);

        model.setInputJson(inputModel);

        Disposable disposable = repository.getParcelDetails(url, model)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SerializableParcelDetails>() {
                    @Override public void accept(SerializableParcelDetails appResponse) throws Exception {

                        //mapNavigator.onSuccess();
                        PlotDetails.communityAr = appResponse.getService_response().get(0).getCommNameAr();
                        PlotDetails.communityEn = appResponse.getService_response().get(0).getCommNameEn();
//                        PlotDetails.area = appResponse.getService_response().get(0).getAreaInSqMt();

                        if(Global.isSaveAsBookmark && Global.isBookmarks){
                            saveAsBookMark(true);
                        }
                        else if(isBookmarks)
                            mapNavigator.getPlotDetais(appResponse);
                        else
                            saveAsBookMark(true);



                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        showErrorMessage(throwable.getMessage());
                    }
                });

        compositeDisposable.add(disposable);
    }

    public void saveAsBookMark(Boolean isSave) {


        //getParceldetails();
        mapNavigator.onStarted();

        SerializeBookMarksModel model = new SerializeBookMarksModel();
        //model.setUserID(1003);
        model.setUserID(Global.sime_userid);
        model.setArea(PlotDetails.area);
        model.setParcelNumber(Integer.parseInt(PlotDetails.parcelNo));
        model.setCommunity(PlotDetails.communityEn);
        model.setCommunityAr(PlotDetails.communityAr);

        HTTPRequestBody.BookMarkBody bookMarkBody = new HTTPRequestBody.BookMarkBody();


        Disposable disposable = repository.saveAsBookMark(model)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SerializableSaveBookMarks>() {
                    @Override
                    public void accept(SerializableSaveBookMarks response) throws Exception {

                        try {
                            if (response != null && !response.toString().isEmpty()) {
                                mapNavigator.onSuccess();
                                if (!response.isError()) {
                                    if (isSave) {
                                        if (response.isExisting())
                                            AlertDialogUtil.errorAlertDialog(activity.getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getPlotAvailableFavEn() : Global.appMsg.getPlotAvailableFavAr(), activity.getResources().getString(R.string.ok), activity);
                                        else
                                            AlertDialogUtil.errorAlertDialog(activity.getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getPlotAddedFavEn() : Global.appMsg.getPlotAddedFavAr(), activity.getResources().getString(R.string.ok), activity);

                                    }
                                } else {
                                    if (isSave) {
                                        AlertDialogUtil.errorAlertDialog(activity.getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getErrorFetchingDataEn() : Global.appMsg.getErrorFetchingDataAr(), activity.getResources().getString(R.string.ok), activity);

                                    }
                                }
                            }
                            else{
                                showErrorMessage("");
                            }
                        } catch (Exception e) {
                            showErrorMessage(e.getMessage());
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mapNavigator.onFailure(throwable.getMessage());
                    }
                });

        compositeDisposable.add(disposable);


    }

    public void validateRequest(String fragmentTag) {

        mapNavigator.onStarted();


        kharetatiApp = KharetatiApp.create(activity);

        String url = Global.base_url_site_plan + "/validateRequest";

        SerializedValidateParcelModel model = new SerializedValidateParcelModel();
        model.setLocale(Global.CURRENT_LOCALE);
        model.setMy_id(Global.loginDetails.username);
        model.setParcel_id(Integer.parseInt(Global.mapSearchResult.getService_response().getParcelId()));
        model.setToken(Global.site_plan_token);

        /*Gson ob = new Gson();
        String x = ob.toJson(model);*/

        Disposable disposable = repository.validateParcel(url, model)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponseModel>() {
                    @Override
                    public void accept(BaseResponseModel response) throws Exception {

                        try {
                            if (response != null) {
                                ParentSiteplanViewModel.initializeDocuments();
                                if(response.getStatus()==405){
                                    mapNavigator.onSuccess();
                                    ParentSiteplanFragment.currentIndex = 0;
                                    navigate(activity, fragmentTag);
                                } else if(response.getStatus() == 406) {
                                    if (response.getMessage_en() != null && !response.getMessage_en().equals("") ||
                                            response.getMessage_ar() != null && !response.getMessage_ar().equals("")) {
                                        AlertDialogUtil.alreadyinProgressAlert(Global.CURRENT_LOCALE.equals("en") ? response.getMessage_en(): response.getMessage_ar(),
                                                activity.getResources().getString(R.string.ok), activity);
                                    } else {
                                        AlertDialogUtil.alreadyinProgressAlert(Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getRequestUnderProgressEn() : Global.appMsg.getRequestUnderProgressAr(),
                                                activity.getResources().getString(R.string.ok), activity);
                                    }
                                }
                                else {
                                    if(response.getMessage_en()!=null && !response.getMessage_en().equals("") ||
                                            response.getMessage_ar()!=null && !response.getMessage_ar().equals("")){
                                        mapNavigator.onFailure(Global.CURRENT_LOCALE.equals("en") ? response.getMessage_en() : response.getMessage_ar());
                                    } else {
                                        mapNavigator.onFailure(Global.CURRENT_LOCALE.equals("en") ? response.getMessage_en() : response.getMessage_ar());
                                    }
                                }
                            }

                        } catch (Exception e) {
                            showErrorMessage(e.getMessage());
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                       showErrorMessage(throwable.getMessage());
                    }
                });

        compositeDisposable.add(disposable);


    }
    public void showErrorMessage(String exception){
        if(Global.appMsg!=null){
            mapNavigator.onFailure(Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getErrorFetchingDataEn():Global.appMsg.getErrorFetchingDataAr());
        }
        else
            mapNavigator.onFailure(activity.getResources().getString(R.string.error_response));

        Log.d(activity.getClass().getSimpleName(),exception);
    }

}
