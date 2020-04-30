package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.MyMapResults;
import dm.sime.com.kharetati.datas.models.RetrieveDocStreamResponse;
import dm.sime.com.kharetati.datas.models.RetrieveMyMapResponse;
import dm.sime.com.kharetati.datas.models.SerializedModel;
import dm.sime.com.kharetati.datas.network.MyApiService;
import dm.sime.com.kharetati.datas.repositories.MyMapRepository;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppConstants;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.view.adapters.MyMapAdapter;
import dm.sime.com.kharetati.view.navigators.MyMapNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MyMapViewModel extends ViewModel {

    private static final String PACKAGE_DRIVE = "com.google.android.apps.docs";
    private static String currentRequestedSitePlanIdForViewing;
    MyMapAdapter adapter;
    RetrieveMyMapResponse model;
    public List<MyMapResults> lstMyMap = new ArrayList<>();
    MutableLiveData<List<MyMapResults>> mutableMyMap = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MyApiService apiService;
    private MyMapRepository repository;
    private KharetatiApp kharetatiApp;
    private Activity activity;
    public MyMapNavigator myMapNavigator;
    private String msg;


    public MyMapViewModel(Activity context, MyMapRepository repository){
        this.activity = context;
        this.repository = repository;
        kharetatiApp = KharetatiApp.create(activity);

    }

    public void initializeMyMapViewModel(Context context){
        model = new RetrieveMyMapResponse();

        //mutableMyMap.setValue(model.getMyMapList());
        if(Global.isUserLoggedIn)
            getAllSitePlans(context);
    }

    public MutableLiveData<List<MyMapResults>> getMutableMyMap(){
        return mutableMyMap;
    }

    public void getMyMapList(){
        //return mutableInAppNotifications.getValue();
    }

    public MyMapResults getCurrentMyMap(int position){
        if (mutableMyMap.getValue() != null ) {
            return mutableMyMap.getValue().get(position);
        }
        return null;
    }

    public void  setMyMapAdapter(List<MyMapResults> lstMyMap) {
        this.lstMyMap =  lstMyMap;
        this.adapter.setMyMap(this.lstMyMap);
        this.adapter.notifyDataSetChanged();



    }

    public MyMapAdapter getMyMapAdapter() {
        return adapter;
    }
    public void clearData(){
        getMutableMyMap().getValue().clear();
        lstMyMap.clear();
    }


    public void getAllSitePlans(Context context){

        myMapNavigator.onStarted();

        String url = Global.base_url_site_plan + "/retrieveMyMaps";

        Map<String, Object> params = new HashMap<>();
        params.put("token", Global.site_plan_token);
        params.put("locale", Global.CURRENT_LOCALE);
        params.put("my_id", Global.loginDetails.username);

        JSONObject body = new JSONObject(params);
        /*body.put("token",Global.site_plan_token);
        body.put("locale",Global.CURRENT_LOCALE);
        body.put("my_id",Global.loginDetails.username);*/


        //HTTPRequestBody.SitePlanBody body = new HTTPRequestBody.SitePlanBody();


        SerializedModel model = new SerializedModel();
        model.setToken(Global.site_plan_token);
        model.setLocale(Global.CURRENT_LOCALE);
        if(Global.isUAE){
            model.setMy_id(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getUuid());
        } else {
            model.setMy_id(Global.loginDetails.username);
        }
        String x = new Gson().toJson(model);
        try {
            Disposable disposable = repository.getAllSitePlans(url,model)
                    .subscribeOn(kharetatiApp.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<RetrieveMyMapResponse>() {
                        @Override public void accept(RetrieveMyMapResponse retrieveMyMapResponse) throws Exception {
                                if(retrieveMyMapResponse !=null)
                                    getSitePlans(retrieveMyMapResponse, context);

                        }
                    }, new Consumer<Throwable>() {
                        @Override public void accept(Throwable throwable) throws Exception {
                            showErrorMessage(throwable.getMessage());
                            //homeNavigator.onFailure("Unable to connect the remote server");
                        }
                    });
            compositeDisposable.add(disposable);
        } catch (Exception ex){
                myMapNavigator.onFailure(ex.getMessage());
        }
    }

    private void getSitePlans(RetrieveMyMapResponse retrieveMyMapResponse, Context context) {
        if(retrieveMyMapResponse.getMyMapResults() != null && retrieveMyMapResponse.getMyMapResults().length > 0) {
            mutableMyMap = new MutableLiveData<>();
            mutableMyMap.setValue(new ArrayList<>(Arrays.asList(retrieveMyMapResponse.getMyMapResults())));
            adapter = new MyMapAdapter(R.layout.adapter_mymap, this, context);
            setMyMapAdapter(new ArrayList<>(Arrays.asList(retrieveMyMapResponse.getMyMapResults())));
            myMapNavigator.onSuccess();
        } else if(retrieveMyMapResponse.getMyMapResults() == null || retrieveMyMapResponse.getMyMapResults().length == 0){
            myMapNavigator.onEmpty(Global.CURRENT_LOCALE.equals("en")? Global.appMsg.getMymaps_not_found_en():Global.appMsg.getMymaps_not_found_ar());
        } else
            myMapNavigator.onFailure(activity.getResources().getString(R.string.error_response));

    }

    public void showMessage(String exception){
        myMapNavigator.onFailure(exception);
    }

    public void viewSitePlan(String requestId) {

        Global.isFindSitePlan = false;
        MyMapViewModel.currentRequestedSitePlanIdForViewing =requestId;
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    AppConstants.PERMISSIONS_STORAGE,
                    AppConstants.REQUEST_EXTERNAL_STORAGE_SITEPLAN
            );
            return;
        }

        myMapNavigator.onStarted();

        SerializedModel model = new SerializedModel();
        model.setToken(Global.site_plan_token);
        model.setLocale(Global.CURRENT_LOCALE);
        model.setRequest_id(requestId);
        try {
            String url =Global.base_url_site_plan + AppUrls.RETRIEVE_SITE_PLAN_DOC_STREAM;
            Disposable disposable = repository.viewSitePlan(url,model)
                    .subscribeOn(kharetatiApp.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<RetrieveDocStreamResponse>() {
                        @Override public void accept(RetrieveDocStreamResponse viewSitePlanResponse) throws Exception {
                            viewSitePlanDoc(viewSitePlanResponse,requestId);

                        }
                    }, new Consumer<Throwable>() {
                        @Override public void accept(Throwable throwable) throws Exception {
                            myMapNavigator.onFailure(throwable.getMessage());
                            //homeNavigator.onFailure("Unable to connect the remote server");
                        }
                    });
            compositeDisposable.add(disposable);
        } catch (Exception ex){
            showErrorMessage(ex.getMessage());
        }
    }

    private void viewSitePlanDoc(RetrieveDocStreamResponse viewSitePlanResponse,String siteplanid) {

        if( viewSitePlanResponse.getStatus().equals("403")){
            msg =(Global.appMsg!=null)? (Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getErrorFetchingDataEn():Global.appMsg.getErrorFetchingDataAr()) :activity.getResources().getString(R.string.error_response);

            myMapNavigator.onFailure(msg);

        }
        else if(viewSitePlanResponse.getStatus().equals("410")){

            String msg =Global.CURRENT_LOCALE.equals("en")?viewSitePlanResponse.getMessage_en():viewSitePlanResponse.getMessage_ar();
            myMapNavigator.onFailure(msg);
        }
        else{
            if (viewSitePlanResponse.getDoc_details() != null)
            {

                byte[] bytes = Base64.decode(viewSitePlanResponse.getDoc_details().getDoc().getBytes(), Base64.DEFAULT);

                String fileName = "SITEPLAN_DOWNLOADED_" + String.valueOf(siteplanid) +
                        ".pdf";

                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                String filePath = extStorageDirectory + "/" + fileName;
                File folder = new File(extStorageDirectory);
                folder.mkdir();

                File pdfFile = new File(folder, fileName);
                try {
                    if (pdfFile.exists())
                        pdfFile.delete();
                    pdfFile.createNewFile();

                    FileOutputStream fileOutputStream = new FileOutputStream(filePath);

                    fileOutputStream.write(bytes);
                    fileOutputStream.close();

                    Uri path = Uri.fromFile(pdfFile);
                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                    pdfIntent.setDataAndType(path, "application/pdf");
                    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    /*ArrayList al = new ArrayList();
                    al.add(path);*/
                    myMapNavigator.onViewSitePlanSuccess();
                    if (Global.isAppInstalled(PACKAGE_DRIVE, activity)) {
                        pdfIntent.setPackage(PACKAGE_DRIVE);
                    }
                    activity.startActivity(pdfIntent);

//                    ((MainActivity)activity).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);


                } catch (IOException e) {
                    e.printStackTrace();

                    showErrorMessage(e.getMessage());
                }
            } else {
                showErrorMessage("");

            }
        }

    }
    public void showErrorMessage(String exception){
        if(Global.appMsg!=null){
            myMapNavigator.onFailure(Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getErrorFetchingDataEn():Global.appMsg.getErrorFetchingDataAr());
        }
        else
            myMapNavigator.onFailure(activity.getResources().getString(R.string.error_response));
        if(exception!=null)
        Log.d(activity.getClass().getSimpleName(),exception);
    }
}
