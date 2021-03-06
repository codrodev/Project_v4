package dm.sime.com.kharetati.view.activities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import ae.sdg.libraryuaepass.UAEPassController;
import dm.sime.com.kharetati.BuildConfig;
import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.ActivityLoginBinding;
import dm.sime.com.kharetati.datas.models.UaePassConfig;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.MyApiService;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.CustomContextWrapper;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;

import dm.sime.com.kharetati.utility.constants.AppConstants;
import dm.sime.com.kharetati.datas.models.LoginDetails;
import dm.sime.com.kharetati.datas.models.User;
import dm.sime.com.kharetati.datas.repositories.UserRepository;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.customview.SwitchCompatEx;
import dm.sime.com.kharetati.view.navigators.AuthListener;
import dm.sime.com.kharetati.view.viewModels.UAEPassRequestModels;
import dm.sime.com.kharetati.view.viewmodelfactories.AuthViewModelFactory;

import dm.sime.com.kharetati.view.viewModels.LoginViewModel;

import static dm.sime.com.kharetati.utility.Global.CURRENT_LOCALE;
import static dm.sime.com.kharetati.utility.Global.MYPREFERENCES;
import static dm.sime.com.kharetati.utility.Global.forceUserToUpdateBuild;
import static dm.sime.com.kharetati.utility.Global.generateRandomID;
import static dm.sime.com.kharetati.utility.constants.AppConstants.FONT_SIZE;
import static dm.sime.com.kharetati.utility.constants.AppConstants.USER_LANGUAGE;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_BOOKMARK;

public class LoginActivity extends AppCompatActivity implements AuthListener {


    private AuthViewModelFactory factory;
    private Gson gson;

    UserRepository repository;
    MyApiService apiService;

    private int leftMargin = 16;
    private int rightMargin = 16;
    private int topMargin = 0;
    private int bottomMargin = 5;
    LoginViewModel viewModel;
    public static LoginViewModel loginVM;
    ActivityLoginBinding binding;
    private ProgressBar progressBar;
    private SharedPreferences sharedpreferences;
    private Tracker mTracker;
    private int elementHeight;
    public FirebaseAnalytics firebaseAnalytics;

    public LoginActivity(){

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Global.uae_code = "";
        Global.isUAEaccessWeburl = false;
        Global.uaePassConfig = null;
        Global.uaeSessionResponse = null;
        Global.isUAE = false;
        Global.isUAEAccessToken = false;
        Global.clientID = "";
        Global.state = "";*/
        Global.isLoginActivity = true;

        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FragmentTAGS.FR_LOGIN);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        gson = new GsonBuilder().serializeNulls().create();
        try {
            repository = new UserRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(this)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        factory = new AuthViewModelFactory(this,repository);
        progressBar = new ProgressBar(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        firebaseAnalytics.setCurrentScreen(this, "LOGIN SCREEN", null /* class override */);

        Intent intent = getIntent();

        viewModel = ViewModelProviders.of(this,factory).get(LoginViewModel.class);

        //binding.setViewmodel(viewModel);
        viewModel.authListener = this;
        loginVM = viewModel;
        adjustFontScale(getResources().getConfiguration(),Global.fontScale);
        Global.isUserLoggedIn= false;



        //getting saved locale
        sharedpreferences = getSharedPreferences(Global.MYPREFERENCES, Context.MODE_PRIVATE);
        String locale = sharedpreferences.getString(USER_LANGUAGE, "defaultStringIfNothingFound");
        //String locale = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(USER_LANGUAGE, "defaultStringIfNothingFound");
        if(!locale.equals("defaultStringIfNothingFound"))
            CURRENT_LOCALE =locale;
        if(Global.isLogout && !Global.isTimeout){
            AlertDialogUtil.successfulLogoutAlert(LoginActivity.this.getResources().getString(R.string.successful_logout),LoginActivity.this.getResources().getString(R.string.ok),LoginActivity.this);
        }



        //getting remembered user credentials if any

        if(CURRENT_LOCALE.equals("en"))binding.cardLogin.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.cardLogin.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en"))binding.layoutEnableUAEPass.setImageDrawable(getResources().getDrawable(R.drawable.uaelogin_en));else binding.layoutEnableUAEPass.setImageDrawable(getResources().getDrawable(R.drawable.uaelogin_ar));



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Global.getUsernamesFromHistory(this));
        binding.editUserName.setAdapter(adapter);



        binding.editUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sharedpreferences.edit().putString("username",s.toString()).apply();
            }
        });
        binding.editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sharedpreferences.edit().putString("password",s.toString()).apply();
            }
        });

        LoginDetails loginDetails = Global.getUserLoginDeatils(this);
        if (loginDetails != null && loginDetails.username != null && loginDetails.pwd != null && Global.isRememberLogin(this)) {
            if(!loginDetails.username.equals(""))binding.editUserName.setText(loginDetails.username);
            if(!loginDetails.pwd.equals(""))binding.editPassword.setText(loginDetails.pwd);
            binding.editUserName.dismissDropDown();
            Global.enableClearTextInEditBox(binding.editUserName,LoginActivity.this);
            Global.enableClearTextInEditBox(binding.editPassword,LoginActivity.this);

        }
        else
        {
            if(Global.isLanguageChanged){
                binding.editUserName.setText(sharedpreferences.getString("username",""));
                binding.editPassword.setText(sharedpreferences.getString("password",""));
            }
            else{
                binding.editUserName.setText("");
                binding.editPassword.setText("");
            }
        }


        binding.editUserName.requestFocus();
        binding.cardLogin.setVisibility(View.GONE);
//        //binding.layoutUAEPass.setVisibility(View.GONE);


        binding.switchLanguage.setVisibility( View.GONE);
        Global.enableClearTextInEditBox(binding.editUserName,LoginActivity.this);
        Global.enableClearTextInEditBox(binding.editPassword,LoginActivity.this);

        binding.editUserName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                binding.editUserName.setFocusableInTouchMode(true);

                return false;
            }
        });
        binding.editPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                binding.editPassword.setFocusableInTouchMode(true);

                return false;
            }
        });


        binding.editUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.showSoftKeyboard(v,LoginActivity.this);
            }
        });
        binding.editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.showSoftKeyboard(v,LoginActivity.this);

            }
        });
        binding.switchLanguage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getActionMasked() == MotionEvent.ACTION_MOVE;
            }
        });
        binding.switchLanguageUae.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getActionMasked() == MotionEvent.ACTION_MOVE;
            }
        });
        binding.chkRememberMe.setChecked(Global.isRememberLogin(this));
        binding.chkRememberMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.rememberUser(binding.chkRememberMe.isChecked(), LoginActivity.this);
                sharedpreferences.edit().putString("username",binding.editUserName.getText().toString()).apply();
                sharedpreferences.edit().putString("password",binding.editPassword.getText().toString()).apply();
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.isConnected(LoginActivity.this)){
                viewModel.setCredentials(binding.editUserName.getText().toString().trim(), binding.editPassword.getText().toString().trim());
                viewModel.onLoginButtonClick();
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Login Screen")
                            .setAction("Action Login")
                            .setLabel("Dubai Id User Logged In")
                            .setValue(1)
                            .build());
                }
                else{
                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog("",Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr(),getResources().getString(R.string.ok),LoginActivity.this);
                    else
                        AlertDialogUtil.errorAlertDialog("",getResources().getString(R.string.internet_connection_problem1),getResources().getString(R.string.ok),LoginActivity.this);
                }

            }
        });
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.isConnected(LoginActivity.this)){
                    AlertDialogUtil.registerAlert(getString(R.string.register),getString(R.string.ok),getString(R.string.cancel),LoginActivity.this);
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Login Screen")
                            .setAction("Action Sign Up")
                            .setLabel("Create New Account")
                            .setValue(1)
                            .build());
                }
                else{
                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog("",Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr(),getResources().getString(R.string.ok),LoginActivity.this);
                    else
                        AlertDialogUtil.errorAlertDialog("",getResources().getString(R.string.internet_connection_problem1),getResources().getString(R.string.ok),LoginActivity.this);
                }

            }
        });
        binding.newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.isConnected(LoginActivity.this)){
                    AlertDialogUtil.registerAlert(getString(R.string.register),getString(R.string.ok),getString(R.string.cancel),LoginActivity.this);
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Login Screen")
                            .setAction("Action Sign Up")
                            .setLabel("Create New Account")
                            .setValue(1)
                            .build());
                }
                else{
                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog("",Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr(),getResources().getString(R.string.ok),LoginActivity.this);
                    else
                        AlertDialogUtil.errorAlertDialog("",getResources().getString(R.string.internet_connection_problem1),getResources().getString(R.string.ok),LoginActivity.this);
                }

            }
        });
        if(CURRENT_LOCALE.equals("en"))binding.imageUAE.setImageResource(R.drawable.uaelogin_en);else binding.imageUAE.setImageResource(R.drawable.uaelogin_ar);


        binding.txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialogUtil.forgotPasswordAlert(getString(R.string.forgot_password),getString(R.string.ok),getString(R.string.cancel),LoginActivity.this);
            }
        });

        binding.txtGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(Global.isConnected(LoginActivity.this)){
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    Global.loginDetails.username = binding.editUserName.getText().toString().trim()!=""?binding.editUserName.getText().toString().trim():"";
                    Global.loginDetails.pwd = binding.editPassword.getText().toString().trim()!=""?binding.editPassword.getText().toString().trim():"";
                    if(Global.loginDetails!=null)
                        sharedpreferences.edit().putString(AppConstants.USER_LOGIN_DETAILS, gson.toJson(Global.loginDetails)).apply();
                    viewModel.onGuestLoginButtonClick();
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Login Screen")
                            .setAction("Action Login")
                            .setLabel("Guest User Logged In")
                            .setValue(1)
                            .build());

                }
                else{
                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog("",Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr(),getResources().getString(R.string.ok),LoginActivity.this);
                    else
                        AlertDialogUtil.errorAlertDialog("",getResources().getString(R.string.internet_connection_problem1),getResources().getString(R.string.ok),LoginActivity.this);
                }
            }
        });

        binding.imgUAEPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.isConnected(LoginActivity.this)){
                    viewModel.login();
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Login Screen")
                            .setAction("Action Login")
                            .setLabel("UAEPass User Logged In")
                            .setValue(1)
                            .build());
                }
                else{
                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog("",Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr(),getResources().getString(R.string.ok),LoginActivity.this);
                    else
                        AlertDialogUtil.errorAlertDialog("",getResources().getString(R.string.internet_connection_problem1),getResources().getString(R.string.ok),LoginActivity.this);
                }
            }
        });
        binding.layoutEnableUAEPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.isConnected(LoginActivity.this)){
                    viewModel.login();
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Login Screen")
                            .setAction("Action Login")
                            .setLabel("UAEPass User Logged In")
                            .setValue(1)
                            .build());
                }
                else{
                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog("",Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr(),getResources().getString(R.string.ok),LoginActivity.this);
                    else
                        AlertDialogUtil.errorAlertDialog("",getResources().getString(R.string.internet_connection_problem1),getResources().getString(R.string.ok),LoginActivity.this);
                }
            }
        });


        binding.txtUAEPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.isConnected(LoginActivity.this)){
                    viewModel.login();
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Login Screen")
                            .setAction("Action Login")
                            .setLabel("UAEPass User Logged In")
                            .setValue(1)
                            .build());
                }
                else{
                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog("",Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr(),getResources().getString(R.string.ok),LoginActivity.this);
                    else
                        AlertDialogUtil.errorAlertDialog("",getResources().getString(R.string.internet_connection_problem1),getResources().getString(R.string.ok),LoginActivity.this);
                }
            }
        });


        binding.editUserName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        binding.editUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (!Global.isConnected(LoginActivity.this)) {
                        if (Global.appMsg != null)
                            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getResources().getString(R.string.ok), LoginActivity.this);
                        else
                            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), LoginActivity.this);
                    } else {
                        if(!viewModel.isValidEmail(binding.editUserName.getText().toString())||binding.editUserName.getText().toString().trim().length()<=5) {
                            onFailure(getResources().getString(R.string.enter_valid_username));
                            binding.editUserName.requestFocus();
                        }
                        else
                            binding.editPassword.requestFocus();

                    }
                    return true;
                }
                return false;

            }
        });


        binding.editPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (!Global.isConnected(LoginActivity.this)) {
                        if (Global.appMsg != null)
                            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getResources().getString(R.string.ok), LoginActivity.this);
                        else
                            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), LoginActivity.this);
                    } else {
                        if(binding.editUserName.getText()!=null){

                            if(binding.editPassword.getText()!=null){
                                if(binding.editPassword.getText().toString().length()>=5){
                                viewModel.setCredentials(binding.editUserName.getText().toString().trim(), binding.editPassword.getText().toString().trim());
                                binding.btnLogin.requestFocus();
                                viewModel.onLoginButtonClick();}
                                else
                                    onFailure(getString(R.string.enter_valid_password));

                            }
                            else
                                onFailure(getString(R.string.enter_password));
                        }
                        else
                            onFailure(getString(R.string.enter_username));

                    }
                    return true;
                }
                return false;

            }
        });
        binding.layoutMyIDUAEPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Global.isConnected(LoginActivity.this)){
                    viewModel.login();
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Login Screen")
                            .setAction("Action Login")
                            .setLabel("UAEPass User Logged In")
                            .setValue(1)
                            .build());
                }
                else{
                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog("",Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr(),getResources().getString(R.string.ok),LoginActivity.this);
                    else
                        AlertDialogUtil.errorAlertDialog("",getResources().getString(R.string.internet_connection_problem1),getResources().getString(R.string.ok),LoginActivity.this);
                }
            }
        });
        binding.layoutEnableUAEPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Global.isConnected(LoginActivity.this)){
                    viewModel.login();
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Login Screen")
                            .setAction("Action Login")
                            .setLabel("UAEPass User Logged In")
                            .setValue(1)
                            .build());
                }
                else{
                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog("",Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr(),getResources().getString(R.string.ok),LoginActivity.this);
                    else
                        AlertDialogUtil.errorAlertDialog("",getResources().getString(R.string.internet_connection_problem1),getResources().getString(R.string.ok),LoginActivity.this);
                }
            }
        });
        binding.txtMyIDGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Global.isConnected(LoginActivity.this)){
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    Global.loginDetails.username = binding.editUserName.getText().toString().trim()!=""?binding.editUserName.getText().toString().trim():"";
                    Global.loginDetails.pwd = binding.editPassword.getText().toString().trim()!=""?binding.editPassword.getText().toString().trim():"";
                    if(Global.loginDetails!=null)
                        sharedpreferences.edit().putString(AppConstants.USER_LOGIN_DETAILS, gson.toJson(Global.loginDetails)).apply();
                    viewModel.onGuestLoginButtonClick();
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Login Screen")
                            .setAction("Action Login")
                            .setLabel("Guest User Logged In")
                            .setValue(1)
                            .build());

                }
                else{
                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog("",Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr(),getResources().getString(R.string.ok),LoginActivity.this);
                    else
                        AlertDialogUtil.errorAlertDialog("",getResources().getString(R.string.internet_connection_problem1),getResources().getString(R.string.ok),LoginActivity.this);
                }
            }
        });

        if(CURRENT_LOCALE.equals("en")){
            binding.switchLanguage.setTextOff( "English" );
            binding.switchLanguage.setTextOn( "العربية" );
        }
        else{
            binding.switchLanguage.setTextOn( "English" );
            binding.switchLanguage.setTextOff( "العربية" );

        }

        binding.switchLanguage.setOnClickListener(new View.OnClickListener() {
            private boolean isChecked;

            @Override
            public void onClick(View v) {


                Global.isLanguageChanged = true;

                    ((SwitchCompatEx)v).setChecked(false);
                    CURRENT_LOCALE = (CURRENT_LOCALE.equals("ar")) ? "en" : "ar";

                    sharedpreferences.edit().putString(USER_LANGUAGE,CURRENT_LOCALE).apply();

                    Global.changeLang(CURRENT_LOCALE, LoginActivity.this);


                    recreate();
                if(CURRENT_LOCALE.equals("en")){
                    binding.switchLanguage.setTextOff( "English" );
                    binding.switchLanguage.setTextOn( "العربية" );
                }
                else{
                    binding.switchLanguage.setTextOn( "English" );
                    binding.switchLanguage.setTextOff( "العربية" );

                }



            }
        });
        binding.switchLanguageUae.setOnClickListener(new View.OnClickListener() {
            private boolean isChecked;

            @Override
            public void onClick(View v) {


                Global.isLanguageChanged = true;

                    ((SwitchCompatEx)v).setChecked(false);
                    CURRENT_LOCALE = (CURRENT_LOCALE.equals("ar")) ? "en" : "ar";

                    sharedpreferences.edit().putString(USER_LANGUAGE,CURRENT_LOCALE).apply();

                    Global.changeLang(CURRENT_LOCALE, LoginActivity.this);

                    recreate();
                if(CURRENT_LOCALE.equals("en")){
                    binding.switchLanguage.setTextOff( "English" );
                    binding.switchLanguage.setTextOn( "العربية" );
                }
                else{
                    binding.switchLanguage.setTextOn( "English" );
                    binding.switchLanguage.setTextOff( "العربية" );

                }



            }
        });



        Global.deviceId =  Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        if(Global.deviceId==null)
            Global.deviceId = generateRandomID();



        binding.layoutRoot.postDelayed(new Runnable() {


            @Override
            public void run() {

            }
        },0);//
        if(!Global.isLanguageChanged){
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            viewModel.uaePassConfigAPI();

                        }
                    });

                }
            }, 3000);
        }

        else
            displayContent();


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Global.alertDialog!=null){
            Global.alertDialog.cancel();
            Global.alertDialog = null;
        }
    }

    @Override
    public void recreate() {

        finish();
        startActivity(getIntent());

        overridePendingTransition(0, 0);
        if(Global.alertDialog!=null)
            Global.alertDialog=null;
        //Global.isRecreate =true;


    }

    private void displayContent(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Global.width = (int)binding.layoutRoot.getWidth();
                Global.height = (int)binding.layoutRoot.getHeight();
                elementHeight = (int) ((((Global.height/2)+(Global.height/4))-160)/10);

                binding.imgBackground.setLayoutParams( new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                binding.imgBackground.setScaleType(ImageView.ScaleType.FIT_XY);
                binding.slantViewLoginHeader.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,((int)(Global.height/2) +100)));

                LinearLayout.LayoutParams cardParams;
                if(Global.uaePassConfig.disableMyId)
                    cardParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)((Global.height/2)-(Global.height/8)));
                else {
                    if (Global.width == 1080 && Global.height == 2123) {
                        cardParams =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Global.height));

                    } else
                        cardParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ((Global.height / 2) + (Global.height / 3)));
                }

                cardParams.setMargins(48,0,48,48);
                binding.cardLogin.setLayoutParams(cardParams);








                //Slant View

                LinearLayout.LayoutParams slantViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Global.height<=800?(Global.height<=480?((int)(Global.height/2))+20:((int)(Global.height/2))):((int)(Global.height/2) +150));
                slantViewParams.gravity = Gravity.CENTER_HORIZONTAL;
                slantViewParams.setMargins(0, (Global.width<=480?(int) -Global.height:((int) ((Global.width>720||(Global.width<=1080)?(-Global.height)-20:(-Global.height)+100)))),0,0);
                binding.slantViewLoginHeader.setLayoutParams(slantViewParams);

                //Kharetati Logo Layout

                LinearLayout.LayoutParams logoLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,((int)(Global.height/2)));
                logoLayoutParams.gravity = Gravity.CENTER;
                logoLayoutParams.setMargins(leftMargin, (int) -(Global.height/2),rightMargin,bottomMargin);
                binding.layoutLogo.setLayoutParams(logoLayoutParams);

                Animation anim = new ScaleAnimation(1f, 1.4f, 1f, 1.4f, Animation.RELATIVE_TO_PARENT,.5f, Animation.RELATIVE_TO_PARENT,.5f);
                anim.setStartOffset(1000);
                anim.setDuration(600);
                anim.setFillEnabled(true);
                anim.setFillAfter(true);
                anim.setInterpolator(new AccelerateDecelerateInterpolator());

                binding.imgsplash.setVisibility(View.GONE);
                binding.copyrightArabic.setVisibility(View.GONE);
                binding.copyrightEnglish.setVisibility(View.GONE);
                binding.logoSplash.setVisibility(View.GONE);
                binding.splashLogoLayout.setVisibility(View.GONE);
                binding.imgBackground.setVisibility(View.VISIBLE);

                binding.imgBackground.setPadding(0, (int) ((Global.height/100)*24),0,0);


                if(!Global.isLanguageChanged){
                    binding.imgBackground.startAnimation(anim);


                    binding.imgBackground.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            animateView(binding.imgBackground, 1000L,500L,true,0f,0f,0f,Global.height);
                            animateView(binding.slantViewLoginHeader,1000L,1000L,true,0f,0f,0f, (-Global.height/4)+100);
                            animateView(binding.cardLogin,1000L,1000L,true,0f,0f,Global.height, (-Global.height/2)+Global.height/3);



                            if(!Global.uaePassConfig.disableMyId){
                                binding.layoutEnableUAEPass.setVisibility(View.GONE);

                                binding.layoutUae.setVisibility(View.GONE);
                                binding.layoutUAEPass.setVisibility(View.GONE);

                                LinearLayout.LayoutParams layoutUaepassarams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,binding.cardLogin.getHeight()-175);

                                layoutUaepassarams.gravity = Gravity.CENTER_HORIZONTAL;
                                layoutUaepassarams.setMargins(32, -275,32,0);
                                binding.layoutUAEPass.setLayoutParams(layoutUaepassarams);
                                LinearLayout.LayoutParams cardViewParams;
                                if(Global.uaePassConfig.disableMyId)
                                    cardViewParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)((Global.height/2)-(Global.height/5)));
                                else{
                                    if (Global.width == 1080 && Global.height == 2123) {
                                        cardViewParams =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Global.height));

                                    } else
                                        cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ((Global.height / 2) + (Global.height / 3)));
                                }
//
                                cardViewParams.gravity = Gravity.CENTER_HORIZONTAL;
                                int cardmarginTop;
                                if(Global.height>=2560){
                                    cardmarginTop= (int) ((-Global.height/4)+600);
                                }
                                else if(Global.height<=2131){
                                    cardViewParams.setMargins(48,-2131,48,48);
                                }
                                else
                                    cardViewParams.setMargins(48, Global.height>=2560?(int) ((-Global.height/4)+600):(Global.height<=1280?(Global.width<=360?(int)(-Global.height/4)+200:(int) ((-Global.height/4)+350)):(Global.width<=1080?(int) ((-Global.height/4)+451):(int) ((-Global.height/4)+500))),48,20);
                                binding.cardLogin.setLayoutParams(cardViewParams);


                            }
                            else{
                                LinearLayout.LayoutParams cardViewParams;
                                if(Global.uaePassConfig.disableMyId){
                                    cardViewParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(int)((Global.height/4)+(Global.height/6)+200));
                                }
                                else{
                                    if (Global.width == 1080 && Global.height == 2123) {
                                        cardViewParams =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Global.height));

                                    } else
                                        cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ((Global.height / 2) + (Global.height / 3)));
                                }

                                cardViewParams.gravity = Gravity.CENTER_HORIZONTAL;

                                cardViewParams.setMargins(24, Global.height>=2560?(int) ((-Global.height/4)+600):(Global.height<=1280?(Global.width<=480?(int)(-Global.height/4)+250:(Global.width<=640?(int)(-Global.height/4)+300:(int) ((-Global.height/4)+350))):(Global.width<=1080?(int) ((-Global.height/4)+560):(int) ((-Global.height/4)+500))),24,20);
                                binding.cardLogin.setLayoutParams(cardViewParams);
                                binding.txtGuest.setVisibility(View.VISIBLE);
                                binding.txtLoginwith.setPaddingRelative(80,0,0,0);

                                LinearLayout.LayoutParams guestLoginParams = new LinearLayout.LayoutParams(SwitchCompatEx.dp2Px(Global.width<=480?260f:(Global.width<=640?220f:(Global.width<=720?300f:((Global.width<=1080||Global.width<=1440)?224f:220f)))), SwitchCompatEx.dp2Px(Global.width<=480?44f:(Global.width<=640?30f:((Global.width<=720||Global.width<=1080||Global.width<=1440)?40f:30f))));
                                guestLoginParams.gravity = Gravity.CENTER;
                                guestLoginParams.setMargins(SwitchCompatEx.dp2Px(Global.width<=480?60f:70f), topMargin+24, SwitchCompatEx.dp2Px(Global.width<=480?60f:70f), bottomMargin);

                                binding.txtGuest.setLayoutParams(guestLoginParams);
                                binding.txtGuest.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                binding.txtGuest.setGravity(Gravity.CENTER);

                            }
                            if(Global.uaePassConfig.disableMyId){
                                binding.switchLanguageUae.setVisibility(View.VISIBLE);
                                binding.switchLanguage.setVisibility(View.GONE);
                            }
                            else{
                                binding.switchLanguage.setVisibility( View.VISIBLE);
                                binding.switchLanguageUae.setVisibility(View.GONE);
                            }

                        }
                    },1500);



                }
                else{

                    onMyIDDisabled(Global.uaePassConfig.disableMyId);
                    binding.imgBackground.setVisibility( View.GONE);
                    binding.switchLanguage.setVisibility(View.VISIBLE);
                    binding.cardLogin.setVisibility(View.VISIBLE);
                    binding.slantViewLoginHeader.setVisibility(View.VISIBLE);
                    binding.copyrightEnglish.setVisibility(View.GONE);
                    binding.copyrightArabic.setVisibility(View.GONE);
                    binding.txtLoginwith.setPaddingRelative(0,0,0,0);
                    LinearLayout.LayoutParams logoLayoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,((int)(Global.height/2)));
                    logoLayoutParams1.gravity = Gravity.CENTER;
                    logoLayoutParams1.setMargins(leftMargin, (int) (-(Global.height/4)-100),rightMargin,bottomMargin);
                    binding.layoutLogo.setLayoutParams(logoLayoutParams1);

                    LinearLayout.LayoutParams slantViewParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)((Global.height/4)+250));
                    slantViewParams1.gravity = Gravity.CENTER_HORIZONTAL;
                    //slantViewParams.setMargins(0,(-height).toInt(),0,0)
                    binding.slantViewLoginHeader.setLayoutParams(slantViewParams1);
                    LinearLayout.LayoutParams cardViewParams;
                    LinearLayout.LayoutParams switchLanguageparams;
                    if(Global.uaePassConfig.disableMyId){
                        cardViewParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)((Global.height/4)+(Global.height/6)+200));

                    }
                    else{
                        cardViewParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)((Global.height/2)+(Global.height/6)));

                    }
//
                    cardViewParams.gravity = Gravity.CENTER_HORIZONTAL;
                    if(Global.uaePassConfig.disableMyId)
                        cardViewParams.setMargins(48, (int)((-Global.height/2)+(Global.height/3)),48,20);
                    else
                        cardViewParams.setMargins(24, Global.height>=2560?(int) ((-Global.height/4)+600):(Global.height<=1280?(Global.width<=480?(int)(-Global.height/4)+250:(Global.width<=640?(int)(-Global.height/4)+300:(int) ((-Global.height/4)+350))):(Global.width<=1080?(int) ((-Global.height/4)+451):(int) ((-Global.height/4)+500))),24,20);

                    binding.cardLogin.setLayoutParams(cardViewParams);
                    if(Global.uaePassConfig.disableMyId){
                        binding.switchLanguageUae.setVisibility(View.VISIBLE);
                        binding.switchLanguage.setVisibility(View.GONE);
                    }
                    else{
                        binding.switchLanguage.setVisibility( View.VISIBLE);
                        binding.switchLanguageUae.setVisibility(View.GONE);
                    }


                }

            }

        }, Global.isLanguageChanged?0:2000);



    }

    public void animateView( View view,Long duration, Long startOffSet, Boolean isFillAfter, Float fromXDelta, Float toXDelta, Float fromYDelta,Float toYDelta) {


        ValueAnimator animate = ValueAnimator.ofFloat(fromYDelta, toYDelta);

        animate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                view.setTranslationY(value);

            }
        });

        animate.setDuration(duration);

        animate.setInterpolator(new LinearInterpolator());

        animate.start();
        if(view == binding.cardLogin){
            binding.cardLogin.setVisibility( View.VISIBLE);
            if(Global.uaePassConfig.disableMyId){
                binding.switchLanguageUae.setVisibility(View.VISIBLE);
                binding.switchLanguage.setVisibility(View.GONE);
            }
            else{
                binding.switchLanguage.setVisibility( View.VISIBLE);
                binding.switchLanguageUae.setVisibility(View.GONE);
            }
            binding.copyrightEnglish.setVisibility(View.GONE);
            binding.copyrightArabic.setVisibility(View.GONE);
            binding.logoSplash.setVisibility(View.GONE);


        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(Global.alertDialog!=null){
              Global.alertDialog.cancel();
              Global.alertDialog =null;

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Global.isLoginActivity = true;

            sharedpreferences = newBase.getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
            String locale = sharedpreferences.getString(USER_LANGUAGE, "defaultStringIfNothingFound");
            Global.uaePassConfig =Global.uaePassConfig == null? new UaePassConfig():Global.uaePassConfig;
            Global.uaePassConfig.disableMyId = sharedpreferences.getBoolean("isDisableMyId",false);
            Global.fontScale =sharedpreferences.getFloat(FONT_SIZE,1f);


            if(!locale.equals("defaultStringIfNothingFound"))
                CURRENT_LOCALE =locale;
            else
                CURRENT_LOCALE ="en";

            super.attachBaseContext(CustomContextWrapper.wrap(newBase, CURRENT_LOCALE));

    }
    public  void adjustFontScale(Configuration configuration, float scale) {

        configuration.fontScale = scale;
        DisplayMetrics metrics = LoginActivity.this.getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        Objects.requireNonNull(wm).getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        LoginActivity.this.getResources().updateConfiguration(configuration, metrics);

    }

    @Override
    protected void onResume() {
        super.onResume();
        CURRENT_LOCALE = CURRENT_LOCALE.equals("en")?"en":"ar";
        Global.enableClearTextInEditBox(binding.editUserName,LoginActivity.this);
        Global.enableClearTextInEditBox(binding.editPassword,LoginActivity.this);
        if(Global.isUAEaccessWeburl && Global.uae_code != null && Global.uae_code.length() > 0){
            //viewModel.getUAESessionToken(Global.uae_access_token);
            viewModel.getUAEAccessToken(Global.uae_code);
        }
        if(Global.isfromWebViewCancel && !UAEPassRequestModels.isPackageInstalled(getPackageManager())){
            AlertDialogUtil.errorAlertDialog("",getString(R.string.uaeloginfail),getString(R.string.ok),LoginActivity.this);
            Global.isfromWebViewCancel=false;
        }
        else if(Global.sessionErrorMsg!=null && !UAEPassRequestModels.isPackageInstalled( getPackageManager())){
            AlertDialogUtil.errorAlertDialog("",Global.sessionErrorMsg,getString(R.string.ok),LoginActivity.this);
            Global.sessionErrorMsg =null;
        }
    }

    //UAE PASS START -- Callback to handle UAE Pass callback
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null && intent.getData() != null) {
            if (BuildConfig.URI_SCHEME.equals(intent.getData().getScheme())) {
                UAEPassController.INSTANCE.resume(intent.getDataString());
            }
        }
    }
//UAE PASS END -- Callback to handle UAE Pass callback

    @Override
    public void onStarted() {

        AlertDialogUtil.showProgressBar(LoginActivity.this,true);
    }

    @Override
    public void onSuccess() {


        binding.txtOR.setVisibility(View.VISIBLE);
        AlertDialogUtil.showProgressBar(LoginActivity.this,false);
        finish();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void finish() {
        Global.isLoginActivity = false;
        Global.isLogout = false;
        Global.isTimeout =false;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        }
        else {
            super.finish();
        }
    }

    @Override
    public void onFailure(String message) {

        AlertDialogUtil.showProgressBar(LoginActivity.this,false);

        AlertDialogUtil.errorAlertDialog("",message,getResources().getString(R.string.ok),this);

    }

    @Override
    public void onProgress() {

    }

    @Override
    public void addUserToHistory(String Username) {
        Global.addToUserNamesHistory(Username, LoginActivity.this);
    }

    @Override
    public void saveUserToRemember(LoginDetails loginDetails) {

        sharedpreferences.edit().putString(AppConstants.USER_LOGIN_DETAILS, gson.toJson(loginDetails)).apply();

    }

    @Override
    public void saveUser(User user) {
        Global.saveUser(LoginActivity.this,user);

    }

    @Override
    public void showAppUpdateAlert() throws PackageManager.NameNotFoundException {

        String localversion = "";
        PackageInfo pInfo = this.getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
        int versionCode;




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            localversion = String.valueOf(pInfo.getLongVersionCode());
        else
            localversion = String.valueOf(pInfo.versionCode);
        String msg;

        msg = (CURRENT_LOCALE.equals("en"))?Global.forceUserToUpdateBuild_msg_en : Global.forceUserToUpdateBuild_msg_ar;


        if (Global.versionCompare(Global.CurrentAndroidVersion, localversion) > 0 && forceUserToUpdateBuild)
        {
            // Show App update alert here
            if(msg!=null && msg!="")AlertDialogUtil.forceUpdateAlert(msg,getResources().getString(R.string.ok),getResources().getString(R.string.cancel),LoginActivity.this);


        }

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(LoginActivity.this,message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConfig(boolean status) {
        progressBar.setVisibility(View.GONE);
        AlertDialogUtil.showProgressBar(this,false);

        onMyIDDisabled(status);

        displayContent();
    }
    public void onMyIDDisabled(boolean status){
        sharedpreferences.edit().putBoolean("isDisableMyId",status).apply();
        //status= false; //comment after testing
        if(status){
            binding.loginContainer.setVisibility(View.GONE);
            binding.imageDubaiID.setVisibility(View.GONE);
            binding.layoutEnableUAEPass.setVisibility(View.VISIBLE);
            binding.layoutUae.setVisibility(View.VISIBLE);
            binding.layoutUAEPass.setVisibility(View.GONE);
            binding.txtMyIDGuest.setVisibility(View.GONE);
            if(Global.uaePassConfig.disableMyId){
                binding.switchLanguageUae.setVisibility(View.VISIBLE);
                binding.switchLanguage.setVisibility(View.GONE);
            }
            else{
                binding.switchLanguage.setVisibility( View.VISIBLE);
                binding.switchLanguageUae.setVisibility(View.GONE);
            }



            LinearLayout.LayoutParams guestLoginParams = new LinearLayout.LayoutParams(SwitchCompatEx.dp2Px(Global.width<=480?260f:(Global.width<=640?220f:(Global.width<=720?204f:((Global.width<=1080||Global.width<=1440)?224f:220f)))), SwitchCompatEx.dp2Px(Global.width<=480?44f:(Global.width<=640?30f:((Global.width<=720||Global.width<=1080||Global.width<=1440)?40f:30f))));
            guestLoginParams.gravity = Gravity.CENTER;
            guestLoginParams.setMargins(SwitchCompatEx.dp2Px(Global.width<=480?60f:70f), topMargin+24, SwitchCompatEx.dp2Px(Global.width<=480?60f:70f), bottomMargin);
            binding.txtGuest.setLayoutParams(guestLoginParams);
            binding.txtGuest.setGravity(Gravity.CENTER_VERTICAL);

        } else {
            binding.layoutUae.setVisibility(View.GONE);
            binding.layoutUAEPass.setVisibility(View.GONE);
            binding.loginContainer.setVisibility(View.VISIBLE);
            binding.imageDubaiID.setVisibility(View.VISIBLE);
            binding.layoutEnableUAEPass.setVisibility(View.GONE);

            binding.imgUAEPass.setVisibility(View.VISIBLE);
            binding.txtUAEPass.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams uaePassImageParams = new LinearLayout.LayoutParams(150,150);
            uaePassImageParams.gravity = Gravity.CENTER;
            uaePassImageParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
            binding.imgEnabledUAEPass.setLayoutParams(uaePassImageParams);
            binding.imgEnabledUAEPass.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams guestLoginParams = new LinearLayout.LayoutParams(SwitchCompatEx.dp2Px(224f), SwitchCompatEx.dp2Px(40f));
            guestLoginParams.gravity = Gravity.CENTER_VERTICAL;
            guestLoginParams.setMargins(leftMargin, topMargin-80, rightMargin, bottomMargin - 5);
            binding.txtGuest.setLayoutParams(guestLoginParams);
            binding.txtGuest.setGravity(Gravity.CENTER_VERTICAL);
            binding.txtGuest.setTextSize(16f);
            binding.txtUAEPass.setTextSize(16f);
            if(Global.uaePassConfig.disableMyId){
                binding.switchLanguageUae.setVisibility(View.VISIBLE);
                binding.switchLanguage.setVisibility(View.GONE);
            }
            else{
                binding.switchLanguage.setVisibility( View.VISIBLE);
                binding.switchLanguageUae.setVisibility(View.GONE);
            }


        }
    }
}
