
package dm.sime.com.kharetati.utility.constants;

public final class AppConstants {

    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final int SPLASH_TIMMER = 3500;
    public static final int SPLASH_SLEEP = 100;

    public static String ESRI_SDK_CLIENTID = "B0C5oLxjWJ9f5sgb";

    public static String USER_LANGUAGE = "USER_LANGUAGE";
    public static String USER_OBJECT = "USER";
    public static String EMAIL_HISTORY = "EMAIL_HISTORY";
    public static String USERNAMES_HISTORY = "USERNAMES_HISTORY";
    public static String PARCELNUMBERS_HISTORY = "PARCELNUMBERS_HISTORY";
    public static String GUEST_OBJECT = "GUEST_DETAILS";
    public static String USER_LOGIN_DETAILS = "USER_LOGIN_DETAILS";
    public static String REMEMBER_USER = "REMEMBER_USER";
    public static String PARCEL_NUMBER =" Parcel ";

    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    public static final String DOC_TYPE_ID_PRIMARY = "primary";
    public static final String DOC_TYPE_ID_IMAGE = "image";
    public static final String DOC_TYPE_ID_VIDEO = "video";
    public static final String DOC_TYPE_ID_AUDIO = "audio";
    public static final String DOC_INITIALS_FILE = "file";
    public static final String EXTERNAL_STORAGE_DOC_PATH = "com.android.externalstorage.documents";
    public static final String GOOGLE_STORAGE_DOC_PATH = "com.google.android.apps.docs.storage";
    public static final String DOWNLOAD_STORAGE_DOC_PATH = "com.android.providers.downloads.documents";
    public static final String MEDIA_STORAGE_DOC_PATH = "com.android.providers.media.documents";
    public static final String GOOGLE_STORAGE_PHOTOS_PATH = "com.google.android.apps.photos.content";
    public static final String  DOC_TYPE_PDF_MIME = "application/pdf";
    public static final String  DOC_TYPE_PDF = "pdf";
    public static final String DOC_INITIALS_CONTENT = "content";
    public static final String  DOC_UPLOAD_INTENT_TITLE = "ChooseFile";
    public static final int FILE_SELECT_CODE = 1051;
    public static final String  DOC_TYPE_FORMAT = "file/*";
    public static final String  DEVICE_TYPE = "ANDROID";
    public static final String  PLATFORM_VERSION = "2.0";
    public static final String  REMARKS  = DEVICE_TYPE + "-" + PLATFORM_VERSION;

    public static String CURRENT_LOCALE = "en";
    public static String PAYMENT_CALL_BACK_TEXT = "paymentcallback";
    public static final String IS_LANDREG_MSG_NOT_CHECKED = "IsLandregMsgChecked";

    public static String GIS_LAYER_USERNAME = "kharetatiuser";//production
    public static String GIS_LAYER_PASSWORD = "kh@ret@t1##";//production
    public static String GIS_LAYER_TOKEN_URL =   "https://www.smartgis.ae/dmgis104/tokens/";
    public static String GIS_LAYER_URL = "https://www.smartgis.ae/dmgis104/rest/services/Kharetati/Kharetati/MapServer";//production

    private AppConstants() {
        // This utility class is not publicly instantiable
    }
}
