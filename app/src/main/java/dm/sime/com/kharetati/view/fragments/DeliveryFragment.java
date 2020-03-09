package dm.sime.com.kharetati.view.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.ActivityDeliveryDeatailsBinding;
import dm.sime.com.kharetati.datas.models.DeliveryDetails;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.viewModels.DeliveryDetailViewModel;
import dm.sime.com.kharetati.view.viewModels.ParentSiteplanViewModel;

import static android.content.Context.MODE_PRIVATE;
import static dm.sime.com.kharetati.utility.Global.makani;

public class DeliveryFragment extends Fragment {


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
        isDeliveryFragment =true;
        initializePage();
        final String spinnerItems[]=getActivity().getResources().getStringArray(R.array.emirates);
        userid=Global.getUser(getActivity()).getEmail();
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Global.hideSoftKeyboard(getActivity());

        setRetainInstance(true);

        ArrayList al=new ArrayList();
        for(int i=0;i<spinnerItems.length;i++)
        {
            al.add(spinnerItems[i]);
        }

        ArrayAdapter<String> aa =new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,al);
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
                            binding.etMakani.setText("");
                            makani="";
                        }
                    }
                    binding.etEmirates.setSelection(emId);
                    ParentSiteplanViewModel.deliveryDetails.setEmirate(Integer.toString(emId));
                    spinner_position = emId;
                } else {
                    spinner_position = 0;
                    ParentSiteplanViewModel.deliveryDetails.setEmirate(Integer.toString(0));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(userid!=null){
            retrieve(userid);
        }

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

            if (TextUtils.isEmpty(name) ||
                    TextUtils.isEmpty(emirates)||TextUtils.isEmpty(email)) {
                if(Global.appMsg!=null)
                    AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getAllFieldsRequiredEn():Global.appMsg.getAllFieldsRequiredAr(), getResources().getString(R.string.ok), getActivity());
                else
                    AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.fields_are_required), getResources().getString(R.string.ok), getActivity());
                Global.isDeliveryByCourier=false;
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
                    HomeFragment.homeVM.getMakaniToDLTM(makani);
                }
            } else {
                if(isValidEmailId() == true && isValidMobile() == true && isValidEmirate() == true) {
                    try {
                        save(userid);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        int count= binding.linearLayout.getChildCount();
        for(int i= 0; i<count;i++){
            if(binding.linearLayout.getChildAt(i)!=binding.deliveryByCourier)
                binding.linearLayout.getChildAt(i).setEnabled(false);
        }


        return binding.getRoot();
    }



    private void initializePage() {
        binding.deliveryByCourier.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {


                    Global.isDeliveryByCourier =false;
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(true);

                    int count= binding.linearLayout.getChildCount();
                    for(int i= 0; i<count;i++){
                        if(binding.linearLayout.getChildAt(i)!=binding.deliveryByCourier)
                        binding.linearLayout.getChildAt(i).setEnabled(false);
                    }

                }
                else{
                    Global.isDeliveryByCourier =true;
                    ParentSiteplanFragment.parentModel.parentSitePlanNavigator.setNextEnabledStatus(false);
                    int count= binding.linearLayout.getChildCount();
                    for(int i= 0; i<count;i++){
                        if(binding.linearLayout.getChildAt(i)!=binding.deliveryByCourier)
                            binding.linearLayout.getChildAt(i).setEnabled(true);
                    }
                    if (TextUtils.isEmpty(name) ||
                            TextUtils.isEmpty(emirates)||TextUtils.isEmpty(email)) {

                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.fields_are_required), getResources().getString(R.string.ok), getActivity());
                        Global.isDeliveryByCourier=false;
                    }else if(!email.contains("@")||!email.contains("."))
                    {
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.enter_valid_email), getResources().getString(R.string.ok), getActivity());

                    }
                    if(!binding.etMakani.getText().toString().equals(""))
                    {
                        if(isValidEmailId() == true && isValidMobile() == true && isValidEmirate() == true) {
                            HomeFragment.homeVM.getMakaniToDLTM(makani);
                        }
                    } else {
                        if(isValidEmailId() == true && isValidMobile() == true && isValidEmirate() == true) {
                            try {
                                save(userid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }

            }
        });

    }

    private boolean isValidEmirate(){
        boolean isValid = true;
        if(ParentSiteplanViewModel.deliveryDetails.getEmirate().equals("0")) {
            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.select_emirate), getResources().getString(R.string.ok), getActivity());
            isValid=false;
            return isValid;
        }
        return isValid;
    }



    public  boolean isValidEmailId(){
        boolean isValid = true;
        if (TextUtils.isEmpty(binding.etEmailaddress.getText().toString())) {

            if(Global.appMsg!=null)
                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getEnterValidEmailEn():Global.appMsg.getEnterValidEmailAr(), getResources().getString(R.string.ok), getActivity());
            else
                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.enter_valid_email), getResources().getString(R.string.ok), getActivity());
            isValid=false;

            return isValid;
        }
        if(!binding.etEmailaddress.getText().toString().contains("@")||!binding.etEmailaddress.getText().toString().contains(".")) {
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

    public void save(String userid) throws JSONException {


        SharedPreferences.Editor editor = getActivity().getSharedPreferences(userid, MODE_PRIVATE).edit();
        editor.putString("name", name);

        if(ParentSiteplanViewModel.deliveryDetails == null){
            ParentSiteplanViewModel.deliveryDetails = new DeliveryDetails();
        }
        if(Global.getCurrentLanguage(getActivity()).compareToIgnoreCase("en") == 0){
            ParentSiteplanViewModel.deliveryDetails.setNameEn(name);
            ParentSiteplanViewModel.deliveryDetails.setNameAr("");
        } else {
            ParentSiteplanViewModel.deliveryDetails.setNameEn("");
            ParentSiteplanViewModel.deliveryDetails.setNameAr(name);
        }
        ParentSiteplanViewModel.deliveryDetails.setEmailId(email);
        ParentSiteplanViewModel.deliveryDetails.setMobileNo(phone);
        ParentSiteplanViewModel.deliveryDetails.setBldgName(building);
        ParentSiteplanViewModel.deliveryDetails.setBldgNo(buildingnum);
        ParentSiteplanViewModel.deliveryDetails.setNearestLandmark(lndmark);
        ParentSiteplanViewModel.deliveryDetails.setStreetAddress(strtAdress);
        ParentSiteplanViewModel.deliveryDetails.setMainAddress(addr);
        ParentSiteplanViewModel.deliveryDetails.setMakaniNo(makani);

        editor.apply();
        editor.commit();
        Global.hideSoftKeyboard(getActivity());
        Toast.makeText(getActivity(), getResources().getString(R.string.deatails_saved), Toast.LENGTH_SHORT).show();

        isDetailsSaved = true;
        Global.isDeliveryByCourier = true;
        binding.deliveryByCourier.setChecked(true);
        if(Global.isDeliveryByCourier) {
            Global.deliveryDetails=new JSONObject();
            if(ParentSiteplanViewModel.deliveryDetails != null){
                Global.deliveryDetails.put("name_en", ParentSiteplanViewModel.deliveryDetails.getNameEn());
                Global.deliveryDetails.put("name_ar", ParentSiteplanViewModel.deliveryDetails.getNameAr());
                Global.deliveryDetails.put("email_id", ParentSiteplanViewModel.deliveryDetails.getEmailId());
                Global.deliveryDetails.put("mobile", ParentSiteplanViewModel.deliveryDetails.getMobileNo());
                Global.deliveryDetails.put("building_name", ParentSiteplanViewModel.deliveryDetails.getBldgName());
                Global.deliveryDetails.put("building_no", ParentSiteplanViewModel.deliveryDetails.getBldgNo());
                Global.deliveryDetails.put("nearest_landmark", ParentSiteplanViewModel.deliveryDetails.getNearestLandmark());
                Global.deliveryDetails.put("street_address", ParentSiteplanViewModel.deliveryDetails.getStreetAddress());
                Global.deliveryDetails.put("main_address", ParentSiteplanViewModel.deliveryDetails.getMainAddress());
                Global.deliveryDetails.put("emirate", ParentSiteplanViewModel.deliveryDetails.getEmirate());
                Global.deliveryDetails.put("makani_no", ParentSiteplanViewModel.deliveryDetails.getMakaniNo());
            }

            Global.deliveryDetails.put("emirate",DeliveryFragment.emId);
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
            } else if(Global.getUser(getActivity()).getEmail() != null) {
                binding.etEmailaddress.setText(Global.getUser(getActivity()).getEmail());
            }
            if(ParentSiteplanViewModel.deliveryDetails.getMobileNo() != null &&
                    ParentSiteplanViewModel.deliveryDetails.getMobileNo().length() > 0){
                binding.etMobile.setText(ParentSiteplanViewModel.deliveryDetails.getMobileNo());
            } else if(Global.getUser(getActivity()).getMobile() != null) {
                binding.etMobile.setText(Global.getUser(getActivity()).getMobile());
            }
            if(ParentSiteplanViewModel.deliveryDetails.getMakaniNo() != null &&
                    ParentSiteplanViewModel.deliveryDetails.getMakaniNo().length() > 0){
                binding.etMakani.setText(ParentSiteplanViewModel.deliveryDetails.getMakaniNo());
            }
            if(Global.getCurrentLanguage(getActivity()).compareToIgnoreCase("en")==0) {
                if (ParentSiteplanViewModel.deliveryDetails.getNameEn() != null &&
                        ParentSiteplanViewModel.deliveryDetails.getNameEn().length() > 0) {
                    binding.etRecievername.setText("");
                    binding.etRecievername.setText(ParentSiteplanViewModel.deliveryDetails.getNameEn());
                }else if (ParentSiteplanViewModel.deliveryDetails.getNameAr() != null &&
                        ParentSiteplanViewModel.deliveryDetails.getNameAr().length() > 0) {
                    binding.etRecievername.setText(ParentSiteplanViewModel.deliveryDetails.getNameAr());
                } else {
                    if(Global.getUser(getActivity()).getFullname() != null) {
                        binding.etRecievername.setText(Global.getUser(getActivity()).getFullname());
                    }
                }
            } else {
                if (ParentSiteplanViewModel.deliveryDetails.getNameAr() != null &&
                        ParentSiteplanViewModel.deliveryDetails.getNameAr().length() > 0)
                {
                    binding.etRecievername.setText(ParentSiteplanViewModel.deliveryDetails.getNameAr());
                }else if(Global.getUser(getActivity()).getFullnameAR() != null && !Global.getUser(getActivity()).getFullnameAR().contentEquals("null")){
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
            if(ParentSiteplanViewModel.deliveryDetails.getEmirate() != null &&
                    ParentSiteplanViewModel.deliveryDetails.getEmirate().length() > 0){
                try {
                    int val = Integer.parseInt(ParentSiteplanViewModel.deliveryDetails.getEmirate());
                    binding.etEmirates.setSelection(fetchEmirate(val));
                } catch (Exception e){

                }
                
            }

        } else {
            if(Global.getUser(getActivity()).getEmail() != null) {
                binding.etEmailaddress.setText(Global.getUser(getActivity()).getEmail());
            }
            if(Global.getUser(getActivity()).getMobile() != null) {
                binding.etMobile.setText(Global.getUser(getActivity()).getMobile());
                
            }
            if(Global.CURRENT_LOCALE.equals("en")) {
                if (Global.getUser(getActivity()).getFullname() != null) {
                    binding.etRecievername.setText(Global.getUser(getActivity()).getFullname());
                }
            } else {
                if (Global.getUser(getActivity()).getFullnameAR() != null) {
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
    }

}
