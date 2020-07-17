package dm.sime.com.kharetati.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentBottomNavigationBinding;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.viewModels.BottomNavigationViewModel;
import dm.sime.com.kharetati.view.viewModels.MapViewModel;

import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_BOOKMARK;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_BOTTOMSHEET;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_HOME;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_MYMAP;

public class BottomNavigationFragmentSheet extends Fragment {

    BottomNavigationViewModel model;
    FragmentBottomNavigationBinding binding;
    private View mRootView;
    private static OnActionListener listener;
    private Tracker mTracker;
    int images[] = {R.drawable.mymap_512,R.drawable.favorites_512,R.drawable.settings_512,R.drawable.more_help_512,R.drawable.accessibility_512,R.drawable.contact_512,R.drawable.about_512,R.drawable.faq_512,R.drawable.terms_512,R.drawable.share_512,R.drawable.rating_512,Global.isUserLoggedIn?R.drawable.logout_512:R.drawable.login_512};
    int ids[] = {R.id.imgMoreIcon,R.id.txtMoreName};
    String keys[]= {"one","two"};

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
        Global.current_fragment_id = FR_BOTTOMSHEET;
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_BOTTOMSHEET);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        binding.setFragmentBottomNavigation(model);
        ((MainActivity)getActivity()).manageActionBar(true);
        ((MainActivity)getActivity()).manageBottomBar(true);
        ((MainActivity)getActivity()).setScreenName(getActivity().getResources().getString(R.string.more));

       /* BottomSheetBehavior behavior = BottomSheetBehavior.from(binding.bottomSheetMore);
        if(Global.isLandScape)
        behavior.setPeekHeight((int) Global.height);*/

        mRootView = binding.getRoot();




        String names[] = new String[]{getContext().getResources().getString(R.string.mymaps),getContext().getResources().getString(R.string.bookmark),getContext().getResources().getString(R.string.title_settings),getContext().getResources().getString(R.string.INTROHELP),getContext().getResources().getString(R.string.ACCESSIBILITY),getContext().getResources().getString(R.string.menu_contact_us),getContext().getResources().getString(R.string.ABOUTUS),getContext().getResources().getString(R.string.FAQMENU),getContext().getResources().getString(R.string.TERMSANDCONDITIONS),getContext().getResources().getString(R.string.SHAREKHARETATIAPP),getContext().getResources().getString(R.string.RATEUS),Global.isUserLoggedIn?getContext().getResources().getString(R.string.logout):getContext().getResources().getString(R.string.login)};

        /*binding.logoutText.setText(Global.isUserLoggedIn? getActivity().getResources().getText(R.string.logout):getActivity().getResources().getText(R.string.login));
        binding.txtLanguage.setText(Global.CURRENT_LOCALE.equals("en")?"عربى":"English");
        binding.imgLanguage.setImageResource(Global.CURRENT_LOCALE.equals("en")?R.drawable.arabic:R.drawable.english);
        binding.iconlayoutHelp.setRotationY(Global.CURRENT_LOCALE.equals("en")?0:180);*/
        //mRootView.findViewById(binding.gridMore.getAdapter().getItemViewType(0)).setRotationY(Global.CURRENT_LOCALE.equals("en")?0:180);



        ArrayList al = new ArrayList();


        for(int i=0;i<images.length;i++){
            HashMap hm = new HashMap();
            hm.put(keys[0],images[i]);
            hm.put(keys[1],names[i]);

            al.add(hm);

        }
        if(!Global.isUserLoggedIn)
            al.remove(0);
        SimpleAdapter sa = new SimpleAdapter(getActivity(),al,R.layout.more_layout_item,keys,ids);
        binding.gridMore.setAdapter(sa);

        binding.gridMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Global.isUserLoggedIn){
                    ((MainActivity)getActivity()).setScreenName(names[position]);
                    if(position==9||position==10)
                        ((MainActivity)getActivity()).setScreenName(getActivity().getResources().getString(R.string.more));
                }
                else{
                    ((MainActivity)getActivity()).setScreenName(names[position+1]);
                    if(position==8||position==9)
                        ((MainActivity)getActivity()).setScreenName(getActivity().getResources().getString(R.string.more));
                }

                switch (position){

                    case 0:
                        if(Global.isUserLoggedIn)onMyMapClicked();else onFavouritesClicked();
                        break;
                    case 1:
                        if(Global.isUserLoggedIn)onFavouritesClicked();else onSettingsClicked();
                        break;
                    case 2:
                        if(Global.isUserLoggedIn)onSettingsClicked();else onHelpClicked();
                        break;
                    case 3:
                        if(Global.isUserLoggedIn)onHelpClicked();else onAccessibilityClicked();
                        break;
                    case 4:
                        if(Global.isUserLoggedIn)onAccessibilityClicked();else onContactusClicked();
                        break;
                    case 5:
                        if(Global.isUserLoggedIn)onContactusClicked();else onAboutUsClicked();
                        break;
                    case 6:
                        if(Global.isUserLoggedIn)onAboutUsClicked();else onFAQClicked();
                        break;
                    case 7:
                        if(Global.isUserLoggedIn)onFAQClicked();else onTermsAndConditionsClicked();
                        break;
                    case 8:
                        if(Global.isUserLoggedIn)onTermsAndConditionsClicked();else onShareClicked();
                        break;
                    case 9:
                        if(Global.isUserLoggedIn)onShareClicked();else onRateusClicked();
                        break;
                    case 10:
                        if(Global.isUserLoggedIn)onRateusClicked();else onLogoutClicked();
                        break;
                    case 11:
                        if(Global.isUserLoggedIn)onLogoutClicked();
                        break;
                }
            }
        });

       /* binding.layoutLanguage.setOnClickListener(new View.OnClickListener() {
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
            *//*locale = new Locale(CURRENT_LOCALE);
            Locale.setDefault(locale);
            android.content.res.Configuration config = new android.content.res.Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());*//*
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
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Khareatai App");
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
                dismiss();
                ArrayList al = new ArrayList();


                if(Global.HelpUrl!=null && !Global.HelpUrl.equals(""))
                al.add(HomeFragment.constructUrl((Global.HelpUrl),getActivity()));

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
                    al.add(HomeFragment.constructUrl((Global.CURRENT_LOCALE.equals("en")?Global.aboutus_en_url:Global.aboutus_ar_url),getActivity()));
                    ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                }
                dismiss();
            }
        });
        binding.help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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

                //Global.faq_url =Global.faq_url+"lang="+Global.CURRENT_LOCALE;

                al.add(HomeFragment.constructUrl(Global.faq_url,getActivity()));
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
                al.add(HomeFragment.constructUrl((Global.CURRENT_LOCALE.equals("en")?Global.terms_en_url:Global.terms_ar_url),getActivity()));
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
                else{
                    Global.logout(getActivity());
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("More Screen")
                            .setAction("Action Logout")
                            .setLabel("Logout")
                            .setValue(1)
                            .build());
                }
            }
        });*/

        return binding.getRoot();
    }

    private void onContactusClicked() {
        ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_CONTACT_US,true,null);
    }

    private void onMyMapClicked(){
        /*ArrayList al = new ArrayList();
        al.add(FR_MYMAP);
        ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_DASHBOARD,true,al);*/
        ((MainActivity)getActivity()).navigateToDashboard(0);
        MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(), FR_MYMAP, null /* class override */);

    }
    private void onFavouritesClicked(){
        /*ArrayList al = new ArrayList();
        al.add(FR_BOOKMARK);
        ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_DASHBOARD,true,al);*/
        ((MainActivity)getActivity()).navigateToDashboard(1);
        MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(), FR_BOOKMARK, null /* class override */);
    }

    private void onSettingsClicked() {
        ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_SETTINGS,true,null);
        MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(), FragmentTAGS.FR_SETTINGS, null /* class override */);
    }

    public void onHelpClicked(){

        ArrayList al = new ArrayList();


        if(Global.HelpUrl!=null && !Global.HelpUrl.equals("")){
            al.add(HomeFragment.constructUrl((Global.HelpUrl),getActivity()));
            al.add(getActivity().getResources().getString(R.string.help));}

        ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
        MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(),getActivity().getResources().getString(R.string.help) , null /* class override */);

    }
    public void onLogoutClicked(){if(Global.isUserLoggedIn)
        AlertDialogUtil.logoutAlert(getActivity().getResources().getString(R.string.logout_msg),getActivity().getResources().getString(R.string.yes),getActivity().getResources().getString(R.string.no),getActivity());
    else{

        Global.logout(getActivity());
        ((MainActivity)getActivity()).finish();
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("More Screen")
                .setAction("Action Logout")
                .setLabel("Logout")
                .setValue(1)
                .build());
    }}
    public void onShareClicked(){
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
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Khareatai App");
        sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        sendIntent.setType("text/html");
        startActivity(Intent.createChooser(sendIntent, "Share with"));

    }

    }
    public void onFAQClicked(){ ArrayList al = new ArrayList();

        //Global.faq_url =Global.faq_url+"lang="+Global.CURRENT_LOCALE;

        al.add(HomeFragment.constructUrl(Global.faq_url,getActivity()));
        al.add(getActivity().getResources().getString(R.string.FAQMENU));
        ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
        MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(),"FAQ SCREEN" , null /* class override */);


    }
    public void onAboutUsClicked(){
        if (!Global.isConnected(getActivity())) {
        if (Global.appMsg != null)
            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getResources().getString(R.string.ok), getActivity());
        else
            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
    }
    else{
        ArrayList al = new ArrayList();
        al.add(HomeFragment.constructUrl((Global.CURRENT_LOCALE.equals("en")?Global.aboutus_en_url:Global.aboutus_ar_url),getActivity()));
            al.add(getActivity().getResources().getString(R.string.ABOUTUS));
        ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
            MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(),"ABOUTUS SCREEN" , null /* class override */);

    }


    }
    public void onTermsAndConditionsClicked(){
        if (!Global.isConnected(getActivity())) {
        if (Global.appMsg != null)
            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getResources().getString(R.string.ok), getActivity());
        else
            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
    }
    else{
        ArrayList al = new ArrayList();
        al.add(HomeFragment.constructUrl((Global.CURRENT_LOCALE.equals("en")?Global.terms_en_url:Global.terms_ar_url),getActivity()));
            al.add(getActivity().getResources().getString(R.string.TERMSANDCONDITIONS));
        ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
            MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(),"TERMS AND CONDITIONS SCREEN" , null /* class override */);
    }


    }
    public void onRateusClicked(){
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
    public void onAccessibilityClicked(){
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).manageActionBar(true);
        ((MainActivity)getActivity()).manageBottomBar(true);
        ((MainActivity)getActivity()).setScreenName(getActivity().getResources().getString(R.string.more));
        MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(),"OPTIONS SCREEN" , null /* class override */);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listener.onAction("dismiss");
    }

    public interface OnActionListener {
        void onAction(String actionCode);
    }
    private String constructUrl(String url){
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        if(!url.endsWith("?")) {
            builder.append("?");
        }
        builder.append("token=" + Global.accessToken + "&");
        //builder.append("remarks=" + Global.getPlatformRemark() + "&");
        String lang = Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0 ? "en" : "ar";
        builder.append("lng=" + lang + "&");
        builder.append("appsrc=kharetati" + "&");
        if(!Global.isUserLoggedIn){
            builder.append("userType=GUEST&");
            //builder.append("user_id="+ Global.sime_userid +"&");
            //builder.append("user_name=GUEST");
        } else {
            if(Global.isUAE){
                builder.append("userType=UAEPASS&");
                builder.append("user_id=" + Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getUuid() + "&");
                if(Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0) {
                    builder.append("user_name=" + Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameEN() + "&");
                } else {
                    builder.append("user_name=" + Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameAR() + "&");
                }
            } else {
                builder.append("userType=MYID&");
               // builder.append("user_id=" + Global.username + "&");
                //builder.append("user_name=" + Global.getUser(getActivity()).getFullname());
            }

        }
        return builder.toString();
    }
}
