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
import dm.sime.com.kharetati.databinding.FragmentHappinessBinding;
import dm.sime.com.kharetati.view.viewModels.HappinessViewModel;

public class HappinessFragment extends Fragment {
    FragmentHappinessBinding binding;
    HappinessViewModel model;
    private View mRootView;

    public static HappinessFragment newInstance(){
        HappinessFragment fragment = new HappinessFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(HappinessViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_happiness, container, false);
        binding.setFragmentHappinessVM(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage() {
    }
}
