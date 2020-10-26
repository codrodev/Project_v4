package dm.sime.com.kharetati.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.StateSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.esri.arcgisruntime.mapping.view.MapView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.tabs.TabLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;


import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISMapImageSublayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.security.Credential;
import com.esri.arcgisruntime.security.UserCredential;
import com.google.firebase.analytics.FirebaseAnalytics;


import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentHomeBinding;
import dm.sime.com.kharetati.datas.models.Applications;
import dm.sime.com.kharetati.datas.models.Areas;
import dm.sime.com.kharetati.datas.models.Controls;
import dm.sime.com.kharetati.datas.models.InAppNotifications;
import dm.sime.com.kharetati.datas.models.LookupValue;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.models.SearchForm;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.HomeRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.ViewAnimationUtils;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.adapters.GridMenuAdapter;
import dm.sime.com.kharetati.view.adapters.GridMenuPagerAdapter;
import dm.sime.com.kharetati.view.adapters.InAppNotificationAdapter;
import dm.sime.com.kharetati.view.customview.CleanableEditText;
import dm.sime.com.kharetati.view.customview.CustPagerTransformer;
import dm.sime.com.kharetati.view.customview.OnSpinerItemClick;
import dm.sime.com.kharetati.view.customview.SpinnerDialog;
import dm.sime.com.kharetati.view.navigators.HomeNavigator;
import dm.sime.com.kharetati.view.viewModels.HomeViewModel;
import dm.sime.com.kharetati.view.viewmodelfactories.HomeViewModelFactory;

import static dm.sime.com.kharetati.utility.Global.CURRENT_LOCALE;
import static dm.sime.com.kharetati.utility.Global.isLand;
import static dm.sime.com.kharetati.utility.Global.isMakani;
import static dm.sime.com.kharetati.utility.Global.isPlotSearch;
import static dm.sime.com.kharetati.utility.Global.searchText;
import static dm.sime.com.kharetati.utility.Global.showSoftKeyboard;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_ATTACHMENT;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_HOME;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_LAND_REGISTRATION_WEB;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_WEBVIEW;

public class HomeFragment extends Fragment implements GridMenuAdapter.OnMenuSelectedListener, EditText.OnEditorActionListener, ViewPager.OnPageChangeListener, HomeNavigator {
    FragmentHomeBinding binding;
    HomeViewModel model;
    private View mRootView;
    String param;
    private GridMenuPagerAdapter gridPagerAdapter;
    List<SearchForm> lstSearchForm;
    private EditText txtParcelID;
    private String parcelNumber;
    private ProgressBar progressBar;
    private MapView mMapView;
    private TextView[] dots;
    HomeViewModelFactory factory;
    private HomeRepository repository;
    public HomeNavigator homeNavigator;
    private SpinnerDialog spinnerDialog;
    private TextView spinnerView;
    public static String communityId;
    public static HomeViewModel homeVM;
    public List<CleanableEditText> lstRuntimeCleanableText;
    InAppNotificationAdapter adapterNotification;
    private Tracker mTracker;
    private RelativeLayout relativeLayout;
    private ImageView imageView;
    private TextView tabTextView;
    public static CleanableEditText x;
    public static boolean isEmpty;


    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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


        try {
            repository = new HomeRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        factory = new HomeViewModelFactory(getActivity(),repository);

        model = ViewModelProviders.of(getActivity(),factory).get(HomeViewModel.class);
        homeVM = model;
        model.homeNavigator =this;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.current_fragment_id = FragmentTAGS.FR_HOME;
        Global.isBookmarks =false;
        Global.isSaveAsBookmark =false;
        Global.isDeliveryByCourier= false;
        Global.isDashboard=false;
        Global.isLandOwnerRegistration = false;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.setFragmentHomeVM(model);
        mRootView = binding.getRoot();

        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_HOME);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(), FR_HOME, null /* class override */);

        model.initializeHomeVM(getContext());

        initializePage();
        try {
            ((MainActivity)getActivity()).getLastlogin();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setRetainInstance(true);
        if(!Global.isAppSelected){
            Global.HelpUrl = Global.CURRENT_LOCALE.equals("en")?Global.home_en_url:Global.home_ar_url;
        }
        return binding.getRoot();
    }

    private void initializePage(){
        model.manageAppBar(getActivity(), true);
        model.manageAppBottomBAtr(getActivity(), true);



    }

    @Override
    public void onResume() {
        super.onResume();
        model.manageAppBar(getActivity(), true);
        model.manageAppBottomBAtr(getActivity(), true);
        ((MainActivity)getActivity()).setScreenName(getActivity().getString(R.string.title_welcome));
        if(Global.appId!=null && model.getApplication( Global.appId)!=null)
        Global.HelpUrl = CURRENT_LOCALE.equals("en")?model.getApplication( Global.appId).getHelpUrlEn():model.getApplication( Global.appId).getHelpUrlAr();
        if(Global.isAppSelected)
           if(!Global.appId.equals("6")) binding.layoutRuntimeContainer.setVisibility(View.VISIBLE);
        else
            binding.layoutRuntimeContainer.setVisibility(View.GONE);
        if(!Global.isAppSelected){
            Global.HelpUrl = Global.CURRENT_LOCALE.equals("en")?Global.home_en_url:Global.home_ar_url;
        }

        Global.selectedTab =0;



    }

    @Override
    public void onMenuSelected(String appID, boolean isAnimation) {
        initializeRuntimeForm(model.getApplication(appID), isAnimation);
        Global.appId = appID;
        Global.HelpUrl = CURRENT_LOCALE.equals("en")?model.getApplication( Global.appId).getHelpUrlEn():model.getApplication( Global.appId).getHelpUrlAr();
        Global.isAppSelected =  true;
        Global.isFirstLoad =false;
        if(!appID.equals("6"))
        binding.layoutRuntimeContainer.setVisibility(View.VISIBLE);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Home Screen")
                .setAction("Action Application")
                .setLabel(model.getSelectedApplication().getNameEn())
                .setValue(Long.parseLong(appID))
                .build());
        if(getActivity()!=null)
            MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(), model.getSelectedApplication().getNameEn(), null /* class override */);
        //model.getApplication(appID).getSearchForm().get(0).getTabs().getControls().get(0);
        Log.d(getClass().getSimpleName(),model.getSelectedApplication().getNameEn());
        if (x != null) {
            x.setText("");
            if (!Global.isFirstLoad) {
                x.requestFocus();
                if (x.requestFocus()) {
                    Global.showSoftKeyboard(x, getActivity());
                }
            }
        }


    }

    private void initializeRuntimeForm(Applications app, boolean isAnimation){

        model.setSelectedApplication(app);
        if(model.getSelectedApplication().getHelpUrlEn() != null && model.getSelectedApplication().getHelpUrlEn().length() > 0){
            Global.helpUrlEn = model.getSelectedApplication().getHelpUrlEn();
        }
        if(model.getSelectedApplication().getHelpUrlAr() != null && model.getSelectedApplication().getHelpUrlAr().length() > 0){
            Global.helpUrlAr = model.getSelectedApplication().getHelpUrlAr();
        }

        if(model.getSelectedApplication().getSearchForm() != null && model.getSelectedApplication().getSearchForm().size() > 1){
            lstSearchForm = app.getSearchForm();

            binding.tabRuntimeLayout.setVisibility(View.VISIBLE);
            if(CURRENT_LOCALE.equals("en")) binding.tabRuntimeLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.tabRuntimeLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            if(CURRENT_LOCALE.equals("en")) binding.layoutControlHeader.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.layoutControlHeader.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            if(CURRENT_LOCALE.equals("en"))  binding.layoutRuntimeContainer.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else  binding.layoutRuntimeContainer.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            if(CURRENT_LOCALE.equals("en"))  binding.layoutHeader.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else  binding.layoutHeader.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            binding.layoutControlHeader.setVisibility(View.GONE);

            binding.layoutRuntimeContainer.setVisibility(Global.isAppSelected?View.VISIBLE:View.GONE);
            binding.tabRuntimeLayout.removeAllTabs();
            //binding.tabRuntimeLayout.setupWithViewPager(binding.viewPagerRuntime);
            //int x =0;
            for(SearchForm form: app.getSearchForm()){
                if (form.getTabs() != null) {
                    //binding.tabRuntimeLayout.addTab(binding.tabRuntimeLayout.newTab().setText(CURRENT_LOCALE.equals("en")?form.getTabs().getNameEn():form.getTabs().getNameAr()));
                    TabLayout.Tab tab = binding.tabRuntimeLayout.newTab();
                    relativeLayout = (RelativeLayout)
                            LayoutInflater.from(getActivity()).inflate(R.layout.tab_custom_view, binding.tabRuntimeLayout, false);

                    tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title);
                    imageView = (ImageView) relativeLayout.findViewById(R.id.image_arrow);

                    tabTextView.setText(CURRENT_LOCALE.equals("en")?form.getTabs().getNameEn():form.getTabs().getNameAr());
                    tab.setCustomView(relativeLayout);
                    //tab.select();

                    binding.tabRuntimeLayout.addTab(tab);

                    binding.tabRuntimeLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                    binding.tabRuntimeLayout.setTabMode(TabLayout.MODE_FIXED);


                }
            }

            runtimeControlRenderer(app.getSearchForm().get(0).getTabs().getControls());
            model.setSelectedTab(app.getSearchForm().get(0).getTabs());
            //renderControl(0);
            binding.tabRuntimeLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (app.getSearchForm().get(tab.getPosition()).getTabs().getControls() != null &&
                            app.getSearchForm().get(tab.getPosition()).getTabs().getControls().size() > 0) {
                        model.setSelectedTab(app.getSearchForm().get(tab.getPosition()).getTabs());
                        Global.selectedTab = tab.getPosition();
                        Global.lstControls = app.getSearchForm().get(tab.getPosition()).getTabs().getControls();
                        runtimeControlRenderer(app.getSearchForm().get(tab.getPosition()).getTabs().getControls());
                        if (x != null) {
                            x.setText("");
                            if (!Global.isFirstLoad) {
                                if(Global.selectedTab == 2){
                                    if(lstRuntimeCleanableText.size()>1){
                                        lstRuntimeCleanableText.get(0).requestFocus();
                                        if(lstRuntimeCleanableText.get(0).requestFocus())
                                            Global.showSoftKeyboard(lstRuntimeCleanableText.get(0), getActivity());

                                    }
                                }
                                else {
                                x.requestFocus();
                                if (x.requestFocus()) {
                                    Global.showSoftKeyboard(x, getActivity());
                                }
                                }


                            }
                        }

                    }

                    imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    imageView.setVisibility(View.GONE);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        } else if(model.getSelectedApplication().getSearchForm() != null && model.getSelectedApplication().getSearchForm().size() == 1){
            binding.tabRuntimeLayout.setVisibility(View.GONE);
            binding.layoutControlHeader.setVisibility(View.VISIBLE);
            binding.layoutRuntimeContainer.setVisibility(Global.isAppSelected?View.VISIBLE:View.GONE);
            binding.txtHeader.setText((CURRENT_LOCALE.equals("en"))?app.getSearchForm().get(0).getTabs().getNameEn():app.getSearchForm().get(0).getTabs().getNameAr());
            binding.txtHeader.setTextColor(getResources().getColor(R.color.white));
            model.setSelectedTab(app.getSearchForm().get(0).getTabs());

            runtimeControlRenderer(app.getSearchForm().get(0).getTabs().getControls());
        } else {
            clearRuntimeParent();

            binding.layoutRuntimeContainer.setVisibility(View.GONE);
            if(!model.getSelectedApplication().getIsNative()){
                ArrayList param = new ArrayList<>();
                param.add( constructUrl(model.getSelectedApplication().getSearchUrl(),getActivity()));
                param.add( model.getSelectedApplication().getNameEn());
                binding.layoutControlHeader.setVisibility(View.GONE);
                binding.layoutRuntimeContainer.setVisibility(View.GONE);

                    if(model.getSelectedApplication().getId().equals("6")){

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(constructUrl(model.getSelectedApplication().getSearchUrl(),getActivity())));
                        startActivity(i);
                    }
            }
        }
        if(isAnimation) {
            ViewAnimationUtils.scaleAnimateViewPopFirstLoad(binding.layoutRuntimeContainer);

        }


    }

    public static String constructUrl(String url,Activity context) {
        StringBuilder builder = new StringBuilder();
        if(url!=null||url!=""){
            builder.append(url);
            if (!url.endsWith("?")) {
                builder.append("?");
            }
        }

        builder.append("remarks=" + URLEncoder.encode(Global.getPlatformRemark() )+ "&");
        String lang = Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0 ? "en" : "ar";
        builder.append("lng=" +URLEncoder.encode( lang )+ "&");
        builder.append("fontSize=" +URLEncoder.encode( ""+(int)(Global.fontSize * Global.fontScale))+ "&");
        builder.append("appsrc="+URLEncoder.encode("kharetati")+"&");
        if(!Global.isUserLoggedIn){
            //Guest
            builder.append("userType="+URLEncoder.encode("GUEST")+"&");
            builder.append("user_id="+ URLEncoder.encode(""+Global.sime_userid )+"&");
            //builder.append("token="+ URLEncoder.encode(Global.app_session_token)+"&");
            builder.append("user_name="+URLEncoder.encode("GUEST")+"&");
            builder.append("access_token=" + URLEncoder.encode(Global.accessToken )+ "&");
        } else {
            if(Global.isUAE){
                builder.append("userType="+ URLEncoder.encode("UAEPASS")+"&");
                builder.append("user_id=" + URLEncoder.encode(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getUuid()) + "&");
                builder.append("token="+ URLEncoder.encode(Global.uaeSessionResponse.getService_response().getToken())+"&");
                builder.append("access_token=" + URLEncoder.encode(Global.uae_access_token ) + "&");
                if(Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0) {
                    builder.append("user_name=" + URLEncoder.encode(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameEN()) + "&");
                } else {
                    builder.append("user_name=" + URLEncoder.encode(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameAR() )+ "&");
                }
            } else {
                //My Id
                builder.append("userType="+URLEncoder.encode("MYID")+"&");
                builder.append("user_id=" + URLEncoder.encode(""+Global.sime_userid) + "&");
                builder.append("user_name=" + URLEncoder.encode(Global.getUser(context).getFullname())+"&");
                builder.append("access_token=" + URLEncoder.encode(Global.accessToken )+ "&");
                builder.append("token="+ URLEncoder.encode(Global.app_session_token )+"&");
            }

        }

        return builder.toString();
    }

    private void clearRuntimeParent(){
        binding.runtimeParent.removeAllViews();
        lstRuntimeCleanableText = new ArrayList<>();
        communityId = "";
    }

    private void runtimeControlRenderer(List<Controls> lstControl){
        clearRuntimeParent();
        for (Controls control:lstControl){
            if (control.getControlType().equals("TEXTBOX")){
                binding.runtimeParent.addView(addCleanableEditText(control));
            } else if(control.getControlType().equals("DROPDOWN")){
                binding.runtimeParent.addView(addSpinner(control));
            }

            if(control.getLkpId() != null && control.getLkpId() != ""){
                model.getLookUp(control.getLkpId(), control.getLkpValue());
            }
        }
    }

    private LinearLayout addCleanableEditText(Controls control){
        Typeface typeface =Typeface.createFromAsset(getActivity().getAssets(),"Dubai-Regular.ttf");



        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,10,0,10);
        layout.setLayoutParams(layoutParams);

        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(0, 0, 0, 0);

        x = new CleanableEditText(getActivity());

        x.setHint(Global.CURRENT_LOCALE.equals("en")?(Boolean.valueOf(control.getIsMandatory())?control.getPlaceHolderEn()+"*":control.getPlaceHolderEn()):(Boolean.valueOf(control.getIsMandatory())?control.getPlaceHolderAr()+"*":control.getPlaceHolderAr()));
        if(control.getInputType().toLowerCase().equals("number")) {

        }
        x.setInputType(InputType.TYPE_CLASS_NUMBER);
        x.setLayoutParams(lparams);
        x.setEms(10);
        x.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
        x.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        x.setTextColor(Color.parseColor("#969696"));
        x.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        x.setMaxLines(1);
        x.setTextDirection(View.TEXT_DIRECTION_LOCALE);
        x.setTextSize(15f);
        x.setType(control.getType());
        if(control.getRegexExp() != null && control.getRegexExp().length() > 0){
            x.setRegXPattern(control.getRegexExp());
        }

        x.setTextSize(14f);

        x.setTypeface(typeface);
        x.setPadding(25,0,25,0);

        x.requestFocus();
        x.setBackground(getActivity().getResources().getDrawable(R.drawable.control_background));
        x.setOnEditorActionListener(this);
        x.setImeOptions(EditorInfo.IME_ACTION_DONE);

        if(Global.hashSearchFieldValue != null && Global.hashSearchFieldValue.size() > 0){
            String val = Global.hashSearchFieldValue.get(control.getType()) == null ? "" : Global.hashSearchFieldValue.get(control.getType());
           // x.setText(val);

        }

        layout.addView(x);
        lstRuntimeCleanableText.add(x);



        return layout;
    }

    private LinearLayout addSpinner(Controls control){
        Typeface typeface =Typeface.createFromAsset(getActivity().getAssets(),"Dubai-Regular.ttf");

        LinearLayout dynamiclayout = new LinearLayout(getActivity());
        dynamiclayout.setGravity(Gravity.CENTER_HORIZONTAL);
        dynamiclayout.setOrientation(LinearLayout.VERTICAL);

        dynamiclayout.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        LinearLayout.LayoutParams dynamcLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(CURRENT_LOCALE.equals("en"))
            dynamcLayoutParams.setMargins(65,10,-85,10);
        else
            dynamcLayoutParams.setMargins(-85,10,65,10);


        LinearLayout spinnerLayout = new LinearLayout(getActivity());
        spinnerLayout.setOrientation(LinearLayout.HORIZONTAL);
        spinnerLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        LinearLayout.LayoutParams spinnerlayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        spinnerlayoutParams.setMargins(0,8,0,8);
        spinnerLayout.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
        spinnerLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.control_background));
        spinnerLayout.setLayoutParams(spinnerlayoutParams);

        LinearLayout chevronlayout = new LinearLayout(getActivity());
        chevronlayout.setOrientation(LinearLayout.HORIZONTAL);
        chevronlayout.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        LinearLayout.LayoutParams chevronlayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        chevronlayoutParams.setMargins(8,8,8,8);
        chevronlayout.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
        chevronlayout.setLayoutParams(chevronlayoutParams);

        ImageView chevronview = new ImageView(getActivity());
        LinearLayout.LayoutParams chevronParams = new LinearLayout.LayoutParams(65, 65);
        chevronParams.setMargins(8,8,8,8);
        chevronview.setImageResource(R.drawable.chevron_black);
        chevronlayout.addView(chevronview,chevronlayoutParams);


        spinnerView = new TextView(getActivity());
        spinnerView.setTextSize(14f);
        spinnerView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        spinnerView.setText(Global.CURRENT_LOCALE.equals("en")?(Boolean.valueOf(control.getIsMandatory())?control.getPlaceHolderEn()+"*":control.getPlaceHolderEn()):(Boolean.valueOf(control.getIsMandatory())?control.getPlaceHolderAr()+"*":control.getPlaceHolderAr()));
        spinnerView.setTypeface(typeface);
        spinnerLayout.addView(spinnerView,dynamcLayoutParams);
        spinnerLayout.addView(chevronlayout,chevronParams);

        dynamiclayout.addView(spinnerLayout);

        spinnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.hideSoftKeyboard(getActivity());
                if(spinnerDialog != null){
                    spinnerDialog.showSpinerDialog();
                }

            }
        });

        chevronlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Global.hideSoftKeyboard(getActivity());
                if(spinnerDialog != null){
                    spinnerDialog.showSpinerDialog();
                }
            }
        });
        spinnerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinnerDialog != null){
                    spinnerDialog.showSpinerDialog();
                }
            }
        });

        return dynamiclayout;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
            if (!Global.isConnected(getActivity())) {
                if (Global.appMsg != null)
                    AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getResources().getString(R.string.ok), getActivity());
                else
                    AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
            } else {
                Global.hideSoftKeyboard(getActivity());

                if (!isSearchBoxEmpty()) {

                    model.getAppsSearchResult(populateSearchText());
                }
                else
                    AlertDialogUtil.errorAlertDialog("",getActivity().getResources().getString(R.string.valid_criteria),getActivity().getResources().getString(R.string.ok),getActivity());
            }
            return true;
        }
        return false;
    }

    private Pattern getsPattern(String pattern){
        return Pattern.compile(pattern);
    }



    private boolean isSearchBoxEmpty(){
        isEmpty = false;
        if (lstRuntimeCleanableText != null && lstRuntimeCleanableText.size() > 0)
        {
            for (int i = 0; i < lstRuntimeCleanableText.size(); i++) {
                CleanableEditText txt = (CleanableEditText) lstRuntimeCleanableText.get(i);
                if ((txt.getText().toString() == null || txt.getText().toString().equals("")) && (communityId == null || communityId.equals(""))) {
                    isEmpty = true;
                    break;
                }
                if(lstRuntimeCleanableText.size() >= 2){
                    if(communityId.isEmpty()|| communityId == null)
                        isEmpty =true;
                }
            }

        }

        return isEmpty;
    }

    private String populateSearchText(){
        StringBuilder builder = new StringBuilder();
        if (communityId != null && communityId.length() > 0){
            builder.append(communityId);
            builder.append("|");
        }
        if(lstRuntimeCleanableText != null && lstRuntimeCleanableText.size() > 0) {
            for (int i = 0; i < lstRuntimeCleanableText.size(); i++) {
                CleanableEditText txt = (CleanableEditText) lstRuntimeCleanableText.get(i);
                if(isMakani(txt)){
                    String s1 = txt.getText().toString().substring(0, 5);
                    String s2 = txt.getText().toString().substring(5, txt.getText().toString().length());

                    builder.append(s1 + " " + s2);
                } else {
                    builder.append(txt.getText().toString());
                }
                if(i < lstRuntimeCleanableText.size() - 1) {
                    builder.append("|");
                }
            }
        }
        return builder.toString();
    }


    private boolean isMakani(CleanableEditText txt){
        Global.isMakani = false;
        for (int i = 0; i < Global.lstControls.size(); i++) {
            if (txt.getType().compareToIgnoreCase("makani")==0) {
                isMakani = true;
            }
        }
        return isMakani;
    }

    private void addBottomDots(int currentPage, int length) {
        dots = new TextView[length];

        binding.layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.gray));
            binding.layoutDots.addView(dots[i]);
        }
        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.light_blue));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        addBottomDots(position, model.getGridPagerSize());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onStarted() {
        if(getActivity()!=null)
            AlertDialogUtil.showProgressBar(getActivity(), true);
    }

    @Override
    public void onFailure(String Msg) {

        if(getActivity()!=null){
        AlertDialogUtil.showProgressBar(getActivity(),false);
        AlertDialogUtil.errorAlertDialog("",Msg,getActivity().getResources().getString(R.string.ok),getActivity());
        }

    }

    @Override
    public void onSuccess() {
        if(getActivity()!=null)
        AlertDialogUtil.showProgressBar(getActivity(),false);
    }

    @Override
    public void populateAreaNames() {
        populateAreaName();
    }

    @Override
    public void populateGridMenu() {

        gridPagerAdapter = new GridMenuPagerAdapter(getActivity(),model, this);
        binding.viewPager.setPageTransformer(false, new CustPagerTransformer(getActivity()));
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.addOnPageChangeListener(this);
        binding.viewPager.setAdapter(gridPagerAdapter);
        if(model.getGridPagerSize() > 1) {
            binding.layoutDots.setVisibility(View.VISIBLE);
            addBottomDots(0, model.getGridPagerSize());
        } else {
            binding.layoutDots.setVisibility(View.GONE);
        }

        if(model.getSelectedApplication() != null && model.getSelectedApplication().getId() != null &&
                model.getSelectedApplication().getId().length() > 0){
            if(model.getSelectedApplication().getSearchForm() != null && model.getSelectedApplication().getSearchForm().size() > 0) {
                initializeRuntimeForm(model.getSelectedApplication(), false);
            }
        }


        if (Global.isFirstLoad && !Global.isRecreate)
            MainActivity.mainVM.getNotifications();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStop() {
        super.onStop();
        Global.isAppSelected = false;

        saveInputValues();
    }

    private void saveInputValues(){
        Global.hashSearchFieldValue = new HashMap<>();
        if(lstRuntimeCleanableText != null && lstRuntimeCleanableText.size() > 0) {
            for (int i = 0; i < lstRuntimeCleanableText.size(); i++) {
                Global.hashSearchFieldValue.put(lstRuntimeCleanableText.get(i).getType(), lstRuntimeCleanableText.get(i).getText().toString());
            }
        }
    }

    private ArrayList<String> getAreaName(List<LookupValue> areas, boolean isEnglish) {
        ArrayList<String> area = new ArrayList<String>();
        //area.add(getResources().getString(R.string.tap_to_choose));
        for (int i = 0; i < areas.size(); i++) {
            area.add(isEnglish ? areas.get(i).getDescEn() : areas.get(i).getDescAr());
        }
        return area;
    }

    private void populateAreaName() {
        if (Global.lookupResponse != null) {
            if(Global.lookupResponse.getLkp() != null && Global.lookupResponse.getLkp().size() > 0){

                ArrayList<String> area = new ArrayList<String>();
                if (CURRENT_LOCALE.compareToIgnoreCase("en") == 0) {
                    area = getAreaName(Global.lookupResponse.getLkp(), true);
                    /*spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.community_drp, R.id.txtCommunity,
                            area);*/
                    spinnerDialog = new SpinnerDialog(getActivity(), area,
                            getResources().getString(R.string.tap_to_choose));
                } else {
                    area = getAreaName(Global.lookupResponse.getLkp(), false);
                    spinnerDialog = new SpinnerDialog(getActivity(), area,
                            getResources().getString(R.string.tap_to_choose));
                    /*spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.community_drp_ar, R.id.txtCommunity,
                            area);*/
                }
                spinnerView.setHint(getResources().getString(R.string.tap_to_choose));
                spinnerDialog.setTitleColor(getResources().getColor(R.color.black));
                spinnerDialog.setSearchIconColor(getResources().getColor(R.color.black));
                spinnerDialog.setSearchTextColor(getResources().getColor(R.color.black));
                spinnerDialog.setItemColor(getResources().getColor(R.color.black));
                spinnerDialog.setItemDividerColor(getResources().getColor(R.color.black));
                spinnerDialog.setCloseColor(getResources().getColor(R.color.black));

                spinnerDialog.setCancellable(true);
                spinnerDialog.setShowKeyboard(false);

                spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {

                        Global.hideSoftKeyboard(getActivity());
                        if(!TextUtils.isEmpty(item)) {
                            communityId = position>0 ? Global.lookupResponse.getLkp().get(position).getId().toString():"";
                            isEmpty = communityId.isEmpty()?true:false;
                            spinnerView.setText(item);


                        }

                    }
                });
            }
        }
    }
}