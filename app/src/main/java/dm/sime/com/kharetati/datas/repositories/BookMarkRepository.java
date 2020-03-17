package dm.sime.com.kharetati.datas.repositories;

import org.json.JSONObject;

import dm.sime.com.kharetati.datas.models.BookmarksResponse;
import dm.sime.com.kharetati.datas.models.GeneralResponse;
import dm.sime.com.kharetati.datas.models.SerializableSaveBookMarks;
import dm.sime.com.kharetati.datas.models.SerializeBookmarkModel;
import dm.sime.com.kharetati.datas.models.ZZBookmark;
import dm.sime.com.kharetati.datas.network.MyApiService;
import dm.sime.com.kharetati.view.viewModels.SerializeBookmarkEditModel;
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

    public Observable<SerializableSaveBookMarks> deleteBookMark(SerializeBookmarkModel model) {
        return api.deleteBookMark(model);
    }

    public Observable<GeneralResponse> editBookMark(SerializeBookmarkEditModel model) {
        return api.editBookMark(model);
    }
}
