package dm.sime.com.kharetati.datas.models.models;

import androidx.databinding.BaseObservable;

import java.util.List;

import dm.sime.com.kharetati.datas.models.Bookmark;

/**
 * Created by Imran on 9/7/2017.
 */

public class BookmarksResponse extends BaseObservable {
    public boolean isError;
    public String message;
    public List<Bookmark> bookmarklist;
}
