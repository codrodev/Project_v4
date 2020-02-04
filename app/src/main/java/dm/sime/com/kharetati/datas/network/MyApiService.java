package dm.sime.com.kharetati.datas.network;

import org.json.JSONObject;

import dm.sime.com.kharetati.datas.models.AccessTokenResponse;
import dm.sime.com.kharetati.datas.models.GetAreaNamesResponse;
import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.KharetatiUser;
import dm.sime.com.kharetati.datas.models.MakaniToDLTMResponse;
import dm.sime.com.kharetati.datas.models.ParcelResponse;
import dm.sime.com.kharetati.datas.models.RetrieveMyMapResponse;
import dm.sime.com.kharetati.datas.models.SessionResponse;
import dm.sime.com.kharetati.datas.models.User;
import dm.sime.com.kharetati.datas.models.UserRegistration;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppConstants;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MyApiService {


    //to get accessToken
    @FormUrlEncoded
    @POST(AppUrls.MYID_ACCESS_TOKEN_URL)
    Observable<AccessTokenResponse> getAccessToken(@Field("username") String username, @Field("password") String password);

    //register logged in user
    @POST(AppUrls.REGISTER_MYID_USER)
    Observable<UserRegistration> registerLoggedUser(@Body HTTPRequestBody map);

    // to get session
    @POST(AppUrls.MYID_SESSION_ID)
    Observable<SessionResponse> getSession(@Query("accessToken") String accessToken);

    //guest user login
    @POST(AppUrls.REGISTER_GUEST_USER)
    Observable<KharetatiUser> guestLogin(@Body HTTPRequestBody.GuestBody guestBody);

    //to get user details
    @GET
    Observable<User> getUserDetails(@Url String url);

    //to get makani response
    @POST(AppUrls.MYID_MAKANI_TO_DLTM)
    Observable<MakaniToDLTMResponse> getMakaniToDLTM(@Body HTTPRequestBody.MakaniBody makaniBody);

    //to get all Siteplans
    @POST
    Observable<RetrieveMyMapResponse> getAllSitePlans(@Url String url,@Body HTTPRequestBody.SitePlanBody sitePlanBody);

    //to save Book mark
    @POST("Bookmark/addBookmark")
    Observable<JSONObject> saveAsBookMark(@Body HTTPRequestBody.BookMarkBody bookMarkBody);


    // to get parcel Id in land search
    @POST(AppUrls.MYID_PARCEL_ID)
    Observable<ParcelResponse> getParcelID(@Body HTTPRequestBody.ParcelBody parcelBody);

    //to get Area Names In land search
    @POST("KharetatiWebService/getAreaNames")
    Observable<GetAreaNamesResponse> getAreaNames(@Body HTTPRequestBody.AreaBody areaBody);
}