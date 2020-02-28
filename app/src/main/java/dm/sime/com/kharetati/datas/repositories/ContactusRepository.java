package dm.sime.com.kharetati.datas.repositories;

import org.json.JSONObject;

import dm.sime.com.kharetati.datas.models.GeneralResponse;
import dm.sime.com.kharetati.datas.models.SerializableFeedBackModel;
import dm.sime.com.kharetati.datas.network.MyApiService;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class ContactusRepository {
    private MyApiService api;

    public ContactusRepository(MyApiService apiService){
        this.api = apiService;
    }

    public Observable<GeneralResponse> sendFeedBack(SerializableFeedBackModel model) {

        return api.sendFeedBack(model);
    }
}
