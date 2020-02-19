package dm.sime.com.kharetati.datas.repositories;

import dm.sime.com.kharetati.datas.models.CreateUpdateRequestResponse;
import dm.sime.com.kharetati.datas.models.SerializedCreateAndUpdateModel;
import dm.sime.com.kharetati.datas.network.MyApiService;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class PayRepository {
    private MyApiService api;
    public PayRepository(MyApiService apiService) {
        this.api = apiService;
    }

    public Observable<CreateUpdateRequestResponse> createAndUpdateRequest(String url, SerializedCreateAndUpdateModel model) {
        return api.createAndUpdateRequest(url,model);
    }
}
