package dm.sime.com.kharetati.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentDeliveryDetailBinding;
import dm.sime.com.kharetati.view.viewModels.DeliveryDetailViewModel;

public class DeliveryDetailFragment extends Fragment {

    FragmentDeliveryDetailBinding binding;
    DeliveryDetailViewModel model;
    private View mRootView;

    public static DeliveryDetailFragment newInstance(){
        DeliveryDetailFragment fragment = new DeliveryDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(DeliveryDetailViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delivery_detail, container, false);
        binding.setFragmentDeliveryDetailVM(model);
        mRootView = binding.getRoot();
        //initializePage();
        return binding.getRoot();
    }
}