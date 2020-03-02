package dm.sime.com.kharetati.datas.repositories;

import org.json.JSONObject;

import dm.sime.com.kharetati.datas.models.BaseResponseModel;
import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.MakaniToDLTMResponse;
import dm.sime.com.kharetati.datas.models.SerializableParcelDetails;
import dm.sime.com.kharetati.datas.models.SerializableSaveBookMarks;
import dm.sime.com.kharetati.datas.models.SerializeBookMarksModel;
import dm.sime.com.kharetati.datas.models.SerializeGetAppRequestModel;
import dm.sime.com.kharetati.datas.models.SerializedValidateParcelModel;
import dm.sime.com.kharetati.datas.network.MyApiService;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;

public class MapRepository {

    private MyApiService api;

    public MapRepository(MyApiService apiService){
        this.api = apiService;
    }

    public Observable<SerializableSaveBookMarks> saveAsBookMark(SerializeBookMarksModel bookMarkBody){
        return api.saveAsBookMark(bookMarkBody);
    }

    public Observable<BaseResponseModel> validateParcel(String url, SerializedValidateParcelModel validateModel){
        return api.validateParcel(url, validateModel);
    }

    public Observable<SerializableParcelDetails> getParcelDetails(String url, SerializeGetAppRequestModel model) {
        return api.getParcelDetails(url,model);
    }
}
