package dm.sime.com.kharetati.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentLandOwnershipSelectionBinding;
import dm.sime.com.kharetati.datas.models.AttachmentBitmap;
import dm.sime.com.kharetati.datas.models.DocArr;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.ImageCropActivity;
import dm.sime.com.kharetati.view.viewModels.LandOwnerViewModel;
import dm.sime.com.kharetati.view.viewModels.ParentSiteplanViewModel;

import static dm.sime.com.kharetati.utility.Global.CURRENT_LOCALE;
import static dm.sime.com.kharetati.utility.Global.isFromMap;
import static dm.sime.com.kharetati.utility.Global.lstAttachedDoc;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_ATTACHMENT;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_LANDOWNER_SELECTION;

public class LandOwnerSelectionFragment extends Fragment {

    FragmentLandOwnershipSelectionBinding binding;
    LandOwnerViewModel model;
    private View mRootView;
    private String[] landOwnedType;
    private Tracker mTracker;


    public static LandOwnerSelectionFragment newInstance(){
        LandOwnerSelectionFragment fragment = new LandOwnerSelectionFragment();
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
        model = ViewModelProviders.of(this).get(LandOwnerViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.current_fragment_id = FragmentTAGS.FR_LANDOWNER_SELECTION;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_land_ownership_selection, container, false);
        if(CURRENT_LOCALE.equals("en")) binding.layout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.layout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.spinLandOwned.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.spinLandOwned.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.rg.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.rg.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        binding.setFragmentLandOwnerSelectionVM(model);
        mRootView = binding.getRoot();

        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_LANDOWNER_SELECTION);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        setRetainInstance(true);

        Global.passportData = null;
        Global.licenseData = null;
        Global.nocData = null;

        isFromMap =false;
        Global.isDeliveryByCourier = false;
        AttachmentFragment.currentSelection= "";
        AttachmentFragment.isGallery =false;
        AttachmentFragment.isCamera =false;
        ImageCropActivity.isImageCropped=false;

        /*if(ParentSiteplanViewModel.getDownloadedDoc()!=null)ParentSiteplanViewModel.getDownloadedDoc().clear();
        if(ParentSiteplanViewModel.getNewlyAttachedDoc()!=null)ParentSiteplanViewModel.getNewlyAttachedDoc().clear();
        if(AttachmentFragment.lstAttachedDoc!=null)AttachmentFragment.lstAttachedDoc.clear();*/
        AttachmentBitmap.passport_copy =null;
        AttachmentBitmap.visa_passport =null;
        AttachmentBitmap.company_license =null;
        AttachmentBitmap.letter_from_owner =null;
        DeliveryFragment.isDeliveryFragment =false;
        ParentSiteplanFragment.currentIndex =0;
        ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
        landOwnedType = new String[] {getResources().getString(R.string.land_owned_By_person),getResources().getString(R.string.land_owned_By_company)};

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

                ParentSiteplanViewModel.status =0;
                Global.spinPosition=position;
                switch (position){
                    case 0: {
                        /*ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
                        binding.rg.setVisibility(View.GONE);
                        Global.rbIsOwner =false;
                        Global.rbNotOwner = false;*/
                        Global.isPerson=true;
                        Global.isCompany=false;
                        ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
                        binding.rg.setVisibility(View.VISIBLE);

                        if(binding.rbIsOwner.isChecked()||binding.rbNotOwner.isChecked()){

                            ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
                        }
                    }
                    break;
                    case 1:{
                        Global.isCompany=true;
                        Global.isPerson=false;
                        Global.rbIsOwner = false;
                        Global.rbNotOwner = false;
                        binding.rg.setVisibility(View.GONE);
                        ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
                    }
                    break;


                }
                ParentSiteplanViewModel.getDownloadedDoc().clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ParentSiteplanViewModel.status =0;
            }
        });

        binding.rbIsOwner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ParentSiteplanViewModel.status =0;
                if(isChecked){
                    Global.rbIsOwner = true;
                    Global.rbNotOwner = false;
                    Global.isPerson = true;
                }


                if (Global.spinPosition == 0){

                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
                }
                else if(Global.spinPosition==1) {
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
                    binding.rbIsOwner.setChecked(false);
                    binding.rbNotOwner.setChecked(false);

                }
                /*else if(Global.spinPosition==2){
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);


                }*/
                ParentSiteplanViewModel.getDownloadedDoc().clear();

            }
        });
        binding.rbNotOwner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ParentSiteplanViewModel.status =0;
               if(isChecked) {
                    Global.rbIsOwner = false;
                    Global.rbNotOwner = true;
               }

                if (Global.spinPosition == 0)
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
                else if(Global.spinPosition==1) {
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
                    binding.rbIsOwner.setChecked(false);
                    binding.rbNotOwner.setChecked(false);

                }
               /* else if(Global.spinPosition==2){
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);

                }*/
                ParentSiteplanViewModel.getDownloadedDoc().clear();
            }
        });


        //initializePage();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        ParentSiteplanViewModel.getDownloadedDoc().clear();
        ParentSiteplanViewModel.getNewlyAttachedDoc().clear();
        AttachmentFragment.lstAttachedDoc.clear();
        ParentSiteplanFragment.currentIndex =0;
        ImageCropActivity.isImageCropped =false;
        if(PayFragment.isFromPayFragment){
            binding.spinLandOwned.setSelection(1);
            binding.rbIsOwner.setChecked(true);
        }

    }
}