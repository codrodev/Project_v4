package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.BookmarksResponse;
import dm.sime.com.kharetati.datas.models.ZZBookmark;
import dm.sime.com.kharetati.datas.models.ZZBookmarkModel;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.MyApiService;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.BookMarkRepository;
import dm.sime.com.kharetati.datas.repositories.UserRepository;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.adapters.BookmarkAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class BookmarkViewModel extends ViewModel {
    BookmarkAdapter adapter;
    ZZBookmarkModel model;
    MutableLiveData<List<ZZBookmark>> mutableBookmark = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private BookMarkRepository repository;
    private KharetatiApp kharetatiApp;
    private Activity activity;



    public BookmarkViewModel(Activity context, BookMarkRepository repository){
        this.activity = context;
        this.repository = repository;
    }

    /*public BookmarkViewModel(){

    }*/

    public void initializeBookmarkViewModel(Context context){
        model = new ZZBookmarkModel();
        mutableBookmark = new MutableLiveData<>();
        mutableBookmark.setValue(model.getMyMapList());
        adapter = new BookmarkAdapter(R.layout.adapter_bookmark, this, context);
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



        kharetatiApp = KharetatiApp.create(activity);


        Disposable disposable = repository.getAllBookMarks(Global.sime_userid)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BookmarksResponse>() {
                    @Override public void accept(BookmarksResponse bookmarksResponse) throws Exception {
                        getBookMarks(bookmarksResponse);

                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        //homeNavigator.onFailure("Unable to connect the remote server");
                    }
                });

        compositeDisposable.add(disposable);

    }

    private void getBookMarks(BookmarksResponse bookmarksResponse) {


    }

}
