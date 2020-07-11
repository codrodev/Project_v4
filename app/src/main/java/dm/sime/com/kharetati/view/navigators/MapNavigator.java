package dm.sime.com.kharetati.view.navigators;

import android.view.Menu;
import android.view.MenuItem;

import dm.sime.com.kharetati.datas.models.SearchResult;
import dm.sime.com.kharetati.datas.models.SerializableParcelDetails;

public interface MapNavigator {

    public void onStarted();
    public void onSuccess();
    public void onFailure(String Msg);

    public void findParcelID(SearchResult response);
    public void findParcelForBookmarks(String plotno);


    public void getPlotDetais(SerializableParcelDetails appResponse);

    public void onMenuSelected(MenuItem item);

    public void onMenCreated(Menu menu);
}
