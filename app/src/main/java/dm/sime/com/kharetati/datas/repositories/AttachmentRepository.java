package dm.sime.com.kharetati.datas.repositories;

import dm.sime.com.kharetati.datas.models.RetrieveDocStreamResponse;
import dm.sime.com.kharetati.datas.models.SerializedModel;
import dm.sime.com.kharetati.datas.network.MyApiService;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class AttachmentRepository {

    private MyApiService api;

    public AttachmentRepository(MyApiService apiService){
        this.api = apiService;
    }

    public Observable<RetrieveDocStreamResponse> retrieveDoc(String url, SerializedModel model) {
        return api.retrieveDoc(url,model);
    }
}
