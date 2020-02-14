package dm.sime.com.kharetati.datas.repositories;

import org.json.JSONObject;

import dm.sime.com.kharetati.datas.models.BookmarksResponse;
import dm.sime.com.kharetati.datas.models.SerializeBookmarkModel;
import dm.sime.com.kharetati.datas.models.ZZBookmark;
import dm.sime.com.kharetati.datas.network.MyApiService;
import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.Response;

public class BookMarkRepository {
    private MyApiService api;

    public BookMarkRepository(MyApiService apiService){
        this.api = apiService;
    }

    public Observable<BookmarksResponse> getAllBookMarks(SerializeBookmarkModel model) {
        return api.getAllBookMarks(model);
    }

    public Observable<JSONObject> deleteBookMark(SerializeBookmarkModel model) {
        return api.deleteBookMark(model);
    }
}
