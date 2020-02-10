package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.annotations.SerializedName;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.BookmarksResponse;
import dm.sime.com.kharetati.datas.models.SerializeBookMarksModel;
import dm.sime.com.kharetati.datas.models.SerializeBookmarkModel;
import dm.sime.com.kharetati.datas.models.ZZBookmark;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.MyApiService;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.BookMarkRepository;
import dm.sime.com.kharetati.datas.repositories.UserRepository;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.adapters.BookmarkAdapter;
import dm.sime.com.kharetati.view.navigators.BookMarksNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;

public class BookmarkViewModel extends ViewModel {
    BookmarkAdapter adapter;
    BookmarksResponse model;
    MutableLiveData<List<ZZBookmark>> mutableBookmark = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private BookMarkRepository repository;
    private KharetatiApp kharetatiApp;
    private Activity activity;
    public BookMarksNavigator bookMarksNavigator;
    @SerializedName("UserID")
    private int userId;
    private ArrayList<ZZBookmark> arrayList;


    public BookmarkViewModel(Activity context, BookMarkRepository repository){
        this.activity = context;
        this.repository = repository;
    }

    /*public BookmarkViewModel(){

    }*/

    public void initializeBookmarkViewModel(Context context){
        model = new BookmarksResponse();
        getAllBookMarks();
        /*mutableBookmark = new MutableLiveData<>();
        mutableBookmark.setValue(model.getMyMapList());
        adapter = new BookmarkAdapter(R.layout.adapter_bookmark, this, context);*/
    }

    public MutableLiveData<List<ZZBookmark>> getMutableBookmark(){
        return mutableBookmark;
    }

    public void getBookmarkList(){
        //return mutableInAppNotifications.getValue();
    }

    public ZZBookmark getCurrentBookmark(int position){
        if (mutableBookmark.getValue() != null ) {
            return mutableBookmark.getValue().get(position);
        }
        return null;
    }

    public void  setBookmarkAdapter(List<ZZBookmark> lstMyMap) {
        this.adapter.setBookmark(lstMyMap);
        this.adapter.notifyDataSetChanged();
    }

    public BookmarkAdapter getBookmarkAdapter() {
        return adapter;
    }

    public void getAllBookMarks() {


        bookMarksNavigator.onStarted();
        kharetatiApp = KharetatiApp.create(activity);
        //userId =Global.sime_userid;
        userId =1003;

        SerializeBookmarkModel model = new SerializeBookmarkModel();
        model.setUserID(1003);




        Disposable disposable = repository.getAllBookMarks(model)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BookmarksResponse>() {
                    @Override public void accept(BookmarksResponse bookmarkResponse) throws Exception {
                        getBookMarks(bookmarkResponse);

                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        bookMarksNavigator.onFailure(throwable.getMessage());

                    }
                });


        compositeDisposable.add(disposable);

    }

    private void getBookMarks(BookmarksResponse bookmarksResponse) {
        mutableBookmark = new MutableLiveData<>();

        mutableBookmark.setValue(Arrays.asList(bookmarksResponse.getBookmarklist()));
        adapter = new BookmarkAdapter(R.layout.adapter_bookmark, this, activity);
        bookMarksNavigator.onSuccess();


    }

}
