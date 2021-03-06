package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.esri.arcgisruntime.tasks.geoprocessing.GeoprocessingLong;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;

import java.util.ArrayList;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.CreateUpdateRequestResponse;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.models.RetrieveProfileDocsResponse;
import dm.sime.com.kharetati.datas.models.SerializedCreateAndUpdateModel;
import dm.sime.com.kharetati.datas.repositories.PayRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.ImageCropActivity;
import dm.sime.com.kharetati.view.activities.LoginActivity;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.fragments.AttachmentFragment;
import dm.sime.com.kharetati.view.fragments.ParentSiteplanFragment;
import dm.sime.com.kharetati.view.fragments.PayFragment;
import dm.sime.com.kharetati.view.fragments.PaymentFragment;
import dm.sime.com.kharetati.view.navigators.PayNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static dm.sime.com.kharetati.view.fragments.PayFragment.paymentType;

public class PayViewModel extends ViewModel {
    private KharetatiApp kharetatiApp;
    public PayNavigator payNavigator;
    private Activity activity;
    private PayRepository repository;
    public static String voucherNo,voucherAmountText,customerName,mobileNo,emailId,eradPaymentURL,callBackURL,responseMsg;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private long parcelId;
    private int voucherAmount;
    public static ArrayList hm;

    public PayViewModel(Activity activity, PayRepository repository) {
        this.activity = activity;
        this.repository = repository;
        kharetatiApp = KharetatiApp.create(activity);
    }

    public void createAndUpdateRequest() throws JSONException {

        payNavigator.onStarted();
        AttachmentFragment.attachmentModel.attachmentNavigator.navigateToPay();
        SerializedCreateAndUpdateModel model = new SerializedCreateAndUpdateModel();
        if(Global.isUAE){
            model.setMy_id(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getUuid());
        } else {
            model.setMy_id(Global.loginDetails.username);
        }
        model.setToken(Global.site_plan_token);
        model.setIs_owner(Global.rbIsOwner);
        model.setIs_owned_by_person(Global.isPerson);
        model.setApplicant_mobile(PayFragment.applicantMobile);
        model.setApplicant_email_id(PayFragment.applicantEmailId);
        model.setRequest_source("KHARETATI");
        model.setParcel_id(Integer.parseInt(Global.mapSearchResult.getService_response().getParcelId()));
        model.setRequest_id(Global.requestId == null ? "" : Global.requestId);
        model.setIs_hard_copy_reqd(Global.isDeliveryByCourier ? "Y":"N");
        model.setPayment_type(paymentType);
        if(Global.isDeliveryByCourier)
            model.setDelivery_details(Global.deliveryDetails);
        if(Global.isPerson){
        if(Global.passportData != null && Global.passportData.size() > 0) {
            model.setPassport_docs(Global.passportData);
        }}
        if(!Global.isPerson){
        if(Global.licenseData != null && Global.licenseData.size() > 0) {
            model.setLicense(Global.licenseData);
        }}
        if((Global.isPerson && !Global.rbIsOwner)||!Global.isPerson){
        if(Global.nocData != null && Global.nocData.size() > 0) {
            model.setNoc_docs(Global.nocData);
        }
        }
        model.setLocale(Global.CURRENT_LOCALE);
        String url = Global.base_url_site_plan+"/createUpdateRequest";
        Gson ob = new Gson();
        String x = ob.toJson(model);
        Log.d(getClass().getSimpleName()+" =================>",x);
        try {
            Disposable disposable = repository.createAndUpdateRequest(url,model)
                    .subscribeOn(kharetatiApp.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CreateUpdateRequestResponse>() {
                        @Override public void accept(CreateUpdateRequestResponse createUpdateRequestResponse) throws Exception {

                            try
                            {
                                Log.d("CreateUpdateResponse:",ob.fromJson(createUpdateRequestResponse.toString(),CreateUpdateRequestResponse.class).toString());
                                createUpdateRequest(ob.fromJson(createUpdateRequestResponse.toString(),CreateUpdateRequestResponse.class));
                                //...
                            }
                            catch (IllegalStateException | JsonSyntaxException exception)
                            {
                                //.
                                createUpdateRequest(createUpdateRequestResponse);
                            }



                        }
                    }, new Consumer<Throwable>() {
                        @Override public void accept(Throwable throwable) throws Exception {
                            payNavigator.onFailure(throwable.getMessage());
                            //homeNavigator.onFailure("Unable to connect the remote server");
                        }
                    });
            compositeDisposable.add(disposable);
        } catch (Exception ex){
            payNavigator.onFailure(ex.getMessage());
        }

    }

    private void createUpdateRequest(CreateUpdateRequestResponse createUpdateRequestResponse) {

        if(createUpdateRequestResponse!=null){
            //ParentSiteplanViewModel.initializeDocuments();
            int status=createUpdateRequestResponse.getStatus();
            String msg=Global.CURRENT_LOCALE.compareToIgnoreCase("en")==0 ?
                    createUpdateRequestResponse.getMessageEn():createUpdateRequestResponse.getMessageAr();

            if(status == 403){
                payNavigator.onFailure(msg);
            } else if(status == 405){
                payNavigator.onFailure(msg);
                ((MainActivity)activity).loadFragment(FragmentTAGS.FR_HOME,true,null);
            }else{
                Global.requestId= createUpdateRequestResponse.getRequestId();
                voucherNo= createUpdateRequestResponse.getVoucherNo();
                voucherAmount= createUpdateRequestResponse.getVoucherAmount();
                voucherAmountText= createUpdateRequestResponse.getVoucherAmountText();
                customerName= createUpdateRequestResponse.getCustomerName();
                parcelId= createUpdateRequestResponse.getparcelID();
                mobileNo= createUpdateRequestResponse.getMobile();
                emailId= createUpdateRequestResponse.getEmailId();
                eradPaymentURL= createUpdateRequestResponse.getEradPaymentUrl();
                callBackURL= createUpdateRequestResponse.getCallbackUrl();
                responseMsg= Global.CURRENT_LOCALE.compareToIgnoreCase("en")==0 ? createUpdateRequestResponse.getMessageEn() : createUpdateRequestResponse.getMessageAr();
                int locale=Global.CURRENT_LOCALE.compareToIgnoreCase("en")== 0 ? 1 : 2;
                Global.paymentUrl = eradPaymentURL+"&locale="+locale+"&VoucherNo="+voucherNo+"&PayeeNameEN="+customerName+"&MobileNo="+mobileNo+"&eMail="+emailId+"&ReturnURL="+callBackURL;



                if(paymentType.compareToIgnoreCase("Pay Now")==0){

                    if(status==600){
                        ImageCropActivity.isImageCropped =false;
                        PaymentFragment.isFromPaymentFagment =true;

                        ArrayList al = new ArrayList<>();
                        al.add(Global.paymentUrl);

                        Bundle bundle = new Bundle();
                        bundle.putString("userid", Global.isUAE?Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getUuid():Global.loginDetails.username);
                        bundle.putString("requestId", Global.requestId);
                        bundle.putString("parcelId", Global.mapSearchResult.getService_response().getParcelId());
                        bundle.putString("voucherNo", voucherNo );
                        bundle.putString("mobileNo", mobileNo);
                        bundle.putString("emailId", emailId);
                        bundle.putString("customerName", customerName);
                        bundle.putBoolean("hasChosenDelivery",Global.isDeliveryByCourier );
                        bundle.putString("paymentType",paymentType);
                        bundle.putBoolean("isOwner",Global.rbIsOwner);
                        bundle.putBoolean("isPerson",Global.isPerson);
                        MainActivity.firebaseAnalytics.logEvent("CreateUpdateRequest", bundle);

                        //((MainActivity)activity).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                        ParentSiteplanFragment.parentModel.retrieveProfileDocs();
                        payNavigator.onSuccess();
                    } else if(status==402){

                        if(msg!=null)
                            payNavigator.onFailure(msg);
                        else
                            showErrorMessage("");
                    }else if(status==405) {

                        ((MainActivity)activity).loadFragment(FragmentTAGS.FR_HOME,true,null);
                    }
                    else {
                        if (msg == null || msg.trim().length() < 1) {
                            showErrorMessage("");
                        } else {
                            payNavigator.onFailure(msg);                        }
                    }
                }
                else if(paymentType.compareToIgnoreCase("Pay later")==0)
                {
                    if(status==600){
                        hm= new ArrayList();
                        hm.add(Global.requestId);
                        hm.add(parcelId);
                        hm.add(voucherNo);
                        hm.add(voucherAmount);
                        hm.add(eradPaymentURL);
                        hm.add(callBackURL);
                        hm.add(customerName);
                        hm.add(mobileNo);
                        hm.add(emailId);
                        ArrayList al = new ArrayList<>();
                        al.add(Global.paymentUrl);
                        //((MainActivity)activity).loadFragment(FragmentTAGS.FR_REQUEST_DETAILS,true,hm);
                        payNavigator.onSuccess();
                        ParentSiteplanFragment.parentModel.retrieveProfileDocs();
                    }

                    else if(status==402){

                        if(msg!=null)
                            payNavigator.onFailure(msg);
                        else
                            showErrorMessage("");
                    }else if(status==405) {

                        ((MainActivity)activity).loadFragment(FragmentTAGS.FR_HOME,true,null);

                    }
                    else {
                        if (msg == null || msg.trim().length() < 1||msg.equals("")) {
                                showErrorMessage("");
                        } else {
                            payNavigator.onFailure(msg);
                        }
                    }

                }

            }

        }

    }
    public void showErrorMessage(String exception){
        if(Global.appMsg!=null){
            payNavigator.onFailure(Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getErrorFetchingDataEn():Global.appMsg.getErrorFetchingDataAr());
        }
        else
            if(activity!=null)payNavigator.onFailure(activity.getResources().getString(R.string.error_response));

        Log.d(activity.getClass().getSimpleName(),exception);
    }
}
