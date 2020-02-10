package dm.sime.com.kharetati.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentBookmarkBinding;
import dm.sime.com.kharetati.datas.models.ZZBookmark;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.BookMarkRepository;
import dm.sime.com.kharetati.datas.repositories.MyMapRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.view.navigators.BookMarksNavigator;
import dm.sime.com.kharetati.view.viewModels.BookmarkViewModel;
import dm.sime.com.kharetati.view.viewmodelfactories.BookMarkViewModelFactory;
import dm.sime.com.kharetati.view.viewmodelfactories.MyMapViewModelFactory;

public class BookmarkFragment extends Fragment implements BookMarksNavigator {

    FragmentBookmarkBinding binding;
    BookmarkViewModel model;
    private View mRootView;
    private BookMarkRepository repository;
    private BookMarkViewModelFactory factory;

    public static BookmarkFragment newInstance(){
        BookmarkFragment fragment = new BookmarkFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            repository = new BookMarkRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        factory = new BookMarkViewModelFactory(getActivity(),repository);
        model = ViewModelProviders.of(this,factory).get(BookmarkViewModel.class);
        model.bookMarksNavigator =this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false);
        binding.setFragmentBookmarkVM(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        model.initializeBookmarkViewModel(getActivity());
        //model.getAllBookMarks();

        model.getMutableBookmark().observe(getActivity(), new Observer<List<ZZBookmark>>() {
            @Override
            public void onChanged(List<ZZBookmark> lstBookmark) {
               if(lstBookmark!=null){
                if (lstBookmark.size() > 0) {
                    model.setBookmarkAdapter(lstBookmark);
                }
            }}
        });
    }

    @Override
    public void onStarted() {
        AlertDialogUtil.showProgressBar(getActivity(),true);
    }

    @Override
    public void onSuccess() {
        AlertDialogUtil.showProgressBar(getActivity(),false);
    }

    @Override
    public void onFailure(String Msg) {

        AlertDialogUtil.showProgressBar(getActivity(),false);
        AlertDialogUtil.errorAlertDialog("",Msg,getActivity().getResources().getString(R.string.ok),getActivity());

    }
}
