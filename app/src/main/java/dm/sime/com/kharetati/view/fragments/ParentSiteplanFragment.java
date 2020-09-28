package dm.sime.com.kharetati.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentParentSiteplanBinding;
import dm.sime.com.kharetati.datas.models.DocArr;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.ParentSitePlanRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.ImageCropActivity;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.customview.SwitchCompatEx;
import dm.sime.com.kharetati.view.navigators.ParentSitePlanNavigator;
import dm.sime.com.kharetati.view.viewModels.ParentSiteplanViewModel;
import dm.sime.com.kharetati.view.viewmodelfactories.ParentSitePlanViewModelFactory;

import static dm.sime.com.kharetati.utility.Global.CURRENT_LOCALE;
import static dm.sime.com.kharetati.utility.Global.isFromMap;
import static dm.sime.com.kharetati.utility.Global.makani;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_ATTACHMENT;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_PARENT_SITEPLAN;

public class ParentSiteplanFragment extends Fragment implements ParentSitePlanNavigator {

    public static final String POSITION = "POSITION";
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
    private Tracker mTracker;
    private boolean isHelpClicked;
    private SharedPreferences sharedpreferences;

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
        ParentSiteplanViewModel.status =0;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_parent_siteplan, container, false);
        if(CURRENT_LOCALE.equals("en")) binding.container.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.container.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.childFragmentContainer.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.childFragmentContainer.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.toolbarLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.toolbarLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.toolbarll.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.toolbarll.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.stepper.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.stepper.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.buttons.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.buttons.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        binding.setFragmentParentSiteplanVM(model);
        setRetainInstance(true);
        mRootView = binding.getRoot();
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_PARENT_SITEPLAN);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        if(Global.uaePassConfig.hideDeliveryDetails){
            /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(SwitchCompatEx.dp2Px(16f),SwitchCompatEx.dp2Px(16f),SwitchCompatEx.dp2Px(16f),SwitchCompatEx.dp2Px(16f));
            params.gravity =Gravity.CENTER;
            binding.stepper.setLayoutParams(params);*/
            binding.stepperFourLayout.setVisibility(View.GONE);
            binding.view3.setVisibility(View.GONE);
        }
        else{
            /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(SwitchCompatEx.dp2Px(16f),SwitchCompatEx.dp2Px(16f),SwitchCompatEx.dp2Px(16f),SwitchCompatEx.dp2Px(16f));
            binding.stepper.setLayoutParams(params);*/
            binding.stepperFourLayout.setVisibility(View.VISIBLE);
            binding.view3.setVisibility(View.VISIBLE);
        }
        binding.stepper.setWeightSum(Global.uaePassConfig.hideDeliveryDetails?2.75f:4f);
        binding.stepper.setGravity(Gravity.CENTER);
        binding.imgBack.setRotationY(Global.CURRENT_LOCALE.equals("en")?0:180);

        MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(), FR_PARENT_SITEPLAN, null /* class override */);
        initializePage();
        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        //isFromMap=false;
        sharedpreferences.edit().putInt("stepperPosition",currentIndex).apply();
    }

    private void initializePage(){
        model.manageAppBar(getActivity(), false);
        model.manageAppBottomBAtr(getActivity(), false);
        pagerArray =  getActivity().getResources().getStringArray(R.array.request_site_plan);
        binding.imgHelp.setRotationY(Global.CURRENT_LOCALE.equals("en")?0:180);
        binding.imgBack.setRotationY(Global.CURRENT_LOCALE.equals("en")?0:180);
        if(isFromMap)
        loadFragment(0);
        if(Global.uaePassConfig.hideDeliveryDetails) pagerArray[2]=pagerArray[3];else pagerArray[2]=pagerArray[2];
        binding.txtHeader.setText(pagerArray[currentIndex]);
        MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(), pagerArray[currentIndex], null /* class override */);
        sharedpreferences = getActivity().getSharedPreferences(POSITION, Context.MODE_PRIVATE);


        changeStepperBackground(currentIndex);
        if(currentIndex != 0 )
            binding.btnPrevious.setVisibility(View.VISIBLE);
        else
            binding.btnPrevious.setVisibility(View.INVISIBLE);
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
                    if (currentIndex == 0) {
                        setNextEnabledStatus(false);
                        if (Global.spinPosition == 2) {

                            Global.rbIsOwner = false;
                            Global.rbNotOwner = false;
                            Global.isPerson = false;
                        }
                        if (!Global.isConnected(getActivity())) {

                            if (Global.appMsg != null)
                                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getResources().getString(R.string.ok), getActivity());
                            else
                                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                        } else{
                            model.retrieveProfileDocs();}
                    }
                    if(currentIndex==1){
                        try {
                            AttachmentFragment.attachmentModel.attachmentNavigator.navigateToPay();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (currentIndex == 2) {
                        if (Global.isDeliveryByCourier) {
                            if (Global.isMakani) {
                                if (makani.trim().length() != 0 && makani.trim().length() <= 10)
                                    model.getMakaniToDLTM(makani);
                                else if (makani.trim().length() == 0)
                                    model.showInvalidMakaniError();
                            } else
                                currentIndex++;

                        } else
                            currentIndex++;
                    } else
                        currentIndex++;
                    loadFragment(currentIndex);
                    if(Global.uaePassConfig.hideDeliveryDetails) pagerArray[2]=pagerArray[3];else pagerArray[2]=pagerArray[2];
                    binding.txtHeader.setText(pagerArray[currentIndex]);
                    if (currentIndex == 0) {
                        binding.btnPrevious.setVisibility(View.INVISIBLE);
                    } else
                        binding.btnPrevious.setVisibility(View.VISIBLE);
                    if (currentIndex == 3)
                        binding.btnNext.setVisibility(View.INVISIBLE);
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
                    sharedpreferences.edit().putInt("stepperPosition",currentIndex).apply();
                    ArrayList al = new ArrayList();
                    if(Global.helpUrlEn!=null||Global.helpUrlAr!=null)
                    al.add(HomeFragment.constructUrl((Global.CURRENT_LOCALE.equals("en")? Global.helpUrlEn:Global.helpUrlAr),getActivity()));
                    al.add(getActivity().getResources().getString(R.string.help));
                    ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                    isHelpClicked =true;
                } else {

                }
            }
        });

        binding.btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentIndex > 0) {
                    currentIndex--;
                    if(currentIndex==1){
                        if(PaymentFragment.isFromPaymentFagment) ImageCropActivity.isImageCropped = false;
                    }
                    loadFragment(currentIndex);
                    if(Global.uaePassConfig.hideDeliveryDetails) pagerArray[2]=pagerArray[3];else pagerArray[2]=pagerArray[2];
                    binding.txtHeader.setText(pagerArray[currentIndex]);
                    if(currentIndex == 0 )
                        binding.btnPrevious.setVisibility(View.INVISIBLE);
                    else
                        binding.btnPrevious.setVisibility(View.VISIBLE);
                    if(currentIndex == 3)
                        binding.btnNext.setVisibility(View.INVISIBLE);
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
            oneUi();

        } else if(index == 1) {
            twoUI();

        } else if(index == 2) {
            threeUI();
        } else if(index == 3) {

           fourUI();
        }
    }

    private void fourUI() {
        binding.txtStepperOne.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
        binding.txtStepperTwo.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
        binding.txtStepperThree.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
        binding.stepperThreeText.setText("");
        binding.stepperTwoText.setText("");
        binding.stepperOneText.setText("");
        binding.txtStepperFour.setBackground(getResources().getDrawable(R.drawable.green_ring_background));
        binding.stepperFourText.setText("4");
        binding.stepperFourText.setTextColor(getResources().getColor(R.color.white));
        binding.view1.setBackgroundColor(getResources().getColor(R.color.stepper_completed_color));
        binding.view2.setBackgroundColor(getResources().getColor(R.color.stepper_completed_color));
        binding.view3.setBackgroundColor(getResources().getColor(R.color.stepper_completed_color));
    }

    private void threeUI() {

        if(!Global.uaePassConfig.hideDeliveryDetails){
        binding.stepperTwoText.setText("");
        binding.stepperOneText.setText("");

        binding.txtStepperOne.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
        binding.txtStepperTwo.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
        binding.txtStepperThree.setBackground(getResources().getDrawable(R.drawable.green_ring_background));
        binding.txtStepperFour.setBackground(getResources().getDrawable(R.drawable.ring_background));
        binding.stepperThreeText.setText("3");
        binding.stepperFourText.setText("4");

        binding.stepperThreeText.setTextColor(getResources().getColor(R.color.stepper_text_color));
        binding.stepperFourText.setTextColor(getResources().getColor(R.color.stepper_text_color));
        binding.view1.setBackgroundColor(getResources().getColor(R.color.stepper_completed_color));
        binding.view2.setBackgroundColor(getResources().getColor(R.color.stepper_completed_color));
        binding.view3.setBackgroundColor(getResources().getColor(R.color.stepper_completed_color));
        }
        else{
            binding.stepperTwoText.setText("");
            binding.stepperOneText.setText("");

            binding.txtStepperOne.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
            binding.txtStepperTwo.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
            binding.txtStepperThree.setBackground(getResources().getDrawable(R.drawable.green_ring_background));
            binding.txtStepperFour.setBackground(getResources().getDrawable(R.drawable.ring_background));
            binding.stepperThreeText.setText("3");
            binding.stepperFourText.setText("4");
            binding.stepperFourText.setVisibility(View.GONE);
            binding.view3.setVisibility(View.GONE);
            binding.txtStepperFour.setVisibility(View.GONE);

            binding.stepperThreeText.setTextColor(getResources().getColor(R.color.stepper_text_color));
            binding.stepperFourText.setTextColor(getResources().getColor(R.color.stepper_text_color));
            binding.view1.setBackgroundColor(getResources().getColor(R.color.stepper_completed_color));
            binding.view2.setBackgroundColor(getResources().getColor(R.color.stepper_completed_color));
            binding.view3.setBackgroundColor(getResources().getColor(R.color.stepper_completed_color));
            binding.btnNext.setVisibility(View.INVISIBLE);

        }
    }

    private void twoUI() {
        binding.stepperOneText.setText("");
        binding.txtStepperOne.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
        binding.txtStepperTwo.setBackground(getResources().getDrawable(R.drawable.green_ring_background));
        binding.stepperTwoText.setText("2");
        binding.txtStepperThree.setBackground(getResources().getDrawable(R.drawable.ring_background));
        binding.stepperThreeText.setText("3");
        binding.txtStepperFour.setBackground(getResources().getDrawable(R.drawable.ring_background));
        binding.stepperFourText.setText("4");
        binding.stepperTwoText.setTextColor(getResources().getColor(R.color.white));
        binding.stepperThreeText.setTextColor(getResources().getColor(R.color.stepper_text_color));
        binding.stepperFourText.setTextColor(getResources().getColor(R.color.stepper_text_color));
        binding.view1.setBackgroundColor(getResources().getColor(R.color.stepper_completed_color));
        binding.view2.setBackgroundColor(getResources().getColor(R.color.stepper_completed_color));
        binding.view3.setBackgroundColor(getResources().getColor(R.color.stepper_completed_color));
    }

    private void oneUi() {
        binding.txtStepperOne.setBackground(getResources().getDrawable(R.drawable.green_ring_background));
        binding.txtStepperTwo.setBackground(getResources().getDrawable(R.drawable.ring_background));
        binding.txtStepperThree.setBackground(getResources().getDrawable(R.drawable.ring_background));
        binding.txtStepperFour.setBackground(getResources().getDrawable(R.drawable.ring_background));
        binding.stepperOneText.setTextColor(getResources().getColor(R.color.white));
        binding.stepperTwoText.setTextColor(getResources().getColor(R.color.stepper_text_color));
        binding.stepperThreeText.setTextColor(getResources().getColor(R.color.stepper_text_color));
        binding.stepperFourText.setTextColor(getResources().getColor(R.color.stepper_text_color));
        binding.view1.setBackgroundColor(getResources().getColor(R.color.stepper_text_color));
        binding.view2.setBackgroundColor(getResources().getColor(R.color.stepper_text_color));
        binding.view3.setBackgroundColor(getResources().getColor(R.color.stepper_text_color));

        binding.stepperFourText.setText("4");
        binding.stepperThreeText.setText("3");
        binding.stepperTwoText.setText("2");
        binding.stepperOneText.setText("1");
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
                fragment = Global.uaePassConfig.hideDeliveryDetails?PayFragment.newInstance():DeliveryFragment.newInstance();
                break;
            case 3:
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
            if(getActivity()!=null)
            binding.btnNext.setBackground(getActivity().getResources().getDrawable(R.drawable.maroon_angle_background));


        }
        else {
            binding.btnNext.setEnabled(status);
            if(getActivity()!=null)
                binding.btnNext.setBackground(getActivity().getResources().getDrawable(R.drawable.marron_angle_disabled_background));

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(Global.isLandScape){
            if(CURRENT_LOCALE.equals("ar")){

            }
        }
        if(isHelpClicked){
            loadFragment(sharedpreferences.getInt("stepperPosition",0));
            if(sharedpreferences.getInt("stepperPosition",0)==3)
                binding.btnNext.setVisibility(View.INVISIBLE);
            else if(sharedpreferences.getInt("stepperPosition",0)==0)
                binding.btnPrevious.setVisibility(View.INVISIBLE);
        }
        int pos= sharedpreferences.getInt("stepperPosition",0);
        if(isFromMap) pos =0;
        switch (pos){

            case 0:{
                binding.btnPrevious.setVisibility(View.INVISIBLE);
                oneUi();
                loadFragment(pos);
                break;

            }case 1:{
                twoUI();
                loadFragment(pos);
                break;
            }case 2:{
                threeUI();
                loadFragment(pos);
                break;
            }case 3:{
                binding.btnNext.setVisibility(View.INVISIBLE);
                fourUI();
                loadFragment(pos);
                break;
            }
            default:{
                oneUi();
                loadFragment(pos);
            }
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

    @Override
    public void onMakaniSuccess() {
        onSuccess();
        Global.isValidMakani = true;
        currentIndex++;
        loadFragment(currentIndex);
        if(Global.uaePassConfig.hideDeliveryDetails) pagerArray[2]=pagerArray[3];else pagerArray[2]=pagerArray[2];
        binding.txtHeader.setText(pagerArray[currentIndex]);
        binding.txtStepperOne.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
        binding.txtStepperTwo.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
        binding.txtStepperThree.setBackground(getResources().getDrawable(R.drawable.stepper_background_completed));
        binding.stepperThreeText.setText("");
        binding.stepperTwoText.setText("");
        binding.stepperOneText.setText("");
        binding.stepperFourText.setVisibility(View.VISIBLE);
        binding.view3.setVisibility(View.VISIBLE);
        binding.txtStepperFour.setVisibility(View.VISIBLE);
        binding.txtStepperFour.setBackground(getResources().getDrawable(R.drawable.green_ring_background));
        binding.stepperFourText.setText("4");


        binding.stepperFourText.setTextColor(getResources().getColor(R.color.white));
        binding.view1.setBackgroundColor(getResources().getColor(R.color.stepper_completed_color));
        binding.view2.setBackgroundColor(getResources().getColor(R.color.stepper_completed_color));
        binding.view3.setBackgroundColor(getResources().getColor(R.color.stepper_completed_color));
        binding.btnNext.setVisibility(View.INVISIBLE);
    }

    public interface onNextListner{
        public boolean onNextClicked();
    }
}
