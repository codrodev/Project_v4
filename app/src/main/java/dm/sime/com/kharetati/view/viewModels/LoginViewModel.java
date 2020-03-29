package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.Bindable;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Observable;

import ae.sdg.libraryuaepass.UAEPassAccessTokenCallback;
import ae.sdg.libraryuaepass.UAEPassController;
import ae.sdg.libraryuaepass.business.authentication.model.UAEPassAccessTokenRequestModel;
import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.GetConfigResponse;
import dm.sime.com.kharetati.datas.models.KharetatiUser;
import dm.sime.com.kharetati.datas.models.SerializedUAEAccessTokenAPIModelResponse;
import dm.sime.com.kharetati.datas.models.SerializedUAEAccessTokenRequestAPIModel;
import dm.sime.com.kharetati.datas.models.SessionUaePassResponse;
import dm.sime.com.kharetati.datas.models.UAEAccessTokenResponse;
import dm.sime.com.kharetati.datas.models.UaePassConfig;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.MyApiService;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.utility.AES;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Encryptions;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.datas.models.AccessTokenResponse;
import dm.sime.com.kharetati.datas.models.AttachmentBitmap;
import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.SessionResponse;
import dm.sime.com.kharetati.datas.models.User;
import dm.sime.com.kharetati.datas.models.UserRegistration;
import dm.sime.com.kharetati.datas.repositories.UserRepository;
import dm.sime.com.kharetati.view.activities.LoginActivity;
import dm.sime.com.kharetati.view.activities.WebViewActivity;
import dm.sime.com.kharetati.view.fragments.DeliveryFragment;
import dm.sime.com.kharetati.view.navigators.AuthListener;
import dm.sime.com.kharetati.view.activities.MainActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static dm.sime.com.kharetati.utility.Global.loginDetails;

public class LoginViewModel extends ViewModel {
    private static final String TAG = "LoginViewModel";

    Activity activity;

    private String email;
    private String password;
    public AuthListener authListener;
    public MyApiService apiService;
    private User user = new User();
    private UserRepository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private KharetatiApp kharetatiApp;
    public static String guestName,guestPassword;


    /* public LoginViewModel(){

     }*/
    public LoginViewModel(Activity context, UserRepository repository){
        this.activity = context;
        this.repository = repository;
    }

    public String getDataEmail() {
        return email;
    }

    public String getDataPassword() {
        return password;
    }

    public void setCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void onGuestLoginButtonClick(){

        authListener.onStarted();
        kharetatiApp = KharetatiApp.create(activity);

        HTTPRequestBody.GuestBody guestBody = new HTTPRequestBody.GuestBody();

        Disposable disposable = repository.guestLogin(guestBody)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<KharetatiUser>() {
                    @Override public void accept(KharetatiUser kharetatiUser) throws Exception {
                        if(kharetatiUser!=null)
                            guestLogin(kharetatiUser);
                        else{
                            if(Global.appMsg!=null){
                                authListener.onFailure(Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getErrorFetchingDataEn():Global.appMsg.getErrorFetchingDataAr());
                            }
                            else
                                authListener.onFailure(activity.getResources().getString(R.string.error_response));

                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        if(Global.appMsg!=null){
                            authListener.onFailure(Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getErrorFetchingDataEn():Global.appMsg.getErrorFetchingDataAr());
                        }
                        else
                            authListener.onFailure(activity.getResources().getString(R.string.error_response));

                    }
                });

        compositeDisposable.add(disposable);

    }

    private void guestLogin(KharetatiUser kharetatiUser) {


        if (kharetatiUser != null && !kharetatiUser.isError()) {
            Global.hashSearchFieldValue = new HashMap<>();
            Global.lastSelectedBottomTab =3;
            Global.isFirstLoad = true;
            Global.sime_userid = kharetatiUser.userID;
            Global.isUserLoggedIn = false;
            Global.arcgis_token = kharetatiUser.getArcgis_token();

            AppUrls.GIS_LAYER_PASSWORD=kharetatiUser.gis_pwd!=null && kharetatiUser.gis_pwd!=""?kharetatiUser.gis_pwd: AppUrls.GIS_LAYER_PASSWORD;
            AppUrls.GIS_LAYER_USERNAME=kharetatiUser.gis_user_name != null && kharetatiUser.gis_user_name!=""?kharetatiUser.gis_user_name: AppUrls.GIS_LAYER_USERNAME;
            AppUrls.GIS_LAYER_URL=kharetatiUser.gis_layer_url != null && kharetatiUser.gis_layer_url!=""?kharetatiUser.gis_layer_url:AppUrls.GIS_LAYER_URL;
            AppUrls.GIS_LAYER_TOKEN_URL=kharetatiUser.gis_token_url != null && kharetatiUser.gis_token_url!=""? kharetatiUser.gis_token_url:AppUrls.GIS_LAYER_TOKEN_URL;
            AppUrls.URL_PLOTFINDER=kharetatiUser.url_plotfinder !=null && kharetatiUser.url_plotfinder!=""? kharetatiUser.url_plotfinder:AppUrls.URL_PLOTFINDER;
            AppUrls.GIS_LAYER_COMMUNITY_URL=kharetatiUser.community_layerid!=null && kharetatiUser.community_layerid!=""?       AppUrls.GIS_LAYER_URL + "/" + kharetatiUser.community_layerid:AppUrls.GIS_LAYER_URL + "/" + AppUrls.community_layerid;
            AppUrls.parcelLayerExportUrl_en=kharetatiUser.parcelLayerExportUrl_en!=null && kharetatiUser.parcelLayerExportUrl_en!=""?  kharetatiUser.parcelLayerExportUrl_en + "?token=" + Global.arcgis_token:AppUrls.parcelLayerExportUrl_en + "?token=" + Global.arcgis_token;
            AppUrls.parcelLayerExportUrl_ar=kharetatiUser.parcelLayerExportUrl_ar!=null && kharetatiUser.parcelLayerExportUrl_ar!=""?  kharetatiUser.parcelLayerExportUrl_ar + "?token=" + Global.arcgis_token:AppUrls.parcelLayerExportUrl_ar + "?token=" + Global.arcgis_token;
            AppUrls.plot_layerid=kharetatiUser.plot_layerid!=null && kharetatiUser.plot_layerid!=""?kharetatiUser.plot_layerid:AppUrls.plot_layerid;
            AppUrls.BASE_AUXULARY_URL=kharetatiUser.auxiliaryServiceUrl!=null && kharetatiUser.auxiliaryServiceUrl!=""?kharetatiUser.auxiliaryServiceUrl:"";
            Global.plotDimLayerId = (kharetatiUser.getPlotDimLayerId() != null && kharetatiUser.getPlotDimLayerId() != "") ? kharetatiUser.getPlotDimLayerId():"";
            Global.plotHighlightLayerId = (kharetatiUser.getPlotHighlightLayerId() != null && kharetatiUser.getPlotHighlightLayerId() != "") ? kharetatiUser.getPlotHighlightLayerId():"";
            Global.plotImgLayerId = (kharetatiUser.getPlotImgLayerId() != null && kharetatiUser.getPlotImgLayerId() != "") ? kharetatiUser.getPlotImgLayerId():"";
            Global.plotLayerParcelAttrName = (kharetatiUser.getPlotLayerParcelAttrName() != null && kharetatiUser.getPlotLayerParcelAttrName() != "") ? kharetatiUser.getPlotLayerParcelAttrName():"";
            Global.plotDimLayerParcelAttrName = (kharetatiUser.getPlotDimLayerParcelAttrName() != null && kharetatiUser.getPlotDimLayerParcelAttrName() != "") ? kharetatiUser.getPlotDimLayerParcelAttrName():"";
            User user = new User();
            Global.accessToken=kharetatiUser.access_token;
            user.setUsername("GUEST");
            guestName=user.getUsername();
            guestPassword="Guest";

            Global.aboutus_en_url = kharetatiUser.getAboutus_en_url();
            Global.aboutus_ar_url = kharetatiUser.getAboutus_ar_url();
            Global.terms_en_url = kharetatiUser.getTerms_en_url();
            Global.terms_ar_url = kharetatiUser.getTerms_ar_url();
            Global.faq_url = kharetatiUser.getFaq_url();
            Global.appMsg = kharetatiUser.getAppMsg();

            Global.bookmarks_en_url = kharetatiUser.bookmarks_en_url;
            Global.bookmarks_ar_url = kharetatiUser.bookmarks_ar_url;
            Global.mymaps_en_url = kharetatiUser.mymaps_en_url;
            Global.mymaps_ar_url = kharetatiUser.mymaps_ar_url;
            Global.home_en_url = kharetatiUser.home_en_url;
            Global.home_ar_url = kharetatiUser.home_ar_url;
            Global.map_en_url = kharetatiUser.map_en_url;
            Global.map_ar_url = kharetatiUser.map_ar_url;

            authListener.saveUser(user);
            Global.isUserLoggedIn = false;

            authListener.onSuccess();

        }
    }

    public void onLoginButtonClick() {

        authListener.onStarted();
        if (getDataEmail().isEmpty() && getDataPassword().isEmpty()) {

            authListener.onFailure(activity.getString(R.string.username_and_password));
        }else if(getDataEmail().isEmpty()){

            authListener.onFailure(activity.getString(R.string.enter_username));


        } else if(getDataPassword().isEmpty()){

            authListener.onFailure(activity.getString(R.string.enter_password));

        }else if(!isValidEmail(getDataEmail())) {
            authListener.onFailure(activity.getString(R.string.enter_valid_username));
            }
            else {


            getAccessTokenAPI(false);
            //AccessTokenResponse accessTokenResponse = repository.getAccessToken(getDataEmail(), getDataPassword());




        }

    }

    private void getAccessTokenAPI(boolean isUAE){
        Log.v(TAG, "UAE Pass App: getAccessTokenAPI(): calling");
        kharetatiApp = KharetatiApp.create(activity);


        Disposable disposable = repository.getAccessToken(isUAE ? "" : getDataEmail(),
                isUAE ? "" : getDataPassword(), isUAE, isUAE ? Global.uae_access_token : "")
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AccessTokenResponse>() {
                    @Override public void accept(AccessTokenResponse accessTokenResponse) throws Exception {
                        Log.v(TAG, "UAE Pass App: getAccessTokenAPI(): success");
                        saveAccessTokenResponse(accessTokenResponse, isUAE);

                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        Log.v(TAG, "UAE Pass App: getAccessTokenAPI(): failed:" + throwable.getMessage());
                        showErrorMessage();

                    }
                });

        compositeDisposable.add(disposable);
    }

    private void saveAccessTokenResponse(AccessTokenResponse accessTokenResponse, boolean isUAE) {
        if (accessTokenResponse != null) {
            Log.v(TAG, "UAE Pass App: saveAccessTokenResponse(): calling");
            if (accessTokenResponse.getAccessToken() != null) {
                Global.hashSearchFieldValue = new HashMap<>();
                //get the response here
                Global.isFirstLoad = true;
                Global.lastSelectedBottomTab =3;
                Global.accessToken = accessTokenResponse.getAccessToken();

                Log.d("Access token : ", Global.accessToken);
                Global.hideSoftKeyboard(activity);
                Global.isUserLoggedIn = true;
                loginDetails.username = email;
                loginDetails.pwd = password;
                //Global.loginDetails.showFormPrefilledOnRememberMe=false;
                Global.forceUserToUpdateBuild_msg_en = accessTokenResponse.getForceUserToUpdateBuildMsgEn();
                Global.forceUserToUpdateBuild_msg_ar = accessTokenResponse.getForceUserToUpdateBuildMsgAr();
                Global.noctemplateUrl = accessTokenResponse.getNoctemplateUrl();
                Global.CurrentAndroidVersion = accessTokenResponse.getCurrentAndroidVersion();
                Global.base_url_site_plan = accessTokenResponse.getBaseurlSmartsiteplanWs();
                Global.site_plan_token = accessTokenResponse.getSmartsiteplanWsToken();

                Global.showLandregInMenu = accessTokenResponse.getShowLandregInMenu();
                Global.showLandregPopup = accessTokenResponse.getShowLandregPopup();
                Global.landregPopupMsgEn = accessTokenResponse.getLandregPopupMsgEn();
                Global.landregPopupMsgAr = accessTokenResponse.getLandregPopupMsgAr();
                Global.landregPopupMsgHeadingEn = accessTokenResponse.getLandregPopupMsgHeadingEn();
                Global.landregPopupMsgHeadingAr = accessTokenResponse.getLandregPopupMsgHeadingAr();
                Global.landregUrl = accessTokenResponse.getLandregUrl();
                Global.aboutus_en_url = accessTokenResponse.getAboutusEnUrl();
                Global.aboutus_ar_url = accessTokenResponse.getAboutusArUrl();
                Global.terms_en_url = accessTokenResponse.getTermsEnUrl();
                Global.terms_ar_url = accessTokenResponse.getTermsArUrl();
                Global.appMsg = accessTokenResponse.getAppMsg();

                Global.bookmarks_en_url = accessTokenResponse.getBookmarks_en_url();
                Global.bookmarks_ar_url = accessTokenResponse.getBookmarks_ar_url();
                Global.mymaps_en_url = accessTokenResponse.getMymaps_en_url();
                Global.mymaps_ar_url = accessTokenResponse.getMymaps_ar_url();
                Global.home_en_url = accessTokenResponse.getHome_en_url();
                Global.home_ar_url = accessTokenResponse.getHome_ar_url();
                Global.map_en_url = accessTokenResponse.getMap_en_url();
                Global.map_ar_url = accessTokenResponse.getMap_ar_url();
                Global.faq_url = accessTokenResponse.getFaq_url();

                AttachmentBitmap.letter_from_owner = null;
                AttachmentBitmap.emirateId_back = null;
                AttachmentBitmap.emirateId_front = null;
                AttachmentBitmap.land_ownership_certificate = null;
                AttachmentBitmap.passport_copy = null;

                AppUrls.GIS_LAYER_PASSWORD = (accessTokenResponse.getGisPwd() != null && accessTokenResponse.getGisPwd() != "") ? accessTokenResponse.getGisPwd() : AppUrls.GIS_LAYER_PASSWORD;
                AppUrls.GIS_LAYER_USERNAME = (accessTokenResponse.getGisUserName() != null && accessTokenResponse.getGisUserName() != "") ? accessTokenResponse.getGisUserName() : AppUrls.GIS_LAYER_USERNAME;
                AppUrls.GIS_LAYER_URL = (accessTokenResponse.getGisLayerUrl() != null && accessTokenResponse.getGisLayerUrl() != "") ? accessTokenResponse.getGisLayerUrl() : AppUrls.GIS_LAYER_URL;
                AppUrls.GIS_LAYER_TOKEN_URL = (accessTokenResponse.getGisTokenUrl() != null && accessTokenResponse.getGisTokenUrl() != "") ? accessTokenResponse.getGisTokenUrl() : AppUrls.GIS_LAYER_TOKEN_URL;
                AppUrls.GIS_LAYER_COMMUNITY_URL = (accessTokenResponse.getCommunityLayerid() != null && accessTokenResponse.getCommunityLayerid() != "") ? AppUrls.GIS_LAYER_URL.toString() + "/" + accessTokenResponse.getCommunityLayerid() : AppUrls.GIS_LAYER_URL.toString() + "/" + AppUrls.community_layerid;

                AppUrls.plot_layerid = (accessTokenResponse.getPlotLayerid() != null && accessTokenResponse.getPlotLayerid() != "") ? accessTokenResponse.getPlotLayerid() : AppUrls.plot_layerid;
                AppUrls.BASE_AUXULARY_URL = (accessTokenResponse.getAuxiliaryServiceUrl() != null && accessTokenResponse.getAuxiliaryServiceUrl() != "") ? accessTokenResponse.getAuxiliaryServiceUrl() : "";
                Global.plotDimLayerId = (accessTokenResponse.getPlotDimLayerId() != null && accessTokenResponse.getPlotDimLayerId() != "") ? accessTokenResponse.getPlotDimLayerId():"";
                Global.plotHighlightLayerId = (accessTokenResponse.getPlotHighlightLayerId() != null && accessTokenResponse.getPlotHighlightLayerId() != "") ? accessTokenResponse.getPlotHighlightLayerId():"";
                Global.plotImgLayerId = (accessTokenResponse.getPlotImgLayerId() != null && accessTokenResponse.getPlotImgLayerId() != "") ? accessTokenResponse.getPlotImgLayerId():"";
                Global.plotLayerParcelAttrName = (accessTokenResponse.getPlotLayerParcelAttrName() != null && accessTokenResponse.getPlotLayerParcelAttrName() != "") ? accessTokenResponse.getPlotLayerParcelAttrName():"";
                Global.plotDimLayerParcelAttrName = (accessTokenResponse.getPlotDimLayerParcelAttrName() != null && accessTokenResponse.getPlotDimLayerParcelAttrName() != "") ? accessTokenResponse.getPlotDimLayerParcelAttrName():"";


                authListener.addUserToHistory(loginDetails.username);
                authListener.saveUserToRemember(loginDetails);

                Disposable disposable = repository.getUserDetails(AppUrls.MY_ID_USER_INFO_URL + Global.accessToken)
                        .subscribeOn(kharetatiApp.subscribeScheduler())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<User>() {
                            @Override public void accept(User user) throws Exception {
                                Log.v(TAG, "UAE Pass App: saveAccessTokenResponse(): success");
                                saveUserDetails(user, isUAE);

                            }
                        }, new Consumer<Throwable>() {
                            @Override public void accept(Throwable throwable) throws Exception {
                                Log.v(TAG, "UAE Pass App: saveAccessTokenResponse(): failed:" + throwable.getMessage());
                                showErrorMessage();

                            }
                        });

                compositeDisposable.add(disposable);


            } else {
                if(Global.appMsg!=null){
                    authListener.onFailure(Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInvalidUserNamePwdEn():Global.appMsg.getInvalidUserNamePwdAr());
                }
                else
                    authListener.onFailure(activity.getResources().getString(R.string.wrong_username_password));
            }
        } else {
            showErrorMessage();
        }
    }

    private void saveUserDetails(User userDetails, boolean isUAE) {
        if (userDetails != null) {
            Log.v(TAG, "UAE Pass App: saveUserDetails(): calling");
            user.setDob(userDetails.getDob());
            user.setPhoto(userDetails.getPhoto());
            user.setFullnameAR(userDetails.getFullnameAR());
            user.setLastname(userDetails.getLastname());
            user.setFirstnameAR(userDetails.getFirstnameAR());
            user.setUsername(userDetails.getUsername());
            user.setEmail(userDetails.getEmail());
            user.setGender(userDetails.getGender());
            user.setFirstname(userDetails.getFirstname());
            user.setMaritalStatus(userDetails.getMaritalStatus());
            user.setIdcardnumber(userDetails.getIdcardnumber());
            user.setLastnameAR(userDetails.getLastnameAR());
            user.setPassportNo(userDetails.getPassportNo());
            user.setEmailVerified(userDetails.getEmailVerified());
            user.setIdcardexpirydate(userDetails.getIdcardexpirydate());
            user.setSponsorNo(userDetails.getSponsorNo());
            user.setUserVerified(userDetails.getUserVerified());
            user.setSponsorType(userDetails.getSponsorType());
            user.setNationalityAR(userDetails.getNationalityAR());
            user.setResidencyNo(userDetails.getResidencyNo());
            user.setResidencyExpiryDate(userDetails.getResidencyExpiryDate());
            user.setPassportIssueDate(userDetails.getPassportIssueDate());
            user.setMobile(userDetails.getMobile());
            user.setIdcardissuedate(userDetails.getIdcardissuedate());
            user.setPassportCountry(userDetails.getPassportCountry());
            user.setIdn(userDetails.getIdn());
            user.setNationality(userDetails.getNationality());
            user.setFullname(userDetails.getFullname());
            user.setPassportExpiryDate(userDetails.getPassportExpiryDate());

            authListener.saveUser(user);

            Global.emiratesID = user.getIdn();
            Global.mobile = user.getMobile();
            Global.email = user.getEmail();
            Global.username = user.getUsername();
            Global.password = "";
            Global.UserType = "REGISTERED";
            Global.DeviceType = "Android";
            if (user.getFirstname() != null)
                Global.FirstName = user.getFirstname();
            if (user.getLastname() != null)
                Global.LastName = user.getLastname();
            if (user.getFullname() != null)
                Global.FullName = user.getFullname();
            if (user.getGender() != null) Global.Gender = user.getGender();
            if (user.getNationality() != null)
                Global.Nationality = user.getNationality();

            HTTPRequestBody body;
            if(isUAE){
                body = new HTTPRequestBody(Global.uaeSessionResponse);
                isUAE = true;
            } else {
                body = new HTTPRequestBody();
                isUAE = false;
            }

            Disposable disposable = repository.registerLoggedUser(body)
                    .subscribeOn(kharetatiApp.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<UserRegistration>() {
                        @Override public void accept(UserRegistration userRegistrationResponse) throws Exception {
                            Log.v(TAG, "UAE Pass App: saveUserDetails(): success");
                            registerUser(userRegistrationResponse);
                        }
                    }, new Consumer<Throwable>() {
                        @Override public void accept(Throwable throwable) throws Exception {
                            Log.v(TAG, "UAE Pass App: saveUserDetails(): failed:" + throwable.getMessage());
                            showErrorMessage();

                        }
                    });

            compositeDisposable.add(disposable);

        } else {
            showErrorMessage();
        }
    }

    private void registerUser(UserRegistration userRegistrationResponse) {
        if (userRegistrationResponse!= null) {
            Log.v(TAG, "UAE Pass App: registerUser(): calling:");
            //User registration is successful
            try {
                authListener.showAppUpdateAlert();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                Log.e("Exception",e.getMessage());
            }

            Global.sime_userid = userRegistrationResponse.getUserID();


            Disposable disposable = repository.getSession(Global.accessToken)
                    .subscribeOn(kharetatiApp.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<SessionResponse>() {
                        @Override public void accept(SessionResponse session) throws Exception {
                            Log.v(TAG, "UAE Pass App: registerUser(): success:");
                            getSession(session);
                        }
                    }, new Consumer<Throwable>() {
                        @Override public void accept(Throwable throwable) throws Exception {
                            Log.v(TAG, "UAE Pass App: registerUser(): failed:" + throwable.getMessage());
                            showErrorMessage();
                            Log.e("Exception",throwable.getMessage());
                        }
                    });

            compositeDisposable.add(disposable);



        } else {

            showErrorMessage();

        }
    }

    private void getSession(SessionResponse session) {

        if (session != null)
        {
            if (session.getMessage().equals("Success") || session.getMessage().equals("نجاح"))
            {
                String isException = "";

                isException = session.getIs_exception();


                if (!Boolean.valueOf(isException))
                {
                    Log.v(TAG, "UAE Pass App: getSession(): success: session" + session.getSession().getToken());
                    Global.session = session.getSession().getToken();

                    Global.isUserLoggedIn =true;
                    // Navigate to Main Activity Here
                    authListener.onSuccess();

                } else {
                    Log.v(TAG, "UAE Pass App: getSession(): failed:");
                    showErrorMessage();
                }
            }

        } else {
            showErrorMessage();
        }
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public void showErrorMessage(){
        if(Global.appMsg!=null){
            authListener.onFailure(Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getErrorFetchingDataEn():Global.appMsg.getErrorFetchingDataAr());
        }
        else
            authListener.onFailure(activity.getString(R.string.error_response));
    }

    public void getConfigAPI(){
        authListener.onStarted();
        kharetatiApp = KharetatiApp.create(activity);

        Disposable disposable = repository.getConfig(AppUrls.URL_GET_CONFIG)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetConfigResponse>() {
                    @Override public void accept(GetConfigResponse configResponse) throws Exception {
                        getConfigAPIResponse(configResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        if(Global.appMsg!=null){
                            authListener.onFailure(Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getErrorFetchingDataEn():Global.appMsg.getErrorFetchingDataAr());
                        }
                        else
                            authListener.onFailure(activity.getResources().getString(R.string.error_response));

                    }
                });

        compositeDisposable.add(disposable);
    }

    public void getConfigAPIResponse(GetConfigResponse configResponse){
        if(configResponse != null){
            authListener.onConfig(false);
            //authListener.onConfig(configResponse.disableMyId);
        }
    }

    public void uaePassConfigAPI(){
        authListener.onStarted();
        kharetatiApp = KharetatiApp.create(activity);
        Log.v(TAG, "uaePassConfigAPI(): calling");
        Disposable disposable = repository.uaePassConfig(AppUrls.URL_UAE_ID_CONFIG)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UaePassConfig>() {
                    @Override public void accept(UaePassConfig configResponse) throws Exception {
                        Log.v(TAG, "uaePassConfigAPI(): success");
                        uaePassConfigAPIResponse(configResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        Log.v(TAG, "uaePassConfigAPI(): failed:" + throwable.getMessage());
                        if(Global.appMsg!=null){
                            authListener.onFailure(Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getErrorFetchingDataEn():Global.appMsg.getErrorFetchingDataAr());
                        }
                        else
                            authListener.onFailure(activity.getResources().getString(R.string.error_response));

                    }
                });

        compositeDisposable.add(disposable);
    }

    public void uaePassConfigAPIResponse(UaePassConfig configResponse) throws Exception {
        if(configResponse != null){
            authListener.onConfig(configResponse.disableMyId);
            Global.uaePassConfig =  configResponse;
            //login();
        }

       // login();
        //UAEPassAccessTokenRequestModel
        //String secretId = AES.decrypt(clientId, "800F4757AC0E7A9ED01B2D5D2C25A59B3");
    }

    public void login() {
        if(Global.uaePassConfig != null){
            Log.v(TAG, "login``(): calling");
            String clientId = Encryptions.decrypt(Global.uaePassConfig.UAEID_clientid);
            Log.v(TAG, "clientId:" + clientId);
            String secretId = Encryptions.decrypt(Global.uaePassConfig.UAEID_secret);
            Log.v(TAG, "secretId:" + secretId);
            String callbackUrl = Encryptions.decrypt(Global.uaePassConfig.UAEID_callback_url);
            Log.v(TAG, "callbackUrl:" + callbackUrl);
            if(UAEPassRequestModels.isPackageInstalled(UAEPassRequestModels.UAE_PASS_PACKAGE_ID, activity.getPackageManager())){
                Log.v(TAG, "UAE Pass App: app installed");
                UAEPassAccessTokenRequestModel requestModel =
                        UAEPassRequestModels.getAuthenticationRequestModel(activity,
                                clientId, secretId, callbackUrl);
                UAEPassController.getInstance().getAccessToken(activity, requestModel, new UAEPassAccessTokenCallback() {
                    @Override
                    public void getToken(String accessToken, String error) {
                        if (error != null) {
                            Log.v(TAG, "UAE Pass App: error received:" + error);
                            Toast.makeText(activity, "Error while getting access token", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.v(TAG, "UAE Pass App: code received:" + accessToken);
                            //Toast.makeText(activity, "Access Token Received", Toast.LENGTH_SHORT).show();
                            //getUAESessionToken(accessToken);
                            getUAEAccessToken(accessToken);
                        }
                    }
                });
            } else {
                Log.v(TAG, "UAE Pass App: app is not installed");
                String language = Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0 ? "en" : "ar";
                String authUrl = Global.uaePassConfig.getAuthCodeUAEID_url.endsWith("?") ? Global.uaePassConfig.getAuthCodeUAEID_url : Global.uaePassConfig.getAuthCodeUAEID_url + "?";
                String url = authUrl + "redirect_uri="+callbackUrl+"&client_id="+clientId+"&state="+secretId+"&response_type=code&scope=urn:uae:digitalid:profile:general&acr_values=urn:safelayer:tws:policies:authentication:level:low&ui_locales=" + language;
                Log.v(TAG, "UAE Pass App: getAuthorizationUrl: " + url);

                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.setData(Uri.parse(url));
                //intent.setData(Uri.parse("http://www.google.com"));
                activity.startActivity(intent);
            }
        }


    }

    public void getUAEAccessToken(String code){
        Log.v(TAG, "UAE Pass App: getUAEAccessToken(): calling");
        authListener.onStarted();
        kharetatiApp = KharetatiApp.create(activity);
        if(Global.uaePassConfig != null){
            String clientId = Encryptions.decrypt(Global.uaePassConfig.UAEID_clientid);
            String secretId = Encryptions.decrypt(Global.uaePassConfig.UAEID_secret);
            String callbackUrl = Encryptions.decrypt(Global.uaePassConfig.UAEID_callback_url);
            String language = Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0 ? "en" : "ar";
            String accessTokenUrl = Global.uaePassConfig.getGetAccessTokenUAEID_url().endsWith("?") ? Global.uaePassConfig.getGetAccessTokenUAEID_url() : Global.uaePassConfig.getGetAccessTokenUAEID_url() + "?";
            String url = accessTokenUrl + "grand_type=authorization_code&redirect_uri="+callbackUrl+"&client_id="+clientId+"&state="+secretId+"&response_type=code&scope=urn:uae:digitalid:profile:general&acr_values=urn:safelayer:tws:policies:authentication:level:low&ui_locales=" + language;
            Global.uae_code = "";
            Global.isUAEaccessWeburl = false;
            Disposable disposable = repository.getUAEAccessToken(url)
                    .subscribeOn(kharetatiApp.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<UAEAccessTokenResponse>() {
                        @Override public void accept(UAEAccessTokenResponse accessTokenResponse) throws Exception {
                            Log.v(TAG, "UAE Pass App: getUAEAccessToken(): success");
                            Global.uae_code = "";
                            Global.isUAEaccessWeburl =false;
                            Global.uae_access_token = accessTokenResponse.getAccess_token();
                            Log.v(TAG, "UAE Pass App: getUAEAccessToken(): Access Token:" + accessTokenResponse.getAccess_token());
                            getUAESessionToken(accessTokenResponse.getAccess_token());
                        }
                    }, new Consumer<Throwable>() {
                        @Override public void accept(Throwable throwable) throws Exception {
                            Log.v(TAG, "UAE Pass App: getUAEAccessToken(): failed:" + throwable.getMessage());
                            showErrorMessage();
                        }
                    });

            compositeDisposable.add(disposable);
        }
    }

    public void getUAESessionToken(String code){
        Log.v(TAG, "UAE Pass App: getUAESessionToken(): calling");
        authListener.onStarted();
        kharetatiApp = KharetatiApp.create(activity);

        String url = AppUrls.BASE_AUXULARY_URL_UAE_SESSION + "getsessionuaepass/" + code + "/" + Global.getPlatformRemark();

        Disposable disposable = repository.getSessionUAEPass(url)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SessionUaePassResponse>() {
                    @Override public void accept(SessionUaePassResponse uaeSessionResponse) throws Exception {
                        Log.v(TAG, "UAE Pass App: getUAESessionToken(): success");
                        Global.uaeSessionResponse = uaeSessionResponse;
                        Log.v(TAG, "UAE Pass App: getUAESessionToken(): sessionToken:" + uaeSessionResponse.getService_response().getUAEPASSDetails().getUuid());
                        getAccessTokenAPI(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        Log.v(TAG, "UAE Pass App: getUAESessionToken(): failed:" + throwable.getMessage());
                        showErrorMessage();
                    }
                });

        compositeDisposable.add(disposable);
    }
}
