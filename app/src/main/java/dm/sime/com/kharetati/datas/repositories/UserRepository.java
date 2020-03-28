package dm.sime.com.kharetati.datas.repositories;



import org.json.JSONObject;

import dm.sime.com.kharetati.datas.models.AccessTokenResponse;
import dm.sime.com.kharetati.datas.models.GetConfigResponse;
import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.KharetatiUser;
import dm.sime.com.kharetati.datas.models.SerializedUAEAccessTokenAPIModelResponse;
import dm.sime.com.kharetati.datas.models.SerializedUAEAccessTokenRequestAPIModel;
import dm.sime.com.kharetati.datas.models.SessionResponse;
import dm.sime.com.kharetati.datas.models.SessionUaePassResponse;
import dm.sime.com.kharetati.datas.models.UAEAccessTokenResponse;
import dm.sime.com.kharetati.datas.models.UaePassConfig;
import dm.sime.com.kharetati.datas.models.User;
import dm.sime.com.kharetati.datas.models.UserRegistration;
import dm.sime.com.kharetati.datas.network.MyApiService;
import io.reactivex.Observable;

public class UserRepository {

    private MyApiService api;

    public UserRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<AccessTokenResponse> getAccessToken(String username, String password, boolean isUAE, String uaeToken){
        return api.getAccessToken(username,password, isUAE, uaeToken);
    }

    public Observable<User> getUserDetails(String accessToken){
        return api.getUserDetails(accessToken);
    }

    public Observable<SessionResponse> getSession(String accessToken){
        return api.getSession(accessToken);
    }

    public Observable<SessionUaePassResponse> getSessionUAEPass(String accessToken){
        return api.getSessionUAEPass(accessToken);
    }

    public Observable<UAEAccessTokenResponse> getUAEAccessToken(String accessToken){
        return api.getUAEAccessToken(accessToken);
    }

    public Observable<UserRegistration> registerLoggedUser(HTTPRequestBody map){
        return api.registerLoggedUser(map);
    }

    public Observable<KharetatiUser> guestLogin(HTTPRequestBody.GuestBody guestBody){
        return api.guestLogin(guestBody);
    }

    public Observable<UaePassConfig> uaePassConfig(String url){
        return api.uaePassConfig(url);
    }

    public Observable<GetConfigResponse> getConfig(String url){
        return api.getConfig(url);
    }

    public Observable<SerializedUAEAccessTokenAPIModelResponse> uaeAccessToken(SerializedUAEAccessTokenRequestAPIModel requestObject){
        return api.uaeAccessToken(requestObject);
    }

}
