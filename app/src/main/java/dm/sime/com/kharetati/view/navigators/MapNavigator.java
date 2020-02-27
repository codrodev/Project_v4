package dm.sime.com.kharetati.view.navigators;

import dm.sime.com.kharetati.datas.models.SearchResult;

public interface MapNavigator {

    public void onStarted();
    public void onSuccess();
    public void onFailure(String Msg);

    public void findParcelID(SearchResult response);
}
