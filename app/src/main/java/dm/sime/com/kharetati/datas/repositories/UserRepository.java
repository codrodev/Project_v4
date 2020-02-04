package dm.sime.com.kharetati.datas.repositories;



import dm.sime.com.kharetati.datas.models.AccessTokenResponse;
import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.KharetatiUser;
import dm.sime.com.kharetati.datas.models.SessionResponse;
import dm.sime.com.kharetati.datas.models.User;
import dm.sime.com.kharetati.datas.models.UserRegistration;
import dm.sime.com.kharetati.datas.network.MyApiService;
import io.reactivex.Observable;

public class UserRepository {

    private MyApiService api;

    public UserRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<AccessTokenResponse> getAccessToken(String username, String password){
        return api.getAccessToken(username,password);
    }

    public Observable<User> getUserDetails(String accessToken){
        return api.getUserDetails(accessToken);
    }

    public Observable<SessionResponse> getSession(String accessToken){
        return api.getSession(accessToken);
    }

    public Observable<UserRegistration> registerLoggedUser(HTTPRequestBody map){
        return api.registerLoggedUser(map);
    }

    public Observable<KharetatiUser> guestLogin(HTTPRequestBody.GuestBody guestBody){
        return api.guestLogin(guestBody);
    }


}
