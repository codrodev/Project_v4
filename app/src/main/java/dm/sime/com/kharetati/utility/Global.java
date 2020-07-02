package dm.sime.com.kharetati.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.URLSpan;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.DeliveryDetails;
import dm.sime.com.kharetati.datas.models.FunctionsOnMap;
import dm.sime.com.kharetati.datas.models.LicenceDocs;
import dm.sime.com.kharetati.datas.models.LookupResponse;
import dm.sime.com.kharetati.datas.models.LookupValue;
import dm.sime.com.kharetati.datas.models.NocDocs;
import dm.sime.com.kharetati.datas.models.NotificationResponse;
import dm.sime.com.kharetati.datas.models.PassportDocs;
import dm.sime.com.kharetati.datas.models.SearchResult;
import dm.sime.com.kharetati.datas.models.SessionUaePassResponse;
import dm.sime.com.kharetati.datas.models.Tabs;
import dm.sime.com.kharetati.datas.models.UaePassConfig;
import dm.sime.com.kharetati.utility.constants.AppConstants;
import dm.sime.com.kharetati.datas.models.AppMsg;
import dm.sime.com.kharetati.datas.models.AreaResponse;
import dm.sime.com.kharetati.datas.models.Docs;
import dm.sime.com.kharetati.datas.models.GuestDetails;
import dm.sime.com.kharetati.datas.models.LoginDetails;
import dm.sime.com.kharetati.datas.models.MyMapResults;
import dm.sime.com.kharetati.datas.models.User;
import dm.sime.com.kharetati.view.activities.LoginActivity;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.customview.CleanableEditText;
import dm.sime.com.kharetati.view.navigators.AuthListener;
import retrofit2.http.Url;

import static dm.sime.com.kharetati.utility.constants.AppConstants.REMEMBER_USER;
import static dm.sime.com.kharetati.utility.constants.AppConstants.USER_LANGUAGE;
import static dm.sime.com.kharetati.utility.constants.AppConstants.USER_OBJECT;

public class Global {
    public static final String MYPREFERENCES = "MyPreferences";
    public static final String APP_SESSION_TOKEN =  "APP_SESSION_TOKEN" ;
    private static final String GUEST_OBJECT = "Guest";
    private static final String USER_LANGUAGE = "UserLaunguage";
    public static String CURRENT_LOCALE = "en";
    public static String CurrentAndroidVersion;
    public static String current_fragment_id_locale_change = null;
    public static Fragment current_fragment_locale_change;
    public static boolean isUserLoggedIn;
    public static int sime_userid;
    public static LoginDetails loginDetails = new LoginDetails();
    public static String deviceId;
    public static String emiratesID;
    public static String mobile;
    public static String email;
    public static String username;
    public static String password;
    public static String UserType;
    public static String DeviceType;
    public static String FirstName;
    public static String LastName;
    public static String FullName;
    public static String Gender;
    public static String Nationality;
    public static String accelaCustomId = null;
    public static boolean hasNewSitePlan;
    public static String newSitePlanTargetUser;
    public static int newSitePlanTargetUserId;
    public static String newSitePlanTargetUserType;
    public static String current_fragment_id = "-NONE-";
    public static Fragment current_fragment;
    public static String arcgis_token = null;
    public static String accessToken = null;
    public static String app_session_token = null;
    public static String session = null;
    public static String base_url_site_plan;
    public static String site_plan_token;
    public static ArrayList<MyMapResults> sitePlanList;
    public static String docID;
    public static String aboutus_en_url;
    public static String aboutus_ar_url;
    public static String terms_en_url;
    public static String terms_ar_url;
    public static AppMsg appMsg;
    public static boolean isPlotSearch = false;
    public static boolean isMakani = false;
    public static boolean isLand = false;
    public static String dltm;
    public static AlertDialog alertDialog;
    public static String subNo;
    public static String LandNo;
    public static String webViewUrl;
    public static boolean isBookmarks;
    public static String bookmarkPlotNo;
    public static int spinPosition;
    public static boolean isPerson;
    public static boolean isCompany;
    public static boolean rbIsOwner;
    public static boolean rbNotOwner;
    public static DeliveryDetails deliveryDetails;
    public static List<PassportDocs> passportData;
    public static List<LicenceDocs> licenseData;
    public static List<NocDocs> nocData;
    public static boolean isDeliveryByCourier;
    public static String paymentUrl;
    public static String searchText;
    public static boolean isSaveAsBookmark;
    public static boolean isLoginActivity;
    public static String faq_url;
    public static boolean isLandScape;
    public static boolean isLogout;
    public static boolean isDashboard;
    public static boolean isHome;
    public static int selectedTab;
    public static boolean isValidMakani;
    public static boolean isMakni;
    public static String HelpUrl;
    public static String appId;
    public static boolean isfromWebViewCancel;
    public static String sessionErrorMsg;
    public static float fontScale;
    public static boolean isRecreate;
    public static String appName;
    public static float fontSize = 14f;
    public static String mapFunction;
    public static String happinessUrl;
    public static String happinessClientID;
    public static String happinessSecretKey;
    public static String happinessServiceCode;
    public static String happinessServiceProvider;
    public static String clientId;
    public static String secretId;
    public static String callbackUrl;
    public static NotificationResponse notificationResponse;
    private static Context context;
    public static boolean isLanguageChanged = false;
    public static String noctemplateUrl;
    public static String forceUserToUpdateBuild_msg_en;
    public static String forceUserToUpdateBuild_msg_ar;
    public static boolean forceUserToUpdateBuild;
    public static LookupResponse lookupResponse;
    public static int LAST_TAB;
    public static String makani;
    public static boolean isHomeMenu;
    public static String landNumber;
    public static String area;
    public static String area_ar;
    public static String desc;
    public static boolean isFindSitePlan;
    public static int arr;
    public static boolean showLandregInMenu;
    public static boolean showLandregPopup;
    public static String landregPopupMsgEn;
    public static String landregPopupMsgAr;
    public static String landregPopupMsgHeadingEn;
    public static String landregPopupMsgHeadingAr;
    public static String landregUrl;
    public static String requestId;
    public static String plotDimLayerId;
    public static String plotHighlightLayerId;
    public static String plotLayerParcelAttrName;
    public static String plotImgLayerId;
    public static String plotDimLayerParcelAttrName;
    public static boolean isLandRegMessageDisplayed = false;
    public static boolean isFromDelivery = false;
    public static Docs[] docArr;
    public static String[] mapHiddenLayers;
    public static String paymentStatus;
    public static String tempStatus;
    public static float height;
    public static float width;
    public static List<FunctionsOnMap> lstMapFunctions;
    public static SearchResult mapSearchResult;
    public static String helpUrlEn;
    public static String helpUrlAr;
    public static String bookmarks_en_url;
    public static String bookmarks_ar_url;
    public static String mymaps_en_url;
    public static String mymaps_ar_url;
    public static String home_en_url;
    public static String home_ar_url;
    public static String map_en_url;
    public static String map_ar_url;
    public static int lastSelectedBottomTab = 3;
    public static String FragmentTagForHelpUrl;
    public static int FragmentTagForDashboardHelpUrl;
    public static boolean isFirstLoad = true;
    public static HashMap<String, String> hashSearchFieldValue;
    public static String  uaePassUrl = "https://qa-id.uaepass.ae/trustedx-authserver/oauth/main-as?redirect_uri=https://smart.gis.gov.ae/kharetatiuaepass&client_id=kharetati_mobile_stage&state=QR3QGVmyyfgX0HmZ&response_type=code&scope=urn:uae:digitalid:profile:general&acr_values=urn:safelayer:tws:policies:authentication:level:low&ui_locales=en";
    public static String emptyString = "";
    public static String uae_code = "";
    public static String uae_access_token = "";
    public static boolean isUAEaccessWeburl = false;
    public static UaePassConfig uaePassConfig;
    public static SessionUaePassResponse uaeSessionResponse;
    public static boolean isUAE = false;
    public static boolean isUAEAccessToken = false;
    public static String clientID = "";
    public static String state = "";

    public static List<FunctionsOnMap> getLstMapFunctions() {
        return lstMapFunctions;
    }

    public static void setLstMapFunctions(List<FunctionsOnMap> lstMapFunctions) {
        Global.lstMapFunctions = lstMapFunctions;
    }



    public static String getCurrentLanguage(Activity activity) {
        return activity.getSharedPreferences(USER_LANGUAGE, Context.MODE_PRIVATE).getString(USER_LANGUAGE, "defaultStringIfNothingFound");
    }

    public static boolean isRememberLogin(Activity activity) {
        return PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).getBoolean(AppConstants.REMEMBER_USER, false);
    }

    /*
     Get user from localstorage
      */
    public static User getUser(Activity activity) {
        String userJson = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).getString(USER_OBJECT, "NOT_AVAILABLE");
        if (userJson.compareToIgnoreCase("NOT_AVAILABLE") == 0) return null;
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        return user;
    }

    public static LoginDetails getUserLoginDeatils(Activity activity) {
        //String userJson = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).getString(AppConstants.USER_LOGIN_DETAILS, "NOT_AVAILABLE");
        String userJson = activity.getSharedPreferences(MYPREFERENCES,Context.MODE_PRIVATE).getString(AppConstants.USER_LOGIN_DETAILS, "NOT_AVAILABLE");
        if (userJson.compareToIgnoreCase("NOT_AVAILABLE") == 0) return null;
        Gson gson = new Gson();
        LoginDetails user = gson.fromJson(userJson, LoginDetails.class);
        return user;
    }

    /**
     * Save user to localstorage
     */
    public static void saveUser(Activity activity, User user) {
        Gson gson = new Gson();
        PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).edit().putString(USER_OBJECT, gson.toJson(user)).apply();
    }

    public static void saveGuestUserDetails(Activity activity, GuestDetails guestDetails) {
        Gson gson = new Gson();
        PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).edit().putString(GUEST_OBJECT, gson.toJson(guestDetails)).apply();
    }

    public static GuestDetails getGuestDetails(Activity activity) {
        String userJson = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).getString(AppConstants.GUEST_OBJECT, "NOT_AVAILABLE");
        if (userJson.compareToIgnoreCase("NOT_AVAILABLE") == 0) return null;
        Gson gson = new Gson();
        GuestDetails user = gson.fromJson(userJson, GuestDetails.class);
        return user;
    }

    public static void rememberUser(Boolean remember, Activity activity) {
        PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).edit().putBoolean(REMEMBER_USER, remember).apply();
    }

    public static String getMakaniWithoutSpace(String makani) {
        String[] arr = makani.split(" ");
        return arr[0] + arr[1];
    }

    public static boolean isConnected(Context context) {

//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netinfo = cm.getActiveNetworkInfo();
//
//        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
//            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//
//            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
//                return true;
//            else return false;
//        } else
//            return false;
        NetworkInfo activeNetworkInfo=null;

        if(context!=null){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();}
        if (activeNetworkInfo == null) return false;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
    public static void showNoInternetAlert(Context activity,String msg){

        if(msg!=null||msg!="")
            AlertDialogUtil.errorAlertDialog("",msg,activity.getResources().getString(R.string.ok),activity);
        else if(Global.appMsg!=null)
            AlertDialogUtil.errorAlertDialog("",Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr(),activity.getResources().getString(R.string.ok),activity);
        else
            AlertDialogUtil.errorAlertDialog("",activity.getResources().getString(R.string.internet_connection_problem1),activity.getResources().getString(R.string.ok),activity);
        AlertDialogUtil.showProgressBar((Activity) activity,false);
        return;
    }

    public static void hideSoftKeyboard(Activity activity) {
        View view;
            if(activity!=null) {
                view = activity.getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View(activity);
                }
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
    }

    public static void showSoftKeyboard(View view, Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public static String rectifyPlotNo(String plotno) {
        try {
            plotno = plotno.trim();
            if (plotno.trim().length() <= 4)
                return "";
            if (plotno.trim().length() <= 20)
                return plotno;
            /*String start = plotno.substring(0, 3);
            String end = plotno.substring(3, plotno.length());
            if (end.length() < 4) {
                while (end.length() != 4)
                    end = "0" + end;
            }
            return (start + end);*/
        } catch (Exception ex) {
            //ignore
        }
        return plotno;
    }

    public static List<String> addToUserNamesHistory(String username, Activity activity) {
        Gson gson = new Gson();
        CaseInsensitiveArrayListString usernamesHistory = null;
        String usernamesHistoryJson = PreferenceManager.getDefaultSharedPreferences((Context) activity).getString(AppConstants.USERNAMES_HISTORY, "NOT_AVAILABLE");
        if (usernamesHistoryJson.compareToIgnoreCase("NOT_AVAILABLE") == 0) {
            usernamesHistory = new CaseInsensitiveArrayListString();
        } else {
            usernamesHistory = gson.fromJson(usernamesHistoryJson, CaseInsensitiveArrayListString.class);
        }

        if (!usernamesHistory.contains(username))
            usernamesHistory.add(username);
        PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).edit().putString(AppConstants.USERNAMES_HISTORY, gson.toJson(usernamesHistory)).apply();
        return usernamesHistory;
    }

    public static List<String> getUsernamesFromHistory(Activity activity) {
        Gson gson = new Gson();
        List<String> usernamesHistory = null;
        String usernamesHistoryJson = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).getString(AppConstants.USERNAMES_HISTORY, "NOT_AVAILABLE");
        if (usernamesHistoryJson.compareToIgnoreCase("NOT_AVAILABLE") == 0)
            usernamesHistory = new ArrayList<>();
        else
            usernamesHistory = gson.fromJson(usernamesHistoryJson, List.class);
        return usernamesHistory;
    }

    public static List<String> getParcelNumbersFromHistory(Activity activity) {
        Gson gson = new Gson();
        List<String> parcelsHistory = null;
        String parcelsHistoryJson = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).getString(AppConstants.PARCELNUMBERS_HISTORY, "NOT_AVAILABLE");
        if (parcelsHistoryJson.compareToIgnoreCase("NOT_AVAILABLE") == 0)
            parcelsHistory = new ArrayList<>();
        else
            parcelsHistory = gson.fromJson(parcelsHistoryJson, List.class);
        return parcelsHistory;
    }

    public static List<String> addToParcelHistory(String parcelNumber, Activity activity) {
        Gson gson = new Gson();
        CaseInsensitiveArrayListString parcelHistory = null;
        String parcelHistoryJson = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).getString(AppConstants.PARCELNUMBERS_HISTORY, "NOT_AVAILABLE");
        if (parcelHistoryJson.compareToIgnoreCase("NOT_AVAILABLE") == 0) {
            parcelHistory = new CaseInsensitiveArrayListString();
        } else {
            parcelHistory = gson.fromJson(parcelHistoryJson, CaseInsensitiveArrayListString.class);
        }

        if (!parcelHistory.contains(parcelNumber))
            parcelHistory.add(0, parcelNumber);
        PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).edit().putString(AppConstants.PARCELNUMBERS_HISTORY, gson.toJson(parcelHistory)).apply();
        int index = parcelHistory.size() <= 10 ? parcelHistory.size() - 1 : 9;
        return parcelHistory.subList(0, index);
    }

    public static List<String> addToEmailHistory(String email, Activity activity) {

        Gson gson = new Gson();
        CaseInsensitiveArrayListString emailHistory = null;
        String emailHistoryJson = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).getString(AppConstants.EMAIL_HISTORY, "NOT_AVAILABLE");
        if (emailHistoryJson.compareToIgnoreCase("NOT_AVAILABLE") == 0) {
            emailHistory = new CaseInsensitiveArrayListString();
        } else {
            emailHistory = gson.fromJson(emailHistoryJson, CaseInsensitiveArrayListString.class);
        }

        if (!emailHistory.contains(email))
            emailHistory.add(email);
        PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).edit().putString(AppConstants.EMAIL_HISTORY, gson.toJson(emailHistory)).apply();
        return emailHistory;
    }

    public static List<String> getEmailsFromHistory(Activity activity) {
        Gson gson = new Gson();
        List<String> emailHistory = null;
        String emailHistoryJson = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext()).getString(AppConstants.EMAIL_HISTORY, "NOT_AVAILABLE");
        if (emailHistoryJson.compareToIgnoreCase("NOT_AVAILABLE") == 0)
            emailHistory = new ArrayList<>();
        else
            emailHistory = gson.fromJson(emailHistoryJson, List.class);
        return emailHistory;
    }

    public static String getValue(String value) {
        if (value == null || (value != null && value.compareToIgnoreCase("null") == 0)) return null;
        return value;
    }

    public static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void changeLang(String lang, Context context) {
        Locale locale;
        context.getSharedPreferences(MYPREFERENCES,Context.MODE_PRIVATE).edit().putString(USER_LANGUAGE, lang).apply();
        //PreferenceManager.getDefaultSharedPreferences(context).edit().putString(USER_LANGUAGE, lang).apply();

        Global.current_fragment = null;
        Global.current_fragment_id = "-NONE-";
        locale = new Locale(lang);
        CURRENT_LOCALE = lang;
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >Build.VERSION_CODES.N){
            config.setLocale(locale);
            context.createConfigurationContext(config);
//            context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
        }
        else{
            config.locale = locale;
            context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
        }



        /*android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());*/

    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static void clenEditText(EditText editText, Context context){
        editText= new CleanableEditText(context);
    }
    public static void enableClearTextInEditBox(final EditText editText, Context context) {
        final Drawable x_editTextUserName = ContextCompat.getDrawable(context, R.drawable.ic_cancel);
        x_editTextUserName.setBounds(-5, 0, x_editTextUserName.getIntrinsicWidth() - 5, x_editTextUserName.getIntrinsicHeight());


        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                x_editTextUserName.setVisible(editText.getText().length() != 0, true);
                if (editText.getText().length() != 0){
                    if(CURRENT_LOCALE.equals("en"))
                        editText.setCompoundDrawables(null, null, x_editTextUserName, null);
                    else
                        editText.setCompoundDrawables(x_editTextUserName, null,null , null);
                }
                /*if (editText.getText().length() != 0){
                    if(CURRENT_LOCALE.equals("en"))
                        editText.setCompoundDrawables(null, null, x_editTextUserName, null);
                    else
                        editText.setCompoundDrawables(x_editTextUserName, null,null , null);
                }
                else
                    editText.setCompoundDrawables(null, null, null, null);*/
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if(CURRENT_LOCALE.equals("en")){
                        if (editText.getCompoundDrawables()[DRAWABLE_RIGHT] != null && event.getRawX() >= ((editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())+50))
                        {
                            editText.setText("");
                            if (editText.isCursorVisible())
                                editText.setCursorVisible(false);

                            return true;
                        }
                    } else {
                        if (editText.getCompoundDrawables()[DRAWABLE_LEFT] != null && event.getRawX() < 300) {
                           // (editText.getLeft() - editText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())
                            editText.setText("");
                            if(editText.isCursorVisible())
                                editText.setCursorVisible(false);

                            return true;
                        }
                    }


                }
                if(!editText.isCursorVisible())
                    editText.setCursorVisible(true);
                return false;
            }
        });



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                x_editTextUserName.setVisible(charSequence.length() != 0, true);
                if (charSequence.length() != 0){
                    if(CURRENT_LOCALE.equals("en"))
                        editText.setCompoundDrawables(null, null, x_editTextUserName, null);
                    else
                        editText.setCompoundDrawables(x_editTextUserName, null,null , null);
                }
                else
                    editText.setCompoundDrawables(null, null, null, null);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                x_editTextUserName.setVisible(charSequence.length() != 0, true);
                if (charSequence.length() != 0){
                    if(CURRENT_LOCALE.equals("en"))
                        editText.setCompoundDrawables(null, null, x_editTextUserName, null);
                    else
                        editText.setCompoundDrawables(x_editTextUserName, null,null , null);
                }
                else
                    editText.setCompoundDrawables(null, null, null, null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public static void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    public static boolean openMakani(final String plotnumber, final Activity activity) {
        Intent intentNativeMakani = activity.getPackageManager().getLaunchIntentForPackage("com.dm.makani");

        if (!Global.isConnected(activity)) {
            AlertDialogUtil.errorAlertDialog(activity.getString(R.string.lbl_warning), ((MainActivity) activity).getString(R.string.internet_connection_problem1), activity.getString(R.string.ok), activity);
            return false;
        }

        AlertDialogUtil.navigateToMakaniAlert(activity.getString(R.string.open_makani_confirm),
                activity.getString(R.string.ok), activity.getString(R.string.cancel), activity, activity, plotnumber);
        return true;
    }

    public static boolean isAppInstalled(String packageName,Activity context) {
        Intent mIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        }
        else {
            return false;
        }
    }

    private static final String arabic = "\u06f0\u06f1\u06f2\u06f3\u06f4\u06f5\u06f6\u06f7\u06f8\u06f9";

    public static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length(); ) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }

    /*public static String arabicNumberToDecimal(String str){
        char[] arabicChars = {'٠','١','٢','٣','٤','٥','٦','٧','٨','٩'};
        StringBuilder builder = new StringBuilder();
        for(int i =0;i<str.length();i++)
        {
            if(Character.isDigit(str.charAt(i)))
            {
                builder.append(arabicChars[(int)(str.charAt(i))-48]);
            }
            else
            {
                builder.append(str.charAt(i));
            }
        }
        return builder.toString();
    }*/

    public static String arabicNumberToDecimal(String str) {
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
                case ' ':
                    builder.append(" ");
                    break;
                case '٠':
                    builder.append("0");
                    break;
                case '١':
                    builder.append("1");
                    break;
                case '٢':
                    builder.append("2");
                    break;
                case '٣':
                    builder.append("3");
                    break;
                case '٤':
                    builder.append("4");
                    break;
                case '٥':
                    builder.append("5");
                    break;
                case '٦':
                    builder.append("6");
                    break;
                case '٧':
                    builder.append("7");
                    break;
                case '٨':
                    builder.append("8");
                    break;
                case '٩':
                    builder.append("9");
                    break;
                default:
                    builder.append(str.charAt(i));
                    break;
            }

        }
        return builder.toString();
    }

    public static void logout(Context context) {
        Global.session = null;

        Global.app_session_token = null;
        context.getSharedPreferences(Global.MYPREFERENCES,Context.MODE_PRIVATE).edit().putString(Global.APP_SESSION_TOKEN,"").apply();
        Global.current_fragment_id = null;
//        AttachmentFragment.deliveryByCourier=false;
        Global.requestId = null;
        Global.isBookmarks = false;
        Global.isRecreate = false;
        isLanguageChanged =true;
        //Global.alertDialog = null;
        Global.isDeliveryByCourier= false;
        Global.isLogout =true;
        Global.selectedTab =0;
        if(Global.alertDialog!=null){
            Global.alertDialog.cancel();
            Global.alertDialog = null;

        }
//        AttachmentFragment.isDeliveryDetails=false;

        /*//Global.loginDetails.showFormPrefilledOnRememberMe=true;
        if(Global.loginDetails!=null && LoginActivity.loginVM.authListener!=null)
            LoginActivity.loginVM.authListener.saveUserToRemember(Global.loginDetails);*/

        Gson gson = new GsonBuilder().serializeNulls().create();
        /*if(loginDetails!=null)
            context.getSharedPreferences(MYPREFERENCES,Context.MODE_PRIVATE).edit().putString(AppConstants.USER_LOGIN_DETAILS, gson.toJson(loginDetails)).apply();*/
        //PreferenceManager.getDefaultSharedPreferences(context).edit().putString(AppConstants.USER_LOGIN_DETAILS, gson.toJson(Global.loginDetails)).apply();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static boolean isValidTrimedString(String value) {
        if (value == null || value.trim() == "") {
            return false;
        }
        return true;
    }


    public static String getPlatformRemark()
    {
        return AppConstants.DEVICE_TYPE + "-" +Build.VERSION.RELEASE;
    }

    public static float getScreenHeight(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        float height = size.y;

        return pixelToDp(activity,height);

    }
    public static float getScreenWidth(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        float width = size.x;

        return pixelToDp(activity,width);

    }
    public static float pixelToDp(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float dpToPixel(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
    public static void expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {

            @Override
            protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                v.getLayoutParams().height = interpolatedTime == 1 ? WindowManager.LayoutParams.WRAP_CONTENT : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {

            @Override
            protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
                super.applyTransformation(interpolatedTime, t);
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static String generateRandomID(){
        Character chars[]= {0,1,2,3,4,5,6,7,8,9,'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        String password = "";

        for(int i=0;i<31;i++){
            password += chars[(int) Math.floor(Math.random() * chars.length)];
        }
        return password;

    }

    public static int sp2Px(Float sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }
    public static int dp2Px(Float dp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());

    }


    public static void hideSoftKeyboard(Activity activity,IBinder view) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view, 0);
    }
}

