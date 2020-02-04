package dm.sime.com.kharetati.datas.repositories;

import org.json.JSONObject;

import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.MakaniToDLTMResponse;
import dm.sime.com.kharetati.datas.network.MyApiService;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;

public class MapRepository {

    private MyApiService api;

    public MapRepository(MyApiService apiService){
        this.api = apiService;
    }

    public Observable<JSONObject> saveAsBookMark(HTTPRequestBody.BookMarkBody bookMarkBody){
        return api.saveAsBookMark(bookMarkBody);
    }
}
