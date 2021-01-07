package dm.sime.com.kharetati.utility.constants;

import dm.sime.com.kharetati.utility.Global;

public class AppUrls {
    public static final String MAKANI_URL = "http://www.makani.ae/geomob/?lang=%s&landnumber=%s";
    public static final String DUBAIID_URL = "https://ids.dubai.gov.ae";
    //public static final String BASE_URL = "http://10.43.3.22/kharetati/";
    //public static final String BASE_URL="https://kharetati.dm.gov.ae/";
// public static final String BASE_URL="https://www.smartgis.ae/kharetativ2/"; //production


//    public static final String BASE_URL="https://smart.gis.gov.ae/kharetativ4/"; //staging
    public static final String BASE_URL = "https://smart.gis.gov.ae/kharetativ5/"; // new staging
//     public static final String BASE_URL = "https://www.smartgis.ae/kharetativ6/"; // latest production

 //public static final String BASE_URL="https://www.smartgis.ae/kharetativ5/"; //new production

  // public static final String BASE_URL="https://www.smartgis.ae/kharetativ5_2/";

    public static final String BASE_AUXULARY_URL_STATIC="https://smart.gis.gov.ae/KharetatiAuxiliaryServiceV3/Service.svc/"; //staging
    public static String BASE_AUXULARY_URL; //staging
    public static String BASE_AUXULARY_URL_UAE_SESSION = "https://smart.gis.gov.ae/KharetatiAuxiliaryServiceV3/Service.svc/"; //staging
    public static final String LOOKUP_URL="https://smart.gis.gov.ae/KharetatiAuxiliaryServiceV3/Service.svc/getlkpdetails"; //staging
    /************************************ GIS Layer Urls***********************************/

    public static String GIS_LAYER_USERNAME = "kharetatiuser";//production
    public static String GIS_LAYER_PASSWORD = "kh@ret@t1##";//production
    // public static String GIS_LAYER_TOKEN_URL = "https://www.makani.ae/arcgis/tokens/";//development
    // public static String GIS_LAYER_TOKEN_URL = "https://www.makani.ae/dmgis103/tokens/";//production
    public static String GIS_LAYER_TOKEN_URL =   "https://www.smartgis.ae/dmgis104/tokens/";
    //public static String GIS_LAYER_URL = "https://www.makani.ae/arcgis/rest/services/ZoningRegulations/ZoningRegulations/MapServer";//development
    // public static String GIS_LAYER_URL = "https://www.makani.ae/dmgis103/rest/services/Makani/ZoningRegulations/MapServer";//production
    public static String GIS_LAYER_URL = "https://www.smartgis.ae/dmgis104/rest/services/Kharetati/Kharetati/MapServer";//production
    //public static String GIS_LAYER_PARCEL_URL = "https://smart.gis.gov.ae/dmgis104/rest/services/Kharetati/Kharetati/MapServer/2";
    //public static String GIS_LAYER_COMMUNITY_URL = "https://www.makani.ae/arcgis/rest/services/ZoningRegulations/ZoningRegulations/MapServer/9";
    public static String community_layerid="16";
    public static String plot_layerid="3";
    public static String GIS_LAYER_COMMUNITY_URL = GIS_LAYER_URL + "/" + community_layerid;
    //public static String parcelLayerExportUrl_en = "http://www.makani.ae/arcgis/rest/services/ZoningRegulations/ZoningRegulations/MapServer/export" ;//+ "?token=" + Global.arcgis_token;
    //public static String parcelLayerExportUrl_ar = "http://www.makani.ae/arcgis/rest/services/ZoningRegulations/ZoningRegulations_Arabic/MapServer/export" ;//+ "?token=" + Global.arcgis_token;
    public static String parcelLayerExportUrl_en = "https://www.smartgis.ae/dmgis104/rest/services/Kharetati/ZoningRegulations/MapServer/export" ;//+ "?token=" + Global.arcgis_token;
    public static String parcelLayerExportUrl_ar = "https://www.smartgis.ae/dmgis104/rest/services/Kharetati/ZoningRegulations/MapServer/export" ;//+ "?token=" + Global.arcgis_token;

    public static String dubaiID_url_en="https://ids.dubai.gov.ae/authenticationendpoint/login.do?SAMLRequest=fVJNb%2BIwFPwrkQ97yycEEpdQUVB3I9HdCGgPvSAnfoClxE79HBb%2B%2FRrI9uPS62hm3ozHE2RN3dJZZw5yBW8doHHyRUa2w4An1Wg3csdBmrrDKgzdMmSpG%2B%2FKlMeDISRRQpwX0CiUzEjkBcTJETvIJRomjYWCMHGDxI1Gm2BE45jGgZcG0StxFvaKkMxclQdjWqS%2BLzh6vCuZ8Pbq6DHwL8kQ1X3N5D4D%2BYM17V3FNNfAOOhsx2oE4jwqXcE1fkZ6KMeCIYojvCOFVkZVqn4QkgvrRjotqWIokErWAFJT0fXsaUltDVreSEh%2FbTaFW%2FxZb4gzQwR9yTtXErsG9Br0UVTwvFp%2BNGjOgn%2BtcPH037W9Bj2G7cl6GqNF2Rm4edqTPSGXHE72%2FcIoHAzicTImzqmpJdLrVt%2BHb%2FumZDq5sOl1Ev1J%2F72c%2Fc9Kpov1z20faPts0RXst0u1F3Lif3K%2BnWnpb2uVLwpVi%2BrszOpa%2FZ3bnYydwOjOLuBPb7KvX236Dw%3D%3D&cardreader=false&commonAuthCallerPath=%252Fsamlsso&forceAuth=false&lang=en&passiveAuth=false&relyingParty=DSG_Service_UserReg_Login&tenantDomain=carbon.super&type=samlsso&sessionDataKey=55d30961-32fd-40ad-bc38-006fa74eacf9&relyingParty=DSG_Service_UserReg_Login&type=samlsso&sp=DSG_Service_UserReg_Login&isSaaSApp=false&authenticators=BasicAuthenticator:LOCAL";
    public static String dubaiID_url_ar="https://ids.dubai.gov.ae/authenticationendpoint/login.do?SAMLRequest=fVJNb%2BIwFPwrkQ97yycEEpdQUVB3I9HdCGgPvSAnfoClxE79HBb%2B%2FRrI9uPS62hm3ozHE2RN3dJZZw5yBW8doHHyRUa2w4An1Wg3csdBmrrDKgzdMmSpG%2B%2FKlMeDISRRQpwX0CiUzEjkBcTJETvIJRomjYWCMHGDxI1Gm2BE45jGgZcG0StxFvaKkMxclQdjWqS%2BLzh6vCuZ8Pbq6DHwL8kQ1X3N5D4D%2BYM17V3FNNfAOOhsx2oE4jwqXcE1fkZ6KMeCIYojvCOFVkZVqn4QkgvrRjotqWIokErWAFJT0fXsaUltDVreSEh%2FbTaFW%2FxZb4gzQwR9yTtXErsG9Br0UVTwvFp%2BNGjOgn%2BtcPH037W9Bj2G7cl6GqNF2Rm4edqTPSGXHE72%2FcIoHAzicTImzqmpJdLrVt%2BHb%2FumZDq5sOl1Ev1J%2F72c%2Fc9Kpov1z20faPts0RXst0u1F3Lif3K%2BnWnpb2uVLwpVi%2BrszOpa%2FZ3bnYydwOjOLuBPb7KvX236Dw%3D%3D&cardreader=false&commonAuthCallerPath=%252Fsamlsso&forceAuth=false&lang=ar&passiveAuth=false&relyingParty=DSG_Service_UserReg_Login&tenantDomain=carbon.super&type=samlsso&sessionDataKey=55d30961-32fd-40ad-bc38-006fa74eacf9&relyingParty=DSG_Service_UserReg_Login&type=samlsso&sp=DSG_Service_UserReg_Login&isSaaSApp=false&authenticators=BasicAuthenticator:LOCAL";
    public static String registration_url_en="https://myid.dubai.gov.ae/Registration.ASPX?lang=en";
    public static String registration_url_ar="https://myid.dubai.gov.ae/Registration.ASPX?lang=ar";
    public static String forgotpassword_url_en="https://myid.dubai.gov.ae/ForgotPassword.aspx?lang=en";
    public static String forgotpassword_url_ar="https://myid.dubai.gov.ae/ForgotPassword.aspx?lang=ar";


    public static final String SAVE_AS_BOOK_MARK = "Bookmark/addBookmark";

    public static final String RETRIEVE_DOC_STREAM = Global.base_url_site_plan+"/retrieveDocStream";
    public static final String RETRIEVE_PROFILE_DOC = Global.base_url_site_plan+"/retrieveProfileDocs";
    public static final String RETRIEVE_MY_MAPS = Global.base_url_site_plan+"/retrieveMyMaps";
    public static final String FIND_SITE_PLANS = "/findSitePlans";
    public static final String VALIDATE_REQUEST = "/validateRequest";
    public static final String RETRIEVE_SITE_PLAN_DOC_STREAM = "/retrieveSitePlanDocStream";
    public static final String CREATE_UPDATE_REQUEST = Global.base_url_site_plan+"/createUpdateRequest";

    public static final String MYID_ACCESS_TOKEN_URL = BASE_URL + "MyId/getAccessToken";
    public static final String MYID_SESSION_ID = BASE_URL + "KharetatiWebService/getSession";
    public static final String UAE_SESSION_ID = BASE_URL + "KharetatiWebService/getSession";
    public static final String MYID_PARCEL_ID = BASE_URL + "KharetatiWebService/getParcelId";
    public static final String MYID_LAND_ACTIVITY = BASE_URL + "KharetatiWebService/getLandActivities";
    public static final String MYID_SUBMIT_ENQUIRY = BASE_URL + "KharetatiWebService/submitEnquiry";
    public static final String MYID_MAKANI_TO_DLTM = BASE_URL + "KharetatiWebService/makaniToDLTM";
    // public static final String MYID_REFESH_TOKEN_URL = BASE_URL + "MyId/getRefreshToken?refresh_token=";
    public static final String MY_ID_USER_INFO_URL = BASE_URL + "MyId/getUserInfo?accessToken=";

    public static final String REGISTER_GUEST_USER = BASE_URL + "Util/addDeviceID";
    public static String URL_PLOTFINDER = BASE_URL + "PlotFinder/";//staging

    public static final String REGISTER_MYID_USER = BASE_URL + "util/registerMobileUser";
    public static final String GET_PAYMENT_URL = BASE_URL + "Util/getVoucher2";
    public static final String URL_GET_OWNER_PLOTS_LIST = BASE_URL + "SitePlan/getPersonLands";
    public static final String URL_SEND_FEEDBACK = BASE_URL + "util/sendFeedBackEmail";

    public static final String URL_GET_IS_PARCEL_BLOCKED = BASE_URL + "Util/isParcelBlocked";
    public static final String URL_VALIDATE_PARCEL = BASE_URL +"Util/validateParcel";
    public static final String URL_UPDATE_GUEST_USER_PROFILE = BASE_URL + "util/updateRegisteredUser";
    public static final String URL_UPLOAD_DOCUMENT = BASE_URL + "SitePlan/uploadDocsForSitePlanRequest2";
    public static final String URL_SAVE_ATTACHMENT = BASE_URL + "SitePlan/saveAttachment";

    public static final String TEST_VOLLEY = BASE_URL + "Util/testVolley";


    //public static final String URL_ZONINGREGULATION = BASE_URL + "PlotFinder/getZoneRegulation";
    public static final String URL_ZONINGREGULATION = BASE_URL + "PlotFinder/getZoneRegulation";//staging
    // public static final String URL_ZONINGREGULATION = "https://www.smartgis.ae/kharetati/PlotFinder/getZoneRegulation"; //production
    // public static String URL_BUILDING_VIOLATIONS="http://stg.gis.gov.ae/ViolationWS/ViolationServlet?TOKEN=kdjfKkjuBNub23INU0hjU&ACTION=GETDATA&parcelid=";
    //public static String URL_BUILDING_VIOLATIONS="https://www.smartgis.ae/BuildingViolationWebService/ViolationServlet?TOKEN=teste&ACTION=GETDATA&parcelid=";
    public static String URL_BUILDING_VIOLATIONS=BASE_URL + "Util/getBuildingViolations";

    public static final String URL_UAE_ID_CONFIG =BASE_URL + "MyID/getUAEIDConfig";
    public static final String URL_GET_CONFIG =BASE_URL + "myid/getConfig";
    public static final String URL_UAE_GET_ACCESS_TOKEN =BASE_URL + "myid/GetAccessToken";
    public static final String URL_UAE_GET_USER_PROFILE =BASE_URL + "myid/GetUserProfile";




    public static final String MY_LAND_REG_URL = "%s?appsrc=kharetati&lng=%s&token=%s";
    public static final String MY_COMPANY_LAND_REG_URL = "%s?appsrc=kharetati&appuser=company&lng=%s&token=%s";
    public static String URL_RATE_US_EN ="https://play.google.com/store/apps/details?id=dm.sime.com.kharetati&hl=en";
    public static String URL_RATE_US_AR ="https://play.google.com/store/apps/details?id=dm.sime.com.kharetati&hl=ar";
    public static String TERM_CONDITION_AR_URL = "http://www.dm.gov.ae/wps/portal/!ut/p/a1/nZHNbsIwEIRfpRw4kjV2UtJj4EBBQohSBPEFJc4mcRvHIbZo1aevU87hp5ZlydLsrL4Z4HAAXidnWSRW6jqpuj9_Po4XoT-erslyvWEBiehkFsxe39h8GzhB3C8gc3rfPOk5Ebk1vwcOXNS2sSXEmRK6ttjdopKmHJJSKxySL0yNtPjU6EoKiWZILLbKOHEmO1DTmTRCZhBTQVI_mwQjP6fuIeF4lFImRpS9BALzQLCEwhbry-Ib6H-CK2xL4EWlU5fzfgpct1ysNl0iUZ2ysADeYo4ttl6pjYWDwaQVpZcpr9BnL0GIXXaT3nAog-2DXNcNF_7Dhss76pcfpxOPXIlddd-O8_8tNmq3UyHzP_OVZD_vuVLHlYkGg1-CQZqu/dl5/d5/L0lHSkovd0RNQUprQUVnQSEhLzRKU0UvYXI!";
    public static String TERM_CONDITION_EN_URL = "http://www.dm.gov.ae/wps/portal/!ut/p/a1/pZLNcoIwFIVfpS5cQiLBAkt0YWXGcax1lGycEC-QlhBMUlv79AW7xp9pJptMzj2Z850ginaI1uwkCmaFqlnVnenzfjQP_dFkiZPlioxx7AXT8fTllczW41aQ9gvwzLtvHvesGN-a3yKKKK9tY0uUHiRXtYVuF5Uw5RCXSsIQf0FmhIWnRlWCCzBDbIB_amHPjRYnxs-Xi3Nn1XBxaI2iiHtRHjkh58Tx8xE4jOHAAR6QHAe-F2UeWkP99_wNABfBlYQJokWlspb2doKo0pQvVh2XuM5IWCCqIQcN2i2VsWhngGleugfpFurkMkBpSzDoReQRtH4w13XDuf-wYXLHJxDvxyON2yq7Ar_bnP_tspGbjQyJ_5EvBPl5y6XcL0w8GPwCFEB6cg!!/?1dmy&current=true&urile=wcm%3apath%3a%2Fdmcontentenglish%2Fhome%2Fwebsite%2Bpolicies%2Ftermsconditions";
    public static String POLICY_AR_URL = "http://www.dm.gov.ae/wps/portal/!ut/p/a1/nZHNbsIwEIRfpT1wTNbYDgnHwIEStSBACOJLZTtO4orEwbGgvH2T9sxf97bS7Ky-GWCwB1bzky6406bmh35no8_hPKLDyRIlyxUJUIzDaTB9W5PZJugE6XUBmuHH7tGVidG9-x0wYLJ2jSshzSppaqdqxy0XWg5QaSo1QGclWu3US2MOWmrVDpBTtmo7baZ7zrb3aKTOIBWYZiGl1JOY5B4NRO6Nh-OxRzHNMQqUECL8-3mH-ldwAysBVhyM6CLeTYAZy-THqg8jrgWJCmBW5coq65emdbBvFbey9LPKL8zJ5wrSLrbwai6YwOZJptuGc_q0YfJA8_rreGRx11_f2nfH-e8CN9xCU223VURG6-jyni8WHhfRhZzj1x-o06FR/?1dmy&current=true&urile=wcm%3apath%3a%2Fdmcontentarabic%2Fhome%2Fwebsite%2Bpolicies%2Fsecurityprivacypolicy";
    public static String POLICY_EN_URL = "http://www.dm.gov.ae/wps/portal/!ut/p/a1/pVFNc4IwFPwr7cEjJEKQcEQPVqfVUcdRcukk4QHpCMGQavn3BXv2a_pub97uvtldxNAesYqfVM6t0hU_9DsbfQ5nlAzHSzxfrvwAx144CSZva3-6CTpAch2Ap95jfHxlYnyPv0MMMVnZ2hYoSUupKwuV5YYLJQe40CUM8BlEoyy81PqgpIJmgBuQ30bZtjbqxGV7ObS9Ui1VihKShR7QSDoiEiOHQCAdGoXECYEQSqMoDCD7-3zH-wVww9wcsfygRRf0boyYNkx-rPpI4kr4NEfMQAYGjFvoxqJ9A9zIwk1LN9cnlwNKuvDCq-l4Pto86em24Iw8LTh_oH_1dTyyuGux7-6n8_nPGjfcoLrcbkvqj9a0fc8WC4cL2vrn-PUX6s_ppA!!/dl5/d5/L0lHSkovd0RNQUhrQUVnQSEhLzRKU0UvZW4!";
}
