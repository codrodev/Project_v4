package dm.sime.com.kharetati.view.navigators;

import dm.sime.com.kharetati.datas.models.SearchResult;
import dm.sime.com.kharetati.datas.models.SerializableParcelDetails;

public interface MapNavigator {

    public void onStarted();
    public void onSuccess();
    public void onFailure(String Msg);

    public void findParcelID(SearchResult response);

    public void getPlotDetais(SerializableParcelDetails appResponse);
}
