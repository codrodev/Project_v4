package dm.sime.com.kharetati.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentBookmarkBinding;
import dm.sime.com.kharetati.datas.models.Bookmark;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.BookMarkRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.navigators.BookMarksNavigator;
import dm.sime.com.kharetati.view.viewModels.BookmarkViewModel;
import dm.sime.com.kharetati.view.viewmodelfactories.BookMarkViewModelFactory;

public class BookmarkFragment extends Fragment implements BookMarksNavigator {

    FragmentBookmarkBinding binding;
    BookmarkViewModel model;
    private View mRootView;
    private BookMarkRepository repository;
    private BookMarkViewModelFactory factory;
    public static BookmarkViewModel bmModel;

    public static BookmarkFragment newInstance(){
        BookmarkFragment fragment = new BookmarkFragment();
        return fragment;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "Dubai-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.getView());
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
        bmModel = model;
        model.bookMarksNavigator =this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.current_fragment_id = FragmentTAGS.FR_BOOKMARK;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false);
        binding.setFragmentBookmarkVM(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        model.initializeBookmarkViewModel(getActivity());
        //model.getAllBookMarks();

        model.getMutableBookmark().observe(getActivity(), new Observer<List<Bookmark>>() {
            @Override
            public void onChanged(List<Bookmark> lstBookmark) {
               if(lstBookmark!=null){
                if (lstBookmark.size() > 0) {
                    model.setBookmarkAdapter(lstBookmark);
                    binding.recyclerBookMarks.setAdapter(model.getBookmarkAdapter());
                    binding.recyclerBookMarks.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.recyclerBookMarks.setHasFixedSize(true);

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
        binding.recyclerBookMarks.setAdapter(model.getBookmarkAdapter());


    }

    @Override
    public void onFailure(String Msg) {

        AlertDialogUtil.showProgressBar(getActivity(),false);
        AlertDialogUtil.errorAlertDialog("",Msg,getActivity().getResources().getString(R.string.ok),getActivity());

    }

    @Override
    public void onDeleteSuccess(List<Bookmark> lstBookmark) {
        AlertDialogUtil.showProgressBar(getActivity(),false);
        //model.getBookmarkAdapter().notifyDataSetChanged();
        if(lstBookmark.size()==0)
            binding.textHeading.setText(getActivity().getResources().getString(R.string.NO_FAVOURITE_PLOTS_FOUND));

    }


}
