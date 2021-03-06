package dm.sime.com.kharetati.view.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentPayBinding;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.PayRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.navigators.PayNavigator;
import dm.sime.com.kharetati.view.viewModels.ParentSiteplanViewModel;
import dm.sime.com.kharetati.view.viewModels.PayViewModel;
import dm.sime.com.kharetati.view.viewmodelfactories.PayViewModelFactory;

import static android.content.Context.MODE_PRIVATE;
import static dm.sime.com.kharetati.utility.Global.CURRENT_LOCALE;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_DELIVERY;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_PAY;
import static dm.sime.com.kharetati.view.fragments.DeliveryFragment.userid;

public class PayFragment extends Fragment implements PayNavigator {


    public static boolean isFromPayFragment;
    private PayRepository repository;
    private PayViewModelFactory factory;
    private PayViewModel model;
    private FragmentPayBinding binding;
    private View mRootView;
    public static String paymentType = "";
    public static String applicantMobile;
    public static String applicantEmailId;
    private Tracker mTracker;
    private SharedPreferences preferences;

    public static PayFragment newInstance(){
        PayFragment fragment = new PayFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            repository = new PayRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        factory = new PayViewModelFactory(getActivity(),repository);

        model = ViewModelProviders.of(getActivity(),factory).get(PayViewModel.class);
        model.payNavigator =this;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.current_fragment_id = FragmentTAGS.FR_PAY;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay, container, false);
        binding.setFragmentPayVM(model);
        mRootView = binding.getRoot();
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_PAY);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        if(CURRENT_LOCALE.equals("en")) binding.rootView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.rootView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setRetainInstance(true);
        preferences = getActivity().getSharedPreferences(userid, MODE_PRIVATE);
        binding.etEmailaddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



            }

            @Override
            public void afterTextChanged(Editable editable) {
                preferences.edit().putString("applicantEmailId",editable.toString().trim()).apply();
            }
        });
        binding.etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



            }

            @Override
            public void afterTextChanged(Editable editable) {
                preferences.edit().putString("applicantMobile",editable.toString().trim()).apply();
            }
        });
            String mobile = preferences.getString("applicantMobile",binding.etMobile.getText().toString().trim());
            String email = preferences.getString("applicantEmailId",binding.etEmailaddress.getText().toString().trim());
            if(email.isEmpty() && mobile.isEmpty())
                setEmailAndMobileField();
            else{

                binding.etMobile.setText(preferences.getString("applicantMobile",binding.etMobile.getText().toString().trim()));
                binding.etEmailaddress.setText(preferences.getString("applicantEmailId",binding.etEmailaddress.getText().toString().trim()));
            }

        isFromPayFragment =true;
        ParentSiteplanFragment.currentIndex =Global.uaePassConfig.hideDeliveryDetails?2:3;
        binding.payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Global.isConnected(getActivity())) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());

                }
                else{

                    paymentType = "Pay Now";
                    if(isValidEmailId() && isValidMobile()) {

                        applicantMobile = binding.etMobile.getText().toString().trim();
                        applicantEmailId = binding.etEmailaddress.getText().toString().trim();
                        preferences.edit().putString("applicantEmailId",applicantEmailId).apply();
                        preferences.edit().putString("applicantMobile",applicantMobile).apply();

                        try {
                            model.createAndUpdateRequest();
                            mTracker.send(new HitBuilders.EventBuilder()
                                    .setCategory("Payment Screen")
                                    .setAction("Action on Payment")
                                    .setLabel("Pay Now")
                                    .setValue(1)
                                    .build());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }}
        });

        binding.payLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Global.isConnected(getActivity())) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());

                }
                else{


                    paymentType = "Pay Later";

                    if(isValidMobile() == true && isValidEmailId() == true) {
                        applicantMobile = binding.etMobile.getText().toString().trim();
                        applicantEmailId = binding.etEmailaddress.getText().toString().trim();
                        preferences.edit().putString("applicantEmailId",applicantEmailId).apply();
                        preferences.edit().putString("applicantMobile",applicantMobile).apply();
                        try {
                            model.createAndUpdateRequest();
                            mTracker.send(new HitBuilders.EventBuilder()
                                    .setCategory("Payment Screen")
                                    .setAction("Action on Payment")
                                    .setLabel("Pay Later")
                                    .setValue(1)
                                    .build());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });



        return binding.getRoot();
    }


    public  boolean isValidEmailId(){
        boolean isValid = true;
        if (!Global.isValidEmail(binding.etEmailaddress.getText().toString())) {

            if(Global.appMsg!=null)
                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getEnterValidEmailEn():Global.appMsg.getEnterValidEmailAr(), getResources().getString(R.string.ok), getActivity());
            else
                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.enter_valid_email), getResources().getString(R.string.ok), getActivity());
            isValid=false;

            return isValid;
        }

        return isValid;
    }

    private boolean isValidMobile(){
        boolean isValid = true;
        if (TextUtils.isEmpty(binding.etMobile.getText().toString())) {

            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.mobile_validation), getResources().getString(R.string.ok), getActivity());
            isValid=false;

            return isValid;
        }
        if (mobileNoInitialValidation() == false) {
            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.mobile_validation), getResources().getString(R.string.ok), getActivity());
            isValid=false;

            return isValid;
        }
        if (binding.etMobile.length() != 12) {
            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.mobile_validation), getResources().getString(R.string.ok), getActivity());
            isValid=false;

            return isValid;
        }
        return isValid;
    }

    private boolean mobileNoInitialValidation(){
        boolean isValid = false;
        if(binding.etMobile.getText().toString().startsWith("971")){
            if(binding.etMobile.getText().toString().length() == 12){
                try {
                    String st = String.valueOf(binding.etMobile.getText().toString().charAt(3));
                    int val = Integer.parseInt(st);
                    if(val > 0){
                        isValid = true;

                    }
                } catch (Exception ex){

                }
            }
        }
        return isValid;
    }
    private void setEmailAndMobileField(){

        if(ParentSiteplanViewModel.applicantMailId != null &&
                ParentSiteplanViewModel.applicantMailId.length() > 0){
            binding.etEmailaddress.setText(ParentSiteplanViewModel.applicantMailId.trim());
        } else if(Global.isUAE){
            if(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getEmail() != null){
                binding.etEmailaddress.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getEmail());
            }
        } else if(Global.getUser(getActivity()).getEmail() != null) {
            binding.etEmailaddress.setText(Global.getUser(getActivity()).getEmail().trim());
        }
        if(ParentSiteplanViewModel.applicantMobileNo != null &&
                ParentSiteplanViewModel.applicantMobileNo.length() > 0){
            binding.etMobile.setText(ParentSiteplanViewModel.applicantMobileNo.trim());
        } else if(Global.isUAE){
            if(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getMobile() != null){
                binding.etMobile.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getMobile());
            }
        }  else if(Global.getUser(getActivity()).getMobile() != null) {
            binding.etMobile.setText(Global.getUser(getActivity()).getMobile().trim());
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
        if(getActivity()!=null){
        AlertDialogUtil.showProgressBar(getActivity(),false);
        AlertDialogUtil.errorAlertDialog("",Msg,getActivity().getResources().getString(R.string.ok),getActivity());
        }
    }
}
