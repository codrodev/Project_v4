package dm.sime.com.kharetati.view.navigators;

import java.util.List;

import dm.sime.com.kharetati.datas.models.Bookmark;

public interface BookMarksNavigator {

    public void onStarted();
    public void onSuccess();
    public void onFailure(String Msg);


   public void onDeleteSuccess(List<Bookmark> lstBookmark);
}
