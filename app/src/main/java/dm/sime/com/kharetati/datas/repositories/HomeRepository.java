package dm.sime.com.kharetati.datas.repositories;

import dm.sime.com.kharetati.datas.models.AppSessionResponse;
import dm.sime.com.kharetati.datas.models.GetAppResponse;
import dm.sime.com.kharetati.datas.models.GetAreaNamesResponse;
import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.KharetatiUser;
import dm.sime.com.kharetati.datas.models.LookupParameterModel;
import dm.sime.com.kharetati.datas.models.LookupResponseModel;
import dm.sime.com.kharetati.datas.models.MakaniToDLTMResponse;
import dm.sime.com.kharetati.datas.models.ParcelResponse;
import dm.sime.com.kharetati.datas.models.SearchParameterModel;
import dm.sime.com.kharetati.datas.models.SearchResult;
import dm.sime.com.kharetati.datas.models.SerializeGetAppRequestModel;
import dm.sime.com.kharetati.datas.models.WebSearchResult;
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

    public Observable<GetAppResponse> getAppResponse(String url, SerializeGetAppRequestModel model) {
        return api.getApps(url, model);
    }

    public Observable<AppSessionResponse> getSession(String url) {
        return api.getAppSession(url);
    }

    public Observable<SearchResult> getMapBasedSearchResult(String url, SearchParameterModel model) {
        return api.getMapBasedSearchResult(url, model);
    }

    public Observable<WebSearchResult> getWebBasedSearchResult(String url, SearchParameterModel model) {
        return api.getWebBasedSearchResult(url, model);
    }

    public Observable<LookupResponseModel> getLookupResult(String url, LookupParameterModel model) {
        return api.getLookupResult(url, model);
    }
}
