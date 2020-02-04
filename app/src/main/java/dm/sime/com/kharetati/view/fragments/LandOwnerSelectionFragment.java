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
import dm.sime.com.kharetati.databinding.FragmentLandOwnershipSelectionBinding;
import dm.sime.com.kharetati.view.viewModels.LandOwnerViewModel;

public class LandOwnerSelectionFragment extends Fragment {

    FragmentLandOwnershipSelectionBinding binding;
    LandOwnerViewModel model;
    private View mRootView;

    public static LandOwnerSelectionFragment newInstance(){
        LandOwnerSelectionFragment fragment = new LandOwnerSelectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(LandOwnerViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_land_ownership_selection, container, false);
        binding.setFragmentLandOwnerSelectionVM(model);
        mRootView = binding.getRoot();
        //initializePage();
        return binding.getRoot();
    }
}