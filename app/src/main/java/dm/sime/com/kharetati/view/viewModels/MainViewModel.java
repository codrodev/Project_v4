package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.util.ArrayList;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.GetAppResponse;
import dm.sime.com.kharetati.datas.models.LoginDetails;
import dm.sime.com.kharetati.datas.models.NotificationResponse;
import dm.sime.com.kharetati.datas.models.SerializeGetAppInputRequestModel;
import dm.sime.com.kharetati.datas.models.SerializeGetAppRequestModel;
import dm.sime.com.kharetati.datas.models.SerializeGetNotificationRequestModel;
import dm.sime.com.kharetati.datas.repositories.MainRepository;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.navigators.MainNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private KharetatiApp kharetatiApp;
    Activity activity;
    MainRepository repository;
    public static MainNavigator mainNavigator;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    /*public MainViewModel(){

    }*/
    public MainViewModel(Activity context, MainRepository repository){
        this.activity = context;
        this.repository = repository;

        kharetatiApp = KharetatiApp.create(activity);
    }

    public MutableLiveData<String> sampleText = new MutableLiveData<String>();

    public MainViewModel(Context context) {
        sampleText = new MutableLiveData<String>();
    }

    public void initialize(){
        sampleText.setValue("sample text");
    }

    public String getValue(){
        return sampleText.getValue();
    }

    public String bottomNavigationTAG(int position){
        switch (position) {
            case 1:
                return FragmentTAGS.FR_DASHBOARD;
            case 2:
                return FragmentTAGS.FR_HAPPINESS;
            case 3:
                return FragmentTAGS.FR_HOME;
            case 4:
                return FragmentTAGS.FR_CONTACT_US;
                case 5:
                return FragmentTAGS.FR_BOTTOMSHEET;
            default:
                return "";
        }
    }
    public int getHeaderHieght(){
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            return dp2Px((Global.getScreenHeight(activity)/100)*15);
        } else {
            // In portrait
            return dp2Px((Global.getScreenHeight(activity)/100)*15);

        }

    }
    private int sp2Px(Float sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }
    private int dp2Px(Float dp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public void getNotifications() {
        mainNavigator.onStarted();

        String url = AppUrls.BASE_AUXULARY_URL + "getnotification";

        SerializeGetAppRequestModel model = new SerializeGetAppRequestModel();
        if(Global.app_session_token==null)
            Global.app_session_token = activity.getSharedPreferences(Global.MYPREFERENCES,Context.MODE_PRIVATE).getString(Global.APP_SESSION_TOKEN,Global.app_session_token);

        SerializeGetAppInputRequestModel inputModel = new SerializeGetAppInputRequestModel();
        if(Global.isUAE){
            inputModel.setTOKEN(Global.uaeSessionResponse == null ? "" : Global.uaeSessionResponse.getService_response().getToken());
            inputModel.setUserId(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getEmail());
        } else {
            inputModel.setTOKEN(Global.app_session_token == null ? "" :!Global.isUserLoggedIn?"": Global.app_session_token);
            inputModel.setUserId(Global.isUserLoggedIn? "":"");
        }
        inputModel.setGuest(!Global.isUserLoggedIn);


        model.setInputJson(inputModel);
        System.out.println("Notification JSON request ===>"+ new Gson().toJson(inputModel));

        Disposable disposable = repository.getAppNotifications(url, model)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<NotificationResponse>() {
                    @Override public void accept(NotificationResponse response) throws Exception {

                        if(!response.getIsException()){
                            System.out.println("Notification JSON Response <==="+ new Gson().toJson(response));
                            Global.notificationResponse = response;
                            getNotification(response);
                        }
                        else{
                            showErrorMessage(response.getMessage()!=null?response.getMessage():"");
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        if(throwable instanceof Exception)
                            Global.logout(activity);
                        else
                            showErrorMessage(throwable.getMessage());
                    }
                });

        compositeDisposable.add(disposable);
    }

    private void getNotification(NotificationResponse response) {
        mainNavigator.updateNotificationUI(response);
    }
    public void showErrorMessage(String exception){
        if(Global.appMsg!=null){
            mainNavigator.onFailure(Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getErrorFetchingDataEn():Global.appMsg.getErrorFetchingDataAr());
        }
        else
            mainNavigator.onFailure(activity.getResources().getString(R.string.error_response));

        Log.e(activity.getClass().getSimpleName(),exception);
    }

    public void cancelNotification() {
        mainNavigator.cancelNotification();
    }

    public void setDotPosition(int position) {
        mainNavigator.setDot(position);
    }
    public void loadWebView(String url, String name){
        ArrayList al = new ArrayList();
        al.add(url);
        al.add(name);
        ((MainActivity)activity).loadFragment(FragmentTAGS.FR_WEBVIEW, true,al);
    }
}


