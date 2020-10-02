package dm.sime.com.kharetati.view.viewModels;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.File;

import ae.sdg.libraryuaepass.business.Environment;
import ae.sdg.libraryuaepass.business.authentication.model.UAEPassAccessTokenRequestModel;
import ae.sdg.libraryuaepass.business.documentsigning.model.DocumentSigningRequestParams;
import ae.sdg.libraryuaepass.business.documentsigning.model.UAEPassDocumentSigningRequestModel;
import dm.sime.com.kharetati.BuildConfig;
import dm.sime.com.kharetati.utility.Global;

/*public class UAEPassRequestModels {

    private static final String UAE_PASS_CLIENT_ID = "kharetati_mobile_stage";
    public static final String UAE_PASS_CLIENT_SECRET = "QR3QGVmyyfgX0HmZ";
    public static final String REDIRECT_URL = "https://smart.gis.gov.ae/kharetatiuaepass";
    private static final String DOCUMENT_SIGNING_SCOPE = "urn:safelayer:eidas:sign:process:document";
    private static final String RESPONSE_TYPE = "code";
    private static final String SCOPE = "urn:uae:digitalid:profile:general";
    private static final String ACR_VALUES_MOBILE = "urn:digitalid:authentication:flow:mobileondevice";
    private static final String ACR_VALUES_WEB = "urn:safelayer:tws:policies:authentication:level:low";
    public static final String UAE_PASS_PACKAGE_ID = "ae.uaepass.mainapp";
    private static final Environment UAE_PASS_ENVIRONMENT = Environment.STAGING;

    *//*private static final String SCHEME = BuildConfig.URI_SCHEME;
    private static final String FAILURE_HOST = BuildConfig.URI_HOST_FAILURE;
    private static final String SUCCESS_HOST = BuildConfig.URI_HOST_SUCCESS;*//*

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        boolean found = true;
        try {
            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            found = false;
        }

        return found;
    }

    public static UAEPassAccessTokenRequestModel getAuthenticationRequestModel(Context context, String clientID,
                                                                               String secretID, String redirectUrl, String environment,
                                                                               String UAE_PASS_SCOPE, String UAE_ACR_VALUES_MOBILE, String UAE_ACR_VALUES_WEB,String locale) {
        final UAEPassAccessTokenRequestModel requestModel = new UAEPassAccessTokenRequestModel();
        if (isPackageInstalled(UAE_PASS_PACKAGE_ID, context.getPackageManager())) {
            requestModel.setAcrValues(UAE_ACR_VALUES_MOBILE);
        } else {
            requestModel.setAcrValues(UAE_ACR_VALUES_WEB);
        }
        requestModel.setClientId(clientID);
        requestModel.setClientSecret(secretID);
        requestModel.setRedirectUrl(redirectUrl);
        requestModel.setScope(UAE_PASS_SCOPE);
        requestModel.setResponseType(RESPONSE_TYPE);
        requestModel.setUiLocale(locale);
        if(environment.equals("PRODUCTION")) {
            requestModel.setEnvironment(Environment.PRODUCTION);
        } else {
            requestModel.setEnvironment(Environment.STAGING);
        }
        return requestModel;
    }

    public static UAEPassDocumentSigningRequestModel getDocumentRequestModel(File file, DocumentSigningRequestParams documentSigningParams) {

        UAEPassDocumentSigningRequestModel requestModel = new UAEPassDocumentSigningRequestModel();
        requestModel.setClientId(Global.uaePassConfig.getUAEID_clientid());
        requestModel.setClientSecret(Global.uaePassConfig.getUAEID_secret());
        requestModel.setScope(Global.uaePassConfig.getUAE_PASS_SCOPE());
        requestModel.setEnvironment(UAE_PASS_ENVIRONMENT);
        requestModel.setDocument(file);
        requestModel.setRequestObject(documentSigningParams);
        requestModel.setRedirectUrl(documentSigningParams.getFinishCallbackUrl());


        return requestModel;
    }

   *//* public static UAEPassDocumentDownloadRequestModel getDocumentDownloadRequestModel(String documentName, String documentURL) {

        UAEPassDocumentDownloadRequestModel requestModel = new UAEPassDocumentDownloadRequestModel();
        requestModel.setDocumentName(documentName);
        requestModel.setDocumentURL(documentURL);
        requestModel.setClientId(UAE_PASS_CLIENT_ID);
        requestModel.setClientSecret(UAE_PASS_CLIENT_SECRET);
        requestModel.setScope(DOCUMENT_SIGNING_SCOPE);
        requestModel.setEnvironment(UAE_PASS_ENVIRONMENT);
        return requestModel;
    }

    public static UAEPassProfileRequestModel getProfileRequestModel(Context context) {
        final UAEPassProfileRequestModel requestModel = new UAEPassProfileRequestModel();
        if (isPackageInstalled(UAE_PASS_PACKAGE_ID, context.getPackageManager())) {
            requestModel.setAcrValues(ACR_VALUES_MOBILE);
        } else {
            requestModel.setAcrValues(ACR_VALUES_WEB);
        }

        requestModel.setClientId(UAE_PASS_CLIENT_ID);
        requestModel.setClientSecret(UAE_PASS_CLIENT_SECRET);
        requestModel.setRedirectUrl(REDIRECT_URL);
        requestModel.setScope(SCOPE);
        requestModel.setResponseType(RESPONSE_TYPE);
        requestModel.setEnvironment(UAE_PASS_ENVIRONMENT);
        return requestModel;
    }*//*

}*/
public class UAEPassRequestModels {

    private static final String UAE_PASS_CLIENT_ID = Global.clientId;
    private static final String UAE_PASS_CLIENT_SECRET = Global.secretId;
    private static final String REDIRECT_URL = Global.callbackUrl;
    //    private static final Environment UAE_PASS_ENVIRONMENT = STAGING;

    private static  Environment UAE_PASS_ENVIRONMENT = Global.uaePassConfig.getUAE_PASS_ENVIRONMENT().compareToIgnoreCase("PRODUCTION")==0?Environment.PRODUCTION:Environment.STAGING;


    private static final String DOCUMENT_SIGNING_SCOPE = "urn:safelayer:eidas:sign:process:document";
    private static final String RESPONSE_TYPE = "code";
    private static final String SCOPE = Global.uaePassConfig.UAE_PASS_SCOPE;
    private static final String ACR_VALUES_MOBILE = Global.uaePassConfig.UAE_PASS_ACR_VALUES_MOBILE;
    private static final String ACR_VALUES_WEB =Global.uaePassConfig.UAE_PASS_ACR_VALUES_WEBVIEW;
    public static final String UAE_PASS_PACKAGE_ID = "ae.uaepass.mainapp";
    private static final String UAE_PASS_QA_PACKAGE_ID = "ae.uaepass.mainapp.qa";

    private static final String SCHEME = BuildConfig.URI_SCHEME;
    private static final String FAILURE_HOST = BuildConfig.URI_HOST_FAILURE;
    private static final String SUCCESS_HOST = BuildConfig.URI_HOST_SUCCESS;


    public static boolean isPackageInstalled(PackageManager packageManager) {
        String packageName = "";
        if (UAEPassRequestModels.UAE_PASS_ENVIRONMENT == Environment.PRODUCTION) {
            packageName = UAE_PASS_PACKAGE_ID;
        } else if (UAEPassRequestModels.UAE_PASS_ENVIRONMENT == Environment.STAGING) {
            packageName = UAE_PASS_QA_PACKAGE_ID;
        }
        boolean found = true;
        try {
            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            found = false;
        }
        return found;
    }

    public static UAEPassAccessTokenRequestModel getAuthenticationRequestModel(Context context) {
        UAEPassAccessTokenRequestModel requestModel = null;
        try{
        String ACR_VALUE = "";
        if (isPackageInstalled(context.getPackageManager())) {
            ACR_VALUE = ACR_VALUES_MOBILE;
        } else {
            ACR_VALUE = ACR_VALUES_WEB;
        }

        requestModel= new UAEPassAccessTokenRequestModel(
                UAE_PASS_ENVIRONMENT,
                UAE_PASS_CLIENT_ID,
                UAE_PASS_CLIENT_SECRET,
                SCHEME,
                FAILURE_HOST,
                SUCCESS_HOST,
                REDIRECT_URL,
                SCOPE,
                RESPONSE_TYPE,
                ACR_VALUE + "&ui_locales=" + Global.CURRENT_LOCALE
        );

        }
        catch (Exception e){
            e.printStackTrace();
        }
       return requestModel;
    }


}

