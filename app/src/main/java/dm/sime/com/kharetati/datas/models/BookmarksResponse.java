package dm.sime.com.kharetati.datas.models;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Imran on 9/7/2017.
 */

public class BookmarksResponse extends BaseObservable {
    @SerializedName("isError")
    public boolean isError;
    @SerializedName("message")
    public String message;
    @SerializedName("bookmarklist")
    public Bookmark[] bookmarklist;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Bookmark[] getBookmarklist() {
        return bookmarklist;
    }

    public void setBookmarklist(Bookmark[] bookmarklist) {
        this.bookmarklist = bookmarklist;
    }


}
