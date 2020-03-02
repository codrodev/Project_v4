package dm.sime.com.kharetati.view.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONException;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentPayBinding;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.PayRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.navigators.PayNavigator;
import dm.sime.com.kharetati.view.viewModels.ParentSiteplanViewModel;
import dm.sime.com.kharetati.view.viewModels.PayViewModel;
import dm.sime.com.kharetati.view.viewmodelfactories.PayViewModelFactory;

public class PayFragment extends Fragment implements PayNavigator {


    private PayRepository repository;
    private PayViewModelFactory factory;
    private PayViewModel model;
    private FragmentPayBinding binding;
    private View mRootView;
    public static String paymentType;
    public static String applicantMobile;
    public static String applicantEmailId;

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay, container, false);
        binding.setFragmentPayVM(model);
        mRootView = binding.getRoot();
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

                        try {
                            model.createAndUpdateRequest();
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
                        try {
                            model.createAndUpdateRequest();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });



        return binding.getRoot();
    }


    private boolean isValidEmailId(){
        boolean isValid = true;
        if (TextUtils.isEmpty(binding.etEmailaddress.getText().toString())) {

            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.fields_are_required), getResources().getString(R.string.ok), getActivity());
            isValid=false;

            return isValid;
        }
        if(!binding.etEmailaddress.getText().toString().contains("@")||!binding.etEmailaddress.getText().toString().contains(".")) {
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
}
