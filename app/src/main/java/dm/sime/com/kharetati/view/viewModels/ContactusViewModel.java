package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;

import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.BookmarksResponse;
import dm.sime.com.kharetati.datas.models.GeneralResponse;
import dm.sime.com.kharetati.datas.models.SerializableFeedBackModel;
import dm.sime.com.kharetati.datas.repositories.ContactusRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.fragments.FeedbackFragment;
import dm.sime.com.kharetati.view.fragments.MapFragment;
import dm.sime.com.kharetati.view.navigators.ContactusNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class ContactusViewModel extends ViewModel {

    private KharetatiApp kharetatiApp;
    private Activity activity;
    private ContactusRepository repository;
    public ContactusNavigator contactusNavigator;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public ContactusViewModel(Activity context, ContactusRepository repository){
        this.activity = context;
        this.repository = repository;
        kharetatiApp = KharetatiApp.create(activity);

    }

    public void sendFeedBack() {
        contactusNavigator.onStarted();

        SerializableFeedBackModel model = new SerializableFeedBackModel();
        model.setName(FeedbackFragment.name);
        model.setEmail(FeedbackFragment.email);
        model.setPhone(FeedbackFragment.phone);
        model.setSubject(FeedbackFragment.subject);
        model.setDescription(FeedbackFragment.description);
        Disposable disposable = repository.sendFeedBack(model)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GeneralResponse>() {
                    @Override public void accept(GeneralResponse feedbackResponse) throws Exception {
                        getFeedBackAck(feedbackResponse);

                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        contactusNavigator.onFailure(throwable.getMessage());

                    }
                });


        compositeDisposable.add(disposable);
    }

    private void getFeedBackAck(GeneralResponse feedbackResponse) {
        contactusNavigator.onSuccess();
        if (feedbackResponse != null && !feedbackResponse.isError()) {
            AlertDialogUtil.FeedBackSuccessAlert(Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getFeedbackSuccessEn(): Global.appMsg.getFeedbackSuccessAr(),activity.getString(R.string.ok),activity);
        } else {
            AlertDialogUtil.errorAlertDialog(activity.getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getTryAgainEn(): Global.appMsg.getTryAgainAr(), activity.getResources().getString(R.string.ok), activity);
        }


    }
}
