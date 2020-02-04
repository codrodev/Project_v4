package dm.sime.com.kharetati.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentParentSiteplanBinding;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.viewModels.ParentSiteplanViewModel;

public class ParentSiteplanFragment extends Fragment {

    FragmentParentSiteplanBinding binding;
    ParentSiteplanViewModel model;
    private View mRootView;
    FragmentManager fragmentManager = null;
    FragmentTransaction tx = null;
    String[] pagerArray;
    int currentIndex = 0;

    public static ParentSiteplanFragment newInstance(){
        ParentSiteplanFragment fragment = new ParentSiteplanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(ParentSiteplanViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_parent_siteplan, container, false);
        binding.setFragmentParentSiteplanVM(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        model.manageAppBar(getActivity(), false);
        model.manageAppBottomBAtr(getActivity(), false);
        pagerArray =  getActivity().getResources().getStringArray(R.array.request_site_plan);
        loadFragment(0);
        binding.txtHeader.setText(pagerArray[currentIndex]);
        changeStepperBackground(currentIndex);
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //binding.viewPagerCreatePackage.setCurrentItem(getNext(), true);
                if(currentIndex < 2) {
                    currentIndex++;
                    loadFragment(currentIndex);
                    binding.txtHeader.setText(pagerArray[currentIndex]);
                    changeStepperBackground(currentIndex);
                }
            }
        });
        binding.btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentIndex > 0) {
                    currentIndex--;
                    loadFragment(currentIndex);
                    binding.txtHeader.setText(pagerArray[currentIndex]);
                    changeStepperBackground(currentIndex);
                }
                //binding.viewPagerCreatePackage.setCurrentItem(getPrevious(), true);
            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void changeStepperBackground(int index){
        if(index == 0){
            binding.txtStepperOne.setBackground(getResources().getDrawable(R.drawable.stepper_background_selected));
            binding.txtStepperTwo.setBackground(getResources().getDrawable(R.drawable.stepper_background));
            binding.txtStepperThree.setBackground(getResources().getDrawable(R.drawable.stepper_background));
            binding.txtStepperFour.setBackground(getResources().getDrawable(R.drawable.stepper_background));
        } else if(index == 1) {
            binding.txtStepperOne.setBackground(getResources().getDrawable(R.drawable.stepper_background));
            binding.txtStepperTwo.setBackground(getResources().getDrawable(R.drawable.stepper_background_selected));
            binding.txtStepperThree.setBackground(getResources().getDrawable(R.drawable.stepper_background));
            binding.txtStepperFour.setBackground(getResources().getDrawable(R.drawable.stepper_background));
        } else if(index == 2) {
            binding.txtStepperOne.setBackground(getResources().getDrawable(R.drawable.stepper_background));
            binding.txtStepperTwo.setBackground(getResources().getDrawable(R.drawable.stepper_background));
            binding.txtStepperThree.setBackground(getResources().getDrawable(R.drawable.stepper_background_selected));
            binding.txtStepperFour.setBackground(getResources().getDrawable(R.drawable.stepper_background));
        }
    }

    public Fragment loadFragment(int index) {
        fragmentManager = getActivity().getSupportFragmentManager();

        tx = fragmentManager.beginTransaction();
        Fragment fragment = null;
        switch (index) {
            case 0:
                fragment = LandOwnerSelectionFragment.newInstance();
                break;
            case 1:
                fragment = AttachmentFragment.newInstance();
                break;
            case 2:
                fragment = DeliveryDetailFragment.newInstance();
                break;
            /*case 3:
                fragment = MapFragment.newInstance();
                break;*/
        }

        tx.replace(R.id.childFragmentContainer, fragment);

        //if (addToBackStack)
            //tx.addToBackStack(fragment_tag);
        tx.commitAllowingStateLoss();
        return fragment;
    }

}
