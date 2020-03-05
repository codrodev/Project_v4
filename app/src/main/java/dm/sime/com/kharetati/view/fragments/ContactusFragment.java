package dm.sime.com.kharetati.view.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISSublayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.security.Credential;
import com.esri.arcgisruntime.security.UserCredential;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentContactusBinding;
import dm.sime.com.kharetati.databinding.FragmentHappinessBinding;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.AttachmentRepository;
import dm.sime.com.kharetati.datas.repositories.ContactusRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppConstants;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.navigators.ContactusNavigator;
import dm.sime.com.kharetati.view.viewModels.ContactusViewModel;
import dm.sime.com.kharetati.view.viewModels.HappinessViewModel;
import dm.sime.com.kharetati.view.viewmodelfactories.AttachmentViewModelFactory;
import dm.sime.com.kharetati.view.viewmodelfactories.ContactusViewModelFactory;

import static dm.sime.com.kharetati.utility.Global.CURRENT_LOCALE;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_WEBVIEW;

public class ContactusFragment extends Fragment implements ContactusNavigator {
    FragmentContactusBinding binding;
    ContactusViewModel model;
    private View mRootView;
    private static final String DM_PHONE_NUMBER = "800900";
    private static final String DM_EMAIL = "info@dm.gov.ae";
    private static final String DM_WEB_SITE = "http://www.dm.gov.ae";
    private static final String DM_FB_EN = "https://www.facebook.com/DubaiMunicipality";
    private static final String DM_FB_AR = "https://www.facebook.com/DubaiMunicipality";
    private static String DM_TWITTER_EN = "";
    private static String DM_TWITTER_AR = "";
    private static final String DM_INSTAGRAM_EN = "https://www.instagram.com/explore/tags/kharetati/?hl=en";
    private static final String DM_INSTAGRAM_AR = "https://www.instagram.com/explore/tags/خريطتي/?hl=ar/";
    private static final String DM_YOUTUBE_EN = "https://www.youtube.com/results?search_query=kharetati";
    private static final String DM_YOUTUBE_AR = "https://www.youtube.com/results?search_query=خريطتي";
    private ArcGISMapImageLayer dynamicLayer;
    public static ContactusViewModel contactUsVM;
    ContactusViewModelFactory factory;
    private ContactusRepository repository;
    private MapView mMapView;

    public static ContactusFragment newInstance(){
        ContactusFragment fragment = new ContactusFragment();
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
            repository = new ContactusRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        factory = new ContactusViewModelFactory(getActivity(),repository);
        model = ViewModelProviders.of(this,factory).get(ContactusViewModel.class);
        contactUsVM =model;
        model.contactusNavigator =this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.current_fragment_id = FragmentTAGS.FR_CONTACT_US;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contactus, container, false);
        binding.setFragmentContactusVM(model);
        mRootView = binding.getRoot();
        initializePage();
        setRetainInstance(true);
        if (!Global.isConnected(getActivity())) {

            if(Global.appMsg!=null)
                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
            else
                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
        }
        else
            initMap(mRootView);
        return binding.getRoot();
    }

    private void initializePage() {
        binding.TVDMPhoneNumber.setText(DM_PHONE_NUMBER + "");
        binding.TVDMEmail.setText(DM_EMAIL);
        binding.TVDMWebsite.setText(DM_WEB_SITE + "");
       binding.LLPhoneContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Global.isConnected(getActivity())) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else
                    call();
            }
        });

        binding.LLDMEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Global.isConnected(getActivity())) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else
                    sendEmail(DM_EMAIL);
            }
        });

        binding.LLDMWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Global.isConnected(getActivity())) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else
                    openURL(DM_WEB_SITE);
            }
        });

        binding.LLDMLiveChat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!Global.isConnected(getActivity())) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else
                    AlertDialogUtil.chatAlert(getString(R.string.please_enter_phone),getResources().getString(R.string.ok),getResources().getString(R.string.cancel),getActivity());
            }
        });

        binding.LLDMFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Global.isConnected(getActivity())) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else
                    ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_FEEDBACK,true,null);
            }
        });

        binding.imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Global.isConnected(getActivity())) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else{
                    if (CURRENT_LOCALE=="ar") {
                        openURL(DM_FB_AR);
                    }else{
                        openURL(DM_FB_EN);
                    }
                }
            }
        });
        binding.imgTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Global.isConnected(getActivity())) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else{
                    if (CURRENT_LOCALE=="ar") {
                        DM_TWITTER_AR ="https://twitter.com/search?q=%23%D8%AE%D8%B1%D9%8A%D8%B7%D8%AA%D9%8A&src=typeahead_click#kharetati'&src=typd";

                        openURL(DM_TWITTER_AR);
                    }else{
                        DM_TWITTER_EN ="https://twitter.com/search?q=%23kharetati&src=typed_query#kharetati'&src=typd";
                        openURL(DM_TWITTER_EN);
                    }}
            }
        });
        binding.imgInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Global.isConnected(getActivity())) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else{

                    if (CURRENT_LOCALE=="ar") {
                        openURL(DM_INSTAGRAM_AR);
                    }else{
                        openURL(DM_INSTAGRAM_EN);
                    }
                }}
        });
        binding.imgYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Global.isConnected(getActivity())) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else{
                    if (CURRENT_LOCALE=="ar") {
                        openURL(DM_YOUTUBE_AR);
                    }else{
                        openURL(DM_YOUTUBE_EN);
                    }}
            }
        });

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Dubai-Regular.ttf");



    }
    private void openURL(String link) {
        ArrayList al = new ArrayList();
        al.add(link);
        ((MainActivity)getActivity()).loadFragment(FR_WEBVIEW,true,al);
    }

    public void call() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, AppConstants.REQUEST_PHONE_PERMISSION);
        }
        else
        {
            AlertDialogUtil.callAlert(getResources().getString(R.string.you_are_about_to_call) + " " + DM_PHONE_NUMBER,getResources().getString(R.string.ok),getString(R.string.cancel),getActivity());
        }

    }

    public void initMap(View v){
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud3984007683,none,GB2PMD17J0YJ2J7EZ071");
        Credential userCredentials = new UserCredential(AppUrls.GIS_LAYER_USERNAME,AppUrls.GIS_LAYER_PASSWORD);
    /*userCredentials.setUserAccount(Constant.GIS_LAYER_USERNAME,Constant.GIS_LAYER_PASSWORD);
    userCredentials.setTokenServiceUrl(Constant.GIS_LAYER_TOKEN_URL);*/
        mMapView = (MapView)binding.getRoot().findViewById(R.id.mapContactUs);
        ArcGISMap map = new ArcGISMap();
        mMapView.setMap(map);
        int[] visible={5};
        dynamicLayer  = new ArcGISMapImageLayer(AppUrls.GIS_LAYER_URL);
        dynamicLayer.setCredential(userCredentials);
        dynamicLayer.addDoneLoadingListener(() -> {
            if (dynamicLayer.getLoadStatus() == LoadStatus.LOADED) {
                List<ArcGISSublayer> layers=dynamicLayer.getSublayers();
                for(int i=0;i<layers.size();i++){
                    ArcGISSublayer layer=layers.get(i);
                    if(layer.getId()==5||layer.getId()==2)
                        layer.setVisible(true);
                    else
                        layer.setVisible(false);
                }
            }
        });
        map.getOperationalLayers().add(dynamicLayer);
        mMapView.setMap(map);
        mMapView.setAttributionTextVisible(false);
        //mMapView.getGraphicsOverlays().add(map);


        SpatialReference mSR = SpatialReference.create(3997);
        //Point p1 = GeometryEngine.project(55.31,25.263,  mSR);
    /*Point p1 = new Point(55.31,25.263,  mSR);
    //Point p2 = GeometryEngine.project(55.313,25.266 , mSR);
    Point p2 =new Point(55.313,25.266 , mSR);
    Envelope initExtent = new Envelope(p1.getX(), p1.getY(), p2.getX(), p2.getY(), mSR);
    Viewpoint vp = new Viewpoint(initExtent);
    mMapView.setViewpoint(vp);
    mMapView.setViewpoint(vp);*/
        mMapView.setViewpointGeometryAsync(new Point(497818.691, 2795353.692),100);
        //Resize image
        Drawable dr = getResources().getDrawable(R.drawable.makani);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        //Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 32, 32, true));

        PictureMarkerSymbol symbol = new PictureMarkerSymbol(new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 96, 96, true)));
        final Point graphicPoint = new Point(497818.691, 2795353.692);

        Graphic graphic = new Graphic(graphicPoint,symbol);

        //Add makani Icon to the map
        GraphicsOverlay graphicsLayer = new GraphicsOverlay();
        // mMapView.getGraphicsOverlays().add(graphicsLayer);
        graphicsLayer.getGraphics().add(graphic);
        mMapView.getGraphicsOverlays().add(graphicsLayer);
        //mMapView.setViewpointGeometryAsync(new Point(497818.691, 2795353.692),100);
        mMapView.setViewpointCenterAsync(graphicPoint);
        //mMapView.setViewpointScaleAsync(2000);
        mMapView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v){
                final Timer timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //mMapView.setViewpointCenterAsync(graphicPoint);
                        mMapView.setViewpointScaleAsync(2000);
                        timer.cancel();
                    }
                }, 1000*1);
            }

            @Override
            public void onViewDetachedFromWindow(View v){
                //mMapView.setVisibility(View.GONE);
            }


        });


        mMapView.setOnTouchListener(new MapView.OnTouchListener (){

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return false;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onMultiPointerTap(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onDoubleTouchDrag(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onRotate(MotionEvent motionEvent, double v) {
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if(mMapView!=null){
                    return Global.openMakani("1190353",getActivity());
                }
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mMapView!=null) {
                    //mMapView.setOnPanListener(null);
                    //mMapView.addMapScaleChangedListener(null);
                    if(event.getAction()==MotionEvent.ACTION_DOWN){
                        return Global.openMakani("1190353",getActivity());
                    }
                }
                return false;
            }
        });





    }

    private void sendEmail(String email) {
        try {
            // send msg
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", email, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.app_name)));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.toString());
        }
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
}
