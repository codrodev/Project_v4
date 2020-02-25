package dm.sime.com.kharetati.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentLandOwnershipSelectionBinding;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.viewModels.LandOwnerViewModel;
import dm.sime.com.kharetati.view.viewModels.ParentSiteplanViewModel;

public class LandOwnerSelectionFragment extends Fragment {

    FragmentLandOwnershipSelectionBinding binding;
    LandOwnerViewModel model;
    private View mRootView;
    private String[] landOwnedType;

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

        ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
        landOwnedType = new String[] {getResources().getString(R.string.select),getResources().getString(R.string.land_owned_By_person),getResources().getString(R.string.land_owned_By_company)};

        ArrayList<String> arrayList= new ArrayList();
        for (int i=0; i<landOwnedType.length; i++){
            arrayList.add(landOwnedType[i]);
        }
        ArrayAdapter<String> adapter;
        if (Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0) {
            adapter= new ArrayAdapter(getActivity(),R.layout.attachment_drp_en,R.id.txtLandOwner,arrayList);
        } else {
            adapter= new ArrayAdapter(getActivity(),R.layout.attachment_drp_ar,R.id.txtLandOwner,arrayList);
        }
        adapter.setDropDownViewResource(R.layout.attachment_drp_view);
        binding.spinLandOwned.setAdapter(adapter);

        binding.spinLandOwned.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Global.spinPosition=position;
                switch (position){
                    case 0: {
                        ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
                        binding.rg.setVisibility(View.GONE);
                        Global.rbIsOwner =false;
                        Global.rbNotOwner = false;

                    }
                    break;
                    case 1:{
                        Global.isPerson=true;
                        Global.isCompany=false;
                        ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
                        binding.rg.setVisibility(View.VISIBLE);

                        if(binding.rbIsOwner.isChecked()||binding.rbNotOwner.isChecked()){

                            ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
                        }

                    }
                    break;
                    case 2:
                        Global.isCompany=true;
                        Global.isPerson=false;
                        binding.rg.setVisibility(View.GONE);
                        ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.rbIsOwner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Global.rbIsOwner = isChecked;

                if (Global.spinPosition == 0){

                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
                }
                else if(Global.spinPosition==1)
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
                else if(Global.spinPosition==2){
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
                    binding.rbIsOwner.setChecked(false);
                    binding.rbNotOwner.setChecked(false);

                }


            }
        });
        binding.rbNotOwner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Global.rbNotOwner = isChecked;
                if (Global.spinPosition == 0)
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
                else if(Global.spinPosition==1)
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
                else if(Global.spinPosition==2){
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
                    binding.rbIsOwner.setChecked(false);
                    binding.rbNotOwner.setChecked(false);
                }

            }
        });





        //initializePage();
        return binding.getRoot();
    }
}