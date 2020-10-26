package dm.sime.com.kharetati.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Set;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.CustomContextWrapper;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.viewModels.UAEPassRequestModels;

import static dm.sime.com.kharetati.utility.Global.CURRENT_LOCALE;
import static dm.sime.com.kharetati.utility.Global.MYPREFERENCES;
import static dm.sime.com.kharetati.utility.constants.AppConstants.USER_LANGUAGE;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_BOOKMARK;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_WEBVIEW_ACTIVITY;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";
    private WebView webView;
    private Tracker mTracker;
    private SharedPreferences sharedpreferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sharedpreferences = newBase.getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
            String locale = sharedpreferences.getString(USER_LANGUAGE, "defaultStringIfNothingFound");
            if(!locale.equals("defaultStringIfNothingFound"))
                CURRENT_LOCALE =locale;
            else
                CURRENT_LOCALE ="en";
            super.attachBaseContext(CustomContextWrapper.wrap(newBase, CURRENT_LOCALE));
        } else {
            super.attachBaseContext(newBase);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_WEBVIEW_ACTIVITY);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        webView = findViewById(R.id.webViewActivity);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setTextZoom(100);
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

            if (url.startsWith("uaepass://") || url.startsWith("uaepassqa://")) {
                if (!UAEPassRequestModels.isPackageInstalled(getPackageManager())) {
                    view.loadUrl(CURRENT_LOCALE.equals("en")?"https://play.google.com/store/apps/details?id=ae.uaepass.mainapp&hl=EN":"https://play.google.com/store/apps/details?id=ae.uaepass.mainapp&hl=AR");

                }
                else{

                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setPackage(UAEPassRequestModels.UAE_PASS_PACKAGE_ID);


                    startActivity(intent);


                    return true;
                }

            } else if (url.contains("error=access_denied")) {
                onBackPressed();
                Global.isfromWebViewCancel = true;
                // AlertDialogUtil.errorAlertDialog("",getApplicationContext().getResources().getString(R.string.uaeloginfail),getApplicationContext().getResources().getString(R.string.ok),getApplicationContext());


            } else
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

            Uri uri = Uri.parse(url);
            Log.v(TAG, "UAE Pass App: getAuthorizationUrl web page: onPageFinished() " + url);
            if(url.contains("kharetatiuaepass")){
                Log.v(TAG, "UAE Pass App: getAuthorizationUrl web page: onPageFinished() : kharetatiuaepass");
                String protocol = uri.getScheme();
                String server = uri.getAuthority();
                String path = uri.getPath();
                Set<String> args = uri.getQueryParameterNames();
                Global.uae_code = uri.getQueryParameter("code");
                if (Global.uae_code != null) {
                    Log.v(TAG, "UAE Pass App: getAuthorizationUrl web page: onPageFinished() : code found");

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
