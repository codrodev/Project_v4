package dm.sime.com.kharetati.datas.repositories;

import dm.sime.com.kharetati.datas.models.RetrieveDocStreamResponse;
import dm.sime.com.kharetati.datas.models.RetrieveMyMapResponse;
import dm.sime.com.kharetati.datas.models.SerializedModel;
import dm.sime.com.kharetati.datas.network.MyApiService;
import io.reactivex.Observable;

public class MyMapRepository {

    private MyApiService api;

    public MyMapRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<RetrieveMyMapResponse> getAllSitePlans(String url, SerializedModel model){
        return api.getAllSitePlans(url ,model);
    }

    public Observable<RetrieveDocStreamResponse> viewSitePlan(String url, SerializedModel model) {
        return api.viewSitePlans(url,model);
    }
}
