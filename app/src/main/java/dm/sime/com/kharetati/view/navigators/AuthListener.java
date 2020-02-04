package dm.sime.com.kharetati.view.navigators;

import android.content.pm.PackageManager;

import dm.sime.com.kharetati.datas.models.LoginDetails;
import dm.sime.com.kharetati.datas.models.User;

public interface AuthListener {
    public void onStarted();
    public void onSuccess();
    public void onFailure(String message);
    public void onProgress();
    public void addUserToHistory(String Username);
    public void saveUserToRemember(LoginDetails loginDetails);
    public void saveUser(User user);
    public void showAppUpdateAlert() throws PackageManager.NameNotFoundException;
    public void showMessage(String message);
}
