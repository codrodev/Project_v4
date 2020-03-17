package dm.sime.com.kharetati.datas.models;

import org.json.JSONObject;

import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.fragments.HomeFragment;

public class HTTPRequestBody {
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

    public static class GuestBody{
        String username ="guest";
        String DeviceID =(Global.deviceId!=null)? Global.deviceId:"";
        String DeviceType ="Android";
        String UserType ="GUEST";
    }
    public static class MakaniBody{
        public static String MAKANI =(Global.makani!=null)?Global.makani:"";
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
