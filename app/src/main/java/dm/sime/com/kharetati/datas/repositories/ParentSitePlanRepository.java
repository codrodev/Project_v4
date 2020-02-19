package dm.sime.com.kharetati.datas.repositories;

import dm.sime.com.kharetati.datas.models.RetrieveProfileDocsResponse;
import dm.sime.com.kharetati.datas.models.SerializedModel;
import dm.sime.com.kharetati.datas.network.MyApiService;
import io.reactivex.Observable;

public class ParentSitePlanRepository {
    private MyApiService api;

    public ParentSitePlanRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<RetrieveProfileDocsResponse> retrieveProfileDocs(String url, SerializedModel model) {
        return api.retrieveProfileDocs(url,model);
    }
}
