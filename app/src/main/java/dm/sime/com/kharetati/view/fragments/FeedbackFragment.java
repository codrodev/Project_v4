package dm.sime.com.kharetati.view.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentFeedbackBinding;
import dm.sime.com.kharetati.datas.models.GuestDetails;
import dm.sime.com.kharetati.datas.models.User;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.viewModels.FeedbackViewModel;

public class FeedbackFragment extends Fragment {

    FragmentFeedbackBinding binding;
    FeedbackViewModel model;
    private View mRootView;
    public static String name,email,phone,subject,description;

    public static FeedbackFragment newInstance(){
        FeedbackFragment fragment = new FeedbackFragment();
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
        model = ViewModelProviders.of(this).get(FeedbackViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.current_fragment_id = FragmentTAGS.FR_FEEDBACK;
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_feedback, container, false);
        binding.setFragmentFeedbackVM(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage() {

        if(!Global.isUserLoggedIn) {
            GuestDetails guestDetails = Global.getGuestDetails(getActivity());
            if(guestDetails!=null){
                binding.name.setText(guestDetails.fullname);
                binding.email.setText(guestDetails.email);
                binding.phone.setText(guestDetails.mobile);

            }
        }
        else{
            User user= Global.getUser(getActivity());
            if(user!=null){
                if(Global.CURRENT_LOCALE.compareToIgnoreCase("en")==0) {
                    if(user.getFullname() != null && user.getFullname().length() > 0) {
                        binding.name.setText(user.getFullname());
                    } else {
                        binding.name.setText(user.getFullnameAR());
                    }
                } else {
                    if(user.getFullnameAR() != null && user.getFullnameAR().length() > 0) {
                        binding.name.setText(user.getFullnameAR());
                    } else {
                        binding.name.setText(user.getFullname());
                    }
                }
                binding.email.setText(user.getEmail());
                binding.phone.setText(user.getMobile());
            }
        }

        binding.btnSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = binding.name.getText().toString().trim();
                email = binding.email.getText().toString().trim();
                phone = binding.phone.getText().toString().trim();
                subject = binding.subject.getText().toString().trim();
                description = binding.desc.getText().toString().trim();

                if (TextUtils.isEmpty(name) ||
                        TextUtils.isEmpty(phone) ||
                        TextUtils.isEmpty(subject) ||
                        TextUtils.isEmpty(description)) {
                    AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),getResources().getString(R.string.all_fields_are_required),getResources().getString(R.string.ok),getActivity());
                    return;
                } else if(!isEmailValid(email)){
                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getEnterValidEmailEn():Global.appMsg.getEnterValidEmailAr(),getResources().getString(R.string.ok),getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),getResources().getString(R.string.enter_valid_email),getResources().getString(R.string.ok),getActivity());

                }
                else
                    ContactusFragment.contactUsVM.sendFeedBack();

            }
        });

    }
    public static boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

}
