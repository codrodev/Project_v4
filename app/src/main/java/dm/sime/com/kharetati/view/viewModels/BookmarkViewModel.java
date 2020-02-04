package dm.sime.com.kharetati.view.viewModels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.ZZBookmark;
import dm.sime.com.kharetati.datas.models.ZZBookmarkModel;
import dm.sime.com.kharetati.view.adapters.BookmarkAdapter;

public class BookmarkViewModel extends ViewModel {
    BookmarkAdapter adapter;
    ZZBookmarkModel model;
    MutableLiveData<List<ZZBookmark>> mutableBookmark = new MutableLiveData<>();

    public BookmarkViewModel(){

    }

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
}
