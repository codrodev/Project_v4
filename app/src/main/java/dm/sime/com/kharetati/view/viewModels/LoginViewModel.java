package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.Bindable;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Observable;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.KharetatiUser;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.MyApiService;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
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

    Activity activity;

    private String email;
    private String password;
    public AuthListener authListener;
    public MyApiService apiService;
    private User user = new User();
    private UserRepository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private KharetatiApp kharetatiApp;
    private static String guestName,guestPassword;


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
                        guestLogin(kharetatiUser);

                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        authListener.onFailure("Unable to register the Guest User");
                    }
                });

        compositeDisposable.add(disposable);

    }

    private void guestLogin(KharetatiUser kharetatiUser) {


        if (kharetatiUser != null && !kharetatiUser.isError()) {

            Global.sime_userid = kharetatiUser.userID;
            Global.isUserLoggedIn = false;
            Global.arcgis_token = kharetatiUser.getArcgis_token();

            AppUrls.GIS_LAYER_PASSWORD=kharetatiUser.gis_pwd!=null && kharetatiUser.gis_pwd!=""?kharetatiUser.gis_pwd: AppUrls.GIS_LAYER_PASSWORD;
            AppUrls.GIS_LAYER_USERNAME=kharetatiUser.gis_user_name != null && kharetatiUser.gis_user_name!=""?kharetatiUser.gis_user_name: AppUrls.GIS_LAYER_USERNAME;
            AppUrls.GIS_LAYER_URL=kharetatiUser.gis_layer_url != null && kharetatiUser.gis_layer_url!=""?kharetatiUser.gis_layer_url:AppUrls.GIS_LAYER_URL;
            AppUrls.GIS_LAYER_TOKEN_URL=kharetatiUser.gis_token_url != null && kharetatiUser.gis_token_url!=""? kharetatiUser.gis_token_url:AppUrls.GIS_LAYER_TOKEN_URL;
            AppUrls.URL_PLOTFINDER=kharetatiUser.url_plotfinder !=null && kharetatiUser.url_plotfinder!=""? kharetatiUser.url_plotfinder:AppUrls.URL_PLOTFINDER;
            AppUrls.GIS_LAYER_COMMUNITY_URL=kharetatiUser.community_layerid!=null && kharetatiUser.community_layerid!=""?       AppUrls.GIS_LAYER_URL + "/" + kharetatiUser.community_layerid:AppUrls.GIS_LAYER_URL + "/" + AppUrls.community_layerid;
            AppUrls.parcelLayerExportUrl_en=kharetatiUser.parcelLayerExportUrl_en!=null && kharetatiUser.parcelLayerExportUrl_en!=""?       kharetatiUser.parcelLayerExportUrl_en + "?token=" + Global.arcgis_token:AppUrls.parcelLayerExportUrl_en + "?token=" + Global.arcgis_token;
            AppUrls.parcelLayerExportUrl_ar=kharetatiUser.parcelLayerExportUrl_ar!=null && kharetatiUser.parcelLayerExportUrl_ar!=""?       kharetatiUser.parcelLayerExportUrl_ar + "?token=" + Global.arcgis_token:AppUrls.parcelLayerExportUrl_ar + "?token=" + Global.arcgis_token;
            AppUrls.plot_layerid=kharetatiUser.plot_layerid!=null && kharetatiUser.plot_layerid!=""?kharetatiUser.plot_layerid:AppUrls.plot_layerid;
            User user = new User();
            Global.accessToken=kharetatiUser.access_token;
            user.setUsername("GUEST");
            guestName=user.getUsername();
            guestPassword="Guest";

            Global.aboutus_en_url = kharetatiUser.getAboutus_en_url();
            Global.aboutus_ar_url = kharetatiUser.getAboutus_ar_url();
            Global.terms_en_url = kharetatiUser.getTerms_en_url();
            Global.terms_ar_url = kharetatiUser.getTerms_ar_url();
            Global.appMsg = kharetatiUser.getAppMsg();

            authListener.saveUser(user);

            authListener.onSuccess();

        }
    }

    public void onLoginButtonClick() {

        authListener.onStarted();
        if (getDataEmail().isEmpty() && getDataPassword().isEmpty()) {

            authListener.onFailure(activity.getResources().getString(R.string.enter_username_and_password));
        }else if(getDataEmail().isEmpty()){

            authListener.onFailure(activity.getResources().getString(R.string.enter_username));

        } else if(getDataPassword().isEmpty()){

            authListener.onFailure(activity.getResources().getString(R.string.enter_password));

        } else {
            //checking email and password are not null

            kharetatiApp = KharetatiApp.create(activity);
            //MyApiService apiService = kharetatiApp.getApiService();


            Disposable disposable = repository.getAccessToken(getDataEmail(), getDataPassword())
                    .subscribeOn(kharetatiApp.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<AccessTokenResponse>() {
                        @Override public void accept(AccessTokenResponse accessTokenResponse) throws Exception {
                            saveAccessTokenResponse(accessTokenResponse);

                        }
                    }, new Consumer<Throwable>() {
                        @Override public void accept(Throwable throwable) throws Exception {

                        }
                    });

            compositeDisposable.add(disposable);
            //AccessTokenResponse accessTokenResponse = repository.getAccessToken(getDataEmail(), getDataPassword());




        }

    }

    private void saveAccessTokenResponse(AccessTokenResponse accessTokenResponse) {
        if (accessTokenResponse != null) {

            if (accessTokenResponse.getAccessToken() != null) {
                //get the response here

                Global.accessToken = accessTokenResponse.getAccessToken();

                Log.d("Access token : ", Global.accessToken);
                Global.hideSoftKeyboard(activity);
                Global.isUserLoggedIn = true;
                loginDetails.username = email;
                loginDetails.pwd = password;
                //Global.loginDetails.showFormPrefilledOnRememberMe=false;
                Global.arcgis_token = accessTokenResponse.getArcgisToken();
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

                AttachmentBitmap.letter_from_owner = null;
                AttachmentBitmap.emirateId_back = null;
                AttachmentBitmap.emirateId_front = null;
                AttachmentBitmap.land_ownership_certificate = null;
                AttachmentBitmap.passport_copy = null;

                AppUrls.GIS_LAYER_PASSWORD = (accessTokenResponse.getGisPwd() != null && accessTokenResponse.getGisPwd() != "") ? accessTokenResponse.getGisPwd() : AppUrls.GIS_LAYER_PASSWORD;
                AppUrls.GIS_LAYER_USERNAME = (accessTokenResponse.getGisUserName() != null && accessTokenResponse.getGisUserName() != "") ? accessTokenResponse.getGisUserName() : AppUrls.GIS_LAYER_USERNAME;
                AppUrls.GIS_LAYER_URL = (accessTokenResponse.getGisLayerUrl() != null && accessTokenResponse.getGisLayerUrl() != "") ? accessTokenResponse.getGisLayerUrl() : AppUrls.GIS_LAYER_URL;
                AppUrls.GIS_LAYER_TOKEN_URL = (accessTokenResponse.getGisTokenUrl() != null && accessTokenResponse.getGisTokenUrl() != "") ? accessTokenResponse.getGisTokenUrl() : AppUrls.GIS_LAYER_TOKEN_URL;
                AppUrls.URL_PLOTFINDER = (accessTokenResponse.getUrlPlotfinder() != null && accessTokenResponse.getUrlPlotfinder() != "") ? accessTokenResponse.getUrlPlotfinder() : AppUrls.URL_PLOTFINDER;
                AppUrls.GIS_LAYER_COMMUNITY_URL = (accessTokenResponse.getCommunityLayerid() != null && accessTokenResponse.getCommunityLayerid() != "") ? AppUrls.GIS_LAYER_URL.toString() + "/" + accessTokenResponse.getCommunityLayerid() : AppUrls.GIS_LAYER_URL.toString() + "/" + AppUrls.community_layerid;
                AppUrls.parcelLayerExportUrl_en = (accessTokenResponse.getParcelLayerExportUrlEn() != null && accessTokenResponse.getParcelLayerExportUrlEn() != "") ? accessTokenResponse.getParcelLayerExportUrlEn().toString() + "?token=" + Global.arcgis_token : AppUrls.parcelLayerExportUrl_en.toString() + "?token=" + Global.arcgis_token;
                AppUrls.parcelLayerExportUrl_ar = (accessTokenResponse.getParcelLayerExportUrlAr() != null && accessTokenResponse.getParcelLayerExportUrlAr() != "") ? accessTokenResponse.getParcelLayerExportUrlAr().toString() + "?token=" + Global.arcgis_token : AppUrls.parcelLayerExportUrl_ar.toString() + "?token=" + Global.arcgis_token;
                AppUrls.plot_layerid = (accessTokenResponse.getPlotLayerid() != null && accessTokenResponse.getPlotLayerid() != "") ? accessTokenResponse.getPlotLayerid() : AppUrls.plot_layerid;

                authListener.addUserToHistory(loginDetails.username);
                authListener.saveUserToRemember(loginDetails);

                Disposable disposable = repository.getUserDetails(AppUrls.MY_ID_USER_INFO_URL + Global.accessToken)
                        .subscribeOn(kharetatiApp.subscribeScheduler())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<User>() {
                            @Override public void accept(User user) throws Exception {
                                saveUserDetails(user);

                            }
                        }, new Consumer<Throwable>() {
                            @Override public void accept(Throwable throwable) throws Exception {

                            }
                        });

                compositeDisposable.add(disposable);


            } else {
                authListener.onFailure(activity.getResources().getString(R.string.wrong_username_password));
            }
        } else {
            authListener.onFailure(activity.getResources().getString(R.string.server_connect_error));
        }
    }

    private void saveUserDetails(User userDetails) {
        if (userDetails != null) {

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

            HTTPRequestBody body = new HTTPRequestBody();

            Disposable disposable = repository.registerLoggedUser(body)
                    .subscribeOn(kharetatiApp.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<UserRegistration>() {
                        @Override public void accept(UserRegistration userRegistrationResponse) throws Exception {

                            registerUser(userRegistrationResponse);
                        }
                    }, new Consumer<Throwable>() {
                        @Override public void accept(Throwable throwable) throws Exception {



                        }
                    });

            compositeDisposable.add(disposable);

        } else {

            authListener.onFailure("Unable to connect with the remote server");

        }
    }

    private void registerUser(UserRegistration userRegistrationResponse) {
        if (userRegistrationResponse!= null) {

            //User registration is successful
            try {
                authListener.showAppUpdateAlert();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            Global.sime_userid = userRegistrationResponse.getUserID();


            Disposable disposable = repository.getSession(Global.accessToken)
                    .subscribeOn(kharetatiApp.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<SessionResponse>() {
                        @Override public void accept(SessionResponse session) throws Exception {

                            getSession(session);
                        }
                    }, new Consumer<Throwable>() {
                        @Override public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(activity, throwable.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            Log.e("Exception",throwable.getMessage());
                        }
                    });

            compositeDisposable.add(disposable);



        } else {

            Log.d("User Registration :", userRegistrationResponse.getErrorDetail().toString());
            authListener.onFailure("Unable to register user");

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
                    Global.session = session.getSession().getToken();


                    // Navigate to Main Activity Here
                    authListener.onSuccess();

                } else {

                    Log.d("Session :", session.getMessage().toString());
                    authListener.onFailure("Unable to create the Session");
                }
            }

        } else {
            authListener.onFailure("Unable to connect with the remote server");
        }
    }
}
