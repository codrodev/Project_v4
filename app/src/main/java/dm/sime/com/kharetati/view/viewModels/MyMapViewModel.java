package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.MakaniToDLTMResponse;
import dm.sime.com.kharetati.datas.models.MyMapResults;
import dm.sime.com.kharetati.datas.models.RetrieveMyMapResponse;
import dm.sime.com.kharetati.datas.network.MyApiService;
import dm.sime.com.kharetati.datas.repositories.MyMapRepository;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.view.adapters.MyMapAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MyMapViewModel extends ViewModel {

    MyMapAdapter adapter;
    RetrieveMyMapResponse model;
    MutableLiveData<List<MyMapResults>> mutableMyMap = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MyApiService apiService;
    private MyMapRepository repository;
    private KharetatiApp kharetatiApp;
    private Activity activity;



    public MyMapViewModel(Activity context, MyMapRepository repository){
        this.activity = context;
        this.repository = repository;
    }

    public void initializeMyMapViewModel(Context context){
        model = new RetrieveMyMapResponse();
        mutableMyMap = new MutableLiveData<>();
        mutableMyMap.setValue(model.getMyMapList());
        adapter = new MyMapAdapter(R.layout.adapter_mymap, this, context);
    }

    public MutableLiveData<List<MyMapResults>> getMutableMyMap(){
        return mutableMyMap;
    }

    public void getMyMapList(){
        //return mutableInAppNotifications.getValue();
    }

    public MyMapResults getCurrentMyMap(int position){
        if (mutableMyMap.getValue() != null ) {
            return mutableMyMap.getValue().get(position);
        }
        return null;
    }

    public void  setMyMapAdapter(List<MyMapResults> lstMyMap) {
        this.adapter.setMyMap(lstMyMap);
        this.adapter.notifyDataSetChanged();
    }

    public MyMapAdapter getMyMapAdapter() {
        return adapter;
    }


    public void getAllSitePlans() throws JSONException {

        String url = AppUrls.RETRIEVE_MY_MAPS;
        JSONObject body = new JSONObject();
        body.put("token",Global.site_plan_token);
        body.put("locale",Global.CURRENT_LOCALE);
        body.put("my_id",Global.loginDetails.username);


        //HTTPRequestBody.SitePlanBody body = new HTTPRequestBody.SitePlanBody();

        kharetatiApp = KharetatiApp.create(activity);

        Disposable disposable = repository.getAllSitePlans(url,body)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RetrieveMyMapResponse>() {
                    @Override public void accept(RetrieveMyMapResponse retrieveMyMapResponse) throws Exception {
                        getSitePlans(retrieveMyMapResponse);

                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        //homeNavigator.onFailure("Unable to connect the remote server");
                    }
                });

        compositeDisposable.add(disposable);

    }

    private void getSitePlans(RetrieveMyMapResponse retrieveMyMapResponse) {

    }
}
