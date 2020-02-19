package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;

import androidx.lifecycle.ViewModel;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.datas.models.CreateUpdateRequestResponse;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.models.RetrieveProfileDocsResponse;
import dm.sime.com.kharetati.datas.models.SerializedCreateAndUpdateModel;
import dm.sime.com.kharetati.datas.repositories.PayRepository;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.view.fragments.PayFragment;
import dm.sime.com.kharetati.view.navigators.PayNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class PayViewModel extends ViewModel {
    private KharetatiApp kharetatiApp;
    public PayNavigator payNavigator;
    private Activity activity;
    private PayRepository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public PayViewModel(Activity activity, PayRepository repository) {
        this.activity = activity;
        this.repository = repository;
        kharetatiApp = KharetatiApp.create(activity);
    }

    public void createAndUpdateRequest() {
        payNavigator.onStarted();
        SerializedCreateAndUpdateModel model = new SerializedCreateAndUpdateModel();
        model.setMy_id(Global.loginDetails.username);
        model.setToken(Global.site_plan_token);
        model.setIs_owner(Global.rbIsOwner);
        model.setIs_owned_by_person(Global.isPerson);
        model.setApplicant_mobile(PayFragment.applicantMobile);
        model.setApplicant_email_id(PayFragment.applicantEmailId);
        model.setRequest_source("KHARETATI");
        model.setParcel_id(Long.parseLong(PlotDetails.parcelNo));
        model.setRequest_id(Global.requestId == null ? "" : Global.requestId);
        model.setIs_hard_copy_reqd(Global.isDeliveryByCourier ? "Y":"N");
        model.setPayment_type(PayFragment.paymentType);
        if(Global.isDeliveryByCourier)
            model.setDelivery_details(Global.deliveryDetails);
        model.setPassport_docs(Global.passportData);
        model.setLicense(Global.licenseData);
        model.setNoc_docs(Global.nocData);
        model.setLocale(Global.CURRENT_LOCALE);
        String url = AppUrls.CREATE_UPDATE_REQUEST;

        try {
            Disposable disposable = repository.createAndUpdateRequest(url,model)
                    .subscribeOn(kharetatiApp.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CreateUpdateRequestResponse>() {
                        @Override public void accept(CreateUpdateRequestResponse createUpdateRequestResponse) throws Exception {
                            createUpdateRequest(createUpdateRequestResponse);

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

    }
}
