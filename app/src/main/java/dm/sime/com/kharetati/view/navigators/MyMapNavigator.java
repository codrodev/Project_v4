package dm.sime.com.kharetati.view.navigators;

public interface MyMapNavigator {
    public void onStarted();
    public void onSuccess();
    public void onFailure(String Msg);

    public void onViewSitePlanSuccess();
}
