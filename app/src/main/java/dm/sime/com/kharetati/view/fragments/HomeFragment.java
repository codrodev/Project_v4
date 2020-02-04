package dm.sime.com.kharetati.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.esri.arcgisruntime.mapping.view.MapView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


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
import dm.sime.com.kharetati.datas.models.InAppNotifications;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.models.SearchForm;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.HomeRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.adapters.GridMenuAdapter;
import dm.sime.com.kharetati.view.adapters.GridMenuPagerAdapter;
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
    private Areas[] lstAreas;
    private TextView spinnerView;
    public static String communityId;
    private CleanableEditText editLandP1;
    private CleanableEditText editLandP2;
    /*BottomSheetBehavior sheetBehavior;
    LinearLayout layoutBottomSheet;*/

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repository = new HomeRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        factory = new HomeViewModelFactory(getActivity(),repository);

        model = ViewModelProviders.of(getActivity(),factory).get(HomeViewModel.class);
        model.homeNavigator =this;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.setFragmentHomeVM(model);
        mRootView = binding.getRoot();
        model.initializeHomeVM(getContext());
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        model.manageAppBar(getActivity(), true);
        model.manageAppBottomBAtr(getActivity(), true);

        /*layoutBottomSheet = (LinearLayout)mRootView.findViewById(R.id.bottomSheetWebView);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);*/

        model.getMutableInAppNotifications().observe(getActivity(), new Observer<List<InAppNotifications>>() {
            @Override
            public void onChanged(List<InAppNotifications> lstInAppNotifications) {
                //model.loading.set(View.GONE);
                if (lstInAppNotifications.size() > 0) {
                    model.setInAppNotificationsAdapter(lstInAppNotifications);
                }
            }
        });

        gridPagerAdapter = new GridMenuPagerAdapter(getActivity(),model, this);
        binding.viewPager.setPageTransformer(false, new CustPagerTransformer(getActivity()));
        binding.viewPager.setOffscreenPageLimit(3);
        binding.viewPager.addOnPageChangeListener(this);
        binding.viewPager.setAdapter(gridPagerAdapter);
        addBottomDots(0, model.getGridPagerSize());
        //sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onResume() {
        super.onResume();
        model.manageAppBar(getActivity(), true);
        model.manageAppBottomBAtr(getActivity(), true);
    }

    @Override
    public void onMenuSelected(String appID) {
        initializeRuntimeForm(model.getApplication(appID));
    }

    private void initializeRuntimeForm(Applications app){
        if(app.getLaunchUrl() != null && TextUtils.getTrimmedLength(app.getLaunchUrl()) > 0){
            binding.layoutRuntimeContainer.setVisibility(View.GONE);
            //toggleBottomSheet();
            Intent intentOpenBrowser = new Intent(Intent.ACTION_VIEW);
            intentOpenBrowser.setData(Uri.parse(app.getLaunchUrl()));
            getActivity().startActivity(intentOpenBrowser);
        } else {
            lstSearchForm = app.getSearchForm();
            Global.setLstMapFunctions(app.getFunctionsOnMap());
            binding.layoutRuntimeContainer.setVisibility(View.VISIBLE);
            binding.tabRuntimeLayout.removeAllTabs();
            //binding.tabRuntimeLayout.setupWithViewPager(binding.viewPagerRuntime);
            for(SearchForm form: app.getSearchForm()){

                //binding.tabRuntimeLayout.addTab(binding.tabRuntimeLayout.newTab().setText(form.getNameEn()));


            }
            binding.tabRuntimeLayout.addTab(binding.tabRuntimeLayout.newTab().setText("PLOT NUMBER"));
            binding.tabRuntimeLayout.addTab(binding.tabRuntimeLayout.newTab().setText("DEED NUMBER"));
            binding.tabRuntimeLayout.addTab(binding.tabRuntimeLayout.newTab().setText("MAKANI NUMBER"));




            renderControl(0);
            binding.tabRuntimeLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    renderControl(tab.getPosition());

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            /*RuntimeViewPagerAdapter runtimePager = new RuntimeViewPagerAdapter(getActivity(),model, app.getSearchForm(),
                    binding.tabRuntimeLayout.getTabCount());
            binding.viewPagerRuntime.setAdapter(runtimePager);*/

        }
        /*sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //sheetBehavior.setText("Close Sheet");
                        WebView web = (WebView)layoutBottomSheet.findViewById(R.id.webView);
                        web.loadUrl(app.getLaunchUrl());
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        //sheetBehavior.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });*/

    }

    private void renderControl(int position){
        binding.runtimeParent.removeAllViews();
        switch (position){
            case 0: {Global.isPlotSearch =true;
                    Global.isLand = false;
                    Global.isMakani =false;}
            break;
            case 1: {Global.isMakani =false;
                    Global.isLand = true;
                    Global.isPlotSearch =false; }
            break;
            case 2: {Global.isMakani =true;
                    Global.isLand =false;
                Global.isPlotSearch =false; }
            break;
        }
        SearchForm form = lstSearchForm.get(position);

        InputFilter[] FilterArray = new InputFilter[3];
        FilterArray[0] = new InputFilter.LengthFilter(10);
        FilterArray[1] = new InputFilter.LengthFilter(10);
        FilterArray[2] = new InputFilter.LengthFilter(11);


        Typeface typeface =Typeface.createFromAsset(getActivity().getAssets(),"Dubai-Regular.ttf");
        if(isLand){

            createLandView();

            //model.landsearch();

        }
        else{
        CleanableEditText x = new CleanableEditText(getActivity());
        //x.setHint(form.getPlaceHolderEn());
        x.setHint(isPlotSearch?"Enter Your Plot Number": (isMakani?"Enter Your Makani Number":(isLand?"Enter Your Deed Number":"")));
        x.setInputType(InputType.TYPE_CLASS_NUMBER);
        x.setEms(10);
        x.setMaxLines(1);
        x.setTextSize(16f);
        x.setFilters(FilterArray);
        x.setTypeface(typeface);
        x.setPadding(8,0,8,0);
        LinearLayout.LayoutParams xparams = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, 150);
        xparams.setMargins(8,24,8,16);
        x.setBackground(getActivity().getResources().getDrawable(R.drawable.border_background));
        binding.runtimeParent.addView(x);
        /*x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.navigate(getActivity(), FragmentTAGS.FR_MAP);
            }
        });*/
        x.setOnEditorActionListener(this);
        }
    }

    private void createLandView() {
        Typeface typeface =Typeface.createFromAsset(getActivity().getAssets(),"Dubai-Regular.ttf");

        LinearLayout dynamiclayout = new LinearLayout(getActivity());
        dynamiclayout.setGravity(Gravity.CENTER_HORIZONTAL);
        dynamiclayout.setOrientation(LinearLayout.VERTICAL);
        //dynamiclayout.setBackgroundColor(Color.RED);
        dynamiclayout.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        LinearLayout.LayoutParams dynamcLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dynamcLayoutParams.setMargins(8,8,8,8);


        LinearLayout spinnerLayout = new LinearLayout(getActivity());
        spinnerLayout.setOrientation(LinearLayout.HORIZONTAL);
        spinnerLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        LinearLayout.LayoutParams spinnerlayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
        spinnerlayoutParams.setMargins(8,8,8,8);
        spinnerLayout.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
        spinnerLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.border_background));
        spinnerLayout.setLayoutParams(spinnerlayoutParams);

        LinearLayout chevronlayout = new LinearLayout(getActivity());
        chevronlayout.setOrientation(LinearLayout.HORIZONTAL);
        chevronlayout.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
        LinearLayout.LayoutParams chevronlayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
        chevronlayoutParams.setMargins(8,8,8,8);
        chevronlayout.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
        chevronlayout.setLayoutParams(chevronlayoutParams);

        ImageView chevronview = new ImageView(getActivity());
        LinearLayout.LayoutParams chevronParams = new LinearLayout.LayoutParams(85, 85);
        chevronParams.setMargins(8,8,8,8);
        chevronview.setImageResource(R.drawable.chevron_black);
        chevronlayout.addView(chevronview,chevronlayoutParams);


        spinnerView = new TextView(getActivity());
        spinnerView.setTextSize(16f);
        spinnerView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        spinnerView.setText("Please Choose Area");
        spinnerView.setTypeface(typeface);
        spinnerLayout.addView(spinnerView,dynamcLayoutParams);
        spinnerLayout.addView(chevronlayout,chevronParams);

        dynamiclayout.addView(spinnerLayout);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120);
        layoutParams.setMargins(8,48,8,8);
        layout.setLayoutParams(layoutParams);

        editLandP1 = new CleanableEditText(getActivity());
        LinearLayout.LayoutParams editP1Params = new LinearLayout.LayoutParams( (int) Global.width/6,(int) Global.width/9);
        editP1Params.setMargins(8,12,8,12);
        editLandP1.setHint("Eg: 123");
        editLandP1.setInputType(InputType.TYPE_CLASS_NUMBER);
        editLandP1.setEms(3);
        editLandP1.setMaxEms(3);
        editLandP1.setGravity(Gravity.CENTER_VERTICAL);
        editLandP1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editLandP1.setMaxLines(1);
        editLandP1.setTextSize(16f);
        editLandP1.setTypeface(typeface);
        editLandP1.setBackground(getActivity().getResources().getDrawable(R.drawable.border_background));

        //editLandP1.setLayoutParams(editP1Params);
        layout.addView(editLandP1,editP1Params);

        TextView slash = new TextView(getActivity());
        LinearLayout.LayoutParams slashParams = new LinearLayout.LayoutParams( (int) Global.width/15, (int) Global.width/9);
        slashParams.setMargins(8,8,8,8);
        //slash.setLayoutParams(slashParams);
        slash.setText("/");
        slash.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        slash.setGravity(Gravity.CENTER_VERTICAL);
        slash.setTextSize(24f);
        slash.setTypeface(typeface);
        slash.setTextColor(getActivity().getResources().getColor(R.color.black));
        layout.addView(slash,slashParams);

        editLandP2 = new CleanableEditText(getActivity());
        LinearLayout.LayoutParams editP2Params = new LinearLayout.LayoutParams((int) Global.width/7, (int) Global.width/9);
        editP2Params.setMargins(8,12,8,12);
        editLandP2.setHint("Eg : 12");
        editLandP2.setInputType(InputType.TYPE_CLASS_NUMBER);
        editLandP2.setGravity(Gravity.CENTER_VERTICAL);
        editLandP2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editLandP2.setEms(2);
        editLandP2.setMaxEms(2);
        editLandP2.setMaxLines(1);
        editLandP2.setTextSize(16f);
        editLandP2.setTypeface(typeface);
        editLandP2.setBackground(getActivity().getResources().getDrawable(R.drawable.border_background));

        //editLandP2.setLayoutParams(editP2Params);
        layout.addView(editLandP2,editP2Params);

        editLandP2.setOnEditorActionListener(this);
        editLandP1.setOnEditorActionListener(this);

        editLandP1.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editLandP2.setImeOptions(EditorInfo.IME_ACTION_DONE);


        dynamiclayout.addView(layout,layoutParams);
        binding.runtimeParent.addView(dynamiclayout,dynamcLayoutParams);

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

        if (Global.areaResponse == null) {
            model.getAreaNames();
        } else {
            populateAreaName();
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

            String s = v.getText().toString();
            //Toast.makeText(getActivity(),s, Toast.LENGTH_LONG).show();
            parcelNumber = v.getText().toString();
            //AlertDialogUtil.showProgressBar(getActivity(),true);
            /*mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Search Parcel")
                    .setAction(parcelNumber)
                    .build());*/

            //PlotDetails.isOwner =false;
            parcelNumber = parcelNumber.replaceAll("\\s+","");
            parcelNumber = parcelNumber.replaceAll("_","");
            if(!Global.isConnected(getActivity())){
                if(Global.appMsg!=null)
                    AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                else
                    AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
            }
            else{

                    if (Global.isPlotSearch) {
                        if (parcelNumber.matches("")) {

                            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.PLEASE_ENTER_PLOTNUMBER), getResources().getString(R.string.ok), getContext());

                        }else if (parcelNumber.trim().length() > 4 && parcelNumber.trim().length() <= 20) {
                            if (parcelNumber != null && parcelNumber != "") {
                                Global.hideSoftKeyboard(getActivity());
                                onStarted();
                                parcelNumber = parcelNumber.replaceAll("\\s+", "");
                                //PlotDetails.clearCommunity();
                                Global.landNumber = null;
                                Global.area = null;
                                Global.area_ar = null;
                                PlotDetails.parcelNo = parcelNumber;
                                progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleSmall);
                                progressBar.setVisibility(View.VISIBLE);
                                String targetLayer = AppUrls.GIS_LAYER_URL.concat("/" + AppUrls.plot_layerid);
                                String[] queryArray = {targetLayer, "PARCEL_ID  = '" + parcelNumber + "'"};

                                ArcGISMap map = new ArcGISMap();
                                ArcGISMapImageLayer dynamicLayer = new ArcGISMapImageLayer(AppUrls.GIS_LAYER_URL);
                                Credential credential = new UserCredential(AppUrls.GIS_LAYER_USERNAME, AppUrls.GIS_LAYER_PASSWORD);
                                dynamicLayer.setCredential(credential);
                                ///mMapView.addLayer(dynamicLayer);
                                map.getOperationalLayers().add(dynamicLayer);

                                binding.map.setMap(map);
                                dynamicLayer.addDoneLoadingListener(() -> {
                                    if (dynamicLayer.getLoadStatus() == LoadStatus.LOADED) {
                                        ArcGISMapImageSublayer sublayer = (ArcGISMapImageSublayer) dynamicLayer.getSublayers().get(2);

                                        sublayer.addDoneLoadingListener(new Runnable() {
                                            @Override
                                            public void run() {
                                                ServiceFeatureTable sublayerTable = sublayer.getTable();
                                                QueryParameters query = new QueryParameters();
                                                query.setWhereClause("PARCEL_ID  = '" + parcelNumber + "'");
                                                ListenableFuture<FeatureQueryResult> sublayerQuery = sublayerTable.queryFeaturesAsync(query);
                                                sublayerQuery.addDoneListener(() -> {
                                                    try {
                                                        FeatureQueryResult result = sublayerQuery.get();
                                                        if (progressBar != null)
                                                            progressBar.setVisibility(View.GONE);
                                                        if (!result.iterator().hasNext()) {

                                                            AlertDialogUtil.errorAlertDialog("", getActivity().getResources().getString(R.string.plot_does_not_exist),
                                                                    getActivity().getResources().getString(R.string.ok), getActivity());
                                                            onFailure(getActivity().getResources().getString(R.string.plot_does_not_exist));
                                                        } else {
                                                            //communicator.navigateToMap(parcelNumber, "");



                                                            model.navigate(getActivity(), FragmentTAGS.FR_MAP);


                                                        }
                                                    } catch (InterruptedException | ExecutionException e) {
                                                        Log.e(HomeFragment.class.getSimpleName(), e.toString());
                                                        AlertDialogUtil.showProgressBar(getActivity(),false);


                                                    }
                                                });
                                            }
                                        });
                                        sublayer.loadAsync();

                                    }
                                });
                                //AlertDialogUtil.showProgressBar(getActivity(),false);

                                ////AsyncQueryTask ayncQuery = new AsyncQueryTask();
                                ////ayncQuery.execute(queryArray);
                            }
                        } else
                            AlertDialogUtil.errorAlertDialog("", getResources().getString(R.string.valid_plot_number), getResources().getString(R.string.ok), getActivity());
                    }
                    else if(isLand){
                        Global.hideSoftKeyboard(getActivity());
                        if (editLandP1.getText().toString() != null && editLandP1.getText().toString().length() > 0)
                        {
                            Global.LandNo = editLandP1.getText().toString();
                            Global.subNo = editLandP2.getText().toString().isEmpty()? "0":editLandP1.getText().toString();

                            if (communityId != null && communityId != "") {
                                Global.hideSoftKeyboard(getActivity());

                                model.getParcelID();
                                communityId =null;


                            } else {
                                AlertDialogUtil.errorAlertDialog("",
                                        getResources().getString(R.string.invalid_area),
                                        getResources().getString(R.string.ok), getContext());
                            }

                        } else {
                            editLandP1.requestFocus();
                            AlertDialogUtil.errorAlertDialog("", getResources().getString(R.string.please_enter_land),
                                    getResources().getString(R.string.ok), getContext());
                        }

                    }
                    else if (Global.isMakani) {
                        Global.hideSoftKeyboard(getActivity());
                        Global.makani = v.getText().toString();
                        model.getMakaniToDLTM();
                    }
                }

            Log.i("Mask", parcelNumber);
            return true;
        }
        return false;
    }

    /*public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }*/

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
        AlertDialogUtil.showProgressBar(getActivity(),false);
        AlertDialogUtil.errorAlertDialog("",Msg,getActivity().getResources().getString(R.string.ok),getActivity());

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
    public void onDestroy() {
        super.onDestroy();

    }

    private ArrayList<String> getAreaName(Areas[] areas, boolean isEnglish) {
        ArrayList<String> area = new ArrayList<String>();
        //area.add(getResources().getString(R.string.tap_to_choose));
        for (int i = 0; i < areas.length; i++) {
            area.add(isEnglish ? areas[i].getAreaNameEN() : areas[i].getAreaNameAR());
        }
        return area;
    }

    private void populateAreaName() {
        if (Global.areaResponse != null) {
            if(Global.areaResponse.getAreas() != null && Global.areaResponse.getAreas().length > 0){
                lstAreas = Global.areaResponse.getAreas();
                ArrayList<String> area = new ArrayList<String>();
                if (CURRENT_LOCALE.compareToIgnoreCase("en") == 0) {
                    area = getAreaName(Global.areaResponse.getAreas(), true);
                    /*spinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.community_drp, R.id.txtCommunity,
                            area);*/
                    spinnerDialog = new SpinnerDialog(getActivity(), area,
                            getResources().getString(R.string.tap_to_choose));
                } else {
                    area = getAreaName(Global.areaResponse.getAreas(), false);
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
                            communityId = Global.areaResponse.getAreas()[position].getAreaID().toString();

                            spinnerView.setText(item);
                            Global.area_ar = Global.areaResponse.getAreas()[position].getAreaNameAR();
                            Global.area = Global.areaResponse.getAreas()[position].getAreaNameEN();
                            /*if(Global.isProbablyArabic(item))
                                Global.area_ar=item;
                            else
                                Global.area=item;*/
                            if (editLandP1.getText().toString() != null && editLandP1.getText().toString().length() > 0) {
                                if (communityId != null && communityId != "") {
                                    if(editLandP2.getText().toString() == null || editLandP2.getText().toString().length() == 0){
                                        Global.subNo = "0";
                                    } else {
                                        Global.subNo = editLandP2.getText().toString();
                                    }
                                    Global.LandNo=editLandP1.getText().toString();

                                    model.getParcelID();
                                    communityId =null;

                                } else {
                                    AlertDialogUtil.errorAlertDialog("",
                                            getResources().getString(R.string.invalid_area),
                                            getResources().getString(R.string.ok), getContext());
                                }
                            } else {
                                editLandP1.requestFocus();
                            }
                        }
                        /*if (editLandP2.getText().toString() != null && editLandP2.getText().toString().length() > 0){

                        } else {
                            editLandP2.requestFocus();
                            AlertDialogUtil.errorAlertDialog("", getResources().getString(R.string.please_enter_land),
                                    getResources().getString(R.string.lbl_ok), getContext());
                        }*/
                    }
                });

                /*spinArea.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.community_drp, R.id.txtCommunity,
                        area));*/
                /*spinnerAdapter.setDropDownViewResource(R.layout.community_dropdown_view);
                spinArea.setAdapter(spinnerAdapter);*/
            }
        }
    }
}