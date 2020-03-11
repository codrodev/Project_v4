package dm.sime.com.kharetati.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.ActivityMainBinding;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.HomeRepository;
import dm.sime.com.kharetati.datas.repositories.MainRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.CustomContextWrapper;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppConstants;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.fragments.FeedbackFragment;
import dm.sime.com.kharetati.view.fragments.RequestDetailsFragment;
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
import dm.sime.com.kharetati.view.viewmodelfactories.HomeViewModelFactory;
import dm.sime.com.kharetati.view.viewmodelfactories.MainViewModelFactory;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static dm.sime.com.kharetati.utility.Global.CURRENT_LOCALE;

public class MainActivity extends AppCompatActivity implements FragmentNavigator, MainNavigator, BottomNavigationFragmentSheet.OnActionListener {

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

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

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
        model.initialize();
        binding.setActivityMainVM(model);
        final BottomNavigationFragmentSheet myBottomSheet = BottomNavigationFragmentSheet.newInstance(this);
        binding.customBottomBar.add(new MeowBottomNavigation.Model(1, R.drawable.ic_dashboard));
        binding.customBottomBar.add(new MeowBottomNavigation.Model(2, R.drawable.ic_happiness));
        binding.customBottomBar.add(new MeowBottomNavigation.Model(3, R.drawable.ic_home_run));
        binding.customBottomBar.add(new MeowBottomNavigation.Model(4, R.drawable.ic_comment));
        binding.customBottomBar.add(new MeowBottomNavigation.Model(5, R.drawable.ic_more));

        binding.customBottomBar.show(3, true);
        countDownTimer = new MyCountDownTimer(startTime, interval);



        binding.txtUsername.setText(Global.isUserLoggedIn?(Global.getUser(this).getFullname()): LoginViewModel.guestName);

        binding.customBottomBar.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model bottomModel) {
                // YOUR CODES
                myBottomModel = bottomModel;
                if(bottomModel.getId() == 5){
                    myBottomSheet.show(getSupportFragmentManager(), myBottomSheet.getTag());
                } else {
                    Global.lastSelectedBottomTab = bottomModel.getId();
                    loadFragment(model.bottomNavigationTAG(bottomModel.getId()), true, null);
                    /*if(savedInstanceState=null)
                    savedInstanceState.putInt("loadPosition",myBottomModel.getId());*/
                }
                return null;
            }
        });

        binding.imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList al = new ArrayList();
                if(Global.FragmentTagForHelpUrl.equals(FragmentTAGS.FR_HOME)){
                    /*if(Global.helpUrlEn != null || Global.helpUrlAr != null) {
                        al.add(Global.CURRENT_LOCALE.equals("en")? Global.helpUrlEn:Global.helpUrlAr);

                    } else {

                    }*/
                    if(Global.home_en_url != null || Global.home_en_url != null) {
                        al.add(Global.CURRENT_LOCALE.equals("en") ? Global.home_en_url : Global.home_en_url);
                    }
                } else if(Global.FragmentTagForHelpUrl.equals(FragmentTAGS.FR_DASHBOARD)){
                    if(Global.FragmentTagForDashboardHelpUrl == 0) {
                        al.add(Global.CURRENT_LOCALE.equals("en")? Global.mymaps_en_url:Global.mymaps_ar_url);

                    } else {
                        al.add(Global.CURRENT_LOCALE.equals("en")? Global.bookmarks_en_url:Global.bookmarks_ar_url);
                    }
                }
                if(al != null & al.size() > 0) {
                    loadFragment(FragmentTAGS.FR_WEBVIEW, true, al);
                } else {
                    if(Global.home_en_url != null || Global.home_en_url != null) {
                        al.add(Global.CURRENT_LOCALE.equals("en") ? Global.home_en_url : Global.home_en_url);
                    }
                    loadFragment(FragmentTAGS.FR_WEBVIEW, true, al);
                }
            }
        });

        openHomePage();
        initializeActivity();
    }

    private void initializeActivity(){

        /*binding.customBottomBar.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // YOUR CODES
                return null;
            }
        });*/
    }

    private void processIntentData(Intent intent) {
        if (intent.getStringExtra(AppConstants.OPEN_FROM_DEEP_LINK) != null) {
            AlertDialogUtil.errorAlertDialog("deep link", "deep link",
                    getString(R.string.ok), this);
        }
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

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            super.attachBaseContext(CustomContextWrapper.wrap(newBase, CURRENT_LOCALE));
        } else {
            super.attachBaseContext(newBase);
        }
    }

    private void openHomePage(){
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null) {
            processIntentData(intent);
        } else {
            loadFragment(FragmentTAGS.FR_HOME, false, null);
        }
    }

    @Override
    public void fragmentNavigator(String fragmentTag, Boolean addToBackStack, List<Object> params) {
        loadFragment(fragmentTag, addToBackStack, params);
    }

    public Fragment loadFragment(String fragment_tag, Boolean addToBackStack, List<Object> params) {
        fragmentManager = getSupportFragmentManager();
        Global.FragmentTagForHelpUrl = "";
        tx = fragmentManager.beginTransaction();
        Fragment fragment = null;
        switch (fragment_tag) {
            case FragmentTAGS.FR_HOME:
                Global.FragmentTagForHelpUrl = FragmentTAGS.FR_HOME;
                fragment = HomeFragment.newInstance();
                break;
            case FragmentTAGS.FR_DASHBOARD:
                Global.FragmentTagForHelpUrl = FragmentTAGS.FR_DASHBOARD;
                fragment = DashboardFragment.newInstance();
                break;
            case FragmentTAGS.FR_MAP:
                fragment = MapFragment.newInstance();
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
            case FragmentTAGS.FR_WEBVIEW:
                String appName = "";
                if(params!=null) {
                    Global.webViewUrl = params.get(0).toString();
                    if(params.size() > 1){
                        appName = params.get(1).toString();
                    }
                }
                fragment = WebViewFragment.newInstance(Global.webViewUrl, appName);
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
    public void manageActionBar(boolean key) {
        changeActionBarStatus(key);
    }

    @Override
    public void manageBottomBar(boolean key) {
        if(!key) {
            binding.customBottomBar.setVisibility(View.GONE);
            LinearLayout.LayoutParams uiContainerParam =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
            uiContainerParam.setMargins(0, 0, 0, 0);
            binding.uiContainer.setLayoutParams(uiContainerParam);
        } else {
            binding.customBottomBar.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams uiContainerParam =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
            uiContainerParam.setMargins(0, 0, 0, 60);
            //uiContainerParam.addRule(LinearLayout.BELOW, R.id.layoutProfile);
            binding.uiContainer.setLayoutParams(uiContainerParam);
        }
    }

    @Override
    public void navigateToDashboard() {

        binding.customBottomBar.show(1, true);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Global.helpUrlEn = "";
        Global.helpUrlAr = "";

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
                    clearBackStack();
                }

            }
            if(Global.current_fragment_id.equals(FragmentTAGS.FR_WEBVIEW))
                onWebViewBack();
            else{
                Fragment fragmentBeforeBackPress = getCurrentFragment();
                // Perform the usual back action
                super.onBackPressed();
                if(count>1){
                fragmentAfterBackPress = getCurrentFragment();
                if(fragmentAfterBackPress.getTag().equals(FragmentTAGS.FR_DASHBOARD)||fragmentAfterBackPress.getTag().equals(FragmentTAGS.FR_BOOKMARK)||fragmentAfterBackPress.getTag().equals(FragmentTAGS.FR_MYMAP)) {
                    Global.FragmentTagForHelpUrl = FragmentTAGS.FR_DASHBOARD;
                    binding.customBottomBar.show(1, true);
                    Global.lastSelectedBottomTab = 1;
                }
                if(Global.current_fragment_id.equals(FragmentTAGS.FR_HAPPINESS)) {
                    binding.customBottomBar.show(2, true);
                    Global.lastSelectedBottomTab = 2;
                }
                if(Global.current_fragment_id.equals(FragmentTAGS.FR_HOME)) {
                    Global.FragmentTagForHelpUrl = FragmentTAGS.FR_HOME;
                    binding.customBottomBar.show(3, true);
                    Global.lastSelectedBottomTab = 3;
                }
                if(Global.current_fragment_id.equals(FragmentTAGS.FR_CONTACT_US)) {
                    binding.customBottomBar.show(4, true);
                    Global.lastSelectedBottomTab = 4;
                }
                if(Global.current_fragment_id.equals(FragmentTAGS.FR_BOTTOMSHEET))
                    binding.customBottomBar.show(5, true);
                }
                if(count == 1) {
                    binding.customBottomBar.show(3, true);
                    Global.lastSelectedBottomTab = 3;
                }
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
        if(Global.alertDialog!=null){
            Global.alertDialog.dismiss();
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
        binding.customBottomBar.show(3, true);
        //if(Global.current_fragment_id.equals(FragmentTAGS.FR_))

    }

    public void changeActionBarStatus(boolean key){
        if(!key) {
            binding.layoutProfile.setVisibility(View.GONE);
            binding.profileUsername.setVisibility(View.GONE);
        } else {
            binding.layoutProfile.setVisibility(View.VISIBLE);
            binding.profileUsername.setVisibility(View.VISIBLE);
        }
    }
    public void clearStack(String FragmentTAG,int loadPosition){
        FragmentManager fragmentManager=getSupportFragmentManager();
        if(fragmentManager!=null)
            while(fragmentManager.getBackStackEntryCount() >=1) {
                if(fragmentManager.getBackStackEntryCount()==1){
                    fragmentManager.popBackStackImmediate();
                    loadFragment(FragmentTAG, false, null);
                    binding.customBottomBar.show(loadPosition, true);
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
            binding.customBottomBar.show(Global.lastSelectedBottomTab, true);
        }
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {


            AlertDialogUtil.timeoutAlertDialog("", CURRENT_LOCALE.equals("en") ? Global.appMsg.getSessionTimeoutEn(): Global.appMsg.getSessionTimeoutAr(), getResources().getString(R.string.ok), MainActivity.this);


        }

        @Override
        public void onTick(long millisUntilFinished) {
            //Toast.makeText(MainActivity.this, "seconds remaining: " + millisUntilFinished / 1000, Toast.LENGTH_SHORT).show();
        }

    }
}
