package dm.sime.com.kharetati.view.fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.ActivityDeliveryDeatailsBinding;
import dm.sime.com.kharetati.datas.models.DeliveryDetails;
import dm.sime.com.kharetati.datas.models.RetrievedDeliveryDetails;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.viewModels.DeliveryDetailViewModel;
import dm.sime.com.kharetati.view.viewModels.ParentSiteplanViewModel;

import static android.content.Context.MODE_PRIVATE;
import static dm.sime.com.kharetati.utility.Global.CURRENT_LOCALE;
import static dm.sime.com.kharetati.utility.Global.makani;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_DELIVERY;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_LANDOWNER_SELECTION;

public class DeliveryFragment extends Fragment implements ParentSiteplanFragment.onNextListner{


    public static boolean isDetailsSaved;
    private ActivityDeliveryDeatailsBinding binding;
    private View mRootView;
    private DeliveryDetailViewModel model;
    private String email,name,building,buildingnum,addr,strtAdress,lndmark,emirates,phone;
    public int spinner_position;
    public static int saved_position;
    public static int emId;
    public static String userid;
    boolean isValid = false;
    private String locale;
    public static Boolean isDeliveryFragment =false;
    private Tracker mTracker;


    public static DeliveryFragment newInstance(){
        DeliveryFragment fragment = new DeliveryFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "Dubai-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.getView());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(DeliveryDetailViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.current_fragment_id = FragmentTAGS.FR_DELIVERY;
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_delivery_deatails, container, false);
        binding.setDeliveryDetailVM(model);
        mRootView = binding.getRoot();

        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_DELIVERY);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        if(CURRENT_LOCALE.equals("en")) binding.linearLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.linearLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.etAdress.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.etAdress.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.etRecievername.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.etRecievername.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.etEmailaddress.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.etEmailaddress.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.etEmirates.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.etEmirates.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.etMobile.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.etMobile.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.etMakani.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.etMakani.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.etAdress.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.etAdress.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.etBuildingName.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.etBuildingName.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.etVillaBuildingNumber.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.etVillaBuildingNumber.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.etLandmark.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.etLandmark.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.etStreetAddress.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.etStreetAddress.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);if(CURRENT_LOCALE.equals("en")) binding.etAdress.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.etAdress.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.txtRecieverName.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.txtRecieverName.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.txtemail.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.txtemail.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.txtEmirate.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.txtEmirate.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.txtmobile.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.txtmobile.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.txtmakani.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.txtmakani.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.txtMaailingAddrress.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.txtMaailingAddrress.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.txtBuildingName.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.txtBuildingName.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.txtBuildingNo.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.txtBuildingNo.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.txtnearestLandmark.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.txtnearestLandmark.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if(CURRENT_LOCALE.equals("en")) binding.txtStreetAddress.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);else binding.txtStreetAddress.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);



        isDeliveryFragment =true;
        ParentSiteplanFragment.listner = this;
        initializePage();
        final String spinnerItems[]=getActivity().getResources().getStringArray(R.array.emirates);
        userid=Global.getUser(getActivity()).getEmail();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Global.hideSoftKeyboard(getActivity());

        setRetainInstance(true);

        ArrayList al=new ArrayList();
        for(int i=0;i<spinnerItems.length;i++)
        {
            al.add(spinnerItems[i]);
        }

        //ArrayAdapter<String> aa =new ArrayAdapter(getActivity(),R.layout.attachment_drp_view,R.id.txtLandOwner,al);
        ArrayAdapter<String> aa;
        if (Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0) {
            aa= new ArrayAdapter(getActivity(),R.layout.attachment_drp_view,R.id.txtLandOwner,al);
        } else {
            aa= new ArrayAdapter(getActivity(),R.layout.attachment_drp_view_ar,R.id.txtLandOwner,al);
        }

        binding.etEmirates.setAdapter(aa);
        binding.etEmirates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                emirates=spinnerItems[position];

                String value = getResources().getStringArray(R.array.emirates_values)[position];
                emId = 0;
                try{
                    emId = Integer.parseInt(value);
                } catch (Exception e){

                }
                if(emId > 0) {
                    if(emId==1 || emId==3){
                        binding.etMakani.setText("");
                        binding.etMakani.setEnabled(false);
                        makani="";
                    }
                    else {
                        binding.etMakani.setEnabled(true);
                        if(position!=convertEmirateId(ParentSiteplanViewModel.deliveryDetails.getEmirate())){
                            //binding.etMakani.setText("");
                            //makani="";
                        }
                    }
                    binding.etEmirates.setSelection(emId);
                    ParentSiteplanViewModel.deliveryDetails.setEmirate(String.valueOf(emId));
                    spinner_position = emId;
                    //save here
                    if(binding.etEmailaddress.getText().toString().length()>0 && binding.etMobile.getText().toString().length()>=10 && binding.etRecievername.getText().toString().length()>0) {
                        try {
                            if(isValidEmailId()&&isValidMobile())
                            save(userid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    spinner_position = 0;
                    ParentSiteplanViewModel.deliveryDetails.setEmirate("0");
                    if(Global.isDeliveryByCourier)
                        setNextEnabledStatus(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(userid!=null){
            retrieve(userid);
        }
        binding.deliveryByCourier.setChecked(Global.isDeliveryByCourier);
        if(Global.isDeliveryByCourier){


            name = binding.etRecievername.getText().toString().trim();
            email = binding.etEmailaddress.getText().toString().trim();
            phone = binding.etMobile.getText().toString().trim();
            makani = binding.etMakani.getText().toString().trim();
            building = binding.etBuildingName.getText().toString().trim();
            lndmark = binding.etLandmark.getText().toString().trim();
            strtAdress = binding.etLandmark.getText().toString().trim();
            addr = binding.etAdress.getText().toString().trim();
            buildingnum=binding.etVillaBuildingNumber.getText().toString().trim();
            emirates = spinnerItems[spinner_position];

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(emirates)||TextUtils.isEmpty(email)) {
                /*if(Global.appMsg!=null)
                    AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getAllFieldsRequiredEn():Global.appMsg.getAllFieldsRequiredAr(), getResources().getString(R.string.ok), getActivity());
                else
                    AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.fields_are_required), getResources().getString(R.string.ok), getActivity());*/
                //Global.isDeliveryByCourier=false;
            }else if(!email.contains("@")||!email.contains("."))
            {
                if(Global.appMsg!=null)
                    AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getEnterValidEmailEn():Global.appMsg.getEnterValidEmailAr(), getResources().getString(R.string.ok), getActivity());
                else
                    AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.enter_valid_email), getResources().getString(R.string.ok), getActivity());

            }
            if(makani.length() > 0)
            {
                if(isValidEmailId() == true && isValidMobile() == true && isValidEmirate() == true) {
                    Global.isMakani =true;
                }
            } else {
                if(isValidEmailId() == true && isValidMobile() == true && isValidEmirate() == true) {
                    setNextEnabledStatus(true);
                }
                else
                    setNextEnabledStatus(false);
            }

        } else {
            setNextEnabledStatus(true);
            int count= binding.linearLayout.getChildCount();
            for(int i= 0; i<count;i++){
                if(binding.linearLayout.getChildAt(i)!=binding.deliveryByCourier)
                    binding.linearLayout.getChildAt(i).setEnabled(false);
                    binding.linearLayout.getChildAt(i).setAlpha(.5f);
            }
        }
        binding.deliveryByCourier.setTextColor(Color.parseColor("#333333"));

        /**/
        /*binding.etRecievername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (v.getText().length() > 0)
                    {
                        if (isValidEmailId() && isValidMobile() && isValidEmirate()) {
                            try {
                                save(userid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
                        }
                        return true;
                    }
                }
                return false;
            }
        });*/
        binding.etRecievername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(Global.isDeliveryByCourier){
                if (charSequence.length() > 0) {
                    if (isValidEmailId() && isValidEmirate() && isValidMobile())
                        setNextEnabledStatus(true);
                    else
                        setNextEnabledStatus(false);
                } else
                    setNextEnabledStatus(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etEmailaddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(Global.isDeliveryByCourier){
                if (charSequence.length() > 0 ) {
                    if(binding.etEmailaddress.getText().toString().contains("@") && binding.etEmailaddress.getText().toString().contains(".")) {
                        if (binding.etRecievername.getText().length() > 0 && isValidEmirate() && isValidMobile())
                            setNextEnabledStatus(true);
                        else
                            setNextEnabledStatus(false);
                    } else
                        setNextEnabledStatus(false);
                } else
                    setNextEnabledStatus(false);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(Global.isDeliveryByCourier){
                if (charSequence.length() >= 12) {
                    if (binding.etRecievername.getText().length() > 0 && isValidEmirate() && isValidEmailId())
                        setNextEnabledStatus(true);
                    else
                        setNextEnabledStatus(false);
                } else
                    setNextEnabledStatus(false);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.etMakani.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (Global.isDeliveryByCourier) {
                        if(charSequence.length()==0){
                            Global.isMakani =false;
                            makani = charSequence.toString();
                            ParentSiteplanViewModel.deliveryDetails.setMakaniNo(makani);
                        }
                        else if (charSequence.length() > 0 ){
                            makani = charSequence.toString();
                            ParentSiteplanViewModel.deliveryDetails.setMakaniNo(makani);
                            Global.isMakani =true;
                            //binding.etMakani.setText(makani);


                                //model.getMakaniToDLTM(makani);
                            /*if (binding.etMakani.getText().length() >=10 )
                                ParentSiteplanFragment.parentModel.getMakaniToDLTM(binding.etMakani.getText().toString().trim());*/



                        }


                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        /*binding.etMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (isValidMobile())
                    {
                        if (isValidEmailId() && isValidEmirate() && binding.etRecievername.getText()!=null) {
                            try {
                                save(userid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
                        }
                        return true;
                    }
                }
                return false;
            }
        });*/
        /*binding.etEmailaddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (isValidEmailId())
                    {
                        if (isValidMobile() && isValidEmirate() && binding.etRecievername.getText()!=null) {
                            try {
                                save(userid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
                        }
                        return true;
                    }
                }
                return false;
            }
        });*/
        return binding.getRoot();
    }
    public void showInvalidMakaniError() {
        if(Global.appMsg!=null)
            ParentSiteplanFragment.parentModel.parentSitePlanNavigator.onFailure(Global.CURRENT_LOCALE.equals("en")? Global.appMsg.getInvalidmakaniEn():Global.appMsg.getInvalidmakaniAr());
        else
            ParentSiteplanFragment.parentModel.parentSitePlanNavigator.onFailure(getActivity().getResources().getString(R.string.invalid_makani));
    }



    private void initializePage() {
        ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);
        binding.deliveryByCourier.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Global.isDeliveryByCourier = isChecked;
                buttonView.setChecked(Global.isDeliveryByCourier);



                if (!isChecked) {


                    Global.isDeliveryByCourier =false;

                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);

                    int count= binding.linearLayout.getChildCount();
                    for(int i= 0; i<count;i++){
                        if(binding.linearLayout.getChildAt(i)!=binding.deliveryByCourier)
                        binding.linearLayout.getChildAt(i).setEnabled(false);
                        binding.linearLayout.getChildAt(i).setAlpha(.5f);
                    }

                }
                else{
                    Global.isDeliveryByCourier =true;
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
                    int count= binding.linearLayout.getChildCount();
                    for(int i= 0; i<count;i++){
                        if(binding.linearLayout.getChildAt(i)!=binding.deliveryByCourier)
                            if(!binding.linearLayout.getChildAt(i).isEnabled()) {
                                binding.linearLayout.getChildAt(i).setEnabled(true);
                                binding.linearLayout.getChildAt(i).setAlpha(1f);
                            }
                    }
                    if (TextUtils.isEmpty(name) ||
                            TextUtils.isEmpty(emirates)||TextUtils.isEmpty(email)) {

                        //AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.fields_are_required), getResources().getString(R.string.ok), getActivity());
                        //Global.isDeliveryByCourier=false;
                    }else if(!email.contains("@")||!email.contains("."))
                    {
                        //AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.enter_valid_email), getResources().getString(R.string.ok), getActivity());

                    }
                    if(!binding.etMakani.getText().toString().trim().equals("") && binding.etMakani.getText().toString().trim().length()>0)
                    {
                            Global.isMakani =true;
                            makani =binding.etMakani.getText().toString().trim();


                        if(isValidEmailId() == true && isValidMobile() == true && isValidEmirate() == true) {

                        }
                    } else {
                        if(isValidEmailId() == true && isValidMobile() == true && isValidEmirate() == true && binding.etRecievername.getText().length()>0) {
                            setNextEnabledStatus(true);
                        }
                        else
                            setNextEnabledStatus(false);
                    }

                }

            }
        });

    }

    private boolean isValidEmirate(){
        boolean isValid = true;
        if(convertEmirateId(ParentSiteplanViewModel.deliveryDetails.getEmirate())==0) {
            //AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.select_emirate), getResources().getString(R.string.ok), getActivity());
            isValid=false;
            return isValid;
        }
        return isValid;
    }



    public  boolean isValidEmailId(){
        boolean isValid = true;
        if (TextUtils.isEmpty(binding.etEmailaddress.getText().toString())) {

            /*if(Global.appMsg!=null)
                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getEnterValidEmailEn():Global.appMsg.getEnterValidEmailAr(), getResources().getString(R.string.ok), getActivity());
            else
                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.enter_valid_email), getResources().getString(R.string.ok), getActivity());*/
            isValid=false;

            return isValid;
        }
        else if(!binding.etEmailaddress.getText().toString().contains("@")||!binding.etEmailaddress.getText().toString().contains(".")) {
            /*if(Global.appMsg!=null)
                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getEnterValidEmailEn():Global.appMsg.getEnterValidEmailAr(), getResources().getString(R.string.ok), getActivity());
            else
                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.enter_valid_email), getResources().getString(R.string.ok), getActivity());*/
            isValid=false;
            return isValid;
        }
        /*if(isValidEmirate() == true && isValidMobile() == true && binding.etRecievername.getText().toString().trim()!=null) {
            try {
                save(userid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

        return isValid;
    }

    private boolean isValidMobile(){
        boolean isValid = true;
        if (TextUtils.isEmpty(binding.etMobile.getText().toString())) {

//            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.mobile_validation), getResources().getString(R.string.ok), getActivity());
            isValid=false;

            return isValid;
        }
        if (mobileNoInitialValidation() == false) {
//            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.mobile_validation), getResources().getString(R.string.ok), getActivity());
            isValid=false;

            return isValid;
        }
        if (binding.etMobile.length() != 12) {
//            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.mobile_validation), getResources().getString(R.string.ok), getActivity());
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
                        /*if(isValidEmailId() == true && isValidEmirate() == true && binding.etRecievername.getText().toString().trim()!=null) {
                            try {
                                save(userid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }*/
                    }
                } catch (Exception ex){

                }
            }
        }
        return isValid;
    }

    public void save(String userid) throws JSONException {


        SharedPreferences.Editor editor = getActivity().getSharedPreferences(userid, MODE_PRIVATE).edit();
        editor.putString("name", binding.etRecievername.getText().toString());

        if(ParentSiteplanViewModel.deliveryDetails == null){
            ParentSiteplanViewModel.deliveryDetails = new RetrievedDeliveryDetails();
        }
        if(Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0){
            ParentSiteplanViewModel.deliveryDetails.setNameEn(binding.etRecievername.getText().toString());
            ParentSiteplanViewModel.deliveryDetails.setNameAr("");
        } else {
            ParentSiteplanViewModel.deliveryDetails.setNameEn("");
            ParentSiteplanViewModel.deliveryDetails.setNameAr(binding.etRecievername.getText().toString());
        }
        ParentSiteplanViewModel.deliveryDetails.setEmailId(binding.etEmailaddress.getText().toString());
        ParentSiteplanViewModel.deliveryDetails.setMobileNo(binding.etMobile.getText().toString());
        ParentSiteplanViewModel.deliveryDetails.setBldgName(binding.etBuildingName.getText().toString());
        ParentSiteplanViewModel.deliveryDetails.setBldgNo(binding.etVillaBuildingNumber.getText().toString());
        ParentSiteplanViewModel.deliveryDetails.setNearestLandmark(binding.etLandmark.getText().toString());
        ParentSiteplanViewModel.deliveryDetails.setStreetAddress(binding.etStreetAddress.getText().toString());
        ParentSiteplanViewModel.deliveryDetails.setMainAddress(binding.etAdress.getText().toString());
        ParentSiteplanViewModel.deliveryDetails.setMakaniNo(makani);

        editor.apply();
        editor.commit();
        Global.hideSoftKeyboard(getActivity());
        //Toast.makeText(getActivity(), getResources().getString(R.string.deatails_saved), Toast.LENGTH_SHORT).show();

        isDetailsSaved = true;
        //Global.isDeliveryByCourier = true;
        binding.deliveryByCourier.setChecked(Global.isDeliveryByCourier);
        if(Global.isDeliveryByCourier) {
            Global.deliveryDetails=new DeliveryDetails();
            if(ParentSiteplanViewModel.deliveryDetails != null){
                Global.deliveryDetails.setNameEn(ParentSiteplanViewModel.deliveryDetails.getNameEn());
                Global.deliveryDetails.setNameAr( ParentSiteplanViewModel.deliveryDetails.getNameAr());
                Global.deliveryDetails.setEmailId( ParentSiteplanViewModel.deliveryDetails.getEmailId());
                Global.deliveryDetails.setMobileNo( ParentSiteplanViewModel.deliveryDetails.getMobileNo());
                Global.deliveryDetails.setBldgName(ParentSiteplanViewModel.deliveryDetails.getBldgName());
                Global.deliveryDetails.setBldgNo( ParentSiteplanViewModel.deliveryDetails.getBldgNo());
                Global.deliveryDetails.setNearestLandmark( ParentSiteplanViewModel.deliveryDetails.getNearestLandmark());
                Global.deliveryDetails.setStreetAddress( ParentSiteplanViewModel.deliveryDetails.getStreetAddress());
                Global.deliveryDetails.setMainAddress(ParentSiteplanViewModel.deliveryDetails.getMainAddress());
                Global.deliveryDetails.setEmirate(Integer.parseInt(ParentSiteplanViewModel.deliveryDetails.getEmirate()));
                Global.deliveryDetails.setMakaniNo( ParentSiteplanViewModel.deliveryDetails.getMakaniNo());



            }

            //Global.deliveryDetails.setEmID(DeliveryFragment.emId);
        }
        ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);


    }

    public void retrieve(String userid){
        SharedPreferences preferences = getActivity().getSharedPreferences(userid, MODE_PRIVATE);

        binding.etRecievername.setText("");
        if(ParentSiteplanViewModel.deliveryDetails != null){
            if(ParentSiteplanViewModel.deliveryDetails.getEmailId() != null &&
                    ParentSiteplanViewModel.deliveryDetails.getEmailId().length() > 0){
                binding.etEmailaddress.setText(ParentSiteplanViewModel.deliveryDetails.getEmailId());
            } else if(Global.isUAE){
                if(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getEmail() != null){
                    binding.etEmailaddress.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getEmail());
                }
            } else if(Global.getUser(getActivity()).getEmail() != null) {
                binding.etEmailaddress.setText(Global.getUser(getActivity()).getEmail());
            }
            if(ParentSiteplanViewModel.deliveryDetails.getMobileNo() != null &&
                    ParentSiteplanViewModel.deliveryDetails.getMobileNo().length() > 0){
                binding.etMobile.setText(ParentSiteplanViewModel.deliveryDetails.getMobileNo());
            } else if(Global.isUAE){
                if(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getMobile() != null){
                    binding.etMobile.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getMobile());
                }
            } else if(Global.getUser(getActivity()).getMobile() != null) {
                binding.etMobile.setText(Global.getUser(getActivity()).getMobile());
            }
            if(ParentSiteplanViewModel.deliveryDetails.getMakaniNo() != null &&
                    ParentSiteplanViewModel.deliveryDetails.getMakaniNo().length() > 0){
                binding.etMakani.setText(ParentSiteplanViewModel.deliveryDetails.getMakaniNo());
            }
            if(Global.CURRENT_LOCALE.compareToIgnoreCase("en")==0) {
                if (ParentSiteplanViewModel.deliveryDetails.getNameEn() != null &&
                        ParentSiteplanViewModel.deliveryDetails.getNameEn().length() > 0) {
                    binding.etRecievername.setText("");
                    binding.etRecievername.setText(ParentSiteplanViewModel.deliveryDetails.getNameEn());
                }else if (ParentSiteplanViewModel.deliveryDetails.getNameAr() != null &&
                        ParentSiteplanViewModel.deliveryDetails.getNameAr().length() > 0) {
                    binding.etRecievername.setText(ParentSiteplanViewModel.deliveryDetails.getNameAr());
                } else {
                    if(Global.isUAE){
                        if(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameEN() != null){
                            binding.etRecievername.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameEN());
                        }
                    } else if(Global.getUser(getActivity()).getFullname() != null) {
                        binding.etRecievername.setText(Global.getUser(getActivity()).getFullname());
                    }
                }
            } else {
                if (ParentSiteplanViewModel.deliveryDetails.getNameAr() != null &&
                        ParentSiteplanViewModel.deliveryDetails.getNameAr().length() > 0) {
                    binding.etRecievername.setText(ParentSiteplanViewModel.deliveryDetails.getNameAr());
                } else if(Global.isUAE){
                    if(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameAR() != null){
                        binding.etRecievername.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameAR());
                    }
                } else if(Global.getUser(getActivity()).getFullnameAR() != null && !Global.getUser(getActivity()).getFullnameAR().contentEquals("null")){
                    binding.etRecievername.setText(Global.getUser(getActivity()).getFullnameAR());
                } else if (ParentSiteplanViewModel.deliveryDetails.getNameEn() != null &&
                        ParentSiteplanViewModel.deliveryDetails.getNameEn().length() > 0) {
                    binding.etRecievername.setText(ParentSiteplanViewModel.deliveryDetails.getNameEn());
                }

            }
            if(ParentSiteplanViewModel.deliveryDetails.getBldgName() != null){
                binding.etBuildingName.setText(ParentSiteplanViewModel.deliveryDetails.getBldgName());
            }
            if(ParentSiteplanViewModel.deliveryDetails.getBldgNo() != null){
                binding.etVillaBuildingNumber.setText(ParentSiteplanViewModel.deliveryDetails.getBldgNo());
            }
            if(ParentSiteplanViewModel.deliveryDetails.getMainAddress() != null){
                binding.etAdress.setText(ParentSiteplanViewModel.deliveryDetails.getMainAddress());
            }
            if(ParentSiteplanViewModel.deliveryDetails.getNearestLandmark() != null){
                binding.etLandmark.setText(ParentSiteplanViewModel.deliveryDetails.getNearestLandmark());
            }
            if(ParentSiteplanViewModel.deliveryDetails.getStreetAddress() != null){
                binding.etStreetAddress.setText(ParentSiteplanViewModel.deliveryDetails.getStreetAddress());
            }
            if(convertEmirateId(ParentSiteplanViewModel.deliveryDetails.getEmirate()) != 0 &&
                    convertEmirateId(ParentSiteplanViewModel.deliveryDetails.getEmirate())> 0){
                try {
                    int val = convertEmirateId(ParentSiteplanViewModel.deliveryDetails.getEmirate());
                    binding.etEmirates.setSelection(fetchEmirate(val));
                } catch (Exception e){

                }
                
            }

        } else {
            if(Global.isUAE){
                if(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getEmail() != null){
                    binding.etEmailaddress.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getEmail());
                }
            } else if(Global.getUser(getActivity()).getEmail() != null) {
                binding.etEmailaddress.setText(Global.getUser(getActivity()).getEmail());
            }
            if(Global.isUAE){
                if(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getMobile() != null){
                    binding.etMobile.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getMobile());
                }
            } else if(Global.getUser(getActivity()).getMobile() != null) {
                binding.etMobile.setText(Global.getUser(getActivity()).getMobile());
                
            }
            if(Global.CURRENT_LOCALE.equals("en")) {
                if(Global.isUAE){
                    if(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameEN() != null){
                        binding.etRecievername.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameEN());
                    }
                } else if (Global.getUser(getActivity()).getFullname() != null) {
                    binding.etRecievername.setText(Global.getUser(getActivity()).getFullname());
                }
            } else {
                if(Global.isUAE){
                    if(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameAR() != null){
                        binding.etRecievername.setText(Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getFullnameAR());
                    }
                } else if (Global.getUser(getActivity()).getFullnameAR() != null) {
                    binding.etRecievername.setText(Global.getUser(getActivity()).getFullnameAR());
                }
            }
            ParentSiteplanViewModel.deliveryDetails.setEmirate("0");

        }

    }

    private int fetchEmirate(int id){
        String[] emirateValue = getResources().getStringArray(R.array.emirates_values);
        int arrayPosition = 0;
        for(int i = 0; i < emirateValue.length; i++){
            if(emirateValue[i].equals(String.valueOf(id))){
                arrayPosition = i;
                break;
            }
        }
        return arrayPosition;
    }
    private int convertEmirateId(String emirateId){
        int id=0;
        try{
            id=Integer.parseInt(emirateId);
        }
        catch(Exception e){

        }

        return id;
    }


    @Override
    public void onResume() {
        super.onResume();
        Global.hideSoftKeyboard(getActivity());
        retrieve(userid);
        if(convertEmirateId(ParentSiteplanViewModel.deliveryDetails.getEmirate())!=0)
            binding.etEmirates.setSelection(convertEmirateId(ParentSiteplanViewModel.deliveryDetails.getEmirate()));
        else if(saved_position!=0)
            binding.etEmirates.setSelection(saved_position);
        else if(spinner_position!=0)
            binding.etEmirates.setSelection(spinner_position);

        /*if(binding.deliveryByCourier.isChecked()){
            Global.isDeliveryByCourier =false;
        }*/
    }

    public void setNextEnabledStatus(boolean enabledStatus){
        if(enabledStatus){
            /*try {
                save(userid);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

        }
        ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(enabledStatus);
    }

    @Override
    public boolean onNextClicked() {
        try {


            if(binding.etMakani.getText().toString().trim().length()>0 && !binding.etMakani.getText().toString().trim().equals("")){
                //HomeFragment.homeVM.getMakaniToDLTM(binding.etMakani.getText().toString().trim());

                if(Global.isValidMakani) {
                    save(userid);
                    return true;
                }
                else{
                    //setNextEnabledStatus(false);

                    return false;
                }

            }
            else{
            save(userid);
            return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}

