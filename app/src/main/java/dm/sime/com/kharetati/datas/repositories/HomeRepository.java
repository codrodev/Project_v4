package dm.sime.com.kharetati.datas.repositories;

import dm.sime.com.kharetati.datas.models.GetAreaNamesResponse;
import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.KharetatiUser;
import dm.sime.com.kharetati.datas.models.MakaniToDLTMResponse;
import dm.sime.com.kharetati.datas.models.ParcelResponse;
import dm.sime.com.kharetati.datas.network.MyApiService;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class HomeRepository {
    private MyApiService api;

    public HomeRepository(MyApiService apiService){
        this.api = apiService;
    }

    public Observable<MakaniToDLTMResponse> getMakaniToDLTM(HTTPRequestBody.MakaniBody makaniBody){
        return api.getMakaniToDLTM(makaniBody);
    }

    public Observable<ParcelResponse> getParcelID(HTTPRequestBody.ParcelBody parcelBody) {
        return api.getParcelID(parcelBody);
    }

    public Observable<GetAreaNamesResponse> getAreaNames(HTTPRequestBody.AreaBody areaBody) {
        return api.getAreaNames(areaBody);
    }
}
