package dm.sime.com.kharetati.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dm.sime.com.kharetati.BR;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.MyMapResults;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.fragments.PayFragment;
import dm.sime.com.kharetati.view.viewModels.MyMapViewModel;

public class MyMapAdapter  extends RecyclerView.Adapter<MyMapAdapter.GenericViewHolder> {
    private int layoutId;
    private MyMapViewModel viewModel;
    static Context context;
    List<MyMapResults> lstMyMap;

    public MyMapAdapter(@LayoutRes int layoutId, MyMapViewModel viewModel, Context context) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
        this.context = context;
        lstMyMap = new ArrayList<>();
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);

        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);

        ((TextView)holder.binding.getRoot().findViewById(R.id.plotNo)).setText(lstMyMap.get(position).getParcelId());
        ((TextView)holder.binding.getRoot().findViewById(R.id.dateAndTime)).setText(lstMyMap.get(position).getReq_created_date());
        ((TextView)holder.binding.getRoot().findViewById(R.id.txtVoucher)).setText(lstMyMap.get(position).getVoucher_no());

        if(Global.CURRENT_LOCALE.compareToIgnoreCase("en")==0){
            ((TextView)holder.binding.getRoot().findViewById(R.id.reqStatus)).setText(lstMyMap.get(position).getRequestStatus());
        } else {
            ((TextView)holder.binding.getRoot().findViewById(R.id.reqStatus)).setText(lstMyMap.get(position).getRequestStatusAr());
        }
        if(Global.CURRENT_LOCALE.compareToIgnoreCase("en")==0){
            ((TextView)holder.binding.getRoot().findViewById(R.id.sapsNo)).setText(lstMyMap.get(position).getRequestId());
        } else {
            ((TextView)holder.binding.getRoot().findViewById(R.id.sapsNo)).setText(lstMyMap.get(position).getRequestId());
        }

        if(Boolean.parseBoolean(lstMyMap.get(position).getIsPaymentPending())){
            ((TextView)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setEnabled(true);
            ((TextView)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setText(R.string.pay_now);
            ((TextView)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setVisibility(View.VISIBLE);


            ((LinearLayout)holder.binding.getRoot().findViewById(R.id.payLayout)).setVisibility(View.VISIBLE);

            ((LinearLayout)holder.binding.getRoot().findViewById(R.id.payLayout)).setBackground(context.getResources().getDrawable(R.drawable.pay_capsule_bg));
            ((LinearLayout)holder.binding.getRoot().findViewById(R.id.payLayout)).setAlpha(1f);
        }
        else {
            ((TextView)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setEnabled(false);
            ((TextView)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setVisibility(View.VISIBLE);
            ((TextView)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setText(R.string.paid);

            ((LinearLayout)holder.binding.getRoot().findViewById(R.id.payLayout)).setVisibility(View.VISIBLE);
            ((LinearLayout)holder.binding.getRoot().findViewById(R.id.payLayout)).setBackground(context.getResources().getDrawable(R.drawable.pay_capsule_bg));
            ((LinearLayout)holder.binding.getRoot().findViewById(R.id.payLayout)).setAlpha(.5f);
        }

        if(lstMyMap.get(position).getRequestStatus().toLowerCase().equals("cancelled")||lstMyMap.get(position).getRequestStatus().toLowerCase().equals("Rejected")){
            ((TextView)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setEnabled(false);
            ((TextView)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setVisibility(View.INVISIBLE);

            ((TextView)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setText(R.string.pay_now);

            ((LinearLayout)holder.binding.getRoot().findViewById(R.id.payLayout)).setVisibility(View.INVISIBLE);

            ((LinearLayout)holder.binding.getRoot().findViewById(R.id.payLayout)).setBackground(context.getResources().getDrawable(R.drawable.pay_capsule_bg));
            ((LinearLayout)holder.binding.getRoot().findViewById(R.id.payLayout)).setAlpha(.5f);
        }
        else if(lstMyMap.get(position).getRequestStatus().toLowerCase().equals("in progress")){
            /*((Button)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setEnabled(true);
            ((Button)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setText(R.string.pay);
            ((Button)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setVisibility(View.VISIBLE);
            ((Button)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setBackground(context.getResources().getDrawable(R.drawable.pay_button_background));*/
        }

        /*if(lstMyMap.get(position).getReqCreatedDate()!=null){

            ((TextView)holder.binding.getRoot().findViewById(R.id.requestDate)).setText(lstMyMap.get(position).getReqCreatedDate());
        }*/


        if(Boolean.parseBoolean(lstMyMap.get(position).getIsPlanReady())){
            //((LinearLayout)holder.binding.getRoot().findViewById(R.id.viewIconBackgrund)).setBackgroundColor(Color.parseColor("#1b87e2"));
            ((ImageView)holder.binding.getRoot().findViewById(R.id.view)).setEnabled(true);
            ((ImageView)holder.binding.getRoot().findViewById(R.id.view)).setAlpha(1f);
        } else {
            //((LinearLayout)holder.binding.getRoot().findViewById(R.id.viewIconBackgrund)).setBackgroundColor(Color.parseColor("#2a2a2a"));
            ((ImageView)holder.binding.getRoot().findViewById(R.id.view)).setEnabled(false);
            ((ImageView)holder.binding.getRoot().findViewById(R.id.view)).setAlpha(.5f);
        }


        holder.binding.getRoot().findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Global.isConnected(context)) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(context.getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , context.getString(R.string.ok), context);
                    else
                        AlertDialogUtil.errorAlertDialog(context.getString(R.string.lbl_warning), context.getString(R.string.internet_connection_problem1), context.getString(R.string.ok), context);
                }
                else{
                        viewModel.viewSitePlan(lstMyMap.get(position).getRequestId());
                }


            }
        });
        holder.binding.getRoot().findViewById(R.id.gotomakani).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Global.isConnected(context)) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(context.getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , context.getString(R.string.ok), context);
                    else
                        AlertDialogUtil.errorAlertDialog(context.getString(R.string.lbl_warning), context.getString(R.string.internet_connection_problem1), context.getString(R.string.ok), context);
                }
                else {
                    if(lstMyMap.get(position).getParcelId() != null && !lstMyMap.get(position).getParcelId().isEmpty()) {
                        Global.openMakani(lstMyMap.get(position).getParcelId(), (Activity) context);
                    }
                }

            }
        });
        holder.binding.getRoot().findViewById(R.id.siteplan_payButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Global.isConnected(context)) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(context.getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , context.getString(R.string.ok), context);
                    else
                        AlertDialogUtil.errorAlertDialog(context.getString(R.string.lbl_warning), context.getString(R.string.internet_connection_problem1), context.getString(R.string.ok), context);
                }
                else{
                    PayFragment.isFromPayFragment = false;
                    Global.paymentStatus = null;
                    List<Object> param=new ArrayList<Object>();
                    param.add(lstMyMap.get(position).getRequestId());
                    param.add(lstMyMap.get(position).getParcelId());
                    param.add(lstMyMap.get(position).getVoucherNo());
                    param.add(lstMyMap.get(position).getVoucherAmount());
                    param.add(lstMyMap.get(position).getEradPaymentUrl());
                    param.add(lstMyMap.get(position).getCallback_url());
                    param.add(lstMyMap.get(position).getCustomerName());
                    param.add(lstMyMap.get(position).getMobile());
                    param.add(lstMyMap.get(position).getEmailId());

                    ((MainActivity) context).loadFragment(FragmentTAGS.FR_REQUEST_DETAILS, true, param);


                }


            }
        });
    }




    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    @Override
    public int getItemCount() {
        return  lstMyMap.size();
    }

    public void setMyMap(List<MyMapResults> lstMyMap) {
        this.lstMyMap = lstMyMap;
    }
    public List<MyMapResults> getMyMap(){
        return this.lstMyMap;
    }

    public static class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ViewDataBinding binding;
        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MyMapViewModel viewModel, int position) {
            binding.setVariable(BR.adapterMyMapVM, viewModel);
            binding.setVariable(BR.position, position);

            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {


        }
    }
}