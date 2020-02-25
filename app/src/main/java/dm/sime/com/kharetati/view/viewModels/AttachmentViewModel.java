package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.DocArr;
import dm.sime.com.kharetati.datas.models.Docs;
import dm.sime.com.kharetati.datas.models.RetrieveDocStreamResponse;
import dm.sime.com.kharetati.datas.models.SerializedModel;
import dm.sime.com.kharetati.datas.repositories.AttachmentRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppConstants;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.view.fragments.AttachmentFragment;
import dm.sime.com.kharetati.view.navigators.AttachmentNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static dm.sime.com.kharetati.view.fragments.AttachmentFragment.COMPANY_LICENCE;
import static dm.sime.com.kharetati.view.fragments.AttachmentFragment.LETTER_FROM_OWNER;
import static dm.sime.com.kharetati.view.fragments.AttachmentFragment.PASSPORT;
import static dm.sime.com.kharetati.view.fragments.AttachmentFragment.VISA_PASSPORT;
import static dm.sime.com.kharetati.view.fragments.AttachmentFragment.isOldDocContainsPassport;

public class AttachmentViewModel extends ViewModel {

    private KharetatiApp kharetatiApp;
    private AttachmentRepository repository;
    private Activity activity;
    public AttachmentNavigator attachmentNavigator;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int checkPermissionCode;
    private File dwldsPath;


    public AttachmentViewModel(Activity context, AttachmentRepository repository){
        this.activity = context;
        this.repository = repository;

        kharetatiApp = KharetatiApp.create(activity);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void retrieveDoc(int docId, int position) {
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            checkPermissionCode= AppConstants.REQUEST_READ_EXTERNAL_STORAGE;
            activity.requestPermissions(
                    AppConstants.PERMISSIONS_STORAGE,checkPermissionCode

            );
            return;
        }
        attachmentNavigator.onStarted();
        String url = AppUrls.RETRIEVE_DOC_STREAM;
        SerializedModel model = new SerializedModel();
        model.setToken(Global.site_plan_token);
        model.setLocale(Global.CURRENT_LOCALE);
        model.setDoc_id(docId);
        try {
            Disposable disposable = repository.retrieveDoc(url,model)
                    .subscribeOn(kharetatiApp.subscribeScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<RetrieveDocStreamResponse>() {
                        @Override public void accept(RetrieveDocStreamResponse retrieveDocStreamResponse) throws Exception {
                            retriveDocs(retrieveDocStreamResponse,docId);

                        }
                    }, new Consumer<Throwable>() {
                        @Override public void accept(Throwable throwable) throws Exception {
                            attachmentNavigator.onFailure(throwable.getMessage());
                            //homeNavigator.onFailure("Unable to connect the remote server");
                        }
                    });
            compositeDisposable.add(disposable);
        } catch (Exception ex){
            attachmentNavigator.onFailure(ex.getMessage());
        }

    }

    private void retriveDocs(RetrieveDocStreamResponse retrieveDocStreamResponse,int docId) {

        if(retrieveDocStreamResponse!=null){
            boolean isError=false;
            String resStatus=retrieveDocStreamResponse.getStatus();
            int status=Integer.parseInt(resStatus);

            String msg=Global.CURRENT_LOCALE.equals("en") ? retrieveDocStreamResponse.getMessage_en():retrieveDocStreamResponse.getMessage_ar();
            if( status == 403){
                isError= true;
                if(msg!=null||msg.equals("")) attachmentNavigator.onFailure(msg);

            }
            else{
                    attachmentNavigator.updateUI(retrieveDocStreamResponse,docId);

            }
        }

    }

    public boolean isImageFormat(String format) {
        boolean isImage = false;
        if (format.compareToIgnoreCase("jpg") == 0 ||
                format.compareToIgnoreCase("png") == 0 ||
                format.compareToIgnoreCase("image/png") == 0 ||
                format.compareToIgnoreCase("image/jpg") == 0 ||
                format.compareToIgnoreCase("image/jpeg") == 0 ||
                format.compareToIgnoreCase("jpeg") == 0) {
            isImage = true;
        }
        return isImage;
    }
}
