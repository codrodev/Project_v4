package dm.sime.com.kharetati.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentBottomNavigationBinding;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.viewModels.BottomNavigationViewModel;
import dm.sime.com.kharetati.view.viewModels.MapViewModel;

public class BottomNavigationFragmentSheet extends BottomSheetDialogFragment {

    BottomNavigationViewModel model;
    FragmentBottomNavigationBinding binding;
    private View mRootView;

    public static BottomNavigationFragmentSheet newInstance() {
        BottomNavigationFragmentSheet f = new BottomNavigationFragmentSheet();
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(BottomNavigationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_navigation, container, false);
        binding.setFragmentBottomNavigation(model);
        mRootView = binding.getRoot();

        binding.layoutShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Global.isConnected(getActivity())) {
                    if (Global.appMsg != null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else{
                    StringBuilder sb = new StringBuilder();

                    sb.append(getString(R.string.try_kharetati_app));
                    sb.append('\n');
                    sb.append('\n');
                    sb.append("Play Store: https://play.google.com/store/apps/details?id=dm.sime.com.kharetati");
                    sb.append('\n');
                    sb.append('\n');
                    sb.append("App Store: https://itunes.apple.com/ca/app/kharetati/id1277642590?mt=8");
                    sb.append('\n');

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.kharetati_app));
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                    sendIntent.setType("text/html");
                    startActivity(Intent.createChooser(sendIntent, "Share with"));

                }
                dismiss();
            }
        });
        binding.layoutRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Global.isConnected(getActivity())) {
                    if (Global.appMsg != null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else
                    AlertDialogUtil.ratingAlertDialog(
                            getResources().getString(R.string.menu_rate_us),
                            getResources().getString(R.string.msg_rate_us),
                            getResources().getString(R.string.rate_it),getResources().getString(R.string.remindme),getActivity()
                    );
            }
        });
        binding.aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Global.isConnected(getActivity())) {
                    if (Global.appMsg != null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else{
                    ArrayList al = new ArrayList();
                    al.add(Global.CURRENT_LOCALE.equals("en")?Global.aboutus_en_url:Global.aboutus_ar_url);
                    ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                }
                dismiss();
            }
        });
        binding.help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList al = new ArrayList();
                al.add("");
                ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                dismiss();
            }
        });
        binding.faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList al = new ArrayList();
                al.add("");
                ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                dismiss();
            }
        });
        binding.termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Global.isConnected(getActivity())) {
                    if (Global.appMsg != null)
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), Global.CURRENT_LOCALE.equals("en") ? Global.appMsg.getInternetConnCheckEn() : Global.appMsg.getInternetConnCheckAr(), getResources().getString(R.string.ok), getActivity());
                    else
                        AlertDialogUtil.errorAlertDialog(getResources().getString(R.string.lbl_warning), getResources().getString(R.string.internet_connection_problem1), getResources().getString(R.string.ok), getActivity());
                }
                else{
                ArrayList al = new ArrayList();
                al.add(Global.CURRENT_LOCALE.equals("en")?Global.terms_en_url:Global.terms_ar_url);
                ((MainActivity)getActivity()).loadFragment(FragmentTAGS.FR_WEBVIEW,true,al);
                }
                dismiss();
            }
        });

        return binding.getRoot();
    }
}
