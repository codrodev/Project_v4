package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.AppMsg;
import dm.sime.com.kharetati.datas.models.Bookmark;
import dm.sime.com.kharetati.datas.models.BookmarksResponse;
import dm.sime.com.kharetati.datas.models.GeneralResponse;
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

public class BookmarkViewModel extends ViewModel implements Filterable {
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
    private List<Bookmark> listData;
    private ItemFilter filter;
    private ArrayList<Bookmark> filteredData;


    public BookmarkViewModel(Activity context, BookMarkRepository repository){
        this.activity = context;
        this.repository = repository;
        kharetatiApp = KharetatiApp.create(activity);
        filteredData = new ArrayList<Bookmark>();
        filter=new ItemFilter();

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
        if(getBookmarkAdapter()!=null){
        getBookmarkAdapter().setBookmark(lstMyMap);
        getBookmarkAdapter().notifyDataSetChanged();}
    }

    public BookmarkAdapter getBookmarkAdapter() {
        return this.adapter;
    }
    public List<Bookmark> getBookMarks(){
        return listData;
    }

    public void getAllBookMarks() {


        bookMarksNavigator.onStarted();
        userId =Global.sime_userid;
        //userId =1003;

        SerializeBookmarkModel model = new SerializeBookmarkModel();
        model.setUserID(Global.sime_userid);




        Disposable disposable = repository.getAllBookMarks(model)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BookmarksResponse>() {
                    @Override public void accept(BookmarksResponse bookmarkResponse) throws Exception {
                       if(bookmarkResponse!=null) {
                           //bookmarkResponse=null;
                           getBookMarks(bookmarkResponse);
                       }
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        showErrorMessage(throwable.getMessage());

                    }
                });


        compositeDisposable.add(disposable);
    }

    private void getBookMarks(BookmarksResponse bookmarksResponse) {
        if(bookmarksResponse != null){
            if(bookmarksResponse.bookmarklist != null && bookmarksResponse.bookmarklist.length > 0){
                mutableBookmark =new MutableLiveData<>();
                listData = Arrays.asList(bookmarksResponse.getBookmarklist());
                addSqMt();
                mutableBookmark.setValue(Arrays.asList(bookmarksResponse.getBookmarklist()));
                adapter = new BookmarkAdapter(R.layout.adapter_bookmark, this, activity);
                BookmarkAdapter.BmAdapter = adapter;
                setBookmarkAdapter(Arrays.asList(bookmarksResponse.getBookmarklist()));      //bookMarksNavigator.updateUI();
                bookMarksNavigator.onSuccess();
            } else if(bookmarksResponse.bookmarklist == null || bookmarksResponse.bookmarklist.length == 0){
                bookMarksNavigator.onEmpty(Global.CURRENT_LOCALE.equals("en")? Global.appMsg.getFavourites_not_found_en():Global.appMsg.getFavourites_not_found_ar());
            } else if(bookmarksResponse.isError || bookmarksResponse.message != null){
                showErrorMessage(bookmarksResponse.message);
            } else
                bookMarksNavigator.onFailure(activity.getResources().getString(R.string.error_response));
        }
    }

    private void addSqMt(){
        if(listData != null && listData.size() > 0) {
            for (Bookmark bm : listData) {
                //bm.Area = null;
                String sqmt = Global.CURRENT_LOCALE.equals("en")? " Sq mts." : " ²م";
                bm.Area = " " + bm.Area + sqmt;
//                bm.ParcelNumber = null;
                bm.ParcelNumber = " " + bm.ParcelNumber;
            }
        }
    }

    public void editBookMark(Bookmark data) {
        bookMarksNavigator.onStarted();
        SerializeBookmarkEditModel model = new SerializeBookmarkEditModel();
        //model.setUserID(1003);
        model.setUserID(Global.sime_userid);
        model.setParcelNumber(data.ParcelNumber);
        model.setDescriptionEn(data.descriptionEn == null ? "" : data.descriptionEn);
        model.setDescriptionAr(data.descriptionAr == null ? "" : data.descriptionAr );

        Disposable disposable = repository.editBookMark(model)
                .subscribeOn(kharetatiApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GeneralResponse>() {
                    @Override public void accept(GeneralResponse editResponse) throws Exception {
                        editBookmarksAdapter(editResponse);

                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        showErrorMessage(throwable.getMessage());

                    }
                });


        compositeDisposable.add(disposable);
    }

    public void deleteBookMark(Bookmark data) {
        bookMarksNavigator.onStarted();
        SerializeBookmarkModel model = new SerializeBookmarkModel();
        //model.setUserID(1003);
        model.setUserID(Global.sime_userid);
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
                        showErrorMessage(throwable.getMessage());

                    }
                });


        compositeDisposable.add(disposable);



    }

    private void editBookmarksAdapter(GeneralResponse response) {
        getAllBookMarks();
        bookMarksNavigator.updateAdapter();
    }
    private void deleteBookmarks(SerializableSaveBookMarks deleteResponse, Bookmark data) {
        try {
            if(deleteResponse != null){

                if(!deleteResponse.isError()){
                    if(deleteResponse.getMessage().compareToIgnoreCase("success")==0){
                        //BookmarksAdapter.this.data.remove(data);

                        //bookMarksNavigator.removeData(data);
                        BookmarkAdapter adapter = new BookmarkAdapter(R.layout.adapter_bookmark, this, activity);
                        adapter.remove(data);

                        //adapter.remove(data);
                        //BookmarkAdapter.BmAdapter.remove(data);

                        //BookmarkAdapter.notifyDataSetChanged();

                        //bookMarksNavigator.updateAdapter();
                        bookMarksNavigator.onDeleteSuccess(BookmarkAdapter.lstBookmark);
                        getAllBookMarks();

                        if(Global.appMsg!=null)
                            AlertDialogUtil.errorAlertDialog("",Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getBookmarkDeletedEn():Global.appMsg.getBookmarkDeletedAr(), activity.getString(R.string.ok), activity);
                        else
                            AlertDialogUtil.errorAlertDialog("", activity.getString(R.string.favourite_deleted), activity.getString(R.string.ok), activity);


                        /*notifyDataSetChanged();


                        if( BookmarksAdapter.this.data.size()==0) fragment.txtMsg.setVisibility(View.VISIBLE);
                        else fragment.txtMsg.setVisibility(View.GONE);*/
                    }
                    else {
                        showErrorMessage(deleteResponse.getMessage());
                    }
                }

            }
        } catch (Exception e) {

            showErrorMessage(e.getMessage());
            e.printStackTrace();
        }

    }

    public void showErrorMessage(String exception){
        if(Global.appMsg!=null){
            bookMarksNavigator.onFailure(Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getErrorFetchingDataEn():Global.appMsg.getErrorFetchingDataAr());
        }
        else
            bookMarksNavigator.onFailure(activity.getResources().getString(R.string.error_response));

        Log.e(activity.getClass().getSimpleName(),exception);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString();

            FilterResults results = new FilterResults();

            int count = getBookMarks().size();
            final ArrayList<Bookmark> nlist = new ArrayList<Bookmark>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = getBookMarks().get(i).ParcelNumber;
                if (filterableString.contains(filterString)) {
                    nlist.add(getBookMarks().get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Bookmark>) results.values;
            setBookmarkAdapter(filteredData);
            if(getBookmarkAdapter()!=null)
                getBookmarkAdapter().notifyDataSetChanged();
        }

    }
}
