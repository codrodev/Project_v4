package dm.sime.com.kharetati.view.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.navigators.FragmentNavigator;
import dm.sime.com.kharetati.view.viewModels.LoginViewModel;
import dm.sime.com.kharetati.view.viewModels.ParentSiteplanViewModel;
import dm.sime.com.kharetati.view.viewModels.PayViewModel;

public class WebViewFragment extends Fragment {

    private static String URL = "url";
    private static String APP_NAME = "app_name";
    WebView webView;
    TextView txtUsername, txtWelcome;
    ImageView imgBack;
    private static String launchUrl, appName;
    FragmentNavigator frNavigator;

    public static WebViewFragment newInstance(String url, String appName){
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        args.putString(APP_NAME, appName);
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
        webView = view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(launchUrl);
        manageAppBottomBAtr(false);
        manageAppBar(false);
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
        }

    }


}
