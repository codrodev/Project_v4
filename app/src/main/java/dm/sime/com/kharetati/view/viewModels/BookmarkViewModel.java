package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.content.Context;
import android.widget.Adapter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.Bookmark;
import dm.sime.com.kharetati.datas.models.BookmarksResponse;
import dm.sime.com.kharetati.datas.models.SerializableSaveBookMarks;
import dm.sime.com.kharetati.datas.models.SerializeBookmarkModel;
import dm.sime.com.kharetati.datas.repositories.BookMarkRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.adapters.BookmarkAdapter;
import dm.sime.com.kharetati.view.navigators.BookMarksNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class BookmarkViewModel extends ViewModel {
    BookmarkAdapter adapter;
    BookmarksResponse model;
    MutableLiveData<List<Bookmark>> mutableBookmark = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private BookMarkRepository repository;
    private KharetatiApp kharetatiApp;
    private Activity activity;
    public BookMarksNavigator bookMarksNavigator;
    @SerializedName("UserID")
    private int userId;



    public BookmarkViewModel(Activity context, BookMarkRepository repository){
        this.activity = context;
        this.repository = repository;
        kharetatiApp = KharetatiApp.create(activity);

    }

    /*public BookmarkViewModel(){

    }*/

    public void initializeBookmarkViewModel(Context context){
        model = new BookmarksResponse();
        getAllBookMarks();
        /*mutableBookmark = new MutableLiveData<>();
        mutableBookmark.setValue(Arrays.asList(model.getBookmarklist()));
        adapter = new BookmarkAdapter(R.layout.adapter_bookmark, this, context);*/
    }

    public MutableLiveData<List<Bookmark>> getMutableBookmark(){
        return mutableBookmark;
    }

    public void getBookmarkList(){
        //return mutableInAppNotifications.getValue();
    }

    public Bookmark getCurrentBookmark(int position){
        if (mutableBookmark.getValue() != null ) {
            return mutableBookmark.getValue().get(position);
        }
        return null;
    }

    public void  setBookmarkAdapter(List<Bookmark> lstMyMap) {
        this.adapter.setBookmark(lstMyMap);
        this.adapter.notifyDataSetChanged();
    }

    public BookmarkAdapter getBookmarkAdapter() {
        return adapter;
    }

    public void getAllBookMarks() {


        bookMarksNavigator.onStarted();
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


        mutableBookmark =new MutableLiveData<>();
        mutableBookmark.setValue(Arrays.asList(bookmarksResponse.getBookmarklist()));
        adapter = new BookmarkAdapter(R.layout.adapter_bookmark, this, activity);

        setBookmarkAdapter(Arrays.asList(bookmarksResponse.getBookmarklist()));      //bookMarksNavigator.updateUI();


        bookMarksNavigator.onSuccess();


    }

    public void deleteBookMark(Bookmark data) {
        bookMarksNavigator.onStarted();
        SerializeBookmarkModel model = new SerializeBookmarkModel();
        model.setUserID(1003);
        model.setParcelNumber(data.ParcelNumber);

        Disposable disposable = repository.deleteBookMark(model)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SerializableSaveBookMarks>() {
                    @Override public void accept(SerializableSaveBookMarks deleteResponse) throws Exception {
                        deleteBookmarks(deleteResponse,data);

                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        bookMarksNavigator.onFailure(throwable.getMessage());

                    }
                });


        compositeDisposable.add(disposable);



    }

    private void deleteBookmarks(SerializableSaveBookMarks deleteResponse, Bookmark data) {
        try {
            if(deleteResponse != null){

                if(!deleteResponse.isError()){
                    if(deleteResponse.getMessage().compareToIgnoreCase("success")==0){
                        //BookmarksAdapter.this.data.remove(data);
                        BookmarkAdapter.lstBookmark.remove(data);
                        //BookmarkAdapter.notifyDataSetChanged();
                        bookMarksNavigator.onDeleteSuccess(BookmarkAdapter.lstBookmark);
                        AlertDialogUtil.errorAlertDialog("", activity.getString(R.string.favourite_deleted), activity.getString(R.string.ok), activity);


                        /*notifyDataSetChanged();


                        if( BookmarksAdapter.this.data.size()==0) fragment.txtMsg.setVisibility(View.VISIBLE);
                        else fragment.txtMsg.setVisibility(View.GONE);*/
                    }
                    else {
                        bookMarksNavigator.onFailure(activity.getResources().getString(R.string.error_response));
                    }
                }

            }
        } catch (Exception e) {

            bookMarksNavigator.onFailure(e.getMessage());
            e.printStackTrace();
        }

    }
}
