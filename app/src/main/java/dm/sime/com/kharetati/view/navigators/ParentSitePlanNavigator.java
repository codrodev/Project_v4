package dm.sime.com.kharetati.view.navigators;

public interface ParentSitePlanNavigator {
    public void setNextEnabledStatus(Boolean status);
    public void onStarted();
    public void onSuccess();
    public void onFailure(String Msg);


    public void navigateToFragment(int position);
}
