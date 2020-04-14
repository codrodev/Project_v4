package dm.sime.com.kharetati.datas.repositories;

import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.MakaniToDLTMResponse;
import dm.sime.com.kharetati.datas.models.RetrieveProfileDocsResponse;
import dm.sime.com.kharetati.datas.models.SerializeGetAppRequestModel;
import dm.sime.com.kharetati.datas.models.SerializedModel;
import dm.sime.com.kharetati.datas.network.MyApiService;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class ParentSitePlanRepository {
    private MyApiService api;

    public ParentSitePlanRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<RetrieveProfileDocsResponse> retrieveProfileDocs(String url, SerializedModel model) {
        return api.retrieveProfileDocs(url,model);
    }

    /*public Observable<MakaniToDLTMResponse> getMakaniToDLTM(HTTPRequestBody.MakaniBody makaniBody) {
        return api.getMakaniToDLTM(makaniBody);
    }*/

    public Observable<MakaniToDLTMResponse> getMakaniToDLTM(String url, SerializeGetAppRequestModel model) {
        return api.getMakaniToDLTM(url,model);
    }
}
