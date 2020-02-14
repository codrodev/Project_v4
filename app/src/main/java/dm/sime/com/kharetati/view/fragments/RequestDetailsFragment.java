package dm.sime.com.kharetati.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;

public class RequestDetailsFragment extends Fragment {
    private static final String REQUEST_NUMBER = "requestNumber";
    private static final String PARCEL_ID = "parcelId";
    private static final String VOUCHER_NUMBER = "voucherNo";
    private static final String VOUCHER_AMOUNT = "voucherAmount";
    private static final String ERAD_URL = "eradUrl";
    private static final String CALLBACK_URL = "callBackUrl";
    private static final String USER_NAME = "userName";
    private static final String MOBILE = "mobile";
    private static final String EMAIL = "email";

    private String requestNumber;
    private String parcelId;
    private String voucherNo;
    private String voucherAmount,eradUrl,callBackUrl,userName,mobile,email;
    private TextView tvRequestNumber, tvParcelId, tvVoucher, tvAmount;


    private Button payNow;
    private Button done;

    public static RequestDetailsFragment newInstance(String requestNumber, String parcelId, String voucherNo, String voucherAmount,String eradUrl,String callBackUrl,String userName,String mobile,String email) {
        RequestDetailsFragment fragment = new RequestDetailsFragment();
        Bundle args = new Bundle();
        args.putString(REQUEST_NUMBER, requestNumber == null?"" : requestNumber);
        args.putString(PARCEL_ID, parcelId == null ?"" :parcelId);
        args.putString(VOUCHER_NUMBER, voucherNo==null ?"" :voucherNo);
        args.putString(VOUCHER_AMOUNT, voucherAmount==null ?"" :voucherAmount);
        args.putString(ERAD_URL, eradUrl==null ?"" :eradUrl);
        args.putString(CALLBACK_URL, callBackUrl==null ?"" :callBackUrl);
        args.putString(USER_NAME, userName==null ?"" :userName);
        args.putString(MOBILE, mobile==null ?"" :mobile);
        args.putString(EMAIL, email==null ?"" :email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "Dubai-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.getView());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requestNumber = getArguments().getString(REQUEST_NUMBER);
            parcelId = getArguments().getString(PARCEL_ID);
            voucherNo = getArguments().getString(VOUCHER_NUMBER);
            voucherAmount = getArguments().getString(VOUCHER_AMOUNT);
            eradUrl = getArguments().getString(ERAD_URL);
            callBackUrl = getArguments().getString(CALLBACK_URL);
            userName = getArguments().getString(USER_NAME);
            email = getArguments().getString(EMAIL);
            mobile = getArguments().getString(MOBILE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Global.current_fragment_id= FragmentTAGS.FR_REQUEST_DETAILS;
        AttachmentFragment.isDeliveryDetails=false;
        View view = inflater.inflate(R.layout.fragment_request_details, container, false);
        /*communicator = (Communicator) getActivity();
        communicator.hideMainMenuBar();
        communicator.hideAppBar();
        final ApplicationController application = (ApplicationController) getActivity().getApplication();*/

        tvRequestNumber=(TextView) view.findViewById(R.id.tvRequestNumber);
        tvParcelId=(TextView) view.findViewById(R.id.tvParcelId);
        tvVoucher=(TextView) view.findViewById(R.id.tvVoucher);
        tvAmount=(TextView) view.findViewById(R.id.tvAmount);
        payNow=(Button) view.findViewById(R.id.payNow);
        done=(Button) view.findViewById(R.id.done);

        tvRequestNumber.setText(requestNumber);
        tvParcelId.setText(parcelId);
        tvVoucher.setText(voucherNo);
        tvAmount.setText(voucherAmount);

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.paymentStatus = null;
                int locale=Global.CURRENT_LOCALE.compareToIgnoreCase("en")== 0 ? 1 : 2;
                AttachmentFragment.callBackURL = callBackUrl;
                AttachmentFragment.paymentUrl = eradUrl+"&locale="+locale+"&VoucherNo="+voucherNo+"&PayeeNameEN="+userName+"&MobileNo="+mobile+"&eMail="+email+"&ReturnURL="+callBackUrl;
                ArrayList al = new ArrayList<>();
                al.add(AttachmentFragment.paymentUrl);
                ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.paymentStatus = null;
                int index = ((MainActivity)getActivity()).getSupportFragmentManager().getBackStackEntryCount()-1;
                FragmentManager.BackStackEntry backEntry = getFragmentManager().getBackStackEntryAt(index-1);
                String tag = backEntry.getName();
                Fragment fragment = getFragmentManager().findFragmentByTag(tag);

                if(fragment.getTag().compareToIgnoreCase(FragmentTAGS.FR_ATTACHMENT)==0){
                    FragmentManager fragmentManager=getFragmentManager();
                    if(fragmentManager!=null)
                        while(fragmentManager.getBackStackEntryCount() >=0) {
                            if(fragmentManager.getBackStackEntryCount()== 0 ){
                                break;
                            } else {
                                fragmentManager.popBackStackImmediate();
                            }
                        }

                }
                else
                    ((MainActivity)getActivity()).onBackPressed();

            }
        });


        return view;
    }
}
