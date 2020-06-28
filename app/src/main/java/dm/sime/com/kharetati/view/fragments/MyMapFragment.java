package dm.sime.com.kharetati.view.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentMymapBinding;
import dm.sime.com.kharetati.datas.models.MyMapResults;
import dm.sime.com.kharetati.datas.models.RetrieveMyMapResponse;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.MyMapRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.AppUrls;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.navigators.MyMapNavigator;
import dm.sime.com.kharetati.view.viewModels.MyMapViewModel;
import dm.sime.com.kharetati.view.viewmodelfactories.MyMapViewModelFactory;

import static dm.sime.com.kharetati.utility.Global.alertDialog;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_BOOKMARK;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_MYMAP;

public class MyMapFragment extends Fragment implements MyMapNavigator {

    FragmentMymapBinding binding;
    MyMapViewModel model;
    private View mRootView;
    private MyMapViewModelFactory factory;
    private MyMapRepository repository;
    private Tracker mTracker;
    private EditText dateFrom;
    private EditText dateTo;
    private Calendar calendar;
    public static int fromyear,frommonth,fromday;
    private Button button_findSitePlan;
    private Date startDate, endDate;
    private ProgressDialog progressDialog;
    private EditText parcelID;
    private AlertDialog alertDialogSearch;
    private IBinder iBinder;
    public static MyMapViewModel myMapModel;
    private boolean isDesending;

    public static MyMapFragment newInstance(){
        MyMapFragment fragment = new MyMapFragment();
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
        try {
            repository = new MyMapRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        factory = new MyMapViewModelFactory(getActivity(),repository);

        model = ViewModelProviders.of(getActivity(),factory).get(MyMapViewModel.class);
        model.myMapNavigator = this;
        myMapModel = model;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.current_fragment_id = FragmentTAGS.FR_MYMAP;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mymap, container, false);
        binding.setFragmentMyMapVM(model);
        mRootView = binding.getRoot();
        Global.HelpUrl = Global.CURRENT_LOCALE.equals("en")?Global.mymaps_en_url:Global.mymaps_ar_url;
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_MYMAP);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        try {
            initializePage();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        binding.sortDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isDesending = true;


                sortSiteplans(isDesending);

            }
        });
        binding.sortAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isDesending = false;


                sortSiteplans(isDesending);

            }
        });
        binding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.findSitePlan.setBackground(getResources().getDrawable(R.drawable.rounded_corners_bg));
                binding.imgSearch.setImageDrawable(getResources().getDrawable(R.drawable.ic_search));
                binding.txtSearch.setTextColor(getResources().getColor(R.color.black));
                binding.reset.setBackground(getResources().getDrawable(R.drawable.rounded_corners_green_bg));
                binding.imgReset.setImageDrawable(getResources().getDrawable(R.drawable.reload_white));
                binding.txtReset.setTextColor(getResources().getColor(R.color.white));
                model.getAllSitePlans(getActivity());
            }
        });
        binding.findSitePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                binding.findSitePlan.setBackground(getResources().getDrawable(R.drawable.rounded_corners_green_bg));
                binding.imgSearch.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_white));
                binding.txtSearch.setTextColor(getResources().getColor(R.color.white));
                binding.reset.setBackground(getResources().getDrawable(R.drawable.rounded_corners_bg));
                binding.imgReset.setImageDrawable(getResources().getDrawable(R.drawable.reload));
                binding.txtReset.setTextColor(getResources().getColor(R.color.black));

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.fragment_find_site_plan, null);
                dialogBuilder.setView(dialogView);
                iBinder = dialogView.getRootView().getWindowToken();
               /* EditText editText = (EditText) dialogView.findViewById(R.id.label_field);


                editText.setText("test label");*/

                dateFrom=(EditText)dialogView.findViewById(R.id.datePicker_from);
                dateTo=(EditText)dialogView.findViewById(R.id.datePicker_to);

                button_findSitePlan=(Button)dialogView.findViewById(R.id.button_findSitePlan);
                parcelID=(EditText)dialogView.findViewById(R.id.parcelId);
                Global.isFindSitePlan = false;
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                button_findSitePlan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Global.hideSoftKeyboard(getActivity());
                        if (!Global.isConnected(getActivity())) {

                            if(Global.appMsg!=null)
                                AlertDialogUtil.errorAlertDialog(getActivity().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , getActivity().getString(R.string.ok), getActivity());
                            else
                                AlertDialogUtil.errorAlertDialog(getActivity().getString(R.string.lbl_warning), getActivity().getString(R.string.internet_connection_problem1), getActivity().getString(R.string.ok), getActivity());
                        }
                        else{
                            if(validateFilters() == true) {


                                Global.hideSoftKeyboard(getActivity(),iBinder);
                                getAllSitePlans();
                            }}
                    }
                });
                parcelID.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        parcelID.setFocusable(true);
                        parcelID.setFocusableInTouchMode(true);
                        return false;
                    }
                });
                dateFrom.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        Global.hideSoftKeyboard(getActivity(),iBinder);
                        dateFrom.setFocusable(true);
                        dateFrom.requestFocus();
                        parcelID.setFocusable(false);
                        chooseFromDate();
                    }
                });
                dateTo.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        Global.hideSoftKeyboard(getActivity(),iBinder);
                        dateTo.setFocusable(true);
                        dateTo.requestFocus();
                        parcelID.setFocusable(false);


                        chooseToDate();
                    }
                });
                alertDialogSearch = dialogBuilder.create();
                alertDialogSearch.show();
            }
        });
        setRetainInstance(true);
        return binding.getRoot();
    }

    private void initializePage() throws JSONException {
        Global.sitePlanList = new ArrayList<MyMapResults>();
        model.initializeMyMapViewModel(getActivity());


        model.getMutableMyMap().observe(getActivity(), new Observer<List<MyMapResults>>() {
            @Override
            public void onChanged(List<MyMapResults> lstMyMap) {
                //model.loading.set(View.GONE);
                if (lstMyMap.size() > 0) {
                    model.setMyMapAdapter(lstMyMap);
                    binding.recylerMyMaps.setAdapter(model.getMyMapAdapter());
                    binding.recylerMyMaps.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.recylerMyMaps.setHasFixedSize(true);
                }
            }
        });
    }

    @Override
    public void onStarted() {
        AlertDialogUtil.showProgressBar(getActivity(),true);
    }

    @Override
    public void onSuccess() {
        AlertDialogUtil.showProgressBar(getActivity(),false);
        binding.txtMessage.setVisibility(View.GONE);
        binding.recylerMyMaps.setAdapter(model.getMyMapAdapter());


    }

    @Override
    public void onEmpty(String message) {
        AlertDialogUtil.showProgressBar(getActivity(),false);
        binding.txtMessage.setVisibility(View.VISIBLE);
        binding.txtMessage.setText(message);
    }

    @Override
    public void onFailure(String Msg) {
        binding.txtMessage.setVisibility(View.GONE);
        AlertDialogUtil.showProgressBar(getActivity(),false);
        if(getResources()!=null)
        AlertDialogUtil.errorAlertDialog("",Msg,getActivity().getResources().getString(R.string.ok),getActivity());

    }

    @Override
    public void sortSiteplans(boolean ascending) {

        if(ascending)
        {
            Collections.sort(model.lstMyMap, new Comparator<MyMapResults>() {
                @Override
                public int compare(MyMapResults siteplan1, MyMapResults siteplan2) {
                    if(siteplan1.getReqCreatedDate()==null || siteplan2.getReqCreatedDate()==null) return 0;
                    return siteplan1.getReqCreatedDate().compareTo(siteplan2.getReqCreatedDate());
                }
            });
        }
        else{

            Collections.sort(model.lstMyMap, new Comparator<MyMapResults>() {
                @Override
                public int compare(MyMapResults siteplan1, MyMapResults siteplan2) {
                    if(siteplan1.getReqCreatedDate()==null || siteplan2.getReqCreatedDate()==null) return 0;
                    return siteplan1.getReqCreatedDate().compareTo(siteplan2.getReqCreatedDate())>=0?-1:0;
                }
            });
        }
        DashboardFragment.sortDescending=!ascending;
        if(ascending)
        {


        }
        else{



        }

        if(model.getMyMapAdapter()!=null)
            model.getMyMapAdapter().notifyDataSetChanged();

    }

    @Override
    public void onViewSitePlanSuccess() {
        AlertDialogUtil.showProgressBar(getActivity(),false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    private boolean validateFilters(){
        boolean isValid = true;
        String parcelNumber = parcelID.getText().toString();
        parcelNumber = parcelNumber.replaceAll("\\s+","");
        parcelNumber = parcelNumber.replaceAll("_","");
        if (parcelNumber.matches("") && (dateFrom.getText().toString().trim() == "" || dateFrom.getText().toString().trim().length() < 1) &&
                (dateTo.getText().toString().trim() == "" || dateTo.getText().toString().trim().length() < 1)) {
            isValid = false;
            if(Global.isConnected(getActivity())) {
                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.enter_plot_or_date), getResources().getString(R.string.ok), getContext());
                parcelID.setFocusableInTouchMode(true);
                parcelID.setFocusable(true);
            } else
                AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getContext());
            return isValid;
        } else if (!dateFrom.getText().toString().trim().equals("") && dateFrom.getText().toString().trim().length() > 0 &&
                dateTo.getText().toString().trim().equals("") && dateTo.getText().toString().trim().length() < 1){
            isValid = false;
            AlertDialogUtil.errorAlertDialog("", getResources().getString(R.string.valid_date), getResources().getString(R.string.ok), getActivity());
            return isValid;
        } else if (dateFrom.getText().toString().trim().equals("") && dateFrom.getText().toString().trim().length() < 1 &&
                !dateTo.getText().toString().trim().equals("") && dateTo.getText().toString().trim().length() > 0){
            isValid = false;
            AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.valid_date), getResources().getString(R.string.ok), getActivity());
            return isValid;
        } else if (parcelNumber.matches("")) {
            if (!dateFrom.getText().toString().trim().equals("") && dateFrom.getText().toString().trim().length() < 1 &&
                    dateTo.getText().toString().trim().equals("") && dateTo.getText().toString().trim().length() < 1) {
                isValid = false;
                if (Global.isConnected(getActivity())) {
                    AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.PLEASE_ENTER_PLOTNUMBER), getResources().getString(R.string.ok), getContext());
                    parcelID.setFocusableInTouchMode(true);
                    parcelID.setFocusable(true);
                } else
                    AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getContext());
                return isValid;
            }
        }
        if(dateFrom.getText().toString().trim() != "" && dateTo.getText().toString().trim() != ""){
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date fromDate = format.parse(dateFrom.getText().toString().trim());
                Date toDate = format.parse(dateTo.getText().toString().trim());
                int val = toDate.compareTo(fromDate);
                if(val >= 0){
                    isValid = true;
                } else {
                    isValid = false;
                    AlertDialogUtil.errorAlertDialog("", getResources().getString(R.string.older_to_date), getResources().getString(R.string.ok), getActivity());
                    return isValid;
                }

                if(new Date().before(toDate)){
                    isValid = false;
                    AlertDialogUtil.errorAlertDialog("", getResources().getString(R.string.future_to_date), getResources().getString(R.string.ok), getActivity());
                    return isValid;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return isValid;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void chooseFromDate() {
        final Calendar calendar = Calendar.getInstance();
        fromyear = calendar.get(Calendar.YEAR);
        frommonth = calendar.get(Calendar.MONTH);
        fromday = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker =
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker view, final int year, final int month,
                                          final int dayOfMonth) {

                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        calendar.set(year, month, dayOfMonth);
                        startDate=calendar.getTime();
                        String dateString = sdf.format(calendar.getTime());

                        dateFrom.setText(dateString);
                        dateTo.setFocusable(true);// set the date

                    }
                }, fromyear, frommonth, fromday); // set date picker to current date

        datePicker.show();
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(final DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void chooseToDate() {
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePicker =
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(final DatePicker view, final int year, final int month,
                                          final int dayOfMonth) {

                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        calendar.set(year, month, dayOfMonth);
                        String dateString = sdf.format(calendar.getTime());
                        dateTo.setText(dateString);
                        dateTo.setFocusable(true);

                    }
                }, year, month, day); // set date picker to current date

        datePicker.show();
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(final DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }

    private void getAllSitePlans(){

        if(!Global.isConnected(getActivity())){
            return;
        }

        String fromDate, toDate;

        if(Global.isProbablyArabic(dateFrom.getText().toString())){
            fromDate = Global.arabicNumberToDecimal(dateFrom.getText().toString());
        } else {
            fromDate = dateFrom.getText().toString();
        }
        if(Global.isProbablyArabic(dateTo.getText().toString())){
            toDate = Global.arabicNumberToDecimal(dateTo.getText().toString());
        } else {
            toDate = dateTo.getText().toString();
        }

        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("token",Global.site_plan_token);
            if(Global.isUAE)
                jsonBody.put("my_id",Global.uaeSessionResponse.getService_response().getUAEPASSDetails().getUuid());
            else
                jsonBody.put("my_id",Global.loginDetails.username);
            jsonBody.put("start_date",fromDate);
            jsonBody.put("end_date",toDate);
            jsonBody.put("voucher_no","");
            jsonBody.put("request_id","");

            if(!parcelID.getText().toString().equals("") && parcelID.getText().toString().length() > 0) {
                jsonBody.put("parcel_id", Long.parseLong(parcelID.getText().toString()));
            } else {
                jsonBody.put("parcel_id", 0);
            }
            jsonBody.put("locale",Global.CURRENT_LOCALE);
            final String locale="en";
            JsonObjectRequest req = new JsonObjectRequest(Global.base_url_site_plan + AppUrls.FIND_SITE_PLANS,jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response != null){

                                    Global.hideSoftKeyboard(getActivity());
                                    Gson gson = new GsonBuilder().serializeNulls().create();

                                    RetrieveMyMapResponse siteplans =  gson.fromJson(response.toString(), RetrieveMyMapResponse.class);
                                    System.out.println(response.toString());

                                    if( siteplans.getMyMapResults() != null && siteplans.getMyMapResults().length != 0)
                                    {
                                        AlertDialogUtil.showProgressBar(getActivity(),false);

                                        model.lstMyMap.clear();
                                        model.lstMyMap = (new ArrayList<>(Arrays.asList(siteplans.getMyMapResults())));
                                        model.setMyMapAdapter( model.lstMyMap);
                                        Global.isFindSitePlan = true;
                                        //getActivity().onBackPressed();
                                        alertDialogSearch.cancel();
                                        Global.hideSoftKeyboard(getActivity());

                                    }
                                    else{

                                        AlertDialogUtil.showProgressBar(getActivity(),false);
                                        AlertDialogUtil.errorAlertDialog("",getString(R.string.no_record), getString(R.string.ok), getActivity());
                                    }
                                }
                            } catch (Exception e) {
                                AlertDialogUtil.showProgressBar(getActivity(),false);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error instanceof AuthFailureError)
                        Global.logout(getActivity());
                    VolleyLog.e("Error: ", error.getMessage());
                }
            }){    //this is the part, that adds the header to the request
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<>();
                    params.put("token", Global.accessToken);
                    return params;
                }};

            AlertDialogUtil.showProgressBar(getActivity(),true);
            Volley.newRequestQueue(getActivity()).add(req);
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtil.showProgressBar(getActivity(),false);
        }

    }

}
