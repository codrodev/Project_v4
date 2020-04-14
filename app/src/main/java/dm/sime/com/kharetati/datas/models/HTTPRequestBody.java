package dm.sime.com.kharetati.datas.models;

import android.util.Log;

import org.json.JSONObject;

import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.fragments.HomeFragment;

public class HTTPRequestBody {
    private static final String TAG = "WebViewActivity";
    String EmirateID  = (Global.emiratesID!=null)? Global.emiratesID:"";
    String Mobile= (Global.mobile!=null)? Global.mobile:"";
    String Email = (Global.email!=null)? Global.email:"";
    String username =(Global.username!=null)? Global.username:"";
    String password = "";
    String DeviceID = (Global.deviceId!=null)? Global.deviceId:"";
    String UserType = "REGISTERED";
    String FirstName = (Global.FirstName!=null)? Global.FirstName:"";
    String LastName = (Global.LastName!=null)? Global.LastName:"";
    String FullName = (Global.FullName!=null)? Global.FullName:"";
    String Gender = (Global.Gender!=null)? Global.Gender:"";
    String Nationality = (Global.Nationality!=null)? Global.Nationality:"";
    String DeviceType = (Global.DeviceType!=null)? Global.DeviceType:"";

    public HTTPRequestBody(){
        EmirateID  = (Global.emiratesID!=null)? Global.emiratesID:"";
        Mobile= (Global.mobile!=null)? Global.mobile:"";
        Email = (Global.email!=null)? Global.email:"";
        username =(Global.username!=null)? Global.username:"";
        password = "";
        DeviceID = (Global.deviceId!=null)? Global.deviceId:"";
        UserType = "REGISTERED";
        FirstName = (Global.FirstName!=null)? Global.FirstName:"";
        LastName = (Global.LastName!=null)? Global.LastName:"";
        FullName = (Global.FullName!=null)? Global.FullName:"";
        Gender = (Global.Gender!=null)? Global.Gender:"";
        Nationality = (Global.Nationality!=null)? Global.Nationality:"";
        DeviceType = (Global.DeviceType!=null)? Global.DeviceType:"";
    }

    public HTTPRequestBody(SessionUaePassResponse uaeSessionResponse){
        EmirateID  = uaeSessionResponse.getService_response().getUAEPASSDetails().getIdn();
        Log.v(TAG, "UAE Pass App: HTTPRequestBody(): EmirateID:" +uaeSessionResponse.getService_response().getUAEPASSDetails().getIdn() );
        Mobile= uaeSessionResponse.getService_response().getUAEPASSDetails().getMobile();
        Log.v(TAG, "UAE Pass App: HTTPRequestBody(): mobile:" +Mobile );
        Email = uaeSessionResponse.getService_response().getUAEPASSDetails().getEmail();
        Log.v(TAG, "UAE Pass App: HTTPRequestBody(): Email:" +Email );
        username = uaeSessionResponse.getService_response().getUAEPASSDetails().getUuid();
        Log.v(TAG, "UAE Pass App: HTTPRequestBody(): username:" +username);
        password = "";
        DeviceID = (Global.deviceId!=null)? Global.deviceId:"";
        UserType = "UAEID";
        FirstName = Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0 ?uaeSessionResponse.getService_response().getUAEPASSDetails().getFirstnameEN() : uaeSessionResponse.getService_response().getUAEPASSDetails().getFirstnameAR();
        LastName = Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0 ? uaeSessionResponse.getService_response().getUAEPASSDetails().getLastnameEN() : uaeSessionResponse.getService_response().getUAEPASSDetails().getLastnameAR();
        FullName = Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0 ?uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameEN() : uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameAR();
        Gender = uaeSessionResponse.getService_response().getUAEPASSDetails().getGender();
        Nationality = Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0 ? uaeSessionResponse.getService_response().getUAEPASSDetails().getNationalityEN() : uaeSessionResponse.getService_response().getUAEPASSDetails().getNationalityAR();
        DeviceType = (Global.DeviceType!=null)? Global.DeviceType:"";
    }

    public static class GuestBody{
        String Username ="guest";
        String DeviceID =(Global.deviceId!=null)? Global.deviceId:"";
        String DeviceType ="Android";
        String UserType ="GUEST";
    }
    public static class MakaniBody{
        public String MAKANI =(Global.makani!=null)?Global.makani:"";
        String SESSION =(Global.session!=null)?Global.session:"";
        String REMARKS =(Global.getPlatformRemark()!=null)? Global.getPlatformRemark():"Android";

    }

    public static class SitePlanBody {
        String token = Global.site_plan_token;
        String my_id = Global.loginDetails.username;
        String locale = Global.CURRENT_LOCALE;

    }

    public static class BookMarkBody {

        int UserID = Global.sime_userid;
        String ParcelNumber = PlotDetails.parcelNo;
        String CommunityAr = PlotDetails.communityAr;
        String Community = PlotDetails.communityEn;
        double Area = PlotDetails.area;

    }

    public static class ParcelBody {

        String SUB_NO=Global.subNo!=null?Global.subNo:"";
        String AREA_ID= HomeFragment.communityId!=null?HomeFragment.communityId:"";
        String LAND_NO=Global.LandNo!=null?Global.LandNo:"";
        String REMARKS=Global.getPlatformRemark()!=null?Global.getPlatformRemark():"";
        String token=Global.session!=null?Global.session:"guest";



    }

    public static class AreaBody {
        String SESSION = Global.session!=null?Global.session :"guest";
    }
}
