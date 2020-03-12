package dm.sime.com.kharetati.datas.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import dm.sime.com.kharetati.utility.Exceptions;
import dm.sime.com.kharetati.utility.Global;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkConnectionInterceptor implements Interceptor {

    private Context applicationContext;

    public NetworkConnectionInterceptor(Context context){
        applicationContext = context.getApplicationContext();
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        if(!Global.isConnected(applicationContext))
            throw new Exceptions.NoInternetException("Make sure you have active data connection");
        if (Global.accessToken!=null){

            Request.Builder requestBuilder = chain.request().newBuilder();
            requestBuilder.header("Content-Type", "application/json");
            requestBuilder.header("token", Global.accessToken);

            return chain.proceed(requestBuilder.build());
        }
        else if (Global.accessToken==null||Global.accessToken.isEmpty()){
            if(!Global.isLoginActivity)
            Global.logout(applicationContext);
        }
        return chain.proceed(chain.request());

    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null){
            return networkInfo.isConnected()||networkInfo.isConnectedOrConnecting();
        }
        return false;
    }
}
