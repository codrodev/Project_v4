package dm.sime.com.kharetati.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentParentSiteplanBinding;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.ParentSitePlanRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.navigators.ParentSitePlanNavigator;
import dm.sime.com.kharetati.view.viewModels.ParentSiteplanViewModel;
import dm.sime.com.kharetati.view.viewmodelfactories.ParentSitePlanViewModelFactory;

public class ParentSiteplanFragment extends Fragment implements ParentSitePlanNavigator {

    FragmentParentSiteplanBinding binding;
    ParentSiteplanViewModel model;
    private View mRootView;
    FragmentManager fragmentManager = null;
    FragmentTransaction tx = null;
    String[] pagerArray;
    public static int currentIndex = 0;
    public static ParentSiteplanViewModel parentModel;
    private ParentSitePlanViewModelFactory factory;
    private ParentSitePlanRepository repository;
    public static onNextListner listner;

    public static ParentSiteplanFragment newInstance(){
        ParentSiteplanFragment fragment = new ParentSiteplanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            repository = new ParentSitePlanRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        factory = new ParentSitePlanViewModelFactory(getActivity(),repository);
        model = ViewModelProviders.of(getActivity(),factory).get(ParentSiteplanViewModel.class);

        parentModel =model;
        model.parentSitePlanNavigator =this;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "Dubai-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.getView());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.current_fragment_id = FragmentTAGS.FR_PARENT_SITEPLAN;
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
        binding.imgHelp.setRotationY(Global.CURRENT_LOCALE.equals("en")?0:180);
        binding.imgBack.setRotationY(Global.CURRENT_LOCALE.equals("en")?0:180);
        loadFragment(0);
        binding.txtHeader.setText(pagerArray[currentIndex]);
        changeStepperBackground(currentIndex);
        if(currentIndex != 0 )
            binding.btnPrevious.setVisibility(View.VISIBLE);
        else
            binding.btnPrevious.setVisibility(View.GONE);
       /* if(currentIndex != 2 )
            binding.btnNext.setVisibility(View.VISIBLE);
        else
            binding.btnNext.setVisibility(View.GONE);*/
        /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,((int)Global.height/10)*2);
        binding.stepperHeader.setLayoutParams(params);*/
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //binding.viewPagerCreatePackage.setCurrentItem(getNext(), true);
                if(currentIndex < 3) {
                    if(currentIndex==0){
                        if(Global.spinPosition ==2){

                            Global.rbIsOwner=false;
                            Global.rbNotOwner=false;
                        }
                        if (!Global.isConnected(getActivity())) {

                            if(Global.appMsg!=null)
                                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                            else
                                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                        }
                        else
                            model.retrieveProfileDocs();
                    }
                    currentIndex++;
                    loadFragment(currentIndex);
                    binding.txtHeader.setText(pagerArray[currentIndex]);
                    if(currentIndex == 0 ){
                        binding.btnPrevious.setVisibility(View.GONE);
                    }
                    else
                        binding.btnPrevious.setVisibility(View.VISIBLE);
                    if(currentIndex==3)
                        binding.btnNext.setVisibility(View.GONE);
                    else
                        binding.btnNext.setVisibility(View.VISIBLE);

                    changeStepperBackground(currentIndex);
                }
            }
        });

        binding.imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Global.helpUrlEn != null || Global.helpUrlAr != null) {
                    ArrayList al = new ArrayList();
                    al.add(Global.CURRENT_LOCALE.equals("en")? Global.helpUrlEn:Global.helpUrlAr);
                    ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                } else {

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
                    if(currentIndex == 0 )
                        binding.btnPrevious.setVisibility(View.GONE);
                    else
                        binding.btnPrevious.setVisibility(View.VISIBLE);
                    if(currentIndex == 3)
                        binding.btnNext.setVisibility(View.GONE);
                    else
                        binding.btnNext.setVisibility(View.VISIBLE);

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

    private boolean isTransitionValid(int index){
        boolean isValid = false;
        if (index == 1){
           /* if(Global.docArr == null || Global.docArr.length == 0){
                if(ParentSiteplanViewModel.getNewlyAttachedDoc() == null || ParentSiteplanViewModel.getNewlyAttachedDoc().size() == )
                binding.btnNext.setEnabled(false);
            } else if (Global.docArr != null && )*/
            //if(ParentSiteplanViewModel.getNewlyAttachedDoc().size() < ParentSiteplanViewModel.)
        }
        return isValid;
    }

    private void changeStepperBackground(int index){
        if(index == 0){
            binding.txtStepperOne.setBackground(getResources().getDrawable(R.drawable.stepper_background_selected));
            binding.txtStepperTwo.setBackground(getResources().getDrawable(R.drawable.stepper_background));
            binding.txtStepperThree.setBackground(getResources().getDrawable(R.drawable.stepper_background));
            binding.txtStepperFour.setBackground(getResources().getDrawable(R.drawable.stepper_background));
        } else if(index == 1) {
            binding.txtStepperOne.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
            binding.txtStepperTwo.setBackground(getResources().getDrawable(R.drawable.stepper_background_selected));
            binding.txtStepperThree.setBackground(getResources().getDrawable(R.drawable.stepper_background));
            binding.txtStepperFour.setBackground(getResources().getDrawable(R.drawable.stepper_background));
        } else if(index == 2) {
            binding.txtStepperOne.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
            binding.txtStepperTwo.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
            binding.txtStepperThree.setBackground(getResources().getDrawable(R.drawable.stepper_background_selected));
            binding.txtStepperFour.setBackground(getResources().getDrawable(R.drawable.stepper_background));
        } else if(index == 3) {
            binding.txtStepperOne.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
            binding.txtStepperTwo.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
            binding.txtStepperThree.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
            binding.txtStepperFour.setBackground(getResources().getDrawable(R.drawable.stepper_background_selected));
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
                fragment = DeliveryFragment.newInstance();
                break;
            case 3:
                listner.onNextClicked();
                fragment = PayFragment.newInstance();
                break;
        }

        tx.replace(R.id.childFragmentContainer, fragment);

        //if (addToBackStack)
        //tx.addToBackStack(fragment_tag);
        tx.commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void setNextEnabledStatus(Boolean status) {
        if(status){
            binding.btnNext.setEnabled(status);
            binding.btnNext.setBackground(getActivity().getResources().getDrawable(R.drawable.gradient_background));


        }
        else {
            binding.btnNext.setEnabled(status);
            binding.btnNext.setBackground(getActivity().getResources().getDrawable(R.drawable.disabled_gradient_background));

        }

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

    @Override
    public void navigateToFragment(int position) {
        loadFragment(position);
    }

    public interface onNextListner{
        public void onNextClicked();
    }
}
