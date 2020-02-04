package dm.sime.com.kharetati.datas.network;



import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import dm.sime.com.kharetati.utility.constants.AppUrls;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    public static MyApiService getClient(NetworkConnectionInterceptor networkConnectionInterceptor){

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor).connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MyApiService.class);

    }
}
