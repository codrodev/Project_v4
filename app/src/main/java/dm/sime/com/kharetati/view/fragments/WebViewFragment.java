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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.navigators.FragmentNavigator;

public class WebViewFragment extends Fragment {

    private static String URL = "url";
    WebView webView;
    private static String launchUrl;
    FragmentNavigator frNavigator;

    public static WebViewFragment newInstance(String url){
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        launchUrl= url;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            launchUrl = getArguments().getString(URL);
        }



    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        Global.current_fragment_id = FragmentTAGS.FR_WEBVIEW;

        webView = view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(launchUrl);
        manageAppBottomBAtr(false);
        if(!webView.canGoBack()){
            ((MainActivity)getActivity()).onWebViewBack();
        }
        return view;
    }

    public void manageAppBottomBAtr(boolean status){
        frNavigator = (FragmentNavigator) getActivity();
        frNavigator.manageBottomBar(status);
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
