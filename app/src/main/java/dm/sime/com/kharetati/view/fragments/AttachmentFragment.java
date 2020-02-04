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

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentAttachmentBinding;
import dm.sime.com.kharetati.view.viewModels.AttachmentViewModel;

public class AttachmentFragment extends Fragment {

    FragmentAttachmentBinding binding;
    AttachmentViewModel model;
    private View mRootView;

    public static AttachmentFragment newInstance(){
        AttachmentFragment fragment = new AttachmentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(AttachmentViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_attachment, container, false);
        binding.setFragmentAttachmentVM(model);
        mRootView = binding.getRoot();
        //initializePage();
        return binding.getRoot();
    }
}