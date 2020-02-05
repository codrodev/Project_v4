package dm.sime.com.kharetati.datas.repositories;

import dm.sime.com.kharetati.datas.models.BookmarksResponse;
import dm.sime.com.kharetati.datas.network.MyApiService;
import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.Response;

public class BookMarkRepository {
    private MyApiService api;

    public BookMarkRepository(MyApiService apiService){
        this.api = apiService;
    }

    public Observable<BookmarksResponse> getAllBookMarks(int userId) {
        return api.getAllBookMarks(userId);
    }
}
