package dm.sime.com.kharetati.view.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.navigators.FragmentNavigator;
import dm.sime.com.kharetati.view.viewModels.ParentSiteplanViewModel;
import dm.sime.com.kharetati.view.viewModels.PayViewModel;

import static dm.sime.com.kharetati.utility.constants.AppConstants.PARCEL_NUMBER;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_DELIVERY;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_WEBVIEW_PAYMENT;

public class PaymentFragment extends Fragment {
    private static String URL = "url";
    private static String APP_NAME = "app_name";
    public static boolean isFromPaymentFagment;
    WebView webView;
    TextView txtUsername, txtWelcome;
    ImageView imgBack;
    private static String launchUrl, appName;
    FragmentNavigator frNavigator;
    private Tracker mTracker;

    public static PaymentFragment newInstance(String url, String appName){
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        args.putString(APP_NAME, appName);
        launchUrl= url;
        isFromPaymentFagment = true;
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
        Global.current_fragment_id = FragmentTAGS.FR_WEBVIEW_PAYMENT;
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        PaymentFragment.isFromPaymentFagment = true;
        //txtUsername = view.findViewById(R.id.txtUsername);
        //txtWelcome = view.findViewById(R.id.txtWelcome);
        imgBack = view.findViewById(R.id.imgBack);
        imgBack.setRotationY(Global.CURRENT_LOCALE.equals("en")?0:180);
        /*txtUsername.setText(Global.isUserLoggedIn?(Global.getUser(getActivity()).getFullname()): LoginViewModel.guestName);
        if(appName != null && appName != "") {
            txtWelcome.setText(appName);
        } else {
            txtWelcome.setText("WELCOME");
        }*/
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_WEBVIEW_PAYMENT);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(),FR_WEBVIEW_PAYMENT , null /* class override */);
        webView = view.findViewById(R.id.webView);
        WebView.setWebContentsDebuggingEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        /*String newUA= "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        webView.getSettings().setUserAgentString(newUA);*/
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(launchUrl);
        //webView.loadUrl("https://smart.gis.gov.ae/kharetativ5/content/resources/paymenttest/payment.html");// for testing
        webView.addJavascriptInterface(new LoadListener(), "HTMLOUT");
        manageAppBottomBAtr(true);
        manageAppBar(true);
        ((MainActivity)getActivity()).setScreenName(getActivity().getResources().getString(R.string.payment));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        PayViewModel.hm = new ArrayList();
        PayFragment.paymentType = "";
        Global.paymentUrl = "";
        //ParentSiteplanViewModel.initializeDocuments();
        setRetainInstance(true);
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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setScreenName(getActivity().getResources().getString(R.string.payment));
    }

    public class MyWebViewClient extends android.webkit.WebViewClient{

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            AlertDialogUtil.showProgressBar(getActivity(),true);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
            AlertDialogUtil.showProgressBar(getActivity(),false);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            AlertDialogUtil.showProgressBar(getActivity(),false);

                    view.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                    RequestDetailsFragment.isFromRequestDetails = true;

        }

    }
    private class LoadListener {
        @JavascriptInterface
        public void processHTML(final String html) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    uploadDocsForSitePlanRequest(html);
                }
            });
        }
    }



    private void uploadDocsForSitePlanRequest(String html) {
        if (html != null && html != "") {
            try {
                //progressDialog = new ProgressDialog(getActivity());
                final Gson gson = new Gson();
                Document doc = Jsoup.parse(html);
                //String paymentStatus = "0";
                Global.paymentStatus = doc.getElementById("txtPaymentStatus").attr("value");
                //Toast.makeText(getActivity(), Global.paymentStatus, Toast.LENGTH_SHORT).show();
                if(Global.paymentStatus.equals("1"))
                    ParentSiteplanFragment.currentIndex = Global.uaePassConfig.hideDeliveryDetails?2:3;


                //paymentStatus = null;

        /*if(paymentStatus != null) {
          Toast.makeText(getActivity(), "uploadDocsForSitePlanRequest(): paymentStatus = " + paymentStatus, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(getActivity(), "uploadDocsForSitePlanRequest(): paymentStatus is null ", Toast.LENGTH_SHORT).show();
        }*/

                if( Global.isUserLoggedIn){
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Payment")
                            .setAction("["+Global.getUser(getActivity()).getUsername() +" ] -"+ PARCEL_NUMBER+"- [ " + PlotDetails.parcelNo +" ]")
                            .build());
                }else{
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Payment")
                            .setAction("Guest - DeviceID = [" +Global.deviceId+ "] -"+ PARCEL_NUMBER+"- [ " + PlotDetails.parcelNo +" ]")
                            .build());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
