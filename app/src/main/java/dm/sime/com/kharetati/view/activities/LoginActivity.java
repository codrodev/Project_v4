package dm.sime.com.kharetati.view.activities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

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

    public LoginActivity(){

    }

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
        Intent intent = getIntent();
        /*LinearLayout.LayoutParams progressBarParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        progressBarParams.gravity = Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL;
        progressBarParams.setMargins((int)Global.width/2,  (int)Global.height/2,(int)Global.width/2,(int)Global.height/2);
        progressBar.setLayoutParams(progressBarParams);
        progressBar.setVisibility(View.GONE);
        binding.layoutRoot.addView(progressBar);*/

        /*RelativeLayout layout = new RelativeLayout(this);
        progressBar = new ProgressBar(this,null,android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar,params);
        binding.cardLogin.addView(layout);*/

        //setContentView(layout);


        //viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        viewModel = ViewModelProviders.of(this,factory).get(LoginViewModel.class);

        //binding.setViewmodel(viewModel);
        viewModel.authListener = this;
        loginVM = viewModel;



        //getting saved locale
        sharedpreferences = getSharedPreferences(Global.MYPREFERENCES, Context.MODE_PRIVATE);
        String locale = sharedpreferences.getString(USER_LANGUAGE, "defaultStringIfNothingFound");
        //String locale = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(USER_LANGUAGE, "defaultStringIfNothingFound");
        if(!locale.equals("defaultStringIfNothingFound"))
            CURRENT_LOCALE =locale;

        //getting remembered user credentials if any

        if(CURRENT_LOCALE.equals("en"))binding.cardLogin.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.cardLogin.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Global.getUsernamesFromHistory(this));
        binding.editUserName.setAdapter(adapter);


        Typeface typeface =  Typeface.createFromAsset(getAssets(),"Dubai-Regular.ttf");
        ((SwitchCompat)findViewById(R.id.switchLanguage)).setSwitchTypeface(typeface);
        binding.switchLanguage.trackLabel.setTypeface(typeface);
        binding.switchLanguage.thumbLabel.setTypeface(typeface);

        LoginDetails loginDetails = Global.getUserLoginDeatils(this);
        if (loginDetails != null && loginDetails.username != null && loginDetails.pwd != null && Global.isRememberLogin(this)) {
            if(!loginDetails.username.equals(""))binding.editUserName.setText(loginDetails.username);
            if(!loginDetails.pwd.equals(""))binding.editPassword.setText(loginDetails.pwd);
            binding.editUserName.dismissDropDown();
            Global.enableClearTextInEditBox(binding.editUserName,LoginActivity.this);
            Global.enableClearTextInEditBox(binding.editPassword,LoginActivity.this);

        }


        binding.editUserName.requestFocus();
        binding.cardLogin.setVisibility(View.GONE);
        binding.layoutUAEPass.setVisibility(View.GONE);


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
        binding.chkRememberMe.setChecked(Global.isRememberLogin(this));
        binding.chkRememberMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.rememberUser(binding.chkRememberMe.isChecked(), LoginActivity.this);
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
        /*binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
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
        });*/


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
        binding.txtEnableUAEPass.setOnClickListener(new View.OnClickListener() {
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

        binding.imgEnableUAEPass.setOnClickListener(new View.OnClickListener() {
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
        /*binding.layoutEnableUAEPass.setOnClickListener(new View.OnClickListener() {
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
        });*/

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






        /*binding.switchLanguage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Global.isLanguageChanged = true;
                    CURRENT_LOCALE = (CURRENT_LOCALE.equals("en")) ? "ar" : "en";
                    Global.changeLang(CURRENT_LOCALE, LoginActivity.this);
                    isChecked =false;
                    ((SwitchCompatEx)buttonView).setChecked(isChecked);
                    ((SwitchCompatEx)buttonView).setTextOff((CURRENT_LOCALE.equals("en")) ? "العربية" : "English");
                    recreate();
                }
            }
        });*/
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
                     //PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(USER_LANGUAGE,CURRENT_LOCALE).apply();

                    sharedpreferences.edit().putString(USER_LANGUAGE,CURRENT_LOCALE).apply();

                    Global.changeLang(CURRENT_LOCALE, LoginActivity.this);





                   /* binding.switchLanguage.setTextOff((CURRENT_LOCALE.equals("ar")) ? "العربية" : "English");
                    binding.switchLanguage.setTextOn((CURRENT_LOCALE.equals("ar")) ? "العربية" : "English");*/
                    //binding.switchLanguage.setTextOn((CURRENT_LOCALE.equals("en")) ? "English" : "العربية");
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


        Global.deviceId = FirebaseInstanceId.getInstance().getToken()!=null?FirebaseInstanceId.getInstance().getToken():generateRandomID();



        binding.layoutRoot.postDelayed(new Runnable() {


            @Override
            public void run() {

            }
        },0);//
        if(!Global.isLanguageChanged)
            viewModel.uaePassConfigAPI();
        else
            displayContent();
        //viewModel.uaePassConfigAPI();

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

                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) ((Global.height / 2) - (Global.height / 14)));
//                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                cardParams.setMargins(48,0,48,-32);
                binding.cardLogin.setLayoutParams(cardParams);





                //Slant View

                LinearLayout.LayoutParams slantViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Global.height / 2));
                slantViewParams.gravity = Gravity.CENTER_HORIZONTAL;
                slantViewParams.setMargins(0, (int) -Global.height,0,0);
                binding.slantViewLoginHeader.setLayoutParams(slantViewParams);

                //Kharetati Logo Layout

                LinearLayout.LayoutParams logoLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,((int)(Global.height/2)));
                logoLayoutParams.gravity = Gravity.CENTER;
                logoLayoutParams.setMargins(leftMargin, (int) -(Global.height/2),rightMargin,bottomMargin);
                binding.layoutLogo.setLayoutParams(logoLayoutParams);

                /*//Progress Bar
                LinearLayout.LayoutParams progressBarParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,elementHeight);
                progressBarParams.gravity = Gravity.CENTER_VERTICAL;
                progressBarParams.setMargins(leftMargin,topMargin+5,rightMargin,bottomMargin);
                progressBar.setLayoutParams(progressBarParams);*/


                //dubai Id Logo

                LinearLayout.LayoutParams dubaiIdParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,elementHeight-24);
                dubaiIdParams.gravity = Gravity.CENTER;
                dubaiIdParams.setMargins(leftMargin,topMargin+5,rightMargin,bottomMargin);
                binding.imageDubaiID.setLayoutParams(dubaiIdParams);

                // username editText

                LinearLayout.LayoutParams userNameParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,elementHeight);
                userNameParams.gravity = Gravity.CENTER_VERTICAL;
                userNameParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
                binding.editUserName.setLayoutParams(userNameParams);
                binding.editUserName.setGravity(Gravity.CENTER_VERTICAL);
                binding.editUserName.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);



                /*final Drawable x_editTextUserName = ContextCompat.getDrawable(LoginActivity.this, R.drawable.login_user);
                x_editTextUserName.setBounds(-16, -16, x_editTextUserName.getIntrinsicWidth() - 16, x_editTextUserName.getIntrinsicHeight()-16);
                if(CURRENT_LOCALE.equals("en"))
                    binding.editUserName.setCompoundDrawables(x_editTextUserName, null, null, null);
                else
                    binding.editUserName.setCompoundDrawables(null, null,x_editTextUserName , null);
*/

                //password editText

                LinearLayout.LayoutParams passwordParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,elementHeight);
                passwordParams.gravity = Gravity.CENTER_VERTICAL;
                passwordParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
                binding.editPassword.setLayoutParams(passwordParams);
                binding.editPassword.setGravity( Gravity.CENTER_VERTICAL);
                binding.editPassword.setLayoutDirection( View.LAYOUT_DIRECTION_LOCALE);

                /*final Drawable x_editTextPass = ContextCompat.getDrawable(LoginActivity.this, R.drawable.login_password);
                x_editTextPass.setBounds(-16, -16, x_editTextPass.getIntrinsicWidth() -16, x_editTextPass.getIntrinsicHeight()-16);
                if(CURRENT_LOCALE.equals("en"))
                    binding.editPassword.setCompoundDrawables(x_editTextPass, null,null , null);
                else
                    binding.editPassword.setCompoundDrawables(null, null,x_editTextPass , null);*/
                // rememberme Layout

                LinearLayout.LayoutParams remebermeLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, elementHeight);
                remebermeLayoutParams.gravity = Gravity.CENTER_VERTICAL;
                remebermeLayoutParams.setMargins(leftMargin-4,topMargin,rightMargin-4,bottomMargin);
                binding.layoutRememberMe.setLayoutParams(remebermeLayoutParams);
                binding.layoutRememberMe.setGravity(Gravity.CENTER_VERTICAL);

                //login Button

                LinearLayout.LayoutParams loginButtonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,elementHeight-8);
                loginButtonParams.gravity = Gravity.CENTER_VERTICAL;
                loginButtonParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
                binding.btnLogin.setLayoutParams(loginButtonParams);


                //text Or Layout

                LinearLayout.LayoutParams orLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,elementHeight-8);
                orLayoutParams.gravity = Gravity.CENTER_VERTICAL;
                orLayoutParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin-5);
                binding.layoutOR.setLayoutParams(orLayoutParams);

                // text OR

                LinearLayout.LayoutParams orParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,elementHeight);
                orParams.gravity = Gravity.CENTER_VERTICAL;
                orParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
                binding.txtOR.setLayoutParams(orParams);
                binding.txtOR.setGravity(Gravity.CENTER_VERTICAL);

                // create Account Button

                /*LinearLayout.LayoutParams signupButtonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,elementHeight);
                signupButtonParams.gravity = Gravity.CENTER_VERTICAL;
                signupButtonParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin-5);
                binding.btnSignUp.setLayoutParams(signupButtonParams);
                binding.btnSignUp.setVisibility(View.GONE);*/

                // Guest Text

                if (!Global.uaePassConfig.disableMyId) {

                    LinearLayout.LayoutParams guestLoginParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, elementHeight);
                    guestLoginParams.gravity = Gravity.CENTER_VERTICAL;
                    guestLoginParams.setMargins(leftMargin, topMargin-92, rightMargin, bottomMargin - 5);
                    binding.txtGuest.setLayoutParams(guestLoginParams);
                    binding.txtGuest.setGravity(Gravity.CENTER_VERTICAL);
                    binding.txtGuest.setTextSize(16f);
                }else{
                    LinearLayout.LayoutParams guestLoginParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, elementHeight);
                    guestLoginParams.gravity = Gravity.CENTER_VERTICAL;
                    guestLoginParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin-92 );
                    binding.txtGuest.setLayoutParams(guestLoginParams);
                    binding.txtGuest.setGravity(Gravity.CENTER_VERTICAL);
                    binding.txtGuest.setTextSize(20f);

                }

                //UAE Pass Text

                if (!Global.uaePassConfig.disableMyId) {

                    LinearLayout.LayoutParams uaePassTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    uaePassTextParams.gravity = Gravity.CENTER_VERTICAL;
                    uaePassTextParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                    binding.txtUAEPass.setLayoutParams(uaePassTextParams);
                    binding.txtUAEPass.setGravity(Gravity.CENTER_VERTICAL);
                }
                else{
                    LinearLayout.LayoutParams uaePassTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    uaePassTextParams.gravity = Gravity.CENTER_VERTICAL;
                    uaePassTextParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                    binding.txtEnableUAEPass.setLayoutParams(uaePassTextParams);
                    binding.txtEnableUAEPass.setGravity(Gravity.CENTER_VERTICAL);
                    binding.txtEnableUAEPass.setTextSize(20f);

                }

                //UAE Pass Image

                if (!Global.uaePassConfig.disableMyId) {
                    LinearLayout.LayoutParams uaePassImageParams = new LinearLayout.LayoutParams(280,120);
                    uaePassImageParams.gravity = Gravity.CENTER;
                    uaePassImageParams.setMargins(leftMargin,topMargin-4,rightMargin,bottomMargin);
                    binding.imgUAEPass.setLayoutParams(uaePassImageParams);
                    binding.imgUAEPass.setScaleType(ImageView.ScaleType.FIT_XY);}
                else{
                    LinearLayout.LayoutParams uaePassImageParams = new LinearLayout.LayoutParams(300,150);
                    uaePassImageParams.gravity = Gravity.CENTER;
                    uaePassImageParams.setMargins(leftMargin,topMargin-4,rightMargin,bottomMargin);
                    binding.imgEnableUAEPass.setLayoutParams(uaePassImageParams);
                    binding.imgEnableUAEPass.setScaleType(ImageView.ScaleType.FIT_XY);


                }






                Animation anim = new ScaleAnimation(1f, 1.4f, 1f, 1.4f, Animation.RELATIVE_TO_PARENT,.5f, Animation.RELATIVE_TO_PARENT,.5f);
                anim.setStartOffset(1000);
                anim.setDuration(600);
                anim.setFillEnabled(true);
                anim.setFillAfter(true);
                anim.setInterpolator(new AccelerateDecelerateInterpolator());



                binding.imgBackground.setPadding(0, (int) ((Global.height/100)*24),0,0);



                if(!Global.isLanguageChanged){
                    binding.imgBackground.startAnimation(anim);
                    //binding.logoSplash.startAnimation(anim);

                    binding.imgBackground.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            animateView(binding.imgBackground, 1000L,500L,true,0f,0f,0f,Global.height);
                            animateView(binding.slantViewLoginHeader,1000L,1000L,true,0f,0f,0f, (-Global.height/4)+100);
                            animateView(binding.cardLogin,1000L,1000L,true,0f,0f,Global.height, (-Global.height/2)+Global.height/3);

                            animateView(binding.layoutUAEPass,1000L,1000L,true,0f,0f,Global.height, (-Global.height/2)+Global.height/3+100);

                            if(!Global.uaePassConfig.disableMyId){
                                binding.layoutEnableUAEPass.setVisibility(View.GONE);
                                binding.layoutUAEPass.setVisibility(View.VISIBLE);
                                LinearLayout.LayoutParams layoutUaepassarams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,binding.cardLogin.getHeight()+175);
//                    LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                layoutUaepassarams.gravity = Gravity.CENTER_HORIZONTAL;
                                layoutUaepassarams.setMargins(32, 10,32,0);
                                binding.layoutUAEPass.setLayoutParams(layoutUaepassarams);
                            }
                            else{
                                binding.txtGuest.setVisibility(View.VISIBLE);
                                binding.layoutEnableUAEPass.setVisibility(View.VISIBLE);
                                LinearLayout.LayoutParams guestLoginParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, elementHeight);
                                guestLoginParams.gravity = Gravity.CENTER_VERTICAL;
                                guestLoginParams.setMargins(leftMargin, topMargin+184, rightMargin, bottomMargin - 5);
                                binding.txtGuest.setLayoutParams(guestLoginParams);
                                binding.txtGuest.setGravity(Gravity.CENTER_VERTICAL);
                                binding.txtGuest.setTextSize(20f);
                                binding.txtUAEPass.setTextSize(20f);
                                binding.layoutUAEPass.setVisibility(View.GONE);
                            }


                        }
                    },1500);



                }
                else{

                   // onConfig(Global.uaePassConfig.disableMyId);

                    onMyIDDisabled(Global.uaePassConfig.disableMyId);
                    binding.imgBackground.setVisibility( View.GONE);
                    binding.switchLanguage.setVisibility(View.VISIBLE);
                    binding.cardLogin.setVisibility(View.VISIBLE);
                    binding.slantViewLoginHeader.setVisibility(View.VISIBLE);

                    LinearLayout.LayoutParams logoLayoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,((int)(Global.height/2)));
                    logoLayoutParams1.gravity = Gravity.CENTER;
                    logoLayoutParams1.setMargins(leftMargin, (int) (-(Global.height/4)-100),rightMargin,bottomMargin);
                    binding.layoutLogo.setLayoutParams(logoLayoutParams1);

                    LinearLayout.LayoutParams slantViewParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)((Global.height/4)+100));
                    slantViewParams1.gravity = Gravity.CENTER_HORIZONTAL;
                    //slantViewParams.setMargins(0,(-height).toInt(),0,0)
                    binding.slantViewLoginHeader.setLayoutParams(slantViewParams1);

                    LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)((Global.height/2)-(Global.height/14)));
//                    LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    cardViewParams.gravity = Gravity.CENTER_HORIZONTAL;
                    cardViewParams.setMargins(48, (int) ((-Global.height/4)+150),48,20);
                    binding.cardLogin.setLayoutParams(cardViewParams);





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
            binding.switchLanguage.setVisibility( View.VISIBLE);
            binding.copyrightEnglish.setVisibility(View.GONE);
            binding.copyrightArabic.setVisibility(View.GONE);
            binding.logoSplash.setVisibility(View.GONE);

        }
        if(view == binding.layoutUAEPass)
            binding.layoutUAEPass.setVisibility(View.VISIBLE);

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

            sharedpreferences = newBase.getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
            String locale = sharedpreferences.getString(USER_LANGUAGE, "defaultStringIfNothingFound");
            Global.uaePassConfig =Global.uaePassConfig == null? new UaePassConfig():Global.uaePassConfig;
            Global.uaePassConfig.disableMyId = sharedpreferences.getBoolean("isDisableMyId",false);
            if(!locale.equals("defaultStringIfNothingFound"))
                CURRENT_LOCALE =locale;
            else
                CURRENT_LOCALE ="en";

            super.attachBaseContext(CustomContextWrapper.wrap(newBase, CURRENT_LOCALE));

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
        if(Global.isfromWebViewCancel && !UAEPassRequestModels.isPackageInstalled(UAEPassRequestModels.UAE_PASS_PACKAGE_ID, getPackageManager())){
            AlertDialogUtil.errorAlertDialog("",getString(R.string.uaeloginfail),getString(R.string.ok),LoginActivity.this);
            Global.isfromWebViewCancel=false;
        }
        else if(Global.sessionErrorMsg!=null && !UAEPassRequestModels.isPackageInstalled(UAEPassRequestModels.UAE_PASS_PACKAGE_ID, getPackageManager())){
            AlertDialogUtil.errorAlertDialog("",Global.sessionErrorMsg,getString(R.string.ok),LoginActivity.this);
            Global.sessionErrorMsg =null;
        }
    }

    @Override
    public void onStarted() {

        //progressBar.setVisibility(View.VISIBLE);

        AlertDialogUtil.showProgressBar(LoginActivity.this,true);
        //binding.txtOR.setVisibility(View.GONE);

    }

    @Override
    public void onSuccess() {
        //progressBar.setVisibility(View.GONE);

        binding.txtOR.setVisibility(View.VISIBLE);
        AlertDialogUtil.showProgressBar(LoginActivity.this,false);
        finish();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onFailure(String message) {
        //progressBar.setVisibility(View.GONE);
        //binding.txtOR.setVisibility(View.VISIBLE);
        AlertDialogUtil.showProgressBar(LoginActivity.this,false);
        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
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
       /* PreferenceManager.getDefaultSharedPreferences(this
        ).edit().putString(AppConstants.USER_LOGIN_DETAILS, gson.toJson(loginDetails)).apply();*/
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
       /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
           versionCode = (int)pInfo.getLongVersionCode();
       else
           versionCode = pInfo.versionCode;*/



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            localversion = String.valueOf(pInfo.getLongVersionCode());
        else
            localversion = String.valueOf(pInfo.versionCode);
        String msg;

        msg = (CURRENT_LOCALE.equals("en"))?Global.forceUserToUpdateBuild_msg_en : Global.forceUserToUpdateBuild_msg_ar;


        if (Global.versionCompare(Global.CurrentAndroidVersion, localversion) > 0 && forceUserToUpdateBuild)
        {
            // Show App update alert here
            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();

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
        binding.layoutUAEPass.setVisibility(View.GONE);
        displayContent();
    }
    public void onMyIDDisabled(boolean status){
        sharedpreferences.edit().putBoolean("isDisableMyId",status).apply();
        if(status){
            binding.loginContainer.setVisibility(View.GONE);
            binding.imageDubaiID.setVisibility(View.GONE);
            binding.layoutEnableUAEPass.setVisibility(View.VISIBLE);
            binding.layoutUAEPass.setVisibility(View.GONE);
            LinearLayout.LayoutParams uaePassImageParams = new LinearLayout.LayoutParams(300,120);
            uaePassImageParams.gravity = Gravity.CENTER;
            uaePassImageParams.setMargins(leftMargin,topMargin-4,rightMargin,bottomMargin);
            binding.imgEnableUAEPass.setLayoutParams(uaePassImageParams);
            binding.imgEnableUAEPass.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams guestLoginParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, elementHeight);
            guestLoginParams.gravity = Gravity.CENTER_VERTICAL;
            guestLoginParams.setMargins(leftMargin, topMargin+184, rightMargin, bottomMargin - 5);
            binding.txtGuest.setLayoutParams(guestLoginParams);
            binding.txtGuest.setGravity(Gravity.CENTER_VERTICAL);
            binding.txtGuest.setTextSize(20f);
            binding.txtUAEPass.setTextSize(20f);

        } else {
            binding.loginContainer.setVisibility(View.VISIBLE);
            binding.imageDubaiID.setVisibility(View.VISIBLE);
            binding.layoutEnableUAEPass.setVisibility(View.GONE);
            binding.layoutUAEPass.setVisibility(View.VISIBLE);
            binding.imgUAEPass.setVisibility(View.VISIBLE);
            binding.txtUAEPass.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams uaePassImageParams = new LinearLayout.LayoutParams(300,120);
            uaePassImageParams.gravity = Gravity.CENTER;
            uaePassImageParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
            binding.imgUAEPass.setLayoutParams(uaePassImageParams);
            binding.imgUAEPass.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams guestLoginParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, elementHeight);
            guestLoginParams.gravity = Gravity.CENTER_VERTICAL;
            guestLoginParams.setMargins(leftMargin, topMargin-92, rightMargin, bottomMargin - 5);
            binding.txtGuest.setLayoutParams(guestLoginParams);
            binding.txtGuest.setGravity(Gravity.CENTER_VERTICAL);
            binding.txtGuest.setTextSize(16f);
            binding.txtUAEPass.setTextSize(16f);

        }
    }
}
