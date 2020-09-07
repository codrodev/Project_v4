package dm.sime.com.kharetati.view.navigators;

import java.util.List;

import dm.sime.com.kharetati.datas.models.Bookmark;


public interface BookMarksNavigator {

    public void onStarted();
    public void onSuccess();
    public void onEmpty(String Msg);
    public void onFailure(String Msg);


   public void onDeleteSuccess(List<Bookmark> lstBookmark);

   public void removeData(Bookmark data);

   public void updateAdapter();
   public void sortBookmarks(boolean descending);


    public void search(String plotNo);
}
