package dm.sime.com.kharetati.view.navigators;

public interface HomeNavigator {
    public void onStarted();
    public void onFailure(String Msg);

    public void onSuccess();

    public void populateAreaNames();

    public void populateGridMenu();
}
