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

import org.json.JSONException;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentMymapBinding;
import dm.sime.com.kharetati.datas.models.MyMapResults;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.MyMapRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.navigators.MyMapNavigator;
import dm.sime.com.kharetati.view.viewModels.MyMapViewModel;
import dm.sime.com.kharetati.view.viewmodelfactories.MyMapViewModelFactory;

public class MyMapFragment extends Fragment implements MyMapNavigator {

    FragmentMymapBinding binding;
    MyMapViewModel model;
    private View mRootView;
    private MyMapViewModelFactory factory;
    private MyMapRepository repository;

    public static MyMapFragment newInstance(){
        MyMapFragment fragment = new MyMapFragment();
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
            repository = new MyMapRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        factory = new MyMapViewModelFactory(getActivity(),repository);

        model = ViewModelProviders.of(getActivity(),factory).get(MyMapViewModel.class);
        model.myMapNavigator = this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mymap, container, false);
        binding.setFragmentMyMapVM(model);
        mRootView = binding.getRoot();
        try {
            initializePage();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return binding.getRoot();
    }

    private void initializePage() throws JSONException {
        model.initializeMyMapViewModel(getActivity());


        model.getMutableMyMap().observe(getActivity(), new Observer<List<MyMapResults>>() {
            @Override
            public void onChanged(List<MyMapResults> lstMyMap) {
                //model.loading.set(View.GONE);
                if (lstMyMap.size() > 0) {
                    model.setMyMapAdapter(lstMyMap);
                    binding.recylerMyMaps.setAdapter(model.getMyMapAdapter());
                    binding.recylerMyMaps.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.recylerMyMaps.setHasFixedSize(true);
                }
            }
        });
    }

    @Override
    public void onStarted() {
        AlertDialogUtil.showProgressBar(getActivity(),true);
    }

    @Override
    public void onSuccess() {
        AlertDialogUtil.showProgressBar(getActivity(),false);
        binding.recylerMyMaps.setAdapter(model.getMyMapAdapter());


    }

    @Override
    public void onFailure(String Msg) {
        AlertDialogUtil.showProgressBar(getActivity(),false);
        if(getResources()!=null)
        AlertDialogUtil.errorAlertDialog("",Msg,getActivity().getResources().getString(R.string.ok),getActivity());

    }

    @Override
    public void onViewSitePlanSuccess() {
        AlertDialogUtil.showProgressBar(getActivity(),false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
