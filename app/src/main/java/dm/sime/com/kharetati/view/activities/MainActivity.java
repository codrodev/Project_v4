package dm.sime.com.kharetati.view.activities;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.LightingColorFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.ActivityMainBinding;
import dm.sime.com.kharetati.datas.models.NotificationResponse;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.HomeRepository;
import dm.sime.com.kharetati.datas.repositories.MainRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.CustomContextWrapper;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.SensorOrientationChangeNotifier;
import dm.sime.com.kharetati.utility.constants.AppConstants;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.adapters.CustPagerTransformer;
import dm.sime.com.kharetati.view.adapters.PagerContentAdapter;
import dm.sime.com.kharetati.view.customview.DataCallback;
import dm.sime.com.kharetati.view.customview.SwitchCompatEx;
import dm.sime.com.kharetati.view.customview.meowbottomnavigation.MeowBottomNavigation;
import dm.sime.com.kharetati.view.fragments.FeedbackFragment;
import dm.sime.com.kharetati.view.fragments.RequestDetailsFragment;
import dm.sime.com.kharetati.view.fragments.SettingsFragment;
import dm.sime.com.kharetati.view.fragments.WebViewFragment;
import dm.sime.com.kharetati.view.fragments.BottomNavigationFragmentSheet;
import dm.sime.com.kharetati.view.fragments.ContactusFragment;
import dm.sime.com.kharetati.view.fragments.DashboardFragment;
import dm.sime.com.kharetati.view.fragments.HappinessFragment;
import dm.sime.com.kharetati.view.fragments.HomeFragment;
import dm.sime.com.kharetati.view.fragments.MapFragment;
import dm.sime.com.kharetati.view.fragments.ParentSiteplanFragment;
import dm.sime.com.kharetati.view.navigators.FragmentNavigator;
import dm.sime.com.kharetati.view.navigators.MainNavigator;
import dm.sime.com.kharetati.view.viewModels.LoginViewModel;
import dm.sime.com.kharetati.view.viewModels.MainViewModel;
import dm.sime.com.kharetati.view.viewModels.MyViewModel;
import dm.sime.com.kharetati.view.viewmodelfactories.HomeViewModelFactory;
import dm.sime.com.kharetati.view.viewmodelfactories.MainViewModelFactory;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static dm.sime.com.kharetati.utility.Global.CURRENT_LOCALE;
import static dm.sime.com.kharetati.utility.Global.MYPREFERENCES;
import static dm.sime.com.kharetati.utility.Global.alertDialog;
import static dm.sime.com.kharetati.utility.Global.isLoginActivity;
import static dm.sime.com.kharetati.utility.constants.AppConstants.FONT_SIZE;
import static dm.sime.com.kharetati.utility.constants.AppConstants.USER_LANGUAGE;

public class MainActivity extends AppCompatActivity implements FragmentNavigator, MainNavigator, DataCallback, BottomNavigationFragmentSheet.OnActionListener {

    ActivityMainBinding binding;
    MainViewModel model;
    public static MainViewModel mainVM;
    FragmentManager fragmentManager = null;
    FragmentTransaction tx = null;
    MainViewModelFactory factory;
    private MainRepository repository;
    public int loadPosition;
    private long startTime=10*60*1000;
    private final long interval = 1 * 1000;
    private MeowBottomNavigation.Model myBottomModel;
    private Fragment fragmentAfterBackPress;
    private MyCountDownTimer countDownTimer;
    private SharedPreferences sharedpreferences;
    private Tracker mTracker;
    public static onPermissionResult listner;
    private DrawerLayout drawer;
    private MeowBottomNavigation customBottomBar;
    private AlertDialog alertDialogNotifications;
    private LinearLayout layoutDots;
    private MyViewModel myViewModel;
    public static FirebaseAnalytics firebaseAnalytics;
    public static boolean isKeyboardShowing;
    private String currentDateandTime;
    private String guestLastLoginTime;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Global.isLoginActivity = false;
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        try {
            repository = new MainRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(this)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        factory = new MainViewModelFactory(this,repository);
        model = ViewModelProviders.of(this,factory).get(MainViewModel.class);
        mainVM = model;
        model.mainNavigator =this;
        fragmentManager = getSupportFragmentManager();
        Global.fontScale =sharedpreferences.getFloat(FONT_SIZE,1f);
        guestLastLoginTime = sharedpreferences.getString("lastLoginTime", currentDateandTime);
        //adjustFontScale(MainActivity.this.getResources().getConfiguration(),Global.fontScale);
        model.initialize();
        binding.setActivityMainVM(model);
        customBottomBar = (MeowBottomNavigation)findViewById(R.id.customBottomBar);
        if(CURRENT_LOCALE.equals("en")) customBottomBar.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else customBottomBar.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.layoutlastlogin.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.layoutlastlogin.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        final BottomNavigationFragmentSheet myBottomSheet = BottomNavigationFragmentSheet.newInstance(this);

        View contentView=findViewById(R.id.rootLayout);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                Log.d("", "keypadHeight = " + keypadHeight);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    if (!isKeyboardShowing) {
                        isKeyboardShowing = true;
                        onKeyboardVisibilityChanged(true);
                    }
                }
                else {
                    // keyboard is closed
                    if (isKeyboardShowing) {
                        isKeyboardShowing = false;
                        onKeyboardVisibilityChanged(false);
                    }
                }
            }
        });
        
        customBottomBar.add(new MeowBottomNavigation.Model(1, R.drawable.ic_dashboard));
        customBottomBar.add(new MeowBottomNavigation.Model(2, R.drawable.happiness_black));
        customBottomBar.add(new MeowBottomNavigation.Model(3, R.drawable.ic_home_run));
        customBottomBar.add(new MeowBottomNavigation.Model(4, R.drawable.chat_144));
        customBottomBar.add(new MeowBottomNavigation.Model(5, R.drawable.more_144));


        countDownTimer = new MyCountDownTimer(startTime, interval);

        //binding.imgHelp.setRotationY(Global.CURRENT_LOCALE.equals("en")?0:180);
        binding.backButton.setRotationY(Global.CURRENT_LOCALE.equals("en")?0:180);
        //model.getNotifications();



        if(Global.isUAE){
            if(Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0) {
                binding.txtUsername.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameEN());
            } else {
                binding.txtUsername.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameAR());
            }
        } else {
            if(Global.isUserLoggedIn){
                if(CURRENT_LOCALE.equals("en"))
                    binding.txtUsername.setText(Global.getUser(this).getFullname()!=null?Global.getUser(this).getFullname():Global.getUser(this).getFullnameAR());
                else
                    binding.txtUsername.setText(Global.getUser(this).getFullnameAR()!=null?Global.getUser(this).getFullnameAR():Global.getUser(this).getFullname());

            }
            else
                binding.txtUsername.setText(getResources().getString(R.string.guest));
        }
        try {
            getLastlogin();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        customBottomBar.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                // your codes
                myBottomModel = item;

                Global.lastSelectedBottomTab = item.getId();
                loadFragment(model.bottomNavigationTAG(item.getId()), true, null);
            }
        });

        customBottomBar.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // your codes
                sharedpreferences.edit().putInt("position",item.getId()).apply();

            }
        });
        customBottomBar.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
                if(item.getId()==3)
                    loadFragment(FragmentTAGS.FR_HOME,true,null);
                else if(item.getId()==5)
                    loadFragment(FragmentTAGS.FR_BOTTOMSHEET,true,null);
            }
        });
        /*customBottomBar.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model bottomModel) {
                // YOUR CODES
                myBottomModel = bottomModel;

                    Global.lastSelectedBottomTab = bottomModel.getId();
                    loadFragment(model.bottomNavigationTAG(bottomModel.getId()), true, null);
                    *//*if(savedInstanceState=null)
                    savedInstanceState.putInt("loadPosition",myBottomModel.getId());*//*

                return null;
            }
        });
        customBottomBar.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // YOUR CODES

               sharedpreferences.edit().putInt("position",model.getId()).apply();
                return null;
            }
        });*/
        Objects.requireNonNull(binding.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        /*binding.imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList al = new ArrayList();
                if(Global.FragmentTagForHelpUrl.equals(FragmentTAGS.FR_HOME)){
                    *//*if(Global.helpUrlEn != null || Global.helpUrlAr != null) {
                        al.add(Global.CURRENT_LOCALE.equals("en")? Global.helpUrlEn:Global.helpUrlAr);

                    } else {

                    }*//*
                    if(Global.home_en_url != null || Global.home_ar_url != null) {
                        al.add(HomeFragment.constructUrl((Global.CURRENT_LOCALE.equals("en") ? Global.home_en_url : Global.home_ar_url),MainActivity.this));
                    }
                } else if(Global.FragmentTagForHelpUrl.equals(FragmentTAGS.FR_DASHBOARD)){
                    Global.isDashboard =true;
                    if(Global.FragmentTagForDashboardHelpUrl == 0) {
                        al.add(HomeFragment.constructUrl((Global.CURRENT_LOCALE.equals("en")? Global.mymaps_en_url:Global.mymaps_ar_url),MainActivity.this));

                    } else {
                        al.add(HomeFragment.constructUrl((Global.CURRENT_LOCALE.equals("en")? Global.bookmarks_en_url:Global.bookmarks_ar_url),MainActivity.this));
                    }
                }
                if(al != null & al.size() > 0) {
                    loadFragment(FragmentTAGS.FR_WEBVIEW, true, al);
                } else {
                    if(Global.home_en_url != null || Global.home_ar_url != null) {
                        al.add(HomeFragment.constructUrl((Global.CURRENT_LOCALE.equals("en") ? Global.home_en_url : Global.home_ar_url),MainActivity.this));
                    }
                    loadFragment(FragmentTAGS.FR_WEBVIEW, true, al);
                }
            }
        });*/



        if(Global.isRecreate){

            loadFragment(sharedpreferences.getString("currentFragment",FragmentTAGS.FR_HOME),true,null);
            binding.customBottomBar.show(5,true);
            //adjustFontScale(getResources().getConfiguration(),Global.fontScale);
        }
        else {customBottomBar.show(3, true);
        openHomePage();
        initializeActivity();}

    }

    void onKeyboardVisibilityChanged(boolean opened) {
        if(opened){
            if(!((Global.current_fragment_id.equals(FragmentTAGS.FR_PARENT_SITEPLAN)||Global.current_fragment_id.equals(FragmentTAGS.FR_LANDOWNER_SELECTION)||Global.current_fragment_id.equals(FragmentTAGS.FR_ATTACHMENT)||Global.current_fragment_id.equals(FragmentTAGS.FR_DELIVERY)||Global.current_fragment_id.equals(FragmentTAGS.FR_PAY)))){

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0,0,0, -SwitchCompatEx.dp2Px(48f));
            binding.uiContainer.setLayoutParams(params);
            }
            customBottomBar.setVisibility(View.GONE);


        }
        else{
            LinearLayout.LayoutParams params1=null;
            if(!((Global.current_fragment_id.equals(FragmentTAGS.FR_PARENT_SITEPLAN)||Global.current_fragment_id.equals(FragmentTAGS.FR_LANDOWNER_SELECTION)||Global.current_fragment_id.equals(FragmentTAGS.FR_ATTACHMENT)||Global.current_fragment_id.equals(FragmentTAGS.FR_DELIVERY)||Global.current_fragment_id.equals(FragmentTAGS.FR_PAY)))){
                params1= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                params1.setMargins(0,0,0, 0);
                binding.uiContainer.setLayoutParams(params1);
                customBottomBar.setVisibility(View.VISIBLE);
            }
        }
    }

    public void getLastlogin() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", new Locale("en"));
        SimpleDateFormat rdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", new Locale("en"));

        SimpleDateFormat sdfEn = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", new Locale("en"));
        SimpleDateFormat sdfAr = new SimpleDateFormat("hh:mm:ss a yyyy/MM/dd ", new Locale("ar"));
        //SimpleDateFormat sdfAr = new SimpleDateFormat(" dd/MM/yyyy HH:mm:ss a", new Locale("ar"));

        //currentDateandTime = sdf.format(new Date());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Dubai"));
        rdf.setTimeZone(TimeZone.getTimeZone("Asia/Dubai"));
        sdfEn.setTimeZone(TimeZone.getTimeZone("Asia/Dubai"));
        sdfAr.setTimeZone(TimeZone.getTimeZone("Asia/Dubai"));
        currentDateandTime = CURRENT_LOCALE.equals("en")?sdfEn.format(new Date()):sdfEn.format(new Date());
        binding.txtLastLogin.setVisibility(View.VISIBLE);

        if (Global.isUAE||Global.isUserLoggedIn) {
            String lastLogin = Global.uaeSessionResponse.getService_response().getLast_login();

            if (lastLogin != null && !lastLogin.equals(""))
            {
                if (lastLogin.contains("|")) {
                    lastLogin = lastLogin.substring(0, lastLogin.lastIndexOf("|"));
                }

                if (CURRENT_LOCALE.equals("en")){
                    binding.txtLastLogin.setText(" " + sdfEn.format(rdf.parse(lastLogin)));
                    //Toast.makeText(MainActivity.this, " " + sdfEn.format(rdf.parse(lastLogin)), Toast.LENGTH_SHORT).show();
                }
                else{
                    binding.txtLastLogin.setText(" " + sdfAr.format(rdf.parse(lastLogin)));
                   // Toast.makeText(MainActivity.this, " " + sdfAr.format(rdf.parse(lastLogin)), Toast.LENGTH_SHORT).show();
                }

            }

        }
        /*else if(Global.isUserLoggedIn) {
            binding.txtLastLogin.setText(" " + currentDateandTime);
        }*/
        else {

            guestLastLoginTime = guestLastLoginTime==null?currentDateandTime:guestLastLoginTime;
            guestLastLoginTime = (Global.CURRENT_LOCALE.equals("en")?sdfEn.format(sdf.parse(guestLastLoginTime)):sdfAr.format(sdf.parse(guestLastLoginTime)));
            binding.txtLastLogin.setText(" " + guestLastLoginTime);
            Toast.makeText(MainActivity.this, " " + guestLastLoginTime, Toast.LENGTH_LONG).show();

            sharedpreferences.edit().putString("lastLoginTime", currentDateandTime).apply();
        }
    }


    private void checkNotifications() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_notifications, null);
        dialogView.setBackground(getResources().getDrawable(R.drawable.rounded_corner_theme_bg));
        dialogBuilder.setView(dialogView);


        alertDialogNotifications = dialogBuilder.create();
        alertDialogNotifications.show();
        alertDialogNotifications.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(0x00000000, 0xFFFCFCFC));
        alertDialogNotifications.getWindow().getDecorView().getBackground().setAlpha(0);
        ViewPager viewPager = (ViewPager) dialogView.findViewById(R.id.viewPager);
        layoutDots = (LinearLayout) dialogView.findViewById(R.id.layoutDots);
        myViewModel =  new MyViewModel();
        myViewModel.initializeMode(this);

        PagerContentAdapter adapter = new PagerContentAdapter(this, model);
        viewPager.setPageTransformer(false, new CustPagerTransformer(this));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position, Global.notificationResponse.getServiceResponse().getGeneralNotifications().size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        viewPager.addOnPageChangeListener(listener);


        layoutDots.setVisibility(View.VISIBLE);
        addBottomDots(0, myViewModel.getLstBody().size());

    }
    private Date stringToDate(String aDate,String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat,new Locale("en"));
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }

    private void addBottomDots(int currentPage, int length) {
        TextView[] dots = new TextView[length];

        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.light_gray));
            layoutDots.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.maroon_light));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == AppConstants.REQUEST_READ_EXTERNAL_STORAGE) {
            if (listner != null) {
                if (permissions != null && permissions.length > 0 && grantResults != null && grantResults.length > 0) {
                    if(isPermissionGranted(permissions, grantResults)){
                        listner.OnPermissionAccepted(true);
                    }
                    else
                        listner.OnPermissionAccepted(false);
                }
            }
        }

    }


    @Override
    public void recreate() {

        //isLoginActivity =true;
        sharedpreferences.edit().putFloat(FONT_SIZE,Global.fontScale).apply();
        startActivity(getIntent());
        finish();
        overridePendingTransition(0, 0);
        Global.isRecreate =true;


        //adjustFontScale(getResources().getConfiguration(),sharedpreferences.getFloat(FONT_SIZE,Global.fontScale));

        if(Global.alertDialog!=null)
            Global.alertDialog=null;


    }

    private boolean isPermissionGranted(String[] permissions, int[] grantResults){
        boolean isGranted = true;
        for (int i =0 ; i < grantResults.length; i++){
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                isGranted = false;
                break;
            }
        }
        return isGranted;
    }

    private void initializeActivity(){

        /*customBottomBar.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // YOUR CODES
                return null;
            }
        });*/
    }

    private void processIntentData(Intent intent) {
        /*if (intent.getStringExtra("access") != null) {
            Global.accessToken = intent.getStringExtra("access");
        }*/
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        /*if(savedInstanceState!=null)
        myBottomModel.setId(savedInstanceState.getInt("position"));*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(CURRENT_LOCALE.equals("ar")){
           // customBottomBar.show(sharedpreferences.getInt("position",3), true);

        }
        if(Global.current_fragment_id.equals(FragmentTAGS.FR_HOME)){
            binding.txtLastLogin.setVisibility(View.VISIBLE);
            binding.layoutlastlogin.setVisibility(View.VISIBLE);
            /*try {
                getLastlogin();
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
        }
        else {
            binding.txtLastLogin.setVisibility(View.GONE);
            binding.layoutlastlogin.setVisibility(View.GONE);
        }
        setScreenName(getResources().getString(R.string.title_welcome));

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sharedpreferences = newBase.getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
            String locale = sharedpreferences.getString(USER_LANGUAGE, "defaultStringIfNothingFound");

            Global.fontScale = sharedpreferences.getFloat(FONT_SIZE,1f);


            if(!locale.equals("defaultStringIfNothingFound"))
                CURRENT_LOCALE =locale;
            else
                CURRENT_LOCALE ="en";
            super.attachBaseContext(CustomContextWrapper.wrap(newBase, CURRENT_LOCALE));

        } else {
            super.attachBaseContext(newBase);
        }
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

        newConfig.setLocale(new Locale(CURRENT_LOCALE));

        newConfig.fontScale = sharedpreferences.getFloat(FONT_SIZE,1f);
        //adjustFontScale(newConfig,newConfig.fontScale);

        sharedpreferences.edit().putString("currentFragment",Global.current_fragment_id).apply();

        int position = sharedpreferences.getInt("position",3);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Global.isLandScape = true;
            //createConfigurationContext(newConfig);

            if(CURRENT_LOCALE.equals("ar")){

                //createConfigurationContext(newConfig);

                if(!Global.current_fragment_id.equals(FragmentTAGS.FR_MAP)||!Global.current_fragment_id.equals(FragmentTAGS.FR_PARENT_SITEPLAN)||!Global.current_fragment_id.equals(FragmentTAGS.FR_LANDOWNER_SELECTION)
                        ||!Global.current_fragment_id.equals(FragmentTAGS.FR_ATTACHMENT)||!Global.current_fragment_id.equals(FragmentTAGS.FR_PAY)||!Global.current_fragment_id.equals(FragmentTAGS.FR_REQUEST_DETAILS)||!Global.current_fragment_id.equals(FragmentTAGS.FR_WEBVIEW)||!Global.current_fragment_id.equals(FragmentTAGS.FR_WEBVIEW_PAYMENT)) {

                    customBottomBar.clearCount(position);
                    customBottomBar.show(position, true);

                    customBottomBar.show(position + 1, false);
                    loadFragment(model.bottomNavigationTAG(position + 1), true, null);
                    customBottomBar.clearAllCounts();
                    customBottomBar.show(position, true);
                    //loadFragment(sharedpreferences.getString("currentFragment",FragmentTAGS.FR_HOME),true, null);
                    loadFragment(model.bottomNavigationTAG(position), true, null);
                }
            loadFragment(sharedpreferences.getString("currentFragment",Global.current_fragment_id),true,null);




            }

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Global.isLandScape = false;
            if(CURRENT_LOCALE.equals("ar")){
                customBottomBar.show(position-1,false);
                loadFragment(model.bottomNavigationTAG(position-1), true, null);
                customBottomBar.clearAllCounts();
                //customBottomBar.getCellById(position-1).disableCell();
                customBottomBar.show(position,true);
                loadFragment(model.bottomNavigationTAG(position), true, null);
                loadFragment(sharedpreferences.getString("currentFragment",Global.current_fragment_id),true,null);
                if(!(Global.current_fragment_id.compareToIgnoreCase(FragmentTAGS.FR_HOME)==0))
                    binding.layoutlastlogin.setVisibility(View.GONE );
            }
        }
        super.onConfigurationChanged(newConfig);
    }


    private void openHomePage(){
        Intent intent = getIntent();
        /*if (intent != null && intent.getData() != null) {
            processIntentData(intent);
        }*/
        /*if (intent != null && intent.getData() != null) {
            processIntentData(intent);
        }*/
        loadFragment(FragmentTAGS.FR_HOME, false, null);
    }

    @Override
    public void fragmentNavigator(String fragmentTag, Boolean addToBackStack, List<Object> params) {
        loadFragment(fragmentTag, addToBackStack, params);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.map_menu,menu);
        //MapFragment.mapVM.mapNavigator.onMenCreated(menu);
        return true;
      }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       // MapFragment.mapVM.mapNavigator.onMenuSelected(item);
        return super.onOptionsItemSelected(item);
    }*/

    public Fragment loadFragment(String fragment_tag, Boolean addToBackStack, List<Object> params) {


        Global.FragmentTagForHelpUrl = "";
        tx = fragmentManager.beginTransaction();
        Fragment fragment = null;
        /*if(fragment_tag.equals(FragmentTAGS.FR_CONTACT_US)||fragment_tag.equals(FragmentTAGS.FR_HAPPINESS))
            binding.imgHelp.setVisibility(View.INVISIBLE);
        else if(fragment_tag.equals(FragmentTAGS.FR_DASHBOARD)||fragment_tag.equals(FragmentTAGS.FR_MYMAP)||fragment_tag.equals(FragmentTAGS.FR_BOOKMARK))
            binding.imgHelp.setVisibility(View.INVISIBLE);
        else
            binding.imgHelp.setVisibility(View.INVISIBLE);*/

        if(fragment_tag.equals(FragmentTAGS.FR_HOME)){
            binding.txtLastLogin.setVisibility(View.VISIBLE);
            binding.layoutlastlogin.setVisibility(View.VISIBLE);
        }
        else{
            binding.txtLastLogin.setVisibility(View.GONE);
        binding.layoutlastlogin.setVisibility(View.GONE);
        }
        if(fragment_tag.equals(FragmentTAGS.FR_WEBVIEW)||fragment_tag.equals(FragmentTAGS.FR_FEEDBACK)||fragment_tag.equals(FragmentTAGS.FR_SETTINGS)){
            binding.backButton.setVisibility(View.VISIBLE);
            binding.txtWelcome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(binding.backButton.getVisibility()==View.VISIBLE) onBackPressed();
                }
            });
        }
        else
            binding.backButton.setVisibility(View.INVISIBLE);
        LinearLayout.LayoutParams headerParams = null;
       /* if(fragment_tag.equals(FragmentTAGS.FR_HOME)){
            headerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerParams.setMargins(0,0,0,-48);
            binding.headerLayout.setLayoutParams(headerParams);}*/

        switch (fragment_tag) {
            case FragmentTAGS.FR_HOME:
                Global.FragmentTagForHelpUrl = FragmentTAGS.FR_HOME;
                fragment = HomeFragment.newInstance();
                break;
            case FragmentTAGS.FR_DASHBOARD:
                Global.FragmentTagForHelpUrl = FragmentTAGS.FR_DASHBOARD;{
                if(params!=null)
                    fragment = DashboardFragment.newInstance((Integer) params.get(0));
                else
                    fragment = DashboardFragment.newInstance(0);

                }
                break;
            case FragmentTAGS.FR_MAP:

                fragment = MapFragment.newInstance(PlotDetails.parcelNo!=null?PlotDetails.parcelNo:"");
                break;
            case FragmentTAGS.FR_REQUEST_SITE_PLAN:
                fragment = ParentSiteplanFragment.newInstance();
                break;
            case FragmentTAGS.FR_HAPPINESS:
                fragment = HappinessFragment.newInstance();
                break;
            case FragmentTAGS.FR_CONTACT_US:
                fragment = ContactusFragment.newInstance();
                break;
            case FragmentTAGS.FR_FEEDBACK:
                fragment = FeedbackFragment.newInstance();
                break;
            case FragmentTAGS.FR_BOTTOMSHEET:
                fragment = BottomNavigationFragmentSheet.newInstance(this);
                break;
            case FragmentTAGS.FR_WEBVIEW:
                String appName = null;
                if(params!=null && params.size()>0) {
                    Global.webViewUrl = params.get(0).toString();
                    if(params.size() > 1){
                        appName = params.get(1).toString();
                    }
                }
                fragment = WebViewFragment.newInstance(Global.webViewUrl, appName);
                break;
            case FragmentTAGS.FR_SETTINGS:
                fragment = SettingsFragment.newInstance();
                break;
            case FragmentTAGS.FR_WEBVIEW_PAYMENT:
                String appName1 = "";
                if(params!=null) {
                    Global.webViewUrl = params.get(0).toString();
                    if(params.size() > 1){
                        appName1 = params.get(1).toString();
                    }
                }
                fragment = WebViewFragment.newInstance(Global.webViewUrl, appName1);
                break;
            case FragmentTAGS.FR_REQUEST_DETAILS:
                if(params != null && params.size() > 0) {
                    fragment = RequestDetailsFragment.newInstance(params.get(0).toString(), params.get(1).toString(), params.get(2).toString(),
                            params.get(3).toString(),params.get(4).toString(),params.get(5).toString(),params.get(6).toString(),params.get(7).toString(),params.get(8).toString());
                } else {
                    fragment = RequestDetailsFragment.newInstance("", "", "",
                            "","","","","","");
                }
                break;
            default:
                fragment = HomeFragment.newInstance();
                break;
        }

        tx.replace(R.id.ui_container, fragment, fragment_tag);

        if (addToBackStack)
            tx.addToBackStack(fragment_tag);
        tx.commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void handleError(Throwable throwable) {

    }

    @Override
    public void openLoginActivity() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedpreferences.edit().putInt("position",customBottomBar.getId()).apply();
        if (Global.current_fragment_id != null) {
            if (!(Global.current_fragment_id.equals(FragmentTAGS.FR_PARENT_SITEPLAN) || Global.current_fragment_id.equals(FragmentTAGS.FR_ATTACHMENT) || Global.current_fragment_id.equals(FragmentTAGS.FR_DELIVERY) || Global.current_fragment_id.equals(FragmentTAGS.FR_PAY) || Global.current_fragment_id.equals(FragmentTAGS.FR_REQUEST_DETAILS))) {
                if (Global.alertDialog != null) {
                    Global.alertDialog.cancel();
                    Global.alertDialog = null;
                }
            }
        }

    }


    @Override
    public void manageActionBar(boolean key) {
        changeActionBarStatus(key);
    }

    @Override
    public void manageBottomBar(boolean key) {
        if(!key) {
            customBottomBar.setVisibility(View.GONE);
            LinearLayout.LayoutParams uiContainerParam =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
            uiContainerParam.setMargins(0, 0, 0, 0);
            binding.uiContainer.setLayoutParams(uiContainerParam);
        } else {
            customBottomBar.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams uiContainerParam =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
            if(Global.current_fragment_id.equals(FragmentTAGS.FR_MAP)){
                uiContainerParam.setMargins(0, 0, 0, 0);
            } else {
                uiContainerParam.setMargins(0, 0, 0, 40);
            }

            //uiContainerParam.addRule(LinearLayout.BELOW, R.id.layoutProfile);
            binding.uiContainer.setLayoutParams(uiContainerParam);
        }
    }
    public void setScreenName(String screenName){
        binding.txtWelcome.setText(screenName);
    }
    @Override
    public void navigateToDashboard(int fragmentposition) {

        ArrayList al = new ArrayList();
        al.add(fragmentposition);

        customBottomBar.show(1, true);
        loadFragment(FragmentTAGS.FR_DASHBOARD,true,al);

    }

    public void navigateToContactus(){
        customBottomBar.show(4, true);
        loadFragment(FragmentTAGS.FR_CONTACT_US,true,null);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        /*Global.helpUrlEn = "";
        Global.helpUrlAr = "";*/

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0)
        {

            AlertDialogUtil.backPressedAlertDialog(
                    "",
                    getResources().getString(R.string.exit_alert_message),
                    getResources().getString(R.string.ok),
                    getResources().getString(R.string.cancel),
                    this);
            //clearBackStack();
        }
        else{

            if(Global.current_fragment_id.equals(FragmentTAGS.FR_WEBVIEW_PAYMENT)){
                if (Global.paymentStatus == null) {
                    AlertDialogUtil.paymentBackAlert("",
                            getString(R.string.CANCELTRANSACTIONALERT),
                            getResources().getString(R.string.ok),
                            getResources().getString(R.string.cancel), MainActivity.this);
                }
                else if (Global.paymentStatus.equals("0")) {
                    //fragmentManager.popBackStack();
                    clearStack(FragmentTAGS.FR_DASHBOARD,1);
                    //createAndLoadFragment(Constant.FR_DOWNLOADEDSITEPLAN, false, null);
                }
                else if (Global.paymentStatus.equals("1")) {
                    //clearStack(FragmentTAGS.FR_DASHBOARD,1);
                    clearBackStack();
                }

            }

            else{
                Fragment fragmentBeforeBackPress = getCurrentFragment();
                // Perform the usual back action
                super.onBackPressed();
                if(count>1){
                fragmentAfterBackPress = getCurrentFragment();
                if(fragmentAfterBackPress.getTag().equals(FragmentTAGS.FR_DASHBOARD)||fragmentAfterBackPress.getTag().equals(FragmentTAGS.FR_BOOKMARK)||fragmentAfterBackPress.getTag().equals(FragmentTAGS.FR_MYMAP)) {
                    Global.FragmentTagForHelpUrl = FragmentTAGS.FR_DASHBOARD;
                    customBottomBar.show(1, true);
                    Global.lastSelectedBottomTab = 1;
                }
                if(Global.current_fragment_id.equals(FragmentTAGS.FR_HAPPINESS)) {
                    customBottomBar.show(2, true);
                    Global.lastSelectedBottomTab = 2;
                }
                if(Global.current_fragment_id.equals(FragmentTAGS.FR_HOME)) {
                    Global.FragmentTagForHelpUrl = FragmentTAGS.FR_HOME;
                    customBottomBar.show(3, true);
                    Global.lastSelectedBottomTab = 3;
                }
                if(Global.current_fragment_id.equals(FragmentTAGS.FR_CONTACT_US)) {
                    customBottomBar.show(4, true);
                    Global.lastSelectedBottomTab = 4;
                }
                if(Global.current_fragment_id.equals(FragmentTAGS.FR_BOTTOMSHEET))
                    customBottomBar.show(5, true);
                }
                if(Global.current_fragment_id.equals(FragmentTAGS.FR_SETTINGS)){
                    //onWebViewBack();
                    customBottomBar.show(3, true);
                    openHomePage();
                }
                if(count == 1) {
                    customBottomBar.show(3, true);
                    Global.lastSelectedBottomTab = 3;
                }
                if(Global.current_fragment_id.equals(FragmentTAGS.FR_WEBVIEW))
                    binding.backButton.setVisibility(View.VISIBLE);
                else
                    binding.backButton.setVisibility(View.INVISIBLE);
            }

        }

    }

    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Fragment currentFragment = fragmentManager.findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Global.alertDialog!=null ){
            Global.alertDialog.cancel();
            Global.alertDialog =null;
        }


    }

    @Override
    public void onStarted() {
        AlertDialogUtil.showProgressBar(this,true);
    }

    @Override
    public void onFailure(String Msg) {

        AlertDialogUtil.showProgressBar(this,false);
        AlertDialogUtil.errorAlertDialog("",Msg,getResources().getString(R.string.ok),this);

    }

    @Override
    public void onSuccess() {

        AlertDialogUtil.showProgressBar(this,false);

    }

    @Override
    public void onWebViewBack() {


        getSupportFragmentManager().popBackStackImmediate();
        if(Global.isDashboard) {
            Global.FragmentTagForHelpUrl = FragmentTAGS.FR_DASHBOARD;
            customBottomBar.show(1, true);
            Global.lastSelectedBottomTab = 1;
        }
        else
            customBottomBar.show(3, true);

        /*if(Global.current_fragment_id.equals(FragmentTAGS.FR_CONTACT_US)||Global.current_fragment_id.equals(FragmentTAGS.FR_HAPPINESS))
            binding.imgHelp.setVisibility(View.INVISIBLE);
        else if(Global.current_fragment_id.equals(FragmentTAGS.FR_DASHBOARD)||Global.current_fragment_id.equals(FragmentTAGS.FR_MYMAP)||Global.current_fragment_id.equals(FragmentTAGS.FR_BOOKMARK))
            binding.imgHelp.setVisibility(View.VISIBLE);
        else
            binding.imgHelp.setVisibility(View.INVISIBLE);*/





        //if(Global.current_fragment_id.equals(FragmentTAGS.FR_))

    }

    @Override
    public void updateNotificationUI(NotificationResponse response) {
        if (response != null) {
            if (response.getServiceResponse() != null) {
                if (response.getServiceResponse().getGeneralNotifications().size() > 0) {
                    onSuccess();
                    if (Global.isFirstLoad||!Global.isRecreate) {
                        if (!((Activity) MainActivity.this).isFinishing()){
                            checkNotifications();
                            layoutDots.setVisibility(View.VISIBLE);
                            addBottomDots(0, response.getServiceResponse().getGeneralNotifications().size());
                        }
                    }
                }
            }
        }

    }

    @Override
    public void cancelNotification() {
        if(alertDialogNotifications!=null)alertDialogNotifications.cancel();
    }

    @Override
    public void setDot(int position) {
        //addBottomDots(position, Global.notificationResponse.getServiceResponse().getGeneralNotifications().size());
    }

    public void changeActionBarStatus(boolean key){
        if(!key) {
            binding.layoutProfile.setVisibility(View.GONE);
            binding.profileUsername.setVisibility(View.GONE);
            binding.headerLayout.setVisibility(View.GONE);
        } else {
            binding.layoutProfile.setVisibility(View.VISIBLE);
            binding.profileUsername.setVisibility(View.VISIBLE);
            binding.headerLayout.setVisibility(View.VISIBLE);
            if(Global.current_fragment_id.equals(FragmentTAGS.FR_HOME))
                binding.layoutlastlogin.setVisibility(View.VISIBLE);
            else
                binding.layoutlastlogin.setVisibility(View.GONE);
        }
    }
    public void clearStack(String FragmentTAG,int loadPosition){
        FragmentManager fragmentManager=getSupportFragmentManager();
        if(fragmentManager!=null)
            while(fragmentManager.getBackStackEntryCount() >=1) {
                if(fragmentManager.getBackStackEntryCount()==1){
                    fragmentManager.popBackStackImmediate();

                    loadFragment(FragmentTAG, false, null);
                    customBottomBar.show(loadPosition, true);
                } else {
                    fragmentManager.popBackStackImmediate();
                }
            }
    }
    public void clearBackStack(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        if(fragmentManager!=null){
            while(fragmentManager.getBackStackEntryCount() > 1)
            {
                int index = getSupportFragmentManager().getBackStackEntryCount()-1;
                FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
                String tag = backEntry.getName();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);

                if(fragment.getTag().compareToIgnoreCase(FragmentTAGS.FR_PAY)==0||fragment.getTag().compareToIgnoreCase(FragmentTAGS.FR_DASHBOARD)==0)
                {
                    break;
                }
                else
                    fragmentManager.popBackStackImmediate();

        }   }
    }

    @Override
    public void onUserInteraction(){

        super.onUserInteraction();

        //Reset the timer on user interaction...
        countDownTimer.cancel();
        if(!(Global.current_fragment_id.equals(FragmentTAGS.FR_WEBVIEW_PAYMENT)))
            countDownTimer.start();
    }

    @Override
    public void onAction(String actionCode) {
        if(actionCode.equals("dismiss")){
            customBottomBar.show(Global.lastSelectedBottomTab, true);
        }
    }

    @Override
    public void onSuccess(JSONObject result) {

    }

    @Override
    public void onDownloadFinish(Object data) {

    }

    public void adjustFontScale(Configuration configuration, float scale) {


        configuration.fontScale = scale;
        DisplayMetrics metrics = MainActivity.this.getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        Objects.requireNonNull(wm).getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        MainActivity.this.getResources().updateConfiguration(configuration, metrics);

    }

    @Override
    public void finish() {
        Global.isLogout = false;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        }
        else {
            super.finish();
        }
    }



    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {

            if(MainActivity.this!=null) {


                if(Global.appMsg!=null)
                    AlertDialogUtil.timeoutAlertDialog("", CURRENT_LOCALE.equals("en") ? Global.appMsg.getSessionTimeoutEn() : Global.appMsg.getSessionTimeoutAr(), getResources().getString(R.string.ok), MainActivity.this);
                else
                    AlertDialogUtil.timeoutAlertDialog("", getResources().getString(R.string.timeout), getResources().getString(R.string.ok), MainActivity.this);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //Toast.makeText(MainActivity.this, "seconds remaining: " + millisUntilFinished / 1000, Toast.LENGTH_SHORT).show();
        }

    }

    public interface onPermissionResult{
        void OnPermissionAccepted(Boolean isAccepted);
    }
}
