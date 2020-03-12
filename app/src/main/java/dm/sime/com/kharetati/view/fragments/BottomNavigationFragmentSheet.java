package dm.sime.com.kharetati.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentBottomNavigationBinding;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.viewModels.BottomNavigationViewModel;
import dm.sime.com.kharetati.view.viewModels.MapViewModel;

public class BottomNavigationFragmentSheet extends BottomSheetDialogFragment {

    BottomNavigationViewModel model;
    FragmentBottomNavigationBinding binding;
    private View mRootView;
    private static OnActionListener listener;

    public static BottomNavigationFragmentSheet newInstance(OnActionListener list) {
        BottomNavigationFragmentSheet f = new BottomNavigationFragmentSheet();
        listener = list;
        return f;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "Dubai-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.getView());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(BottomNavigationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_navigation, container, false);
        Global.current_fragment_id = FragmentTAGS.FR_BOTTOMSHEET;
        binding.setFragmentBottomNavigation(model);
        mRootView = binding.getRoot();


        binding.logoutText.setText(Global.isUserLoggedIn? getActivity().getResources().getText(R.string.logout):getActivity().getResources().getText(R.string.login));
        binding.txtLanguage.setText(Global.CURRENT_LOCALE.equals("en")?"عربى":"English");
        binding.imgLanguage.setImageResource(Global.CURRENT_LOCALE.equals("en")?R.drawable.arabic:R.drawable.english);
        binding.iconlayoutHelp.setRotationY(Global.CURRENT_LOCALE.equals("en")?0:180);
        binding.layoutLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
                if(fragments!=null){
                    fragments.removeAll(Collections.singleton(null));//remove null values
                    if (fragments.size() > 0){
                        Global.current_fragment_id_locale_change = fragments.get(fragments.size() - 1).getTag();
                        Global.isLanguageChanged=true;
                    }
                }


                Global.CURRENT_LOCALE = (Global.CURRENT_LOCALE.compareToIgnoreCase("en")==0 ? "ar" : "en");
                Global.changeLang(Global.CURRENT_LOCALE,getActivity());
            /*locale = new Locale(CURRENT_LOCALE);
            Locale.setDefault(locale);
            android.content.res.Configuration config = new android.content.res.Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());*/
                getActivity().recreate();
                dismiss();
            }
        });
        binding.layoutShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Global.isConnected(getActivity())) {
                    if (Global.appMsg != null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else{
                    StringBuilder sb = new StringBuilder();

                    sb.append(getString(R.string.try_kharetati_app));
                    sb.append('\n');
                    sb.append('\n');
                    sb.append("Play Store: https://play.google.com/store/apps/details?id=dm.sime.com.kharetati");
                    sb.append('\n');
                    sb.append('\n');
                    sb.append("App Store: https://itunes.apple.com/ca/app/kharetati/id1277642590?mt=8");
                    sb.append('\n');

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.kharetati_app));
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                    sendIntent.setType("text/html");
                    startActivity(Intent.createChooser(sendIntent, "Share with"));

                }
                dismiss();
            }
        });
        binding.layoutRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Global.isConnected(getActivity())) {
                    if (Global.appMsg != null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else
                    AlertDialogUtil.ratingAlertDialog(
                            getResources().getString(R.string.menu_rate_us),
                            getResources().getString(R.string.msg_rate_us),
                            getResources().getString(R.string.rate_it),getResources().getString(R.string.remindme),getActivity()
                    );
            }
        });
        binding.layoutAccessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            }
        });
        binding.layoutHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList al = new ArrayList();
                al.add(Global.CURRENT_LOCALE.equals("en")? Global.home_en_url:Global.home_ar_url);
                ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
            }
        });

        binding.aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Global.isConnected(getActivity())) {
                    if (Global.appMsg != null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else{
                    ArrayList al = new ArrayList();
                    al.add(Global.CURRENT_LOCALE.equals("en")?Global.aboutus_en_url:Global.aboutus_ar_url);
                    ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                }
                dismiss();
            }
        });
        binding.help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList al = new ArrayList();
                al.add("");
                ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                dismiss();
            }
        });
        binding.faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList al = new ArrayList();
                al.add(Global.faq_url);
                ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                dismiss();
            }
        });
        binding.termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Global.isConnected(getActivity())) {
                    if (Global.appMsg != null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else{
                ArrayList al = new ArrayList();
                al.add(Global.CURRENT_LOCALE.equals("en")?Global.terms_en_url:Global.terms_ar_url);
                ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                }
                dismiss();
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Global.isUserLoggedIn)
                    AlertDialogUtil.logoutAlert(getActivity().getResources().getString(R.string.logout_msg),getActivity().getResources().getString(R.string.ok),getActivity().getResources().getString(R.string.cancel),getActivity());
                else
                    Global.logout(getActivity());
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listener.onAction("dismiss");
    }

    public interface OnActionListener {
        void onAction(String actionCode);
    }
}
