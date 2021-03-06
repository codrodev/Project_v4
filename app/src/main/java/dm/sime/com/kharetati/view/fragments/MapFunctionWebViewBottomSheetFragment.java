package dm.sime.com.kharetati.view.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;

import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_BOTTOMSHEET;

public class MapFunctionWebViewBottomSheetFragment extends BottomSheetDialogFragment {


    private static String URL = "url";
    WebView webView;
    private static String launchUrl;
    private View mRootView;
    private Tracker mTracker;

    public MapFunctionWebViewBottomSheetFragment(String url) {
        Bundle args = new Bundle();
        args.putString(URL, url);
        launchUrl= url;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            launchUrl = getArguments().getString(URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        webView = view.findViewById(R.id.webView);
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FragmentTAGS.FR_BOTTOMSHEET_WEBVIEW);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MapFunctionWebViewBottomSheetFragment.MyWebViewClient());
        webView.loadUrl(launchUrl);
        return view;
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
