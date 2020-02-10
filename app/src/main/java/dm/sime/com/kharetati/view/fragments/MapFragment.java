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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.MapRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.ViewAnimationUtils;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.adapters.DashboardPagerAdapter;
import dm.sime.com.kharetati.view.navigators.MapNavigator;
import dm.sime.com.kharetati.view.viewModels.MapViewModel;
import dm.sime.com.kharetati.view.viewmodelfactories.MapViewModelFactory;

public class MapFragment extends Fragment implements MapNavigator {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String parcelId, latLong;
    public static boolean isMakani,isLand;
    double latitude, longitude;
    MapViewModel model;
    FragmentMapBinding binding;
    private View mRootView;
    private ArcGISMapImageLayer dynamicLayer = null;
    private GraphicsOverlay graphicsLayer = null;
    private UserCredential userCredentials = null;
    static String token = Global.arcgis_token;
    Snackbar snack;
    private boolean hasCommunityTaskCompleted=true;
    private boolean hasPlotTaskCompleted=true;
    public Point parcelCenter;

    private Envelope initExtent;
    MapView mapView;

    private int extentPadding=100;

    private boolean skipOnTextChangeEvent=false;
    MapFunctionBottomSheetFragment myBottomSheet;
    private MapRepository repository;
    private MapViewModelFactory factory;
    private ArrayAdapter<String> adapterHistory;
    private ListView searchhistoryListView;


    public MapFragment() {
    }

    private static MapFragment mapFragment=null;

    public static MapFragment newInstance(String param1, String param2){
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        model.mapNavigator =this;
        if (getArguments() != null) {
            parcelId = getArguments().getString(ARG_PARAM1);
            latLong = getArguments().getString(ARG_PARAM2);
            if(Global.current_fragment_id == FragmentTAGS.FR_MAKANI) {
                isMakani = true;
                isLand = false;
                String[] arr = latLong.split(" ");
                latitude = Double.parseDouble(arr[0]);
                longitude = Double.parseDouble(arr[1]);
                parcelId = Global.rectifyPlotNo(parcelId);
            } else if(Global.current_fragment_id == FragmentTAGS.FR_LAND) {
                parcelId = Global.rectifyPlotNo(parcelId);
                isMakani = false;
                isLand = true;
            }else{
                parcelId = Global.rectifyPlotNo(parcelId);
                isMakani = false;
                isLand = false;

            }
        }

        /*ApplicationController application = (ApplicationController) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(FR_MAP);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());*/

        setRetainInstance(true);
        PlotDetails.currentState=new PlotDetails.CurrentState();
        PlotDetails.clearCommunity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        binding.setFragmentMapVM(model);
        mRootView = binding.getRoot();
        mapView = mRootView.findViewById(R.id.mapView);
        binding.txtPlotNo.setText(parcelId);
        onStarted();
        initializePage();
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud3984007683,none,GB2PMD17J0YJ2J7EZ071");
        //progressBar = new //progressBar(getActivity(),null,android.R.attr.//progressBarStyleSmall);
        snack = Snackbar.make(getActivity().findViewById(R.id.ui_container), R.string.click_on_map, Snackbar.LENGTH_LONG);

        return binding.getRoot();
    }

    private void initializePage(){
        model.manageAppBar(getActivity(), false);
        model.manageAppBottomBAtr(getActivity(), false);

        SpatialReference mSR = SpatialReference.create(3997);
        Point px = new Point(54.84, 24.85);
        Point py = new Point(55.55, 25.34);

        Point p1 = new Point(54.84,24.85,  0, mSR);
        Point p2 = new Point(55.55,25.34 , 0,mSR);
        initExtent = new Envelope(p1.getX(), p1.getY(), p2.getX(), p2.getY(), mSR);

        Viewpoint vp = new Viewpoint(initExtent);
        mapView.setViewpoint(vp);

        SpatialReference sr=SpatialReference.create(3997);

        ArcGISMap map = new ArcGISMap(sr);
        mapView.setMap(map);

        // set up gesture for interacting with the MapView
        MapViewTouchListener mapViewTouchListener = new MapViewTouchListener(getActivity(), mapView);
        mapView.setOnTouchListener(mapViewTouchListener);


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
                ArcGISMapImageSublayer sublayer= (ArcGISMapImageSublayer) dynamicLayer.getSublayers().get(7);
                sublayer.setDefinitionExpression("PARCEL_ID ='" + parcelId + "'");
                if(Global.isConnected(getActivity())){

                    if(isMakani) {
                        mapView.setVisibility(View.VISIBLE);
                        //mapToolbar.setVisibility(View.VISIBLE);
                        //findParcel();
                        if (parcelId != null && parcelId.trim().length() != 0) {
                            findParcel(parcelId);
                        }

                    } else {
                        if(parcelId!=null && parcelId.trim().length()!=0)
                        {
                            mapView.setVisibility(View.VISIBLE);
                            //mapToolbar.setVisibility(View.VISIBLE);
                            findParcel(parcelId);

                        }
                        else{

//                            imgNext.setVisibility(View.GONE);
                            //progressDialog.hide();

                        }
                    }

                }
                else{
                    //progressDialog.hide();
                }
                //initiateFindParcelRequest();
            }
            else{
                /*imgNext.clearAnimation();
                imgNext.setVisibility(View.GONE);*/
                //progressDialog.hide();
            }
        });



        ViewAnimationUtils.blinkAnimationView(binding.imgBack);

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
         myBottomSheet = MapFunctionBottomSheetFragment.newInstance();
        if(isMakani){
            binding.imgBookmark.setVisibility(View.GONE);
        }
        else{
            binding.imgBookmark.setVisibility(View.VISIBLE);
        }

        adapterHistory = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, Global.getParcelNumbersFromHistory(getActivity()));


        binding.txtPlotNo.setVisibility(View.VISIBLE);
        binding.txtPlotNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE && binding.txtPlotNo.getText().toString().trim().length()!=0 ) {
                    isMakani = false;
                    Global.landNumber = null;
                    Global.area = null;
                    Global.area_ar = null;
                    parcelId = binding.txtPlotNo.getText().toString();
                    return initiateFindParcelRequest();
                }
                Global.hideSoftKeyboard(getActivity());
                return false;
            }
        });


        searchhistoryListView=(ListView)binding.getRoot().findViewById(R.id.fragment_map_searchhistory);
        searchhistoryListView.setAdapter(adapterHistory);
        searchhistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                skipOnTextChangeEvent=true;
                binding.txtPlotNo.setText(searchhistoryListView.getItemAtPosition(position).toString());
                skipOnTextChangeEvent=false;
                searchhistoryListView.setVisibility(View.GONE);
                if(!isMakani) {
                    parcelId = searchhistoryListView.getItemAtPosition(position).toString();
                    initiateFindParcelRequest();
                }
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
                if (!Global.isConnected(getActivity())) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());

                }
                else{
                    onStarted();
                    isMakani = false;
                    Global.landNumber = null;
                    Global.area = null;
                    Global.area_ar = null;
                    if(binding.txtPlotNo.getText()!=null){
                        parcelId = binding.txtPlotNo.getText().toString();
                        initiateFindParcelRequest();
                    }
                    else{
                        onFailure(getString(R.string.PLEASE_ENTER_PLOTNUMBER));

                    }


                    }
            }
        });


        // Button Bookmark click event
        binding.imgBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isMakani){

                }
                try {
                    if(parcelId.length() > 0) {
                        if (!Global.isConnected(getActivity())) {

                            if(Global.appMsg!=null)
                                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                            else
                                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());

                        }
                        else
                            model.saveAsBookMark(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                    if(mapView!=null && PlotDetails.currentState.graphic!=null)
                    {
                        mapView.setViewpointGeometryAsync(PlotDetails.currentState.graphic.getGeometry(), extentPadding);
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
                    if(parcelId.length() > 0) {
                        if (!Global.isConnected(getActivity())) {

                            if(Global.appMsg!=null)
                                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                            else
                                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());

                        }
                        else
                            Global.openMakani(parcelId, getActivity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
                        if(!graphic.isEmpty()){
                            if(PlotDetails.plotGeometry!=null)
                            {
                                //goToNext();
                                myBottomSheet.show(getActivity().getSupportFragmentManager(), myBottomSheet.getTag());
                                // map click event perform here

                            }
                        }
                    }catch(InterruptedException | ExecutionException ie){
                        ie.printStackTrace();
                    }

                }
            });

            return super.onSingleTapConfirmed(e);
        }

    }
    private boolean initiateFindParcelRequest(){
        onStarted();
        //searchhistoryListView.setVisibility(View.GONE);
        PlotDetails.clearCommunity();
        //parcelId = txtPlotNo.getText().toString().trim().replaceAll("\\s+","");
        parcelId = parcelId.replaceAll("_","");
        if(Global.isProbablyArabic(parcelId))
            parcelId=Global.arabicToDecimal(parcelId);
        parcelId=Global.rectifyPlotNo(parcelId);
        if(Global.isConnected(getActivity())){
            if(isMakani == true){
                if (parcelId.length() <= 20) {
                    if (parcelId.length() > 4) {
                        findParcel(parcelId);
                    }
                    else{
                        onFailure(getString(R.string.valid_plot_number));
                        Toast.makeText(MapFragment.this.getActivity(), R.string.valid_plot_number,
                                Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    onFailure(getString(R.string.valid_plot_number));
                    Toast.makeText(MapFragment.this.getActivity(), R.string.valid_plot_number,
                            Toast.LENGTH_LONG).show();
                }
                Global.hideSoftKeyboard(getActivity());
            } else {
                if (parcelId.length() <= 20) {
                    if (parcelId.length() > 4) {

                        findParcel(parcelId);
                        Global.hideSoftKeyboard(getActivity());

                        return true;
                    } else
                        onFailure(getString(R.string.valid_plot_number));
                } else {
                    onFailure(getString(R.string.valid_plot_number));
                }

            }
        }
        return false;
    }



    /*private boolean checkNetworkConnection(){
        if(Global.isConnected(getActivity())){
            layoutNetworkCon.setVisibility(View.INVISIBLE);
            imgNext.setVisibility(View.VISIBLE);
            return true;
        }
        else{
            layoutNetworkCon.setVisibility(View.VISIBLE);
            imgNext.setVisibility(View.INVISIBLE);
            return false;
        }
    }*/

    public ArcGISSublayer getOrthoLayer(){
        List<ArcGISSublayer> layers=dynamicLayer.getSublayers();
        for(int i=0;i<layers.size();i++){
            ArcGISSublayer layer=layers.get(i);
            if(layer.getId()==5)
                return layer;
        }

        return null;
    }

    public void findCommunity(){
        try{
            ArcGISMapImageSublayer communityLayer= (ArcGISMapImageSublayer) dynamicLayer.getSublayers().get(Integer.valueOf(AppUrls.community_layerid));
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
                                hasCommunityTaskCompleted=true;
                                break;
                            }


                            showSnackBar();
                            onSuccess();
                            searchhistoryListView.setVisibility(View.GONE);
                            myBottomSheet.show(getActivity().getSupportFragmentManager(), myBottomSheet.getTag());

                            /*imgNext.setVisibility(View.VISIBLE );
                            imgNext.setEnabled(true);
                            imgNext.startAnimation(animation);
                            btnFind.setEnabled(true);*/



                            mapView.setViewpointGeometryAsync(PlotDetails.plotGeometry,extentPadding).addDoneListener(new Runnable() {
                                @Override
                                public void run() {
                                    createExportParams();
                                }
                            });
                        } catch (InterruptedException | ExecutionException e) {
                            Log.e(MainActivity.class.getSimpleName(), e.toString());

                            //btnFind.setEnabled(true);
                            onFailure(e.getMessage());
                        }
                    });
                }
            });
        }
        catch(Exception ex){
            onFailure(ex.getMessage());
        }
    }
    public void findParcel(String parcelId){

        if(PlotDetails.currentState.graphic!=null){
            graphicsLayer.getGraphics().remove(PlotDetails.currentState.graphic);
            graphicsLayer.getGraphics().remove(PlotDetails.currentState.textLabel);
        }
        //btnFind.setEnabled(false);
        HashMap<Integer, String> layerDefs = new HashMap<Integer, String>();
        layerDefs.put(Integer.valueOf(7), "PARCEL_ID ='" + parcelId + "'");

        if(dynamicLayer.getSublayers().size()>0){
            ArcGISMapImageSublayer sublayerComm= (ArcGISMapImageSublayer) dynamicLayer.getSublayers().get(7);
            sublayerComm.setDefinitionExpression("PARCEL_ID ='" + parcelId + "'");
            ArcGISMapImageSublayer sublayer= (ArcGISMapImageSublayer) dynamicLayer.getSublayers().get(Integer.valueOf(AppUrls.plot_layerid));

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
                                    findCommunity();
                                    break;
                                }
                                //if(progressDialog != null)
                                //    progressDialog.hide();
                                if(!result.iterator().hasNext()){

                                    onFailure(getActivity().getResources().getString(R.string.plot_does_not_exist));
                                    /*imgNext.setVisibility(View.GONE);
                                    imgNext.clearAnimation();*/
                                }else{
                                    /*imgNext.setVisibility(View.VISIBLE);
                                    imgNext.setEnabled(false);*/
                                }
                                //btnFind.setEnabled(true);

                            } catch (InterruptedException | ExecutionException e) {
                                Log.e(MainActivity.class.getSimpleName(), e.toString());

                                onFailure(e.getMessage());

                                /*btnFind.setEnabled(true);
                                imgNext.setVisibility(View.GONE);
                                imgNext.clearAnimation();*/
                            }
                        });
                    }
                    else{

                        /*btnFind.setEnabled(true);
                        imgNext.setVisibility(View.GONE);
                        imgNext.clearAnimation();*/
                    }

                }
            });
            sublayer.loadAsync();
        }
        else{

            /*btnFind.setEnabled(true);
            imgNext.setVisibility(View.GONE);*/
            dynamicLayer.retryLoadAsync();
        }



        String targetLayer = AppUrls.GIS_LAYER_URL.concat("/" + AppUrls.plot_layerid);


        hasPlotTaskCompleted=false;
    }

    private int convertToDp(double input) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (input * scale + 0.5f);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showSnackBar() {


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
    }

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
        String parecelID = parcelId;
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
        exportParam.layerDefs = "3:COMM_PARCEL_ID=" + parcelId + ";6:PARCEL_ID=" + parcelId + ";7:PARCEL_ID=" + parcelId;

        PlotDetails.exportParam=exportParam;
        PlotDetails.emailParam.plotnumber=parecelID;

    }

}
