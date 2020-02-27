package dm.sime.com.kharetati.view.activities;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.ActivityMainBinding;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.HomeRepository;
import dm.sime.com.kharetati.datas.repositories.MainRepository;
import dm.sime.com.kharetati.utility.CustomContextWrapper;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
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

public class MainActivity extends AppCompatActivity implements FragmentNavigator, MainNavigator {

    ActivityMainBinding binding;
    MainViewModel model;
    FragmentManager fragmentManager = null;
    FragmentTransaction tx = null;
    MainViewModelFactory factory;
    private MainRepository repository;

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

        model.initialize();
        binding.setActivityMainVM(model);
        initializeActivity();
    }

    private void initializeActivity(){
        final BottomNavigationFragmentSheet myBottomSheet = BottomNavigationFragmentSheet.newInstance();
        binding.customBottomBar.add(new MeowBottomNavigation.Model(1, R.drawable.ic_dashboard_white_24dp));
        binding.customBottomBar.add(new MeowBottomNavigation.Model(2, R.drawable.ic_insert_emoticon_white_24dp));
        binding.customBottomBar.add(new MeowBottomNavigation.Model(3, R.drawable.ic_home_white_24dp));
        binding.customBottomBar.add(new MeowBottomNavigation.Model(4, R.drawable.ic_message_white_24dp));
        binding.customBottomBar.add(new MeowBottomNavigation.Model(5, R.drawable.ic_more_horiz_white_24dp));

        binding.customBottomBar.show(3, true);
        binding.txtUsername.setText(Global.isUserLoggedIn?(Global.getUser(this).getFullname()): LoginViewModel.guestName);

        binding.customBottomBar.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model bottomModel) {
                // YOUR CODES
                if(bottomModel.getId() == 5){
                    myBottomSheet.show(getSupportFragmentManager(), myBottomSheet.getTag());
                } else {
                    loadFragment(model.bottomNavigationTAG(bottomModel.getId()), false, null);
                }
                return null;
            }
        });



        openHomePage();

        /*binding.customBottomBar.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // YOUR CODES
                return null;
            }
        });*/
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
        loadFragment(FragmentTAGS.FR_HOME, false, null);
    }

    @Override
    public void fragmentNavigator(String fragmentTag, Boolean addToBackStack, List<Object> params) {
        loadFragment(fragmentTag, addToBackStack, params);
    }

    public Fragment loadFragment(String fragment_tag, Boolean addToBackStack, List<Object> params) {
        fragmentManager = getSupportFragmentManager();

        tx = fragmentManager.beginTransaction();
        Fragment fragment = null;
        switch (fragment_tag) {
            case FragmentTAGS.FR_HOME:
                fragment = HomeFragment.newInstance();
                break;
            case FragmentTAGS.FR_DASHBOARD:
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
            case FragmentTAGS.FR_WEBVIEW:
                if(params!=null)
                    Global.webViewUrl =params.get(0).toString();
                fragment = WebViewFragment.newInstance(Global.webViewUrl);
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
            /*RelativeLayout.LayoutParams uiContainerParam =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);
            uiContainerParam.setMargins(0, 0, 0, 0);
            binding.uiContainer.setLayoutParams(uiContainerParam);*/
        } else {
            binding.customBottomBar.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams uiContainerParam =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT);
            uiContainerParam.setMargins(0, 0, 0, 60);
            uiContainerParam.addRule(RelativeLayout.BELOW, R.id.layoutProfile);
            binding.uiContainer.setLayoutParams(uiContainerParam);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Global.current_fragment_id.equals(FragmentTAGS.FR_WEBVIEW)){
            onWebViewBack();
        }
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

    }

    @Override
    public void onFailure(String Msg) {

    }

    @Override
    public void onWebViewBack() {
        binding.customBottomBar.show(3, true);

    }

    private void changeActionBarStatus(boolean key){
        if(!key) {
            binding.layoutProfile.setVisibility(View.GONE);
        } else {
            binding.layoutProfile.setVisibility(View.VISIBLE);
        }
    }
}
