package dm.sime.com.kharetati.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import java.util.Set;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.webViewActivity);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());

        if (getIntent().getData() != null) {

            webView.loadUrl(getIntent().getData().toString());

        }
    }
    public class MyWebViewClient extends android.webkit.WebViewClient{

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            AlertDialogUtil.showProgressBar(WebViewActivity.this,true);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.v(TAG, "UAE Pass App: getAuthorizationUrl web page: shouldOverrideUrlLoading() " + url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){

            AlertDialogUtil.showProgressBar(WebViewActivity.this,false);

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            AlertDialogUtil.showProgressBar(WebViewActivity.this,false);
            //Uri uri = Uri.parse("https://smart.gis.gov.ae/kharetatiuaepass?code=5dddaf8e4318f4532572640be058463ced28b84a5e894f0303f9217226cd45ee&state=QR3QGVmyyfgX0HmZ");
            Uri uri = Uri.parse(url);
            Log.v(TAG, "UAE Pass App: getAuthorizationUrl web page: onPageFinished() " + url);
            if(url.contains("kharetatiuaepass")){
                Log.v(TAG, "UAE Pass App: getAuthorizationUrl web page: onPageFinished() : kharetatiuaepass");
                String protocol = uri.getScheme();
                String server = uri.getAuthority();
                String path = uri.getPath();
                Set<String> args = uri.getQueryParameterNames();
                if(uri.getQueryParameter("code") != null){
                    Log.v(TAG, "UAE Pass App: getAuthorizationUrl web page: onPageFinished() : code found");
                    Global.uae_code = uri.getQueryParameter("code");
                    Log.v(TAG, "UAE Pass App: getAuthorizationUrl web page: onPageFinished() : code:" + Global.uae_code);
                    Global.isUAEaccessWeburl = true;
                    finish();
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Global.alertDialog!=null){
            Global.alertDialog.cancel();
            Global.alertDialog =null;
        }
    }
}
