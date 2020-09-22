package dm.sime.com.kharetati.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.Bookmark;
import dm.sime.com.kharetati.utility.constants.AppConstants;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.LoginActivity;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.activities.WebViewActivity;
import dm.sime.com.kharetati.view.customview.CleanableEditText;
import dm.sime.com.kharetati.view.fragments.AttachmentFragment;
import dm.sime.com.kharetati.view.fragments.BookmarkFragment;
import dm.sime.com.kharetati.view.fragments.WebViewFragment;
import dm.sime.com.kharetati.view.navigators.MainNavigator;

import static dm.sime.com.kharetati.utility.constants.AppConstants.USER_LOGIN_DETAILS;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_CHAT_WEBVIEW;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_CONTACT_US;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_WEBVIEW;

public class AlertDialogUtil {


    private static final String MAKANI_PACKAGE = "com.dm.makani" ;
    public static final String DM_CHAT_URL_AR ="https://chat.dm.gov.ae/Arabic/chatOrCallback.html?lang=ar&dir=rtl&chatUsername=";
    public static final String DM_CHAT_URL_EN = "https://chat.dm.gov.ae/English/chatOrCallback.html?chatUsername=";
    private static final String DM_PHONE_NUMBER = "800900";


    private static ViewGroup viewGroup;

    public static void errorAlertDialog(String title, String message, String btnTxt, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                //.setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_error_outline_black_24dp)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        positiveButton.setAllCaps(false);

        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setTypeface(face);
        textView.setPadding(80, 25, 25, 10);
    }
    public static void ratingAlertDialog(String title, String message, String btnTxt, String btnTxt2, final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }
                })
                .setNegativeButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        if (Global.CURRENT_LOCALE == "ar") {
                            intent.setData(Uri.parse(AppUrls.URL_RATE_US_AR));
                        } else {
                            intent.setData(Uri.parse(AppUrls.URL_RATE_US_EN));
                        }
                        context.startActivity(intent);

                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);


    }
    public static void FeedBackSuccessAlert(String message, String btnTxt, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //((MainActivity)context).loadFragment(FR_CONTACT_US,true,null);
                        ((MainActivity)context).onBackPressed();

                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
//        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
//        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }
    public static void callAlert(String message, String btnTxt, String btnTxt2, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();


                    }

                }).setNegativeButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + DM_PHONE_NUMBER));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        context.startActivity(intent);
                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
//        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
//        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }public static void logoutAlert(String message, String btnTxt, String btnTxt2, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }

                }).setNegativeButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Global.logout(context);
                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
//        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
//        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }
    public static void chatAlert(String message, String btnTxt, String btnTxt2, final Context context) {

        final CleanableEditText input = new CleanableEditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        input.setGravity(Gravity.CENTER);
        input.setEms(12);
        input.requestFocus();
        input.setHint("9715XXXXXXXX");
        input.setBackgroundColor(Color.parseColor("#00000000"));
        input.setPadding(40,0,40,0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(80,10,80,0);
        input.setLayoutParams(params);
        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        /*AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage(context.getResources().getString(R.string.please_enter_phone));

                        alert.setView(input);

*/


                    }

                }).setNegativeButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String chatURL="";
                        if (Global.CURRENT_LOCALE=="ar") {
                            chatURL = DM_CHAT_URL_AR;
                        } else {
                            chatURL = DM_CHAT_URL_EN;
                        }
                        if (!mobileNoInitialValidation(input))
                            Toast.makeText(context, context.getResources().getString(R.string.mobile_validation), Toast.LENGTH_SHORT).show();
                        else {
                            Global.hideSoftKeyboard((MainActivity)context);
                            chatURL += input.getText().toString();
                            ArrayList al = new ArrayList();
                            al.add(chatURL);
                            al.add(context.getResources().getString(R.string.livechat));
                            ((MainActivity)context).loadFragment(FR_CHAT_WEBVIEW,true,al);

                        /*alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Put actions for OK button here

                            }
                        });*/
                        }
                    }
                }).setView(input).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        //TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);


        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        input.setTypeface(face);
        //textView1.setTypeface(face);

        textView.setPadding(25, 25, 25, 10);


    }
    public static void timeoutAlertDialog(String title, String message, String btnTxt, Activity context) {
        try {

            if (!((MainActivity) context).isFinishing()) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setMessage(message)
                        .setTitle(title)
                        .setCancelable(false)
                        .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Global.current_fragment_id = null;
                                //Global.loginDetails.showFormPrefilledOnRememberMe=true;
                                Global.isTimeout = true;
                                Global.logout(context);
                        /*Gson gson = new GsonBuilder().serializeNulls().create();
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(USER_LOGIN_DETAILS, gson.toJson(Global.loginDetails)).apply();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);*/

                            }

                        }).show();

                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
//        TextView textView1 = (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

                TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
                //TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
                Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
                textView.setTypeface(face);
                positiveButton.setAllCaps(false);
                //negativeButton.setAllCaps(false);
                positiveButton.setTypeface(face);
                //negativeButton.setTypeface(face);
//        textView1.setTypeface(face);

                textView.setPadding(80, 25, 25, 10);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }



    public static void navigateToMakaniAlert(String message, String btnTxt, String btnTxt2, final Context context, final Activity activity, String plot) {
        final String plotnumber = (Global.current_fragment_id == FR_CONTACT_US ? "1190353" : plot);

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }

                }).setNegativeButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intentOpenBrowser = new Intent(Intent.ACTION_VIEW);
                        //intentOpenBrowser.addCategory(Intent.CATEGORY_BROWSABLE);
                        String makaniurl;
                        if (Global.CURRENT_LOCALE.equals("en")) {
                            Global.webViewUrl = String.format(AppUrls.MAKANI_URL, "E", plotnumber);
                        } else {
                            Global.webViewUrl = String.format(AppUrls.MAKANI_URL, "A", plotnumber);
                        }
                        intentOpenBrowser.setData(Uri.parse(Global.webViewUrl));
                        MainActivity.firebaseAnalytics.setCurrentScreen((MainActivity)context,"MAKANI SCREEN" , null /* class override */);
                        if(Global.isAppInstalled(MAKANI_PACKAGE,activity)){
                            intentOpenBrowser.setPackage(MAKANI_PACKAGE);
                            activity.startActivity(intentOpenBrowser);

                        }
                        else{
                            /*ArrayList al = new ArrayList();
                            al.add(Global.webViewUrl);
                            al.add(context.getResources().getString(R.string.makani));
                            ((MainActivity)context).loadFragment(FR_WEBVIEW,true,al);*/
                            intentOpenBrowser.setData(Uri.parse(Global.webViewUrl));
                            activity.startActivity(intentOpenBrowser);
                        }

                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1 = (TextView) alertDialog.findViewById(R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }
    public static void backPressedAlertDialog(String title, String message, String btnTxt, String btnTxt2, final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }
                })
                .setNegativeButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(Global.current_fragment_id.equals(FR_WEBVIEW))
                            ((Activity) context).onBackPressed();
                        else{
                            ((Activity) context).finish();
                            Global.isRecreate = false;
                        }
                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        //TextView textView1 = (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        //textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }
    public static void paymentBackAlert(String title,String message, String btnTxt, String btnTxt2, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //(((MainActivity)context)).getSupportFragmentManager().popBackStackImmediate();

                        dialog.cancel();

                    }

                }).setNegativeButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ((MainActivity)context).changeActionBarStatus(true);
                        (((MainActivity)context)).clearBackStack();
                        (((MainActivity)context)).getSupportFragmentManager().beginTransaction().remove(new WebViewFragment()).commit();
                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
//        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
//        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }

   /* private static final String DM_PHONE_NUMBER ="800900" ;

    public static void warningAlertDialog(String title, String message, String btnTxt, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();


                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }



    public static void ratingAlertDialog(String title, String message, String btnTxt, String btnTxt2, final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_thumb_up_black_24dp)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        if (Global.CURRENT_LOCALE == "ar") {
                            intent.setData(Uri.parse(Constant.URL_RATE_US_AR));
                        } else {
                            intent.setData(Uri.parse(Constant.URL_RATE_US_EN));
                        }
                        context.startActivity(intent);


                    }
                })
                .setNegativeButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);


    }
    public static void DubaiIdAlert(String title, String message, String btnTxt, String btnTxt2, final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_thumb_up_black_24dp)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = Global.getCurrentLanguage((MainActivity)context).compareToIgnoreCase("en")==0 ? Constant.dubaiID_url_en : Constant.dubaiID_url_ar;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);


                    }
                })
                .setNegativeButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);


    }

    public static void permissionAlertDialog(String title, String message, String btnTxt, String btnTxt2, final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);


                    }
                })
                .setNegativeButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }

    public static void backPressedAlertDialog(String title, String message, String btnTxt, String btnTxt2, final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(Global.current_fragment_id==FR_PAYMENT)
                            ((Activity) context).onBackPressed();
                        else
                            ((Activity) context).finish();



                    }
                })
                .setNegativeButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1 = (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }

    public static void timeoutAlertDialog(String title, String message, String btnTxt, final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Constant.parcelLayerExportUrl_en = Constant.parcelLayerExportUrl_en.substring(0, Constant.parcelLayerExportUrl_en.indexOf("?"));
                        Constant.parcelLayerExportUrl_ar = Constant.parcelLayerExportUrl_ar.substring(0, Constant.parcelLayerExportUrl_ar.indexOf("?"));
                        Global.current_fragment_id = null;
                        //Global.loginDetails.showFormPrefilledOnRememberMe=true;
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(USER_LOGIN_DETAILS, gson.toJson(Global.loginDetails)).apply();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);


                    }

                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1 = (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        //TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        //negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        //negativeButton.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }

    public static void guestLoginAlertDialog(String title, String message, String btnTxt, String btnTxt2, final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Global.current_fragment_id = null;
                        //Global.loginDetails.showFormPrefilledOnRememberMe=true;
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(USER_LOGIN_DETAILS, gson.toJson(Global.loginDetails)).apply();
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);


                    }

                }).setNegativeButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1 = (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }



    public static void callAlert(String message, String btnTxt, String btnTxt2, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + DM_PHONE_NUMBER));
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        context.startActivity(intent);

                    }

                }).setNegativeButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void chatAlert(String message, String btnTxt, String btnTxt2, final Context context) {

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        input.setGravity(Gravity.CENTER);
        input.requestFocus();
        input.setHint("05XXXXXXXX");
        input.setBackgroundColor(Color.parseColor("#00000000"));
        input.setPadding(40,0,40,0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(80,10,80,0);
        input.setLayoutParams(params);
        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        *//*AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage(context.getResources().getString(R.string.please_enter_phone));

                        alert.setView(input);

*//*

                        String chatURL="";
                        if (Constant.CURRENT_LOCALE=="ar") {
                            chatURL = DM_CHAT_URL_AR;
                        } else {
                            chatURL = DM_CHAT_URL_EN;
                        }
                        if (input.getText().toString().trim().length() <8 )
                            Toast.makeText(context, context.getResources().getString(R.string.enter_valid_mobile), Toast.LENGTH_SHORT).show();
                        else {
                            chatURL += input.getText().toString();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(chatURL));
                            context.startActivity(i);
                        *//*alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Put actions for OK button here

                            }
                        });*//*
                        }
                    }

                }).setNegativeButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setView(input).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);


        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        input.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(25, 25, 25, 10);

    }
    public static void paymentBackAlert(String title,String message, String btnTxt, String btnTxt2, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_help_black_24dp)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //(((MainActivity)context)).getSupportFragmentManager().popBackStackImmediate();
                        ((MainActivity)context).getSupportActionBar().show();
                        (((MainActivity)context)).clearBackStack();
                        (((MainActivity)context)).getSupportFragmentManager().beginTransaction().remove(new PaymentFragment()).commit();

                        //(((MainActivity)context)).clearBackStack();
                        *//*if(AttachmentFragment.submit != null) {
                            AttachmentFragment.submit.setEnabled(false);
                        }*//*

                    }

                }).setNegativeButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }public static void paymentErrorAlert(String message, String btnTxt, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }

                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }
    public static void deliveryByCourierAlert(String message, String btnTxt, String btnTxt2, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_help_black_24dp)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //context.startActivity(new Intent(context,));
                        ((MainActivity)context).createAndLoadFragment(FR_DELIVERY,true,null);

                    }


                }).setNegativeButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AttachmentFragment.deliveryByCourier=false;
                        AttachmentFragment.chk_deliveryByCourier.setChecked(false);
                        dialog.cancel();

                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }
    public static void updateAlert(String message, String btnTxt, String btnTxt2, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_help_black_24dp)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PackageInfo pinfo = null;
                        try {
                            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        String name=pinfo.packageName;
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse( "https://play.google.com/store/apps/details?id=" + name));
                        context.startActivity(intent);

                    }


                }).setNegativeButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();




                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }

    public static void submitEnquiryAlert(String message, String btnTxt, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_help_black_24dp)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)context).onBackPressed();

                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }
    public static void FeedBackSuccessAlert(String message, String btnTxt, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_help_black_24dp)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)context).createAndLoadFragment(FR_CONTACT_US,false,null);

                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }
    public static void landRegistrationAlert(String title, String message,String btnDontShow, String btnOk, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setNegativeButton(btnDontShow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(IS_LANDREG_MSG_NOT_CHECKED, false).apply();
                        dialog.cancel();

                    }
                }).setPositiveButton(btnOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Global.isLandRegMessageDisplayed = true;
                        dialog.cancel();

                    }
                }).show();


        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1 = (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(true);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        textView1.setTypeface(face);
        negativeButton.setTextColor(context.getResources().getColor(R.color.land_dialog_color));
        positiveButton.setTextColor(context.getResources().getColor(R.color.land_dialog_color));

        textView.setPadding(80, 25, 80, 10);
        textView1.setPadding(33, 15, 33, 10);

    }

    public static void alreadyinProgressAlert(String message, String btnTxt, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_help_black_24dp)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                        context.getApplicationContext();
                        ((MainActivity)context).createAndLoadFragment(Constant.FR_DOWNLOADEDSITEPLAN, false, null);

                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }
    public static void leavingdeliveryByCourierAlert(String message, String btnTxt, String btnTxt2, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_help_black_24dp)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //context.startActivity(new Intent(context,));
                        ((MainActivity)context).back();
                        AttachmentFragment.deliveryByCourier=false;
                        AttachmentFragment.chk_deliveryByCourier.setChecked(false);
                        AttachmentFragment.isDeliveryDetails =true;


                    }


                }).setNegativeButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.cancel();

                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }public static void rootDetectionAlert(String message, String btnTxt, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //context.startActivity(new Intent(context,));
                        ((LanguageActivity)context).finish();



                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }


    static AlertDialog b;
    static AlertDialog.Builder dialogBuilder;

    public static void ShowProgressDialog(Context context) {
        dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.progressdialog, null);
        TextView textView=(TextView) dialogView.findViewById(R.id.textView_progress);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setText(context.getResources().getString(R.string.msg_loading));
        textView.setTypeface(face);
        textView.setPadding(40, 5, 40, 5);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.show();
    }

    public static void HideProgressDialog(){

        b.dismiss();
    }

    public interface positiveButtonClicked{
        public void onPositiveButtonClicked();
    }
    public interface negativeButtonClicked{
        public void onNegativeButtonClicked();
    }
    public static void forceUpdateAlert(String message, String btnTxt, String btnTxt2, final Context context) {
        //final String plotnumber = (Global.current_fragment_id == FR_CONTACT_US ? "1190353" : plot);

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        PackageInfo pinfo = null;
                        try {
                            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        String name=pinfo.packageName;
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse( "https://play.google.com/store/apps/details?id=" + name));
                        context.startActivity(intent);



                    }

                }).setNegativeButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        TextView textView1 = (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }*/

    public static void downloaNocAlert(String message, String btnTxt, String btnTxt2, final Activity context) {
        //final String plotnumber = (Global.current_fragment_id == FR_CONTACT_US ? "1190353" : plot);

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();

                    }

                }).setNegativeButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int permission = ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            // We don't have permission so prompt the user
                            ActivityCompat.requestPermissions((MainActivity)context,
                                    AppConstants.PERMISSIONS_STORAGE,
                                    AppConstants.REQUEST_READ_EXTERNAL_STORAGE
                            );
                            return;
                        }
                        new AttachmentFragment.DownloadFile(context).execute(Global.noctemplateUrl, "NocTemplate.pdf");
                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        //TextView textView1 = (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        //textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }

    public static void registerAlert(String message, String btnTxt, String btnTxt2, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }


                }).setNegativeButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Global.webViewUrl = Global.CURRENT_LOCALE.compareToIgnoreCase("en")==0 ? AppUrls.registration_url_en: AppUrls.registration_url_ar ;

                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.setData(Uri.parse(Global.webViewUrl));
                        context.startActivity(intent);

                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        //TextView textView1= (TextView) alertDialog.findViewById(androidx.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        //textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }
    public static void forgotPasswordAlert(String message, String btnTxt, String btnTxt2, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }


                }).setNegativeButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Global.webViewUrl = Global.CURRENT_LOCALE.compareToIgnoreCase("en")==0 ? AppUrls.forgotpassword_url_en: AppUrls.forgotpassword_url_ar ;

                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.setData(Uri.parse(Global.webViewUrl));
                        context.startActivity(intent);

                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        //TextView textView1= (TextView) alertDialog.findViewById(android.R.id.);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        //textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }
    public static void dubaiIDAlert(String message, String btnTxt, String btnTxt2, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String url = Global.CURRENT_LOCALE.compareToIgnoreCase("en")==0 ? AppUrls.dubaiID_url_en: AppUrls.dubaiID_url_ar;

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    }


                }).setNegativeButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        //TextView textView1= (TextView) alertDialog.findViewById(android.R.id.);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        //textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }
    public static void bookMarksDeleteAlert(String title, String message, String btnTxt, String btnTxt2, final Context context, Bookmark data,int position) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.cancel();

                    }

                }).setNegativeButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        BookmarkFragment.bmModel.deleteBookMark(data);
                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
//        TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
//        textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }

    public static void bookMarksEditAlert(final Activity context, Bookmark data) {


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setCancelable(false);

        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");


        View viewInflated = LayoutInflater.from(context).inflate(R.layout.bookmarks_description_dialog,
                (ViewGroup)context.findViewById(android.R.id.content), false);
        // Set up the input
        final CleanableEditText txtdescEn = (CleanableEditText) viewInflated.findViewById(R.id.edtBookmarksDescriptionDialogEn);
        txtdescEn.requestFocus();
        txtdescEn.setTypeface(face);
        txtdescEn.setText((data.descriptionEn!=null&&!data.descriptionEn.isEmpty())?data.descriptionEn:((data.descriptionAr!=null&&!data.descriptionAr.isEmpty())?data.descriptionAr:""));
        txtdescEn.setImeOptions(EditorInfo.IME_ACTION_NONE);

        final CleanableEditText txtdescAr = (CleanableEditText) viewInflated.findViewById(R.id.edtBookmarksDescriptionDialogAr);
        txtdescAr.setImeOptions(EditorInfo.IME_ACTION_NONE);
        txtdescAr.requestFocus();
        txtdescAr.setTypeface(face);

        txtdescAr.setText((data.descriptionAr!=null&&!data.descriptionAr.isEmpty())?data.descriptionAr:((data.descriptionEn!=null&&!data.descriptionEn.isEmpty())?data.descriptionEn:""));

        final TextInputLayout enInputLayout = (TextInputLayout) viewInflated.findViewById(R.id.enInputLayout);
        final TextInputLayout arInputLayout = (TextInputLayout) viewInflated.findViewById(R.id.arInputLayout);

        if(Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0){
            txtdescEn.setVisibility(View.VISIBLE);
            txtdescEn.requestFocus();
            enInputLayout.setVisibility(View.VISIBLE);
            txtdescAr.setVisibility(View.GONE);
            arInputLayout.setVisibility(View.GONE);
        } else {
            txtdescEn.setVisibility(View.GONE);
            txtdescAr.requestFocus();
            enInputLayout.setVisibility(View.GONE);
            txtdescAr.setVisibility(View.VISIBLE);
            arInputLayout.setVisibility(View.VISIBLE);
        }
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final android.app.AlertDialog dialog = builder.create();

        dialog.show();

        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        TextView positiveButton = (Button) dialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) dialog.findViewById(android.R.id.button2);

        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);

        negativeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                if(txtdescEn.getText().toString().isEmpty() && txtdescAr.getText().toString().isEmpty()){
                    Toast.makeText(context,context.getResources().getString(R.string.enter_favourite_name) , Toast.LENGTH_SHORT).show();
                } else {
                    data.descriptionEn = txtdescEn.getText().toString()!=null? txtdescEn.getText().toString():"";
                    data.descriptionAr = txtdescAr.getText().toString()!=null?txtdescAr.getText().toString():"";
                    dialog.dismiss();
                    BookmarkFragment.bmModel.editBookMark(data);
                }
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });

    }

    public static void showProgressBar(Activity context,boolean isShow) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup


        if (context != null) {
            if (!((Activity) context).isFinishing())
            {
                viewGroup = context.findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(context).inflate(R.layout.progressbar_layout, viewGroup, false);



                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(context);


                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                if (Global.alertDialog == null){
                    Global.alertDialog = builder.create();
                    /*Global.alertDialog.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(0x00000000, 0x00000000));
                    Global.alertDialog.getWindow().getDecorView().getBackground().setAlpha(0);*/
                }

                if (isShow) {
                    Global.alertDialog.setCancelable(false);
                    if (Global.alertDialog != null && !Global.alertDialog.isShowing())
                        Global.alertDialog.show();
                } else {
                    if (Global.alertDialog != null)
                        Global.alertDialog.cancel();
                }
            }
            else {
                if (Global.alertDialog != null)
                    Global.alertDialog.cancel();

            }
        }

    }

    public static void alreadyinProgressAlert(String message, String btnTxt, final Context context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                        context.getApplicationContext();

                        ((MainActivity)context).loadFragment(FragmentTAGS.FR_DASHBOARD,true,null);
                        //MainActivity.mainVM.mainNavigator.onSuccess();
                        MainActivity.mainVM.mainNavigator.navigateToDashboard(0);

                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        //TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        //textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }

    public static void successfulLogoutAlert(String message, String btnTxt, final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                        if(dialog!=null)
                            dialog.cancel();

                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        //TextView textView1= (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face= Typeface.createFromAsset(context.getAssets(),"Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        //textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);
    }
    public static void forceUpdateAlert(String message, String btnTxt, String btnTxt2, final Context context) {
        //final String plotnumber = (Global.current_fragment_id == FR_CONTACT_US ? "1190353" : plot);

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        PackageInfo pinfo = null;
                        try {
                            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        String name=pinfo.packageName;
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse( "https://play.google.com/store/apps/details?id=" + name));
                        context.startActivity(intent);



                    }

                }).setNegativeButton(btnTxt2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        //TextView textView1 = (TextView) alertDialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);

        TextView positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
        TextView negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "Dubai-Regular.ttf");
        textView.setTypeface(face);
        positiveButton.setAllCaps(false);
        negativeButton.setAllCaps(false);
        positiveButton.setTypeface(face);
        negativeButton.setTypeface(face);
        //textView1.setTypeface(face);

        textView.setPadding(80, 25, 25, 10);

    }
    public static boolean isValidMobile(EditText input,Activity context){
        boolean isValid = true;
        if (TextUtils.isEmpty(input.getText().toString())) {

            //AlertDialogUtil.errorAlertDialog(context.getResources().getString(R.string.lbl_warning), context.getResources().getString(R.string.mobile_validation), context.getResources().getString(R.string.ok),context);
            isValid=false;

            return isValid;
        }
        if (mobileNoInitialValidation(input) == false) {
            //AlertDialogUtil.errorAlertDialog(context.getResources().getString(R.string.lbl_warning), context.getResources().getString(R.string.mobile_validation), context.getResources().getString(R.string.ok), context);
            isValid=false;

            return isValid;
        }
        if (input.getText().length() != 12) {
            //AlertDialogUtil.errorAlertDialog(context.getString(R.string.lbl_warning),context.getString(R.string.mobile_validation), context.getResources().getString(R.string.ok), context);
            isValid=false;

            return isValid;
        }
        return isValid;
    }

    public static boolean mobileNoInitialValidation(EditText input){
        boolean isValid = false;
        if (!input.getText().toString().isEmpty()) {
            if (input.getText().toString().length() == 12) {
                if (input.getText().toString().startsWith("971")) {

                    try {
                        String st = String.valueOf(input.getText().toString().charAt(3));
                        int val = Integer.parseInt(st);
                        if (val > 0) {
                            isValid = true;

                        }
                    } catch (Exception ex) {

                    }

                }
            }
        }
        return isValid;
    }
}
