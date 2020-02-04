package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.MakaniToDLTMResponse;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.MyApiService;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.HomeRepository;
import dm.sime.com.kharetati.datas.repositories.MapRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.view.navigators.FragmentNavigator;
import dm.sime.com.kharetati.view.navigators.MapNavigator;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapViewModel extends ViewModel {

    FragmentNavigator frNavigator;
    private Activity activity;
    public MapNavigator mapNavigator;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    int lastIndex = 0;
    private MyApiService apiService;
    private MapRepository repository;
    private KharetatiApp kharetatiApp;

    public MapViewModel(){

    }
    public MapViewModel(Activity context, MapRepository repository){
        this.activity = context;
        this.repository = repository;
    }

    public void manageAppBar(Context ctx, boolean status){
        frNavigator = (FragmentNavigator) ctx;
        frNavigator.manageActionBar(status);
    }

    public void manageAppBottomBAtr(Context ctx, boolean status){
        frNavigator = (FragmentNavigator) ctx;
        frNavigator.manageBottomBar(status);
    }

    public void navigate(Context ctx, String fragmentTag){
        frNavigator = (FragmentNavigator) ctx;
        frNavigator.fragmentNavigator(fragmentTag, true, null);
    }

    public void saveAsBookMark(Boolean isSave) {

        mapNavigator.onStarted();
        apiService = ApiFactory.getClient(new NetworkConnectionInterceptor(KharetatiApp.getInstance().getApplicationContext()));
        repository = new MapRepository(apiService);

        kharetatiApp = KharetatiApp.create(activity);




        HTTPRequestBody.BookMarkBody bookMarkBody = new HTTPRequestBody.BookMarkBody();


        Disposable disposable = repository.saveAsBookMark(bookMarkBody)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JSONObject>() {
                    @Override
                    public void accept(JSONObject response) throws Exception {

                            try {
                   if (response != null ) {
                       mapNavigator.onSuccess();
                       if (!response.getBoolean("isError")) {
                           if(isSave){
                               if(response.getBoolean("isExisting"))
                                   AlertDialogUtil.errorAlertDialog(activity.getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getPlotAvailableFavEn(): Global.appMsg.getPlotAvailableFavAr(), activity.getResources().getString(R.string.ok), activity);
                               else
                                   AlertDialogUtil.errorAlertDialog(activity.getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getPlotAddedFavEn(): Global.appMsg.getPlotAddedFavAr(), activity.getResources().getString(R.string.ok), activity);

                           }
                       } else {
                           if(isSave) {
                               AlertDialogUtil.errorAlertDialog(activity.getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getErrorFetchingDataEn(): Global.appMsg.getErrorFetchingDataAr(), activity.getResources().getString(R.string.ok), activity);

                           }
                       }
                   }
               } catch (Exception e) {
                   mapNavigator.onFailure(e.getMessage());
                   e.printStackTrace();
               }


                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        mapNavigator.onFailure("Unable to connect the remote server");
                    }
                });

        compositeDisposable.add(disposable);


    }
}