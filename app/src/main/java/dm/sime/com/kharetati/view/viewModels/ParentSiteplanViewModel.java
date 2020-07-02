package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.DeliveryDetails;
import dm.sime.com.kharetati.datas.models.DocArr;
import dm.sime.com.kharetati.datas.models.HTTPRequestBody;
import dm.sime.com.kharetati.datas.models.MakaniToDLTMResponse;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.models.RetrieveProfileDocsResponse;
import dm.sime.com.kharetati.datas.models.RetrievedDeliveryDetails;
import dm.sime.com.kharetati.datas.models.SerializeGetAppInputRequestModel;
import dm.sime.com.kharetati.datas.models.SerializeGetAppRequestModel;
import dm.sime.com.kharetati.datas.models.SerializedModel;
import dm.sime.com.kharetati.datas.repositories.ParentSitePlanRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.fragments.AttachmentFragment;
import dm.sime.com.kharetati.view.fragments.DeliveryFragment;
import dm.sime.com.kharetati.view.fragments.ParentSiteplanFragment;
import dm.sime.com.kharetati.view.fragments.PayFragment;
import dm.sime.com.kharetati.view.navigators.FragmentNavigator;
import dm.sime.com.kharetati.view.navigators.ParentSitePlanNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static android.content.Context.MODE_PRIVATE;
import static dm.sime.com.kharetati.utility.Global.makani;

public class ParentSiteplanViewModel extends ViewModel {

    private KharetatiApp kharetatiApp;
    private Activity activity;
    private ParentSitePlanRepository repository;
    FragmentNavigator frNavigator;
    public ParentSitePlanNavigator parentSitePlanNavigator;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public static int status;
    public static RetrievedDeliveryDetails deliveryDetails= new RetrievedDeliveryDetails();
    public static String applicantMailId;
    public static String applicantMobileNo;

    private static ArrayList<DocArr> downloadedDoc;
    private static ArrayList<DocArr> newlyAttachedDoc;

    public static ArrayList<DocArr> getDownloadedDoc() {
        return downloadedDoc;
    }

    public static void addDownloadedDoc(DocArr doc) {
        ParentSiteplanViewModel.downloadedDoc.add(doc);
    }

    public static ArrayList<DocArr> getNewlyAttachedDoc() {
        return newlyAttachedDoc;
    }

    public static void addNewlyAttachedDoc(DocArr doc) {
        ParentSiteplanViewModel.newlyAttachedDoc.add(doc);
    }

    public ParentSiteplanViewModel(Activity context, ParentSitePlanRepository repository) {
        this.activity = context;
        this.repository = repository;
        kharetatiApp = KharetatiApp.create(activity);
        if(downloadedDoc == null){
            downloadedDoc = new ArrayList<>();
        }
        if(newlyAttachedDoc == null){
            newlyAttachedDoc = new ArrayList<>();
        }
    }

    public static void initializeDocuments(){
        downloadedDoc = new ArrayList<>();
        newlyAttachedDoc = new ArrayList<>();
    }

    public void manageAppBar(Context ctx, boolean status){
        frNavigator = (FragmentNavigator) ctx;
        frNavigator.manageActionBar(status);
    }

    public void manageAppBottomBAtr(Context ctx, boolean status){
        frNavigator = (FragmentNavigator) ctx;
        frNavigator.manageBottomBar(status);
    }

    public void retrieveProfileDocs(){

        parentSitePlanNavigator.onStarted();
        String url = Global.base_url_site_plan+"/retrieveProfileDocs";
        SerializedModel model = new SerializedModel();
        model.setToken(Global.site_plan_token);
        model.setLocale(Global.CURRENT_LOCALE);
        if(Global.isUAE){
            model.setMy_id(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getUuid());
        } else {
            model.setMy_id(Global.loginDetails.username);
        }
        model.setIs_owner(Global.rbIsOwner);
        model.setIs_owned_by_person(Global.isPerson);
        String x =new Gson().toJson(model);
        try {
            Disposable disposable = repository.retrieveProfileDocs(url,model)
                    .subscribeOn(kharetatiApp.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<RetrieveProfileDocsResponse>() {
                        @Override public void accept(RetrieveProfileDocsResponse retrieveProfileDocsResponse) throws Exception {
                            getProfileDoc(retrieveProfileDocsResponse);

                        }
                    }, new Consumer<Throwable>() {
                        @Override public void accept(Throwable throwable) throws Exception {
                            AttachmentFragment.attachmentModel.attachmentNavigator.nextButtonStatusForAttachment();;
                                if(throwable instanceof JsonSyntaxException){
                                    AlertDialogUtil.showProgressBar(activity,false);
                                    AttachmentFragment.attachmentModel.attachmentNavigator.nextButtonStatusForAttachment();
                                }
                                else
                                    showErrorMessage(throwable.getMessage());

                        }
                    });
            compositeDisposable.add(disposable);
        } catch (Exception ex){
            showErrorMessage(ex.getMessage());
        }

    }
    public void getMakaniToDLTM(String makani){

        parentSitePlanNavigator.onStarted();

        //Global.makani ="3003295320";

        HTTPRequestBody.MakaniBody makaniBody = new HTTPRequestBody.MakaniBody();
        makaniBody.MAKANI =makani;
        String url = AppUrls.BASE_AUXULARY_URL + "/makanitodltm";

        SerializeGetAppRequestModel model = new SerializeGetAppRequestModel();

        SerializeGetAppInputRequestModel inputModel = new SerializeGetAppInputRequestModel();
        inputModel.setMAKANI(makani);
        if(Global.isUAE){
            inputModel.setTOKEN(Global.uaeSessionResponse == null ? "" : Global.uaeSessionResponse.getService_response().getToken());
        } else {
            inputModel.setTOKEN(Global.app_session_token == null ? "" : Global.app_session_token);
        }
        inputModel.setREMARKS(Global.getPlatformRemark());
        model.setInputJson(inputModel);

        Disposable disposable = repository.getMakaniToDLTM(url,model)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MakaniToDLTMResponse>() {
                    @Override public void accept(MakaniToDLTMResponse makaniToDLTMResponse) throws Exception {
                        gotoMakani(makaniToDLTMResponse);
                        parentSitePlanNavigator.setNextEnabledStatus(true);

                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        showErrorMessage(throwable.getMessage());
                    }
                });

        compositeDisposable.add(disposable);

    }

    private void gotoMakani(MakaniToDLTMResponse makaniToDLTMResponse) {

        //Global.makani = getMakaniNumber();

        if (!Global.isConnected(activity)) {

            showInternetError();
            Global.isValidMakani =false;
        }
        else{

            if(makaniToDLTMResponse!=null){

                if (makaniToDLTMResponse != null && !Boolean.valueOf(makaniToDLTMResponse.getIs_exception())) {
                    Global.dltm = makaniToDLTMResponse.getDLTMContainer().getDLTM();
                    String parcelId = makaniToDLTMResponse.getDLTMContainer().getPARCEL_ID();
                    PlotDetails.parcelNo=parcelId;


                    if(Global.isValidTrimedString(Global.dltm)){
                        Global.landNumber = null;
                        Global.area = null;
                        Global.area_ar = null;
                        Global.isMakani =true;
                        Global.isValidMakani =true;
                        //homeNavigator.onSuccess();


                        parentSitePlanNavigator.onMakaniSuccess();


                        /*if(DeliveryFragment.isDeliveryFragment)
                            return ;
                        else
                            navigate(activity, FragmentTAGS.FR_MAP);*/

                    } else {
                        showInvalidMakaniError();
                        Global.isValidMakani =false;

                    }
                }
                else{
                    showInvalidMakaniError();
                    Global.isValidMakani =false;
                }

            }
            else{
                showErrorMessage("");
                Global.isValidMakani =false;
            }
        }

    }



    public void showInvalidMakaniError() {
        if(Global.appMsg!=null)
            parentSitePlanNavigator.onFailure(Global.CURRENT_LOCALE.equals("en")? Global.appMsg.getInvalidmakaniEn():Global.appMsg.getInvalidmakaniAr());
        else
            parentSitePlanNavigator.onFailure(activity.getResources().getString(R.string.invalid_makani));
    }
    private void showInternetError() {
        if(Global.appMsg!=null)
            parentSitePlanNavigator.onFailure(Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr());
        else
            parentSitePlanNavigator.onFailure(activity.getResources().getString(R.string.internet_connection_problem1));
        return;
    }

    private void getProfileDoc(RetrieveProfileDocsResponse retrieveProfileDocsResponse) {

        parentSitePlanNavigator.onSuccess();
        if(retrieveProfileDocsResponse!=null){

            if (retrieveProfileDocsResponse.getDocs() != null && retrieveProfileDocsResponse.getDocs().length > 0) {
                Global.docArr = retrieveProfileDocsResponse.getDocs();
            }

            if(retrieveProfileDocsResponse.getDeliveryDetails() != null){
                deliveryDetails = retrieveProfileDocsResponse.getDeliveryDetails();
            }
            if(retrieveProfileDocsResponse.getEmail_id() != null){
                applicantMailId = retrieveProfileDocsResponse.getEmail_id();
            } else {
                applicantMailId = "";
            }
            if(retrieveProfileDocsResponse.getMobile_no() != null){
                applicantMobileNo = retrieveProfileDocsResponse.getMobile_no();
            } else {
                applicantMobileNo = "";
            }

            status = Integer.parseInt(retrieveProfileDocsResponse.getStatus());
            String msg = "";
            if (Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0){
                if (retrieveProfileDocsResponse.getMessageEn() != null && retrieveProfileDocsResponse.getMessageEn().length() > 0) {
                    msg = retrieveProfileDocsResponse.getMessageEn();
                }
            } else {
                if (retrieveProfileDocsResponse.getMessageAr() != null && retrieveProfileDocsResponse.getMessageAr().length() > 0) {
                    msg = retrieveProfileDocsResponse.getMessageAr();
                }
            }

            if(PayFragment.paymentType != null && PayFragment.paymentType.length() > 0){
                if(PayFragment.paymentType.compareToIgnoreCase("Pay Now")==0){
                    ParentSiteplanFragment.currentIndex = 0;
                    if(Global.paymentUrl != null && Global.paymentUrl.length() > 0){
                        ArrayList al = new ArrayList<>();
                        al.add(Global.paymentUrl);
                        al.add(activity.getResources().getString(R.string.payment));

                        ((MainActivity)activity).loadFragment(FragmentTAGS.FR_WEBVIEW_PAYMENT,true,al);
                    }
                } else if(PayFragment.paymentType.compareToIgnoreCase("Pay later")==0){
                    ParentSiteplanFragment.currentIndex = 0;
                    if(PayViewModel.hm != null && PayViewModel.hm.size() > 0) {
                        ((MainActivity) activity).loadFragment(FragmentTAGS.FR_REQUEST_DETAILS, true, PayViewModel.hm);
                    }
                }
            } else {
                if (status == 403) {
                    if (Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0) {
                        if (msg != null || !msg.equals("")) parentSitePlanNavigator.onFailure(msg);
                    }
                } else if (status == 501) {
                    parentSitePlanNavigator.navigateToFragment(1);
                } else if (status == 500) {
                    parentSitePlanNavigator.navigateToFragment(1);
                } else if (status == 502) {
                    parentSitePlanNavigator.navigateToFragment(1);
                } else if (status == 503) {
                    parentSitePlanNavigator.navigateToFragment(1);
                } else if (status == 504 || status == 410) {
                    parentSitePlanNavigator.navigateToFragment(1);
                } else {
                    if (!msg.equals("")||msg!=null)
                        parentSitePlanNavigator.onFailure(msg);
                    else
                        showErrorMessage("");

                }
            }

        }

    }



    public void showErrorMessage(String exception){
        if(Global.appMsg!=null){
            parentSitePlanNavigator.onFailure(Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getErrorFetchingDataEn():Global.appMsg.getErrorFetchingDataAr());
        }
        else
            parentSitePlanNavigator.onFailure(activity.getResources().getString(R.string.error_response));

        Log.d(activity.getClass().getSimpleName(),exception);
    }
}
