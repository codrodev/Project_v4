package dm.sime.com.kharetati.view.navigators;

import android.app.Activity;

import org.json.JSONException;

import dm.sime.com.kharetati.datas.models.RetrieveDocStreamResponse;

public interface AttachmentNavigator {
    public void onStarted();
    public void onSuccess();
    public void onFailure(String Msg);
    public void navigateToPay() throws JSONException;

    public void updateUI(RetrieveDocStreamResponse retrieveDocStreamResponse, int docId);
}
