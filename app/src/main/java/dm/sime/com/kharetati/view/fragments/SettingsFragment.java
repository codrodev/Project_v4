package dm.sime.com.kharetati.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Collections;
import java.util.List;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentFeedbackBinding;
import dm.sime.com.kharetati.databinding.FragmentSettingsBinding;
import dm.sime.com.kharetati.utility.CustomContextWrapper;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.LoginActivity;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.viewModels.FeedbackViewModel;
import dm.sime.com.kharetati.view.viewModels.LoginViewModel;
import dm.sime.com.kharetati.view.viewModels.SettingsViewModel;

import static android.content.Context.WINDOW_SERVICE;
import static dm.sime.com.kharetati.utility.Global.CURRENT_LOCALE;
import static dm.sime.com.kharetati.utility.Global.MYPREFERENCES;
import static dm.sime.com.kharetati.utility.constants.AppConstants.FONT_SIZE;
import static dm.sime.com.kharetati.utility.constants.AppConstants.TALK_BACK;
import static dm.sime.com.kharetati.utility.constants.AppConstants.USER_LANGUAGE;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_FEEDBACK;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_SETTINGS;

public class SettingsFragment extends Fragment {
    FragmentSettingsBinding binding;
    SettingsViewModel model;
    private View mRootView;

    private Tracker mTracker;
    private SharedPreferences sharedpreferences;

    public static SettingsFragment newInstance(){
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "Dubai-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.getView());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(SettingsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.current_fragment_id = FragmentTAGS.FR_SETTINGS;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        binding.setFragmentSettingsVM(model);
        mRootView = binding.getRoot();
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_SETTINGS);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        ((MainActivity)getActivity()).manageBottomBar(false);
        ((MainActivity)getActivity()).manageActionBar(false);
        sharedpreferences = getActivity().getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        String locale = sharedpreferences.getString(USER_LANGUAGE, "defaultStringIfNothingFound");
        if(!locale.equals("defaultStringIfNothingFound"))
            CURRENT_LOCALE =locale;
        else
            CURRENT_LOCALE ="en";
        initializePage();
        return binding.getRoot();

    }

    private void initializePage() {
        if(getActivity()!=null)
        adjustFontScale(getActivity().getResources().getConfiguration(),Global.fontScale);

        if(Global.isUAE){
            if(Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0) {
                binding.txtUsername.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameEN());
            } else {
                binding.txtUsername.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameAR());
            }
        } else {
            if(Global.isUserLoggedIn){
                if(getActivity()!=null) {
                    if (CURRENT_LOCALE.equals("en"))
                        binding.txtUsername.setText(Global.getUser(getActivity()).getFullname() != null ? Global.getUser(getActivity()).getFullname() : Global.getUser(getActivity()).getFullnameAR());
                    else
                        binding.txtUsername.setText(Global.getUser(getActivity()).getFullnameAR() != null ? Global.getUser(getActivity()).getFullnameAR() : Global.getUser(getActivity()).getFullname());
                }
            }
            else
                binding.txtUsername.setText(LoginViewModel.guestName);
        }


        if(CURRENT_LOCALE.equals("en")){
            binding.txtEnglish.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_maroon_bg));
            binding.txtEnglish.setTextColor(getActivity().getResources().getColor(R.color.white));
            binding.txtArabic.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_white_bg));
            binding.txtArabic.setTextColor(getActivity().getResources().getColor(R.color.black));
        }
        else{
            binding.txtArabic.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_maroon_bg));
            binding.txtArabic.setTextColor(getActivity().getResources().getColor(R.color.white));
            binding.txtEnglish.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_white_bg));
            binding.txtEnglish.setTextColor(getActivity().getResources().getColor(R.color.black));
        }
        binding.txtEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtEnglish.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_maroon_bg));
                binding.txtEnglish.setTextColor(getActivity().getResources().getColor(R.color.white));
                binding.txtArabic.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_white_bg));
                binding.txtArabic.setTextColor(getActivity().getResources().getColor(R.color.black));
                CURRENT_LOCALE = (CURRENT_LOCALE.equals("en")) ? "ar" : "en";

                sharedpreferences.edit().putString(USER_LANGUAGE,CURRENT_LOCALE).apply();
                sharedpreferences.edit().putString("currentFragment",Global.current_fragment_id).apply();

                Global.changeLang(CURRENT_LOCALE, getActivity());
                ((MainActivity)getActivity()).recreate();

            }
        });
        binding.txtArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtArabic.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_maroon_bg));
                binding.txtArabic.setTextColor(getActivity().getResources().getColor(R.color.white));
                binding.txtEnglish.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_white_bg));
                binding.txtEnglish.setTextColor(getActivity().getResources().getColor(R.color.black));

                List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
                if(fragments!=null){
                    fragments.removeAll(Collections.singleton(null));//remove null values
                    if (fragments.size() > 0){
                        Global.current_fragment_id_locale_change = fragments.get(fragments.size() - 1).getTag();
                        Global.isLanguageChanged=true;
                    }
                }

                CURRENT_LOCALE = (CURRENT_LOCALE.equals("ar")) ? "en" : "ar";


                sharedpreferences.edit().putString(USER_LANGUAGE,CURRENT_LOCALE).apply();
                sharedpreferences.edit().putString("currentFragment",Global.current_fragment_id).apply();

                Global.changeLang(CURRENT_LOCALE, getActivity());
                ((MainActivity)getActivity()).recreate();
            }
        });

        if(sharedpreferences.getString(TALK_BACK,"off").equals("on")){
            binding.txtOn.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_maroon_bg));
            binding.txtOn.setTextColor(getActivity().getResources().getColor(R.color.white));
            binding.txtOff.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_white_bg));
            binding.txtOff.setTextColor(getActivity().getResources().getColor(R.color.black));
        }
        else{
            binding.txtOff.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_maroon_bg));
            binding.txtOff.setTextColor(getActivity().getResources().getColor(R.color.white));
            binding.txtOn.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_white_bg));
            binding.txtOn.setTextColor(getActivity().getResources().getColor(R.color.black));
        }
        binding.txtOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtOn.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_maroon_bg));
                binding.txtOn.setTextColor(getActivity().getResources().getColor(R.color.white));
                binding.txtOff.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_white_bg));
                binding.txtOff.setTextColor(getActivity().getResources().getColor(R.color.black));
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                sharedpreferences.edit().putString(TALK_BACK,"on").apply();
            }
        });
        binding.txtOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtOff.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_maroon_bg));
                binding.txtOff.setTextColor(getActivity().getResources().getColor(R.color.white));
                binding.txtOn.setBackground(getActivity().getResources().getDrawable(R.drawable.capsule_white_bg));
                binding.txtOn.setTextColor(getActivity().getResources().getColor(R.color.black));
                sharedpreferences.edit().putString(TALK_BACK,"off").apply();
            }
        });

        Global.fontScale = sharedpreferences.getFloat(FONT_SIZE,1f);
        if(Global.fontScale==0.5f)
            font1Clicked();
        else if(Global.fontScale==0.75f)
            font2Clicked();
        else if(Global.fontScale==1f)
            font3Clicked();
        else if(Global.fontScale==1.25f)
            font4Clicked();
        else if(Global.fontScale==1.5f)
            font5Clicked();

        binding.fontSize1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                font1Clicked();
                Global.fontScale = 0.5f;
                if(getActivity()!=null)
                    adjustFontScale(getActivity().getResources().getConfiguration(),Global.fontScale);
                sharedpreferences.edit().putFloat(FONT_SIZE,Global.fontScale).apply();
                sharedpreferences.edit().putString("currentFragment",Global.current_fragment_id).apply();
                ((MainActivity)getActivity()).recreate();
            }
        }); binding.fontSize2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                font2Clicked();
                Global.fontScale = 0.75f;
                if(getActivity()!=null)
                    adjustFontScale(getActivity().getResources().getConfiguration(),Global.fontScale);
                sharedpreferences.edit().putFloat(FONT_SIZE,Global.fontScale).apply();
                sharedpreferences.edit().putString("currentFragment",Global.current_fragment_id).apply();
                ((MainActivity)getActivity()).recreate();
            }
        }); binding.fontSize3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                font3Clicked();
                Global.fontScale = 1f;
                if(getActivity()!=null)
                    adjustFontScale(getActivity().getResources().getConfiguration(),Global.fontScale);
                sharedpreferences.edit().putFloat(FONT_SIZE,Global.fontScale).apply();
                sharedpreferences.edit().putString("currentFragment",Global.current_fragment_id).apply();
                ((MainActivity)getActivity()).recreate();
            }
        }); binding.fontSize4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                font4Clicked();
                Global.fontScale = 1.25f;
                if(getActivity()!=null)
                    adjustFontScale(getActivity().getResources().getConfiguration(),Global.fontScale);
                sharedpreferences.edit().putFloat(FONT_SIZE,Global.fontScale).apply();
                sharedpreferences.edit().putString("currentFragment",Global.current_fragment_id).apply();
                ((MainActivity)getActivity()).recreate();
            }
        }); binding.fontSize5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                font5Clicked();
                Global.fontScale = 1.5f;
                if(getActivity()!=null)
                    adjustFontScale(getActivity().getResources().getConfiguration(),Global.fontScale);
                sharedpreferences.edit().putFloat(FONT_SIZE,Global.fontScale).apply();
                sharedpreferences.edit().putString("currentFragment",Global.current_fragment_id).apply();
                ((MainActivity)getActivity()).recreate();

            }
        });

    }
    public void font1Clicked(){
        binding.fontSize1.setBackground(getActivity().getResources().getDrawable(R.drawable.border_background));
        binding.fontSize2.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize3.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize4.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize5.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize1.setTextColor(getActivity().getResources().getColor(R.color.white));
        binding.fontSize2.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize3.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize4.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize5.setTextColor(getActivity().getResources().getColor(R.color.black));
    }
    public void font2Clicked(){
        binding.fontSize1.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize2.setBackground(getActivity().getResources().getDrawable(R.drawable.border_background));
        binding.fontSize3.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize4.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize5.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize1.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize2.setTextColor(getActivity().getResources().getColor(R.color.white));
        binding.fontSize3.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize4.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize5.setTextColor(getActivity().getResources().getColor(R.color.black));

    }public void font3Clicked(){
        binding.fontSize1.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize2.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize3.setBackground(getActivity().getResources().getDrawable(R.drawable.border_background));
        binding.fontSize4.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize5.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize1.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize2.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize3.setTextColor(getActivity().getResources().getColor(R.color.white));
        binding.fontSize4.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize5.setTextColor(getActivity().getResources().getColor(R.color.black));
    }public void font4Clicked(){
        binding.fontSize1.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize2.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize3.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize4.setBackground(getActivity().getResources().getDrawable(R.drawable.border_background));
        binding.fontSize5.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize1.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize2.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize3.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize4.setTextColor(getActivity().getResources().getColor(R.color.white));
        binding.fontSize5.setTextColor(getActivity().getResources().getColor(R.color.black));

    }public void font5Clicked(){
        binding.fontSize1.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize2.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize3.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize4.setBackground(getActivity().getResources().getDrawable(R.drawable.grey_border_bg));
        binding.fontSize5.setBackground(getActivity().getResources().getDrawable(R.drawable.border_background));
        binding.fontSize1.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize2.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize3.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize4.setTextColor(getActivity().getResources().getColor(R.color.black));
        binding.fontSize5.setTextColor(getActivity().getResources().getColor(R.color.white));
    }

    public  void adjustFontScale(Configuration configuration, float scale) {

        configuration.fontScale = scale;
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getActivity().getResources().updateConfiguration(configuration, metrics);

    }
}
