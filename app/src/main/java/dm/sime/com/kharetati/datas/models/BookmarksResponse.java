package dm.sime.com.kharetati.datas.models;

import androidx.databinding.BaseObservable;

import java.util.List;

/**
 * Created by Imran on 9/7/2017.
 */

public class BookmarksResponse extends BaseObservable {
    public boolean isError;
    public String message;
    public List<Bookmark> bookmarklist;
}
