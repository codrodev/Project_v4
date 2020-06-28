package dm.sime.com.kharetati.view.navigators;

import java.util.List;

import dm.sime.com.kharetati.datas.models.NotificationResponse;

public interface MainNavigator {

    void handleError(Throwable throwable);

    void openLoginActivity();

    void fragmentNavigator(String fragment_tag, Boolean addToBackStack, List<Object> params);

    void manageActionBar(boolean key);

    void manageBottomBar(boolean key);

    public void navigateToDashboard(int position);

    public void onStarted();

    public void onFailure(String Msg);

    public void onSuccess();

    public void onWebViewBack();

    public void updateNotificationUI(NotificationResponse response);

    public void cancelNotification();

    public void setDot(int position);
}
