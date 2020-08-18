package dm.sime.com.kharetati.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.navigators.FragmentNavigator;
import dm.sime.com.kharetati.view.viewModels.LoginViewModel;
import dm.sime.com.kharetati.view.viewModels.ParentSiteplanViewModel;
import dm.sime.com.kharetati.view.viewModels.PayViewModel;

import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_BOOKMARK;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_WEBVIEW;

public class  WebViewFragment extends Fragment {

    private static String URL = "url";
    private static String APP_NAME = "app_name";
    WebView webView;
    TextView txtUsername, txtWelcome;
    ImageView imgBack;
    private static String launchUrl, appName;
    FragmentNavigator frNavigator;
    private WebSettings webSettings;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private Tracker mTracker;

    public static WebViewFragment newInstance(String url, String appName){
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        args.putString(APP_NAME, appName);
        fragment.setArguments(args);
        launchUrl= url;
        return fragment;
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
        if (getArguments() != null) {
            launchUrl = getArguments().getString(URL);
            appName = getArguments().getString(APP_NAME);
        }



    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.current_fragment_id = FragmentTAGS.FR_WEBVIEW;
        ParentSiteplanFragment.currentIndex = getActivity().getSharedPreferences(ParentSiteplanFragment.POSITION,Context.MODE_PRIVATE).getInt("stepperPosition",0);
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_WEBVIEW);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        //txtUsername = view.findViewById(R.id.txtUsername);
        //txtWelcome = view.findViewById(R.id.txtWelcome);
        imgBack = view.findViewById(R.id.imgBack);
        imgBack.setRotationY(Global.CURRENT_LOCALE.equals("en")?0:180);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //((MainActivity)getActivity()).setScreenName(Global.isUserLoggedIn?Global.getUser(getActivity()).getFullname(): LoginViewModel.guestName);

        /*if(appName != null && appName != "") {
            txtWelcome.setText(appName);
        } else {
            txtWelcome.setText("WELCOME");
        }*/
        webView = view.findViewById(R.id.webView);
        webSettings = webView.getSettings();
        //Global.fontSize = getActivity().getResources().getDimension(R.dimen.txtSize);
        webSettings.setDefaultFontSize((int)Global.fontSize);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccess(true);
        WebView.setWebContentsDebuggingEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webSettings.setTextZoom(100);
        /*String newUA= "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        webView.getSettings().setUserAgentString(newUA);*/
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient(getActivity()) );
        webView.loadUrl(launchUrl);
       /* manageAppBottomBAtr(true);
        manageAppBar(true);
        ((MainActivity)getActivity()).setScreenName(appName!=null?appName:getActivity().getResources().getString(R.string.title_welcome));*/
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().onBackPressed();
            }
        });

        PayViewModel.hm = new ArrayList();
        PayFragment.paymentType = "";
        Global.paymentUrl = "";
        ParentSiteplanViewModel.initializeDocuments();
        setRetainInstance(true);

        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else if(Build.VERSION.SDK_INT >=11 && Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (webView.canGoBack())
                        webView.goBack();
                    return true;

                }
                return false;
            }

        });


        return view;
    }

    public void manageAppBottomBAtr(boolean status){
        frNavigator = (FragmentNavigator) getActivity();
        frNavigator.manageBottomBar(status);
    }

    public void manageAppBar(boolean status){
        frNavigator = (FragmentNavigator) getActivity();
        frNavigator.manageActionBar(status);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            Uri[] results = null;

            // Check that the response is a good one
            if (resultCode == getActivity().RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;

        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            if (requestCode == FILECHOOSER_RESULTCODE) {

                if (null == this.mUploadMessage) {
                    return;

                }

                Uri result = null;

                try {
                    if (resultCode != getActivity().RESULT_OK) {

                        result = null;

                    } else {

                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {

                }

                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;

            }
        }

        return;
    }

    @Override
    public void onResume() {
        super.onResume();
        manageAppBottomBAtr(true);
        manageAppBar(true);
        ((MainActivity)getActivity()).setScreenName(appName!=null?appName:getActivity().getResources().getString(R.string.title_welcome));
        MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(),appName!=null?appName:"WEBVIEW SCREEN" , null /* class override */);
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }

        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
            // Double check that we don't have any existing callbacks
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePath;

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                } catch (IOException ex) {
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }

            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");

            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[0];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

            startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

            return true;
        }

    }

    public class MyWebViewClient extends android.webkit.WebViewClient{

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            AlertDialogUtil.showProgressBar(getActivity(),true);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.contains("inappshare://")){
                if (!Global.isConnected(getActivity())) {
                    if (Global.appMsg != null)
                        AlertDialogUtil.errorAlertDialog(getActivity().getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getActivity().getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getActivity().getResources().getString(R.string.lbl_warning), getActivity().getResources().getString(R.string.internet_connection_problem1), getActivity().getResources().getString(R.string.ok), getActivity());
                }
                else{

                    String completeString ="";

                    Uri uri = Uri.parse(url);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT,uri.getQueryParameter("subject") );

                    String urlstring = uri.getQueryParameter("url");
                    if(urlstring.contains("enc=")){
                        // String shareUrl = urlstring.substring(urlstring.)
                        String startString  = urlstring.substring(0,urlstring.lastIndexOf("enc=")+4);
                        String  encstring =urlstring.substring(urlstring.lastIndexOf("enc=")+4);




                        completeString = startString+encstring.replace("+","%2B").replace("/","%2F").replace(" ","+").replace("=","%3D");
                    }
                    sendIntent.putExtra(Intent.EXTRA_TEXT,completeString);
                    sendIntent.setType("text/html");
                    startActivity(Intent.createChooser(sendIntent, "Share with"));

                }
            }
            else
                view.loadUrl(url);

            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
            AlertDialogUtil.showProgressBar(getActivity(),false);
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            AlertDialogUtil.showProgressBar(getActivity(),false);
            Global.hideSoftKeyboard(getActivity());
        }

    }


}
