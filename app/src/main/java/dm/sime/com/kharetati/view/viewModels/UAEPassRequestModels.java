package dm.sime.com.kharetati.view.viewModels;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.File;

import ae.sdg.libraryuaepass.business.Environment;
import ae.sdg.libraryuaepass.business.authentication.model.UAEPassAccessTokenRequestModel;
import ae.sdg.libraryuaepass.business.authentication.model.AuthTokenModel;
import ae.sdg.libraryuaepass.business.documentsigning.model.DocumentSigningRequestParams;
import ae.sdg.libraryuaepass.business.documentsigning.model.UAEPassDocumentSigningRequestModel;

public class UAEPassRequestModels {

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
                                                                               String secretID, String redirectUrl) {
        final UAEPassAccessTokenRequestModel requestModel = new UAEPassAccessTokenRequestModel();
        if (isPackageInstalled(UAE_PASS_PACKAGE_ID, context.getPackageManager())) {
            requestModel.setAcrValues(ACR_VALUES_MOBILE);
        } else {
            requestModel.setAcrValues(ACR_VALUES_WEB);
        }

        requestModel.setClientId(clientID);
        requestModel.setClientSecret(secretID);
        requestModel.setRedirectUrl(redirectUrl);
        requestModel.setScope(SCOPE);
        requestModel.setResponseType(RESPONSE_TYPE);
        requestModel.setEnvironment(UAE_PASS_ENVIRONMENT);
        return requestModel;
    }

    public static UAEPassDocumentSigningRequestModel getDocumentRequestModel(File file, DocumentSigningRequestParams documentSigningParams) {

        UAEPassDocumentSigningRequestModel requestModel = new UAEPassDocumentSigningRequestModel();
        requestModel.setClientId(UAE_PASS_CLIENT_ID);
        requestModel.setClientSecret(UAE_PASS_CLIENT_SECRET);
        requestModel.setScope(DOCUMENT_SIGNING_SCOPE);
        requestModel.setEnvironment(UAE_PASS_ENVIRONMENT);
        requestModel.setDocument(file);
        requestModel.setRequestObject(documentSigningParams);
        requestModel.setRedirectUrl(documentSigningParams.getFinishCallbackUrl());

        return requestModel;
    }

   /* public static UAEPassDocumentDownloadRequestModel getDocumentDownloadRequestModel(String documentName, String documentURL) {

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
    }*/

}
