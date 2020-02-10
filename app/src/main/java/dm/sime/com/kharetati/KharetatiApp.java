package dm.sime.com.kharetati;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.MyApiService;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;


public class KharetatiApp extends Application {

    private MyApiService apiService;
    private NetworkConnectionInterceptor networkConnectionInterceptor;
    private Scheduler scheduler;



    private static KharetatiApp kharetatiApp = null;

    public static synchronized KharetatiApp getInstance() {
        return kharetatiApp;
    }

    private static KharetatiApp get(Context context) {
        return (KharetatiApp) context.getApplicationContext();
    }

    public static KharetatiApp create(Context context) {
        return KharetatiApp.get(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        kharetatiApp = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        networkConnectionInterceptor = new NetworkConnectionInterceptor(kharetatiApp);
    }
    public MyApiService getApiService() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if(apiService == null){
            apiService = ApiFactory.getClient(networkConnectionInterceptor);
        }

        return apiService;
    }
    public Scheduler subscribeScheduler() {
        if (scheduler == null) {
            scheduler = Schedulers.io();
        }

        return scheduler;
    }
}
