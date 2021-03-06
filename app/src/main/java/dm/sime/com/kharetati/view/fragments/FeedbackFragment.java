package dm.sime.com.kharetati.view.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentFeedbackBinding;
import dm.sime.com.kharetati.datas.models.GuestDetails;
import dm.sime.com.kharetati.datas.models.User;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.viewModels.FeedbackViewModel;

import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_DELIVERY;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_FEEDBACK;

public class FeedbackFragment extends Fragment {

    FragmentFeedbackBinding binding;
    FeedbackViewModel model;
    private View mRootView;
    public static String name,email,phone,subject,description;
    private Tracker mTracker;

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
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_FEEDBACK);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        ((MainActivity)getActivity()).manageActionBar(true);
        ((MainActivity)getActivity()).manageBottomBar(true);
        ((MainActivity)getActivity()).setScreenName(getActivity().getResources().getString(R.string.feedback));
        MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(),FR_FEEDBACK , null /* class override */);
        initializePage();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return binding.getRoot();

    }

    private void initializePage() {

        if(!Global.isUserLoggedIn) {
            GuestDetails guestDetails = Global.getGuestDetails(getActivity());
            if(guestDetails!=null){
                /*binding.name.setText(guestDetails.fullname);
                binding.email.setText(guestDetails.email);
                binding.phone.setText(guestDetails.mobile);*/

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
            if(Global.isUAE){
                if(Global.CURRENT_LOCALE.compareToIgnoreCase("en")==0) {
                    if(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameEN() != null && Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameEN().length() > 0) {
                        binding.name.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameEN());
                    } else {
                        binding.name.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameAR());
                    }
                } else {
                    if(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameAR() != null && Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameAR().length() > 0) {
                        binding.name.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameAR());
                    } else {
                        binding.name.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameEN());
                    }
                }
                binding.email.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getEmail());
                binding.phone.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getMobile());
            }

        }

        binding.desc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.desc) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).onBackPressed();
            }
        });

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
                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getAllFieldsRequiredEn():Global.appMsg.getAllFieldsRequiredAr(),getResources().getString(R.string.ok),getActivity());
                    else
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
