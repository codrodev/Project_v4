package dm.sime.com.kharetati.datas.repositories;

import org.json.JSONObject;

import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.RetrieveMyMapResponse;
import dm.sime.com.kharetati.datas.models.SerializeMyMapModel;
import dm.sime.com.kharetati.datas.network.MyApiService;
import io.reactivex.Observable;

public class MyMapRepository {

    private MyApiService api;

    public MyMapRepository(MyApiService apiService){
        this.api = apiService;
    }

    /*public Observable<RetrieveMyMapResponse> getAllSitePlans(String url, JSONObject sitePlanBody){
        return api.getAllSitePlans(url ,sitePlanBody);
    }*/

    public Observable<RetrieveMyMapResponse> getAllSitePlans(String url, SerializeMyMapModel model){
        return api.getAllSitePlans(url ,model);
    }
}
