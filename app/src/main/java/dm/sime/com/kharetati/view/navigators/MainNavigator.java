package dm.sime.com.kharetati.view.navigators;

import java.util.List;

public interface MainNavigator {

    void handleError(Throwable throwable);

    void openLoginActivity();

    void fragmentNavigator(String fragment_tag, Boolean addToBackStack, List<Object> params);

    void manageActionBar(boolean key);

    void manageBottomBar(boolean key);

    public void navigateToDashboard();

    public void onStarted();

    public void onFailure(String Msg);

    public void onSuccess();

    public void onWebViewBack();
}
