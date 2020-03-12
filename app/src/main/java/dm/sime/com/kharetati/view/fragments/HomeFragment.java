package dm.sime.com.kharetati.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.android.material.tabs.TabLayout;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
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
    /*BottomSheetBehavior sheetBehavior;
    LinearLayout layoutBottomSheet;*/

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.setFragmentHomeVM(model);
        mRootView = binding.getRoot();
        model.initializeHomeVM(getContext());
        initializePage();
        setRetainInstance(true);
        return binding.getRoot();
    }

    private void initializePage(){
        model.manageAppBar(getActivity(), true);
        model.manageAppBottomBAtr(getActivity(), true);

        /*layoutBottomSheet = (LinearLayout)mRootView.findViewById(R.id.bottomSheetWebView);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);*/

        /*model.getMutableInAppNotifications().observe(getActivity(), new Observer<List<InAppNotifications>>() {
            @Override
            public void onChanged(List<InAppNotifications> lstInAppNotifications) {
                //model.loading.set(View.GONE);
                if (lstInAppNotifications.size() > 0) {
                    model.setInAppNotificationsAdapter(lstInAppNotifications);
                }
            }
        });*/

    }

    private void initializeInAppNotification(){
        LinearLayoutManager linearLayoutManager;
        linearLayoutManager =  new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        /*if(Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0){

        } else {
            linearLayoutManager =  new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, true);
        }*/


        adapterNotification = new InAppNotificationAdapter(model, getActivity());
        binding.recycleNotification.setAdapter(adapterNotification);
        binding.recycleNotification.setLayoutManager(linearLayoutManager);
        adapterNotification.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        model.manageAppBar(getActivity(), true);
        model.manageAppBottomBAtr(getActivity(), true);
        if(model.getSelectedApplication() != null && model.getSelectedApplication().getId() != null &&
                model.getSelectedApplication().getId().length() > 0){
            if(model.getSelectedApplication().getSearchForm() != null && model.getSelectedApplication().getSearchForm().size() > 0) {
                initializeRuntimeForm(model.getSelectedApplication());
            }
        }
        //initializeInAppNotification();
    }

    @Override
    public void onMenuSelected(String appID) {
        initializeRuntimeForm(model.getApplication(appID));
    }

    private void initializeRuntimeForm(Applications app){
        Global.isFirstLoad = false;
        model.setSelectedApplication(app);
        if(model.getSelectedApplication().getHelpUrlEn() != null && model.getSelectedApplication().getHelpUrlEn().length() > 0){
            Global.helpUrlEn = model.getSelectedApplication().getHelpUrlEn();
        }
        if(model.getSelectedApplication().getHelpUrlAr() != null && model.getSelectedApplication().getHelpUrlAr().length() > 0){
            Global.helpUrlEn = model.getSelectedApplication().getHelpUrlAr();
        }

        if(model.getSelectedApplication().getSearchForm() != null && model.getSelectedApplication().getSearchForm().size() > 1){
            lstSearchForm = app.getSearchForm();
            binding.tabRuntimeLayout.setVisibility(View.VISIBLE);
            binding.layoutControlHeader.setVisibility(View.GONE);
            binding.layoutRuntimeContainer.setVisibility(View.VISIBLE);
            binding.tabRuntimeLayout.removeAllTabs();
            //binding.tabRuntimeLayout.setupWithViewPager(binding.viewPagerRuntime);
            int x =0;
            for(SearchForm form: app.getSearchForm()){
                if (form.getTabs() != null) {
                    binding.tabRuntimeLayout.addTab(binding.tabRuntimeLayout.newTab().setText(CURRENT_LOCALE.equals("en")?form.getTabs().getNameEn():form.getTabs().getNameAr()));
                    binding.tabRuntimeLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                    binding.tabRuntimeLayout.setTabMode(TabLayout.MODE_FIXED);
                    /*ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) binding.tabRuntimeLayout.getLayoutParams();
                    p.setMargins(10, 10, 10, 0);
                    binding.tabRuntimeLayout.requestLayout();*/
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
                        runtimeControlRenderer(app.getSearchForm().get(tab.getPosition()).getTabs().getControls());
                    }
                   // renderControl(tab.getPosition());

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        } else if(model.getSelectedApplication().getSearchForm() != null && model.getSelectedApplication().getSearchForm().size() == 1){
            binding.tabRuntimeLayout.setVisibility(View.GONE);
            binding.layoutControlHeader.setVisibility(View.VISIBLE);
            binding.layoutRuntimeContainer.setVisibility(View.VISIBLE);
            binding.txtHeader.setText(app.getSearchForm().get(0).getTabs().getNameEn());
            binding.txtHeader.setTextColor(getResources().getColor(R.color.white));
            model.setSelectedTab(app.getSearchForm().get(0).getTabs());

            runtimeControlRenderer(app.getSearchForm().get(0).getTabs().getControls());
        } else {
            clearRuntimeParent();
            binding.layoutControlHeader.setVisibility(View.GONE);
            binding.layoutRuntimeContainer.setVisibility(View.GONE);
            if(!model.getSelectedApplication().getIsNative()){
                ArrayList param = new ArrayList<>();
                param.add( constructUrl(model.getSelectedApplication().getSearchUrl()));
                param.add( model.getSelectedApplication().getNameEn());
                model.navigateWithParam(getActivity(), FR_WEBVIEW, param);
            }
        }

    }

    private String constructUrl(String url){
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        if(!url.endsWith("?")) {
            builder.append("?");
        }
        builder.append("token=" + Global.accessToken + "&");
        builder.append("remarks=" + Global.getPlatformRemark() + "&");
        String lang = Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0 ? "en" : "ar";
        builder.append("lang=" + lang + "&");
        if(!Global.isUserLoggedIn){
            builder.append("isGuest=true&");
            builder.append("user_id="+ Global.sime_userid +"&");
            builder.append("user_name=GUEST");
        } else {
            builder.append("user_id=" + Global.username + "&");
            builder.append("user_name=" + Global.getUser(getActivity()).getFullname());
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

       /* InputFilter[] FilterArray = new InputFilter[3];
        FilterArray[0] = new InputFilter.LengthFilter(10);
        *//*FilterArray[1] = new InputFilter.LengthFilter(10);
        FilterArray[2] = new InputFilter.LengthFilter(11);*/
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,8,0,8);
        layout.setLayoutParams(layoutParams);

        CleanableEditText x = new CleanableEditText(getActivity());
        //x.setHint(form.getPlaceHolderEn());
        x.setHint(Global.CURRENT_LOCALE.equals("en")?control.getPlaceHolderEn():control.getPlaceHolderAr());
        if(control.getInputType().toLowerCase().equals("number")) {

        }
        x.setInputType(InputType.TYPE_CLASS_NUMBER);
        x.setEms(10);
        x.setMaxLines(1);
        x.setType(control.getType());
        if(control.getRegexExp() != null && control.getRegexExp().length() > 0){
            x.setRegXPattern(control.getRegexExp());
        }
        x.setTextSize(16f);
        //x.setFilters(FilterArray);
        x.setTypeface(typeface);
        x.setPadding(8,0,8,0);
        if(isPlotSearch||isMakani)
            x.requestFocus();
        x.setBackground(getActivity().getResources().getDrawable(R.drawable.control_background));
        x.setOnEditorActionListener(this);
        x.setImeOptions(EditorInfo.IME_ACTION_DONE);

        layout.addView(x);
        lstRuntimeCleanableText.add(x);
        return layout;
    }

    private LinearLayout addSpinner(Controls control){
        Typeface typeface =Typeface.createFromAsset(getActivity().getAssets(),"Dubai-Regular.ttf");

        LinearLayout dynamiclayout = new LinearLayout(getActivity());
        dynamiclayout.setGravity(Gravity.CENTER_HORIZONTAL);
        dynamiclayout.setOrientation(LinearLayout.VERTICAL);
        //dynamiclayout.setBackgroundColor(Color.RED);
        dynamiclayout.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        LinearLayout.LayoutParams dynamcLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dynamcLayoutParams.setMargins(0,8,0,8);


        LinearLayout spinnerLayout = new LinearLayout(getActivity());
        spinnerLayout.setOrientation(LinearLayout.HORIZONTAL);
        spinnerLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        LinearLayout.LayoutParams spinnerlayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        spinnerlayoutParams.setMargins(65,8,65,8);
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
        spinnerView.setTextSize(16f);
        spinnerView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        spinnerView.setText(Global.CURRENT_LOCALE.equals("en")?control.getPlaceHolderEn():control.getPlaceHolderAr());
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
                if (!isSearchBoxEmpty()) {
                    model.getAppsSearchResult(populateSearchText());
                }
            }
            return true;
        }
        return false;
    }

    private Pattern getsPattern(String pattern){
        return Pattern.compile(pattern);
    }

    private boolean isValid(CharSequence c, String pattern) {
        return getsPattern(pattern).matcher(c).matches();
    }

    private boolean isSearchBoxEmpty(){
        boolean isEmpty = false;
        if(lstRuntimeCleanableText != null && lstRuntimeCleanableText.size() > 0) {
            for (int i = 0; i < lstRuntimeCleanableText.size(); i++){
                CleanableEditText txt = (CleanableEditText)lstRuntimeCleanableText.get(i);
                //if(isValid(txt.toString(), txt.getRegXPattern())){
                    if(txt.getText().toString() == null || txt.getText().toString().equals("")){
                        isEmpty = true;
                        break;
                    }
               /* } else {
                    isEmpty = true;
                    break;
                }*/
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
                    if(txt.length()>=5){
                    String s1 = txt.getText().toString().substring(0, 5);
                    String s2 = txt.getText().toString().substring(5, txt.getText().toString().length());

                    builder.append(s1 + " " + s2);
                    }
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
        boolean isMakani = false;

        if(txt.getType().toLowerCase().equals("makani")){
            isMakani = true;
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
        AlertDialogUtil.showProgressBar(getActivity(),true);
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

        //initializeRuntimeForm(model.getDefaultApplication(0));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


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
                        //Toast.makeText(MainActivity.this, item + "  " + position + "", Toast.LENGTH_SHORT).show();
                        //spinArea.setText(item + " Position: " + position);
                        if(!TextUtils.isEmpty(item)) {
                            communityId = Global.lookupResponse.getLkp().get(position).getId().toString();
                            spinnerView.setText(item);
                        }

                    }
                });
            }
        }
    }
}