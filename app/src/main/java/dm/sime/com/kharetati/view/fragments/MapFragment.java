package dm.sime.com.kharetati.view.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.arcgisservices.ArcGISMapServiceInfo;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.ImmutablePart;
import com.esri.arcgisruntime.geometry.ImmutablePartCollection;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.layers.ArcGISMapImageSublayer;
import com.esri.arcgisruntime.layers.ArcGISSublayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.security.Credential;
import com.esri.arcgisruntime.security.UserCredential;

import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;

import com.esri.arcgisruntime.symbology.TextSymbol;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentMapBinding;
import dm.sime.com.kharetati.datas.models.AgsExtent;
import dm.sime.com.kharetati.datas.models.EmailParam;
import dm.sime.com.kharetati.datas.models.ExportParam;
import dm.sime.com.kharetati.datas.models.Functions;
import dm.sime.com.kharetati.datas.models.LayerDefinition;
import dm.sime.com.kharetati.datas.models.Params;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.models.SearchResult;
import dm.sime.com.kharetati.datas.models.SerializableParcelDetails;
import dm.sime.com.kharetati.datas.models.User;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.MapRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.ViewAnimationUtils;
import dm.sime.com.kharetati.utility.constants.AppConstants;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.adapters.DashboardPagerAdapter;
import dm.sime.com.kharetati.view.navigators.MapNavigator;
import dm.sime.com.kharetati.view.viewModels.MapViewModel;
import dm.sime.com.kharetati.view.viewModels.ParentSiteplanViewModel;
import dm.sime.com.kharetati.view.viewmodelfactories.MapViewModelFactory;

import static dm.sime.com.kharetati.utility.Global.CURRENT_LOCALE;

public class MapFragment extends Fragment implements MapNavigator, EditText.OnEditorActionListener,MapFunctionBottomsheetDialogFragment.OnFunctionMenuSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    public static boolean isMakani,isLand;
    MapViewModel model;
    FragmentMapBinding binding;
    private View mRootView;
    private ArcGISMapImageLayer dynamicLayer = null;
    private GraphicsOverlay graphicsLayer = null;
    private UserCredential userCredentials = null;
    static String token = Global.arcgis_token;
    Snackbar snack;

    private Envelope initExtent;
    MapView mapView;

    private int extentPadding=100;

    private boolean skipOnTextChangeEvent=false;
    private MapRepository repository;
    private MapViewModelFactory factory;
    private ArrayAdapter<String> adapterHistory;
    private ListView searchhistoryListView;
    BottomSheetBehavior sheetBehavior, mapSheetBehaviour;
    WebView webView;
    public static MapViewModel mapVM;
    BottomSheetDialogFragment bottomSheetDialogFragment;
    private String parcelId;
    private String lastSelectedWebFunction;


    public MapFragment() {
    }

    private static MapFragment mapFragment=null;

    public static MapFragment newInstance(String param1){
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
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
        if(getArguments() != null)
            parcelId = getArguments().getString(ARG_PARAM1);

        try {
            repository = new MapRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        factory = new MapViewModelFactory(getActivity(),repository);

        model = ViewModelProviders.of(getActivity(),factory).get(MapViewModel.class);
        mapVM =model;
        model.mapNavigator =this;

        setRetainInstance(true);
        PlotDetails.currentState=new PlotDetails.CurrentState();
        PlotDetails.clearCommunity();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.current_fragment_id = FragmentTAGS.FR_MAP;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        binding.setFragmentMapVM(model);
        mRootView = binding.getRoot();
        mapView = mRootView.findViewById(R.id.mapView);
        bottomSheetDialogFragment = MapFunctionBottomsheetDialogFragment.newInstance(this);
        LinearLayout layoutBottomSheet = (LinearLayout)mRootView.findViewById(R.id.bottomSheet);
        webView = (WebView)layoutBottomSheet.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        setRetainInstance(true);
        initializePage();
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);


        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        binding.frameLayout.setVisibility(View.VISIBLE);
                        if(Global.mapSearchResult.getService_response().getMap().getFunctions() != null &&
                                Global.mapSearchResult.getService_response().getMap().getFunctions().size() > 1) {
                            setMapFunctionSheetPeekHeight(60);
                        }
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        binding.frameLayout.setVisibility(View.INVISIBLE);
                        setMapFunctionSheetPeekHeight(0);
                        if(Global.mapSearchResult.getService_response().getMap().getFunctions() != null &&
                                Global.mapSearchResult.getService_response().getMap().getFunctions().size() > 1) {
                            setMapFunctionSheetPeekHeight(0);
                        }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        binding.frameLayout.setVisibility(View.VISIBLE);
                        if(Global.mapSearchResult.getService_response().getMap().getFunctions() != null &&
                                Global.mapSearchResult.getService_response().getMap().getFunctions().size() > 1) {
                            setMapFunctionSheetPeekHeight(60);
                        }
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
        });
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        View bottomSheet = mRootView.findViewById(R.id.mapFunctionSheet);
        mapSheetBehaviour = BottomSheetBehavior.from(bottomSheet);

        mapSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        //mapSheetBehaviour.setPeekHeight(100);

        mapSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:

                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:

                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        bottomSheet.findViewById(R.id.layoutParentMapFunctionBottomSheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toggleBottomSheet();
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toggleBottomSheet();
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        if(Global.isBookmarks){

            fromBookmarks(parcelId);

        }
        else if(Global.mapSearchResult.getService_response().getMap().getFunctions() != null &&
                Global.mapSearchResult.getService_response().getMap().getFunctions().size() == 1){
            setWebSheetPeekHeight(600);;
            setMapFunctionSheetPeekHeight(0);

        } else {
            setWebSheetPeekHeight(0);
            setMapFunctionSheetPeekHeight(60);
        }
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud3984007683,none,GB2PMD17J0YJ2J7EZ071");
        mapView.setAttributionTextVisible(false);

        if(Global.isBookmarks){
            binding.txtPlotNo.setText(parcelId);
            model.manageAppBar(getActivity(), false);
            model.manageAppBottomBAtr(getActivity(), false);
        }
        else{
            binding.txtPlotNo.setText(Global.mapSearchResult.getService_response().getParcelId());
            onStarted();
        }



        return binding.getRoot();
    }

    private void setWebSheetPeekHeight(int value){
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        //sheetBehavior.setPeekHeight(0);
        sheetBehavior.setPeekHeight(value);
    }

    private void setMapFunctionSheetPeekHeight(int value){
        mapSheetBehaviour.setPeekHeight(value);
    }

    private  void initializePage(){
        ParentSiteplanViewModel.initializeDocuments();
        model.manageAppBar(getActivity(), false);
        model.manageAppBottomBAtr(getActivity(), false);
        binding.txtPlotNo.setInputType(InputType.TYPE_CLASS_NUMBER);
        binding.txtPlotNo.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.txtPlotNo.setEms(10);
        binding.txtPlotNo.setOnEditorActionListener(this);

        //ArcGISMap map = new ArcGISMap(Global.mapSearchResult.getService_response().getMap().getDetails().getServiceUrl());
        ArcGISMap map = new ArcGISMap();
        mapView.setMap(map);


        // set up gesture for interacting with the MapView
        MapViewTouchListener mapViewTouchListener = new MapViewTouchListener(getActivity(), mapView);
        mapView.setOnTouchListener(mapViewTouchListener);


        if(!Global.isBookmarks) {
            dynamicLayer = new ArcGISMapImageLayer(Global.mapSearchResult.getService_response().getMap().getDetails().getServiceUrl());
            Credential credential = new UserCredential(Global.mapSearchResult.getService_response().getMap().getDetails().getServiceTokenUserName(),
                    Global.mapSearchResult.getService_response().getMap().getDetails().getServiceTokenPassword());
            dynamicLayer.setCredential(credential);
            map.getOperationalLayers().add(dynamicLayer);


            graphicsLayer = new GraphicsOverlay();
            mapView.getGraphicsOverlays().add(graphicsLayer);

            dynamicLayer.addDoneLoadingListener(() -> {
                if (dynamicLayer.getLoadStatus() == LoadStatus.LOADED) {
                    ArcGISMapServiceInfo mapServiceInfo = dynamicLayer.getMapServiceInfo();
                    //only show dimensions for this plot

                    LayerDefinition retriveLayer = getLayerDefination(Global.mapSearchResult.getService_response().getMap().getDetails().getSearch().getLayerId());

                    ArcGISMapImageSublayer sublayer = (ArcGISMapImageSublayer) dynamicLayer.getSublayers().get(Integer.parseInt(retriveLayer.getId()));
                    sublayer.setDefinitionExpression(retriveLayer.getQueryClause());

                    if (getActivity() != null) {
                        if (Global.isConnected(getActivity())) {
                            findParcel();
                        }
                    }

                } else {
                /*imgNext.clearAnimation();
                imgNext.setVisibility(View.GONE);*/
                    //progressDialog.hide();
                }
            });


            ViewAnimationUtils.blinkAnimationView(binding.imgBack);
        }

        binding.imgLayer.setTag("layer");
        //Button Layer Click event
        binding.imgLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //model.navigate(getActivity(), FragmentTAGS.FR_REQUEST_SITE_PLAN);

                if (!Global.isConnected(getActivity())) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());

                }
                else{
                    try {
                            if(view.getTag().toString().equalsIgnoreCase("layer")){
                            ((ImageView)view).setImageResource(R.drawable.ic_layers_24dp);
                            view.setTag("layer_active");
                            if(getOrthoLayer()!=null)
                            {
                                ArcGISSublayer ortho=getOrthoLayer();
                                ortho.setVisible(true);
                                mapView.setViewpointScaleAsync(ortho.getMaxScale()+1);
                            }
                        }else{
                            ((ImageView)view).setImageAlpha(50);
                            view.setTag("layer");
                            if(getOrthoLayer()!=null)
                            {
                                ArcGISSublayer ortho=getOrthoLayer();
                                ortho.setVisible(false);
                            }

                        }
                        //map.loadAsync();
                        dynamicLayer.retryLoadAsync();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
         //myBottomSheet = MapFunctionBottomSheetFragment.newInstance(this);
        if(isMakani){
            binding.imgBookmark.setVisibility(View.GONE);
        }
        else{
            binding.imgBookmark.setVisibility(View.VISIBLE);
        }

        adapterHistory = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, Global.getParcelNumbersFromHistory(getActivity()));


        binding.txtPlotNo.setVisibility(View.VISIBLE);
        /*binding.txtPlotNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE && binding.txtPlotNo.getText().toString().trim().length()!=0 ) {

                }
                Global.hideSoftKeyboard(getActivity());
                return false;
            }
        });
*/

        searchhistoryListView=(ListView)binding.getRoot().findViewById(R.id.fragment_map_searchhistory);
        searchhistoryListView.setAdapter(adapterHistory);
        searchhistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                skipOnTextChangeEvent=true;
                binding.txtPlotNo.setText(searchhistoryListView.getItemAtPosition(position).toString());
                skipOnTextChangeEvent=false;
                searchhistoryListView.setVisibility(View.GONE);
                if(Global.isBookmarks)
                    findParcel(searchhistoryListView.getItemAtPosition(position).toString().trim());
                else
                    HomeFragment.homeVM.getSearchResult(searchhistoryListView.getItemAtPosition(position).toString().trim());
            }
        });

        Handler handlerHistoryVisibilityController = new Handler();
        final Runnable runnableHistoryVisibilityController = new Runnable() {
            public void run() {
                if(binding.txtPlotNo.getText().toString().trim().length()>1)
                {
                    if(adapterHistory.getCount()>0)
                        searchhistoryListView.setVisibility(View.VISIBLE);
                    else
                        searchhistoryListView.setVisibility(View.GONE);
                }
                else{
                    searchhistoryListView.setVisibility(View.GONE);
                }
            }
        };

        binding.txtPlotNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!skipOnTextChangeEvent)
                {
                    adapterHistory.getFilter().filter(s.toString());
                    adapterHistory.notifyDataSetChanged();
                }

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!skipOnTextChangeEvent)
                {
                    final Timer timer=new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handlerHistoryVisibilityController.post(runnableHistoryVisibilityController);
                            timer.cancel();
                        }
                    },100);
                }
            }
        });
        binding.txtPlotNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) skipOnTextChangeEvent=false;
            }
        });
        binding.txtPlotNo.setImeOptions(EditorInfo.IME_ACTION_DONE);



        //Button search Button click event

        binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPlot();
            }
        });


        // Button Bookmark click event
        binding.imgBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                     if (!Global.isConnected(getActivity())) {

                            if(Global.appMsg!=null)
                                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                            else
                                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());

                        }
                        else{
                            Global.isSaveAsBookmark =true;
                            model.getParceldetails();
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onBackPressed();
            }
        });


        // Button Reset click event
        binding.imgRecenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Global.isConnected(getActivity())) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());

                }
                else{
                    if(mapView!=null && PlotDetails.plotGeometry!=null)
                    {
                        mapView.setViewpointGeometryAsync(PlotDetails.plotGeometry, extentPadding);
                        final Timer timer=new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                ////mMapView.zoomout();
                                timer.cancel();
                                //initiateFindParcelRequest();
                            }
                        }, 1000*1);
                    }
                }
            }
        });
        binding.imgMakani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                        if (!Global.isConnected(getActivity())) {

                            if(Global.appMsg!=null)
                                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                            else
                                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());

                        }
                        else{
                                if(Global.isBookmarks)
                                    Global.openMakani(parcelId,getActivity());
                                else
                                    Global.openMakani(Global.mapSearchResult.getService_response().getParcelId(), getActivity());
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0){
                    ArrayList al = new ArrayList();
                    if(Global.map_en_url != null && Global.map_en_url.length() > 0){
                        al.add(Global.map_en_url);
                        ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                    } else {
                        al.add(Global.helpUrlEn);
                        ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                    }
                } else {
                    ArrayList al = new ArrayList();
                    if(Global.map_ar_url != null && Global.map_ar_url.length() > 0){
                        al.add(Global.map_ar_url);
                        ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                    } else {
                        al.add(Global.helpUrlEn);
                        ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                    }
                }
            }
        });
    }

    private void searchPlot(){
        if(binding.txtPlotNo.getText().toString().trim().length()!=0) {
            if (!Global.isConnected(getActivity())) {

                if (Global.appMsg != null)
                    AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getResources().getString(R.string.ok), getActivity());
                else
                    AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());

            } else {
                onStarted();
                isMakani = false;
                Global.landNumber = null;
                Global.area = null;
                Global.area_ar = null;
                if (Global.isBookmarks)
                    findParcel(binding.txtPlotNo.getText().toString().trim());
                else
                    HomeFragment.homeVM.getSearchResult(binding.txtPlotNo.getText().toString().trim());

            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
            searchPlot();
            return true;
        }
        return false;
    }


    @Override
    public void onStarted() {
        AlertDialogUtil.showProgressBar(getActivity(),true);
    }

    @Override
    public void onSuccess() {
        AlertDialogUtil.showProgressBar(getActivity(),false);
    }

    @Override
    public void onFailure(String Msg) {
        AlertDialogUtil.showProgressBar(getActivity(),false);
        AlertDialogUtil.errorAlertDialog("",Msg,getActivity().getResources().getString(R.string.ok),getActivity());
    }

    @Override
    public void findParcelID(SearchResult response) {
        if(response.getIs_exception().equals("true")){
            if(response.getMessage() != null && response.getMessage().length() > 0){
                onFailure(response.getMessage());
            } else {
                onFailure(getActivity().getResources().getString(R.string.community_error));
            }
        } else {
            Global.mapSearchResult = response;
            findParcel();
        }
    }

    @Override
    public void getPlotDetais(SerializableParcelDetails appResponse) {

        onSuccess();

        if(Global.isBookmarks){
            if(mapView!=null && PlotDetails.plotGeometry!=null)
            {
                mapView.setViewpointGeometryAsync(PlotDetails.plotGeometry, extentPadding);
                ArcGISMapImageSublayer plotLayer=
                        (ArcGISMapImageSublayer) dynamicLayer.getSublayers().get(Integer.parseInt(Global.plotHighlightLayerId));
                ArcGISMapImageSublayer communityLayer=
                        (ArcGISMapImageSublayer) dynamicLayer.getSublayers().get(Integer.parseInt(Global.plotDimLayerId));

                plotLayer.loadAsync();
                communityLayer.loadAsync();
                plotLayer.addDoneLoadingListener(new Runnable() {
                    @Override
                    public void run() {

                        plotLayer.setDefinitionExpression("PARCEL_ID ='" + parcelId + "'");
                    }
                });
                communityLayer.addDoneLoadingListener(new Runnable() {
                    @Override
                    public void run() {

                        communityLayer.setDefinitionExpression("COMM_PARCEL_ID ='" + parcelId + "'");
                        onSuccess();
                        searchhistoryListView.setVisibility(View.GONE);

                        // bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                    }
                });
                final Timer timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ////mMapView.zoomout();
                        timer.cancel();
                        //initiateFindParcelRequest();
                    }
                }, 1000*1);
            }

        }


    }

    @Override
    public void onFunctionMenuSelected(int position) {
        Functions fun = Global.mapSearchResult.getService_response().getMap().getFunctions().get(position);
        mapFunctionAction(fun);
    }

    private void mapFunctionAction(Functions fun){
        if(fun.getInternalFunctions() != null && fun.getInternalFunctions().length() > 0){
            if (fun.getInternalFunctions().equals(FragmentTAGS.FR_REQUEST_SITE_PLAN)){
                model.validateRequest(FragmentTAGS.FR_REQUEST_SITE_PLAN);
                //validateParcel call required
            }
            setWebSheetPeekHeight(0);
        } else if (fun.getLaunchUrl() != null && fun.getLaunchUrl().length() > 0 &&
                fun.getLaunchUrl().contains("http")){
            setWebSheetPeekHeight(0);
            StringBuilder builder = new StringBuilder();
            builder.append(fun.getLaunchUrl());
            if(!fun.getLaunchUrl().endsWith("?")) {
                builder.append("?");
            }
            builder.append("token=" + Global.accessToken + "&");
            builder.append("remarks=" + Global.getPlatformRemark() + "&");
            String lang = Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0 ? "en" : "ar";
            builder.append("lang=" + lang + "&");
            if(!Global.isUserLoggedIn){
                builder.append("isGuest=true&");
                builder.append("user_id="+ Global.sime_userid +"&");
                builder.append("user_name=GUEST&");
            } else {
                builder.append("user_id=" + Global.username + "&");
                builder.append("user_name=" + Global.getUser(getActivity()).getFullname() + "&");
            }
            if(fun.getParams() != null && fun.getParams().size() > 0){

               /* for (Params p : fun.getParams()){
                    builder.append(p.getParam1() + "&");
                }*/

                for(int i = 0; i< fun.getParams().size(); i++){
                    builder.append(fun.getParams().get(i).getParam1());
                    if(i < fun.getParams().size() - 1){
                        builder.append("&");
                    }
                }
            }

            if(lastSelectedWebFunction == null || lastSelectedWebFunction.length() == 0 ||
                    !lastSelectedWebFunction.equals(fun.getNameEn())){
                webView.loadUrl(builder.toString());
                lastSelectedWebFunction = fun.getNameEn();
            }

            //toggleBottomSheet();
            setWebSheetPeekHeight(600);
            setMapFunctionSheetPeekHeight(0);
        }
    }

    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            binding.frameLayout.setVisibility(View.INVISIBLE);

        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            binding.frameLayout.setVisibility(View.VISIBLE);
        }
    }

    class MapViewTouchListener extends DefaultMapViewOnTouchListener {

        /**
         * Constructs a DefaultMapViewOnTouchListener with the specified Context and MapView.
         *
         * @param context the context from which this is being created
         * @param mapView the MapView with which to interact
         */
        public MapViewTouchListener(Context context, MapView mapView){
            super(context, mapView);
        }

        /**
         * Override the onSingleTapConfirmed gesture to handle tapping on the MapView
         * and detected if the Graphic was selected.
         * @param e the motion event
         * @return true if the listener has consumed the event; false otherwise
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // get the screen point where user tapped
            android.graphics.Point screenPoint = new android.graphics.Point((int)e.getX(), (int)e.getY());

            // identify graphics on the graphics overlay
            final ListenableFuture<IdentifyGraphicsOverlayResult> identifyGraphic = mapView.identifyGraphicsOverlayAsync(graphicsLayer, screenPoint, 10.0, false, 2);

            identifyGraphic.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        IdentifyGraphicsOverlayResult grOverlayResult = identifyGraphic.get();
                        // get the list of graphics returned by identify graphic overlay
                        List<Graphic> graphic = grOverlayResult.getGraphics();
                        // get size of list in results
                        int identifyResultSize = graphic.size();

                            if(PlotDetails.plotGeometry!=null)
                            {
                                //goToNext();
                                if(!Global.isBookmarks){
                                    if(Global.mapSearchResult.getService_response().getMap().getFunctions() != null &&
                                            Global.mapSearchResult.getService_response().getMap().getFunctions().size() > 1) {
                                        //myBottomSheet.show(getActivity().getSupportFragmentManager(), myBottomSheet.getTag());
                                        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                                    } else {
                                        if(Global.mapSearchResult.getService_response().getMap().getFunctions() != null){
                                            mapFunctionAction(Global.mapSearchResult.getService_response().getMap().getFunctions().get(0));
                                        }
                                    }
                                }
                                //myBottomSheet.show(getActivity().getSupportFragmentManager(), myBottomSheet.getTag());
                                // map click event perform here

                            }
                        
                    }catch(InterruptedException | ExecutionException ie){
                        ie.printStackTrace();
                    }

                }
            });

            return super.onSingleTapConfirmed(e);
        }

    }


    public ArcGISSublayer getOrthoLayer(){
        List<ArcGISSublayer> layers=dynamicLayer.getSublayers();
        for(int i=0;i<layers.size();i++){
            ArcGISSublayer layer=layers.get(i);
            if(layer.getId()==6)
                return layer;
        }

        return null;
    }
    public void fromBookmarksFindCommunity(){
        try{

            ArcGISMapImageSublayer communityLayer= (ArcGISMapImageSublayer) dynamicLayer.getSublayers().get(Integer.parseInt(Global.plotDimLayerId));
            communityLayer.loadAsync();
            communityLayer.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    ServiceFeatureTable sublayerTable = communityLayer.getTable();
                    QueryParameters query = new QueryParameters();
                    query.setWhereClause("COMM_NUM  = '" + parcelId.substring(0,3) + "'");
                    ListenableFuture<FeatureQueryResult> sublayerQuery = sublayerTable.queryFeaturesAsync(query,ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);
                    sublayerQuery.addDoneListener(() -> {
                        try {
                            FeatureQueryResult result = sublayerQuery.get();
                            PlotDetails.emailParam=new EmailParam();
                            for (Feature feature : result) {
                                List<Field> fields=result.getFields();
                                for(Field field : fields)
                                {

                                    if(field.getName().compareToIgnoreCase("COMMUNITY_E")==0){
                                        PlotDetails.communityEn=feature.getAttributes().get("COMMUNITY_E")!=null?feature.getAttributes().get("COMMUNITY_E").toString():"-";
                                        PlotDetails.emailParam.communityEn=PlotDetails.communityEn;
                                    }
                                    if(field.getName().compareToIgnoreCase("COMMUNITY_A")==0){
                                        PlotDetails.communityAr=feature.getAttributes().get("COMMUNITY_A")!=null?feature.getAttributes().get("COMMUNITY_A").toString():"-";
                                        PlotDetails.emailParam.communityAr=PlotDetails.communityAr;
                                    }
                                    PlotDetails.emailParam.plotArea=PlotDetails.area;

                                }
                                break;
                            }
                            onSuccess();


                        } catch (InterruptedException | ExecutionException e) {
                            Log.e(MainActivity.class.getSimpleName(), e.toString());
                            model.showErrorMessage(e.getMessage());
                        }
                    });
                }
            });
        }
        catch(Exception ex){

        }
    }

    public void findCommunity(){
        LayerDefinition retriveLayer = getLayerDefination(Global.mapSearchResult.getService_response().getMap().getDetails().getDimension().getLayerId());

        try{


            ArcGISMapImageSublayer communityLayer=
                    (ArcGISMapImageSublayer) dynamicLayer.getSublayers().get(Integer.valueOf(retriveLayer.getId()));
            communityLayer.setDefinitionExpression(retriveLayer.getQueryClause());
            communityLayer.loadAsync();
            communityLayer.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    ServiceFeatureTable sublayerTable = communityLayer.getTable();
                    QueryParameters query = new QueryParameters();
                    query.setWhereClause(retriveLayer.getQueryClause());
                    ListenableFuture<FeatureQueryResult> sublayerQuery = sublayerTable.queryFeaturesAsync(query,ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);
                    sublayerQuery.addDoneListener(() -> {
                        try {
                            FeatureQueryResult result = sublayerQuery.get();
                            PlotDetails.emailParam=new EmailParam();
                            for (Feature feature : result) {
                                List<Field> fields=result.getFields();
                                for(Field field : fields)
                                {

                                    if(field.getName().compareToIgnoreCase("COMMUNITY_E")==0){
                                        PlotDetails.communityEn=feature.getAttributes().get("COMMUNITY_E")!=null?feature.getAttributes().get("COMMUNITY_E").toString():"-";
                                        PlotDetails.emailParam.communityEn=PlotDetails.communityEn;
                                    }
                                    if(field.getName().compareToIgnoreCase("COMMUNITY_A")==0){
                                        PlotDetails.communityAr=feature.getAttributes().get("COMMUNITY_A")!=null?feature.getAttributes().get("COMMUNITY_A").toString():"-";
                                        PlotDetails.emailParam.communityAr=PlotDetails.communityAr;
                                    }
                                    PlotDetails.emailParam.plotArea=PlotDetails.area;

                                }
                                break;
                            }


                            //showSnackBar();
                            onSuccess();
                            searchhistoryListView.setVisibility(View.GONE);
                            if(!Global.isBookmarks) {
                                if(Global.mapSearchResult.getService_response().getMap().getFunctions() != null &&
                                        Global.mapSearchResult.getService_response().getMap().getFunctions().size() > 1) {
                                    bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                                } else {
                                    if(Global.mapSearchResult.getService_response().getMap().getFunctions() != null){
                                        mapFunctionAction(Global.mapSearchResult.getService_response().getMap().getFunctions().get(0));
                                    }
                                }
                            }


                            mapView.setViewpointGeometryAsync(PlotDetails.plotGeometry,extentPadding).addDoneListener(new Runnable() {
                                @Override
                                public void run() {
                                    createExportParams();
                                }
                            });
                        } catch (InterruptedException | ExecutionException e) {

                            model.showErrorMessage(e.getMessage());
                        }
                    });
                }
            });
        }
        catch(Exception ex){
           model.showErrorMessage(ex.getMessage());
        }
    }

    private LayerDefinition getLayerDefination(String id){
        for(int i = 0; i < Global.mapSearchResult.getService_response().getMap().getDetails().getLayerDefinition().size(); i++){
            if(id.equals(Global.mapSearchResult.getService_response().getMap().getDetails().getLayerDefinition().get(i).getId())){
                return Global.mapSearchResult.getService_response().getMap().getDetails().getLayerDefinition().get(i);
            }
        }
        return null;
    }

    public void findParcel(String parcelId){

        if(PlotDetails.currentState.graphic!=null){
            graphicsLayer.getGraphics().remove(PlotDetails.currentState.graphic);
            graphicsLayer.getGraphics().remove(PlotDetails.currentState.textLabel);
        }

        HashMap<Integer, String> layerDefs = new HashMap<Integer, String>();
        layerDefs.put(Integer.valueOf(AppUrls.plot_layerid), "PARCEL_ID ='" + parcelId + "'");

        if(dynamicLayer.getSublayers().size()>0){
            ArcGISMapImageSublayer sublayerComm= (ArcGISMapImageSublayer) dynamicLayer.getSublayers().get(Integer.parseInt(AppUrls.plot_layerid));
            sublayerComm.setDefinitionExpression("PARCEL_ID ='" + parcelId + "'");
            ArcGISMapImageSublayer sublayer= (ArcGISMapImageSublayer) dynamicLayer.getSublayers().get(Integer.parseInt(AppUrls.plot_layerid));

            sublayer.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    ServiceFeatureTable sublayerTable = sublayer.getTable();
                    QueryParameters query = new QueryParameters();
                    query.setWhereClause("PARCEL_ID  = '" + parcelId + "'");

                    if(sublayerTable!=null){
                        ListenableFuture<FeatureQueryResult> sublayerQuery = sublayerTable.queryFeaturesAsync(query,ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);

                        sublayerQuery.addDoneListener(() -> {
                            try {
                                FeatureQueryResult result = sublayerQuery.get();
                                SimpleLineSymbol sls = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 3);
                                SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.argb(30, 243, 247, 129),
                                        sls);
                                simpleFillSymbol.setOutline(sls);

                                for (Feature feature : result) {

                                    List<Field> fields=result.getFields();
                                    for(Field field : fields) {
                                        if(field.getName().compareToIgnoreCase("SHAPE.AREA")==0) {
                                            PlotDetails.area = Global.round((double)feature.getAttributes().get("SHAPE.AREA"),2);
                                            break;
                                        }

                                    }


                                    Graphic sublayerGraphic = new Graphic(feature.getGeometry(), simpleFillSymbol);
                                    PlotDetails.currentState.graphic=sublayerGraphic;
                                    PlotDetails.plotGeometry=feature.getGeometry();
                                    graphicsLayer.getGraphics().add(sublayerGraphic);


                                    TextSymbol txtSymbol=null;
                                    Graphic parcelTextLabel;
                                    if(isMakani){
                                        txtSymbol=new TextSymbol(16,Global.makani,Color.argb(255, 0, 0, 0),TextSymbol.HorizontalAlignment.CENTER,TextSymbol.VerticalAlignment.MIDDLE);
                                        txtSymbol.setHaloWidth(2);
                                        txtSymbol.setHaloColor(Color.argb(255, 255, 255, 255));

                                        parcelTextLabel = new Graphic(feature.getGeometry().getExtent().getCenter(), txtSymbol);
                                        graphicsLayer.getGraphics().add(parcelTextLabel);

                                        Drawable dr = getResources().getDrawable(R.drawable.makani);
                                        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                                        PictureMarkerSymbol makanisymbol = new PictureMarkerSymbol(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 96, 96, true)));
                                        makanisymbol.setOffsetY(10);
                                        Graphic makaniGraphic = new Graphic(feature.getGeometry().getExtent().getCenter(),makanisymbol);
                                        graphicsLayer.getGraphics().add(makaniGraphic);
                                    }
                                    else{
                                        txtSymbol=new TextSymbol(16,parcelId,Color.argb(255, 0, 0, 0),TextSymbol.HorizontalAlignment.CENTER,TextSymbol.VerticalAlignment.MIDDLE);
                                        txtSymbol.setHaloWidth(2);
                                        txtSymbol.setHaloColor(Color.argb(255, 255, 255, 255));
                                        txtSymbol.setOffsetY(30);
                                        parcelTextLabel = new Graphic(feature.getGeometry().getExtent().getCenter(), txtSymbol);
                                        graphicsLayer.getGraphics().add(parcelTextLabel);
                                    }

                                    PlotDetails.currentState.textLabel=parcelTextLabel;
                                    PlotDetails.parcelNo=parcelId;
                                    Global.addToParcelHistory(parcelId,getActivity());
                                    //fromBookmarksFindCommunity();
                                    model.getParceldetails();
                                    break;
                                }
                                //if(progressDialog != null)
                                //    progressDialog.hide();
                                if(!result.iterator().hasNext()){
                                    AlertDialogUtil.errorAlertDialog("",getActivity().getResources().getString(R.string.plot_does_not_exist),
                                            getActivity().getResources().getString(R.string.ok), getActivity());

                                }

                            } catch (InterruptedException | ExecutionException e) {
                                Log.e(MainActivity.class.getSimpleName(), e.toString());

                            }
                        });
                    }
                }
            });
            sublayer.loadAsync();
        }
        else{

            dynamicLayer.retryLoadAsync();
        }



//        String targetLayer = Constant.GIS_LAYER_URL.concat("/" + Constant.plot_layerid);

    }

    public void findParcel(){

        LayerDefinition retriveLayer = getLayerDefination(Global.mapSearchResult.getService_response().getMap().getDetails().getSearch().getLayerId());

        if(retriveLayer != null){
            HashMap<Integer, String> layerDefs = new HashMap<Integer, String>();
            layerDefs.put(Integer.valueOf(retriveLayer.getId()), retriveLayer.getQueryClause());
        }


        if(dynamicLayer.getSublayers().size()>0){
            ArcGISMapImageSublayer sublayerComm= (ArcGISMapImageSublayer) dynamicLayer.getSublayers().get(Integer.valueOf(retriveLayer.getId()));
            sublayerComm.setDefinitionExpression(retriveLayer.getQueryClause());

            ArcGISMapImageSublayer sublayer= (ArcGISMapImageSublayer) dynamicLayer.getSublayers().get(Integer.valueOf(retriveLayer.getId()));

            sublayer.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    ServiceFeatureTable sublayerTable = sublayer.getTable();
                    QueryParameters query = new QueryParameters();
                    QueryParameters queryDimention = new QueryParameters();
                    query.setWhereClause(retriveLayer.getQueryClause());
                    queryDimention.setWhereClause(retriveLayer.getQueryClause());

                    if(sublayerTable!=null){
                        ListenableFuture<FeatureQueryResult> sublayerQuery = sublayerTable.queryFeaturesAsync(query,ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);

                        sublayerQuery.addDoneListener(() -> {
                            try {
                                FeatureQueryResult result = sublayerQuery.get();
                                /*SimpleLineSymbol sls = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3);
                                SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.argb(30, 243, 247, 129),
                                        sls);
                                simpleFillSymbol.setOutline(sls);
*/
                                for (Feature feature : result) {

                                    List<Field> fields=result.getFields();
                                    for(Field field : fields) {
                                        if(field.getName().compareToIgnoreCase("SHAPE.AREA")==0) {
                                            PlotDetails.area = Global.round((double)feature.getAttributes().get("SHAPE.AREA"),2);
                                            break;
                                        }

                                    }


//                                    Graphic sublayerGraphic = new Graphic(feature.getGeometry(), simpleFillSymbol);
                                    PlotDetails.currentState.graphic=new Graphic(feature.getGeometry(),new SimpleFillSymbol());
                                    PlotDetails.plotGeometry=feature.getGeometry();
//                                    graphicsLayer.getGraphics().add(sublayerGraphic);

                                    findCommunity();
                                    break;
                                }
                                if(!result.iterator().hasNext()){
                                    onFailure(getActivity().getResources().getString(R.string.plot_does_not_exist));
                                }else{

                                }

                            } catch (InterruptedException | ExecutionException e) {
                                model.showErrorMessage(e.getMessage());
                            }
                        });
                    }
                    else{
                        model.showErrorMessage("");

                    }

                }
            });
            sublayer.loadAsync();
        }
        else{

            dynamicLayer.retryLoadAsync();
        }
    }
    public void fromBookmarks(String parcelId){

        onStarted();
        SpatialReference mSR = SpatialReference.create(3997);


        Point p1 = new Point(54.84,24.85,  0, mSR);
        Point p2 = new Point(55.55,25.34 , 0,mSR);
        initExtent = new Envelope(p1.getX(), p1.getY(), p2.getX(), p2.getY(), mSR);

        Viewpoint vp = new Viewpoint(initExtent);
        mapView.setViewpoint(vp);

        SpatialReference sr=SpatialReference.create(3997);

        ArcGISMap map = new ArcGISMap(sr);
        mapView.setMap(map);

        // set up gesture for interacting with the MapView
        MapViewTouchListener mMapViewTouchListener = new MapViewTouchListener(this.getActivity(), mapView);
        mapView.setOnTouchListener(mMapViewTouchListener);

        dynamicLayer = new ArcGISMapImageLayer(AppUrls.GIS_LAYER_URL);
        Credential credential=new UserCredential(AppUrls.GIS_LAYER_USERNAME,AppUrls.GIS_LAYER_PASSWORD);
        dynamicLayer.setCredential(credential);

        map.getOperationalLayers().add(dynamicLayer);

        graphicsLayer = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsLayer);



        dynamicLayer.addDoneLoadingListener(() -> {
            if (dynamicLayer.getLoadStatus() == LoadStatus.LOADED) {
                ArcGISMapServiceInfo mapServiceInfo = dynamicLayer.getMapServiceInfo();
                //only show dimensions for this plot
                ArcGISMapImageSublayer sublayer= (ArcGISMapImageSublayer) dynamicLayer.getSublayers().get(Integer.parseInt(AppUrls.plot_layerid));
                sublayer.setDefinitionExpression("PARCEL_ID ='" + parcelId + "'");
                if(Global.isConnected(getActivity())){

                    if(isMakani) {
                        mapView.setVisibility(View.VISIBLE);

                        if (parcelId != null && parcelId.trim().length() != 0) {
                            findParcel(parcelId);
                        }

                    } else {
                        if(parcelId!=null && parcelId.trim().length()!=0)
                        {
                            mapView.setVisibility(View.VISIBLE);
                            findParcel(parcelId);

                        }
                    }

                }
                else{
                    model.showErrorMessage("");
                }
                //initiateFindParcelRequest();
            }
        });
    }

    private int convertToDp(double input) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (input * scale + 0.5f);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
   /* private void showSnackBar() {


        snack.setDuration(6000);

        View view =(View) snack.getView();


        view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        TextView tv = (TextView) view
                .findViewById(R.id.snackbar_text);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //params.setMargins(80,0,90,0);

        tv.setLayoutParams( params);
        tv.setTextColor(Color.WHITE);//change textColor
        tv.setGravity(Gravity.CENTER);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "Dubai-Regular.ttf");
        tv.setTypeface(font);
        tv.setTextSize(16);

        AlertDialogUtil.showProgressBar(getActivity(),false);
        snack.show();
    }*/

    private void createExportParams(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height2 = displaymetrics.heightPixels;
        int width2 = displaymetrics.widthPixels;
        int density = displaymetrics.densityDpi;

        ArrayList<dm.sime.com.kharetati.datas.models.Point> parcelScreenCoordinates = new ArrayList<dm.sime.com.kharetati.datas.models.Point>();
        ArrayList<dm.sime.com.kharetati.datas.models.Point> circleScreenCoordinates = new ArrayList<dm.sime.com.kharetati.datas.models.Point>();
        int width = 664/96*(int)displaymetrics.xdpi;//664*2;
        int height = 528/96*(int)displaymetrics.ydpi;//528*2;

        float centreX=mapView.getX() + mapView.getWidth()  / 2;
        float centreY=mapView.getY() + mapView.getHeight() / 2;

        //Point screenCenterAgs = mapView.locationToScreen(mapView.center);
        dm.sime.com.kharetati.datas.models.Point screenCenter = new dm.sime.com.kharetati.datas.models.Point(centreX, centreY);

        int toleranceWidth = width / 2;
        int toleranceHeight = height / 2;
        //int correctionTolWidth=toleranceWidth+100;
        //int correctionTolHeight=toleranceHeight+100;

        Point bottomLeft = mapView.screenToLocation(new android.graphics.Point((int)screenCenter.x - toleranceWidth,  (int)screenCenter.y + toleranceHeight));
        Point topRight = mapView.screenToLocation(new android.graphics.Point((int)screenCenter.x + toleranceWidth, (int) screenCenter.y - toleranceHeight));
        //Point bottomLeft = mapView.toMapPoint((float) screenCenter.x - toleranceWidth, (float) screenCenter.y + toleranceHeight);
        //Point topRight = mapView.toMapPoint((float) screenCenter.x + toleranceWidth, (float) screenCenter.y - toleranceHeight);
        AgsExtent extent = new AgsExtent(bottomLeft.getX(), bottomLeft.getY(), topRight.getX(), topRight.getY());
        android.graphics.Point labelPosScreenPointTmp = mapView.locationToScreen(PlotDetails.plotGeometry.getExtent().getCenter());
        dm.sime.com.kharetati.datas.models.Point labelPosScreenPoint = new dm.sime.com.kharetati.datas.models.Point(labelPosScreenPointTmp.x, labelPosScreenPointTmp.y);
        String parecelID = Global.mapSearchResult.getService_response().getParcelId();
        int offsetWidth = width / 2 - (int) screenCenter.x;
        int offsetHeight = height / 2 - (int) screenCenter.y;
        Polygon polygon = (Polygon) PlotDetails.plotGeometry;
        ImmutablePartCollection parts = polygon.getParts();
        for  (ImmutablePart part : parts) {
            for (Point pt : part.getPoints())
            {
                Point point=new Point((int)pt.getX(),(int)pt.getY(),mapView.getSpatialReference());
                android.graphics.Point screen = mapView.locationToScreen(point);
                parcelScreenCoordinates.add(new dm.sime.com.kharetati.datas.models.Point(screen.x+offsetWidth,screen.y+offsetHeight));
            }
        }

        ExportParam exportParam = new ExportParam();
        exportParam.circle = circleScreenCoordinates.toArray(new dm.sime.com.kharetati.datas.models.Point[circleScreenCoordinates.size()]);
        exportParam.parcel=parcelScreenCoordinates.toArray(new dm.sime.com.kharetati.datas.models.Point[parcelScreenCoordinates.size()]);
        exportParam.width = width;
        exportParam.height = height;
        exportParam.extent = extent;
        exportParam.url = Global.getCurrentLanguage(getActivity()).compareToIgnoreCase("en")==0?AppUrls.parcelLayerExportUrl_en:AppUrls.parcelLayerExportUrl_ar;
        exportParam.visibleLayers = "0, 1, 2, 8, 9, 3, 6, 7";
        exportParam.imageFormat = "jpg";
        exportParam.dpi = displaymetrics.densityDpi;//96;
        exportParam.center = screenCenter;
        exportParam.label = parecelID;
        exportParam.parcelOutlineColor = "#000000";
        exportParam.parcelFillColor = "#F3F781";
        exportParam.circleOutlineColor = "#000000";
        exportParam.labelColor = "#3B240B";
        exportParam.parcelOutlineWidth = 3*(int)displaymetrics.density;
        exportParam.circleOutlineWidth = 1;
        exportParam.showCircle = false;
        exportParam.labelFontSize = 12*(int)displaymetrics.density;
        exportParam.labelPosition = new dm.sime.com.kharetati.datas.models.Point(labelPosScreenPoint.x + offsetWidth, labelPosScreenPoint.y + offsetHeight);
        //exportParam.layerDefs = "3:COMM_PARCEL_ID=" + parcelId + ";6:PARCEL_ID=" + parcelId + ";7:PARCEL_ID=" + parcelId;

        PlotDetails.exportParam=exportParam;
        PlotDetails.emailParam.plotnumber=parecelID;

    }

}
