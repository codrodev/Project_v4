package dm.sime.com.kharetati.view.navigators;

public interface MyMapNavigator {
    public void onStarted();
    public void onSuccess();
    public void onEmpty(String Msg);
    public void onFailure(String Msg);

    public void sortSiteplans(boolean ascending);

    public void onViewSitePlanSuccess();
}
