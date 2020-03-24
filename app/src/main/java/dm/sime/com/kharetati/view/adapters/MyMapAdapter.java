package dm.sime.com.kharetati.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

        if(Global.CURRENT_LOCALE.compareToIgnoreCase("en")==0){
            ((TextView)holder.binding.getRoot().findViewById(R.id.reqStatus)).setText(lstMyMap.get(position).getRequestStatus());
        } else {
            ((TextView)holder.binding.getRoot().findViewById(R.id.reqStatus)).setText(lstMyMap.get(position).getRequestStatusAr());
        }

        if(Boolean.parseBoolean(lstMyMap.get(position).getIsPaymentPending()) == true){
            ((Button)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setEnabled(true);
            ((Button)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setText(R.string.pay);
            ((Button)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setBackground(context.getResources().getDrawable(R.drawable.gradient_background));
        }
        else {
            ((Button)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setEnabled(false);
            ((Button)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setText(R.string.paid);
            ((Button)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setBackground(context.getResources().getDrawable(R.drawable.disabled_gradient_background));
        }

        if(lstMyMap.get(position).getRequestStatus().toLowerCase().equals("cancelled")){
            ((Button)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setEnabled(false);
            ((Button)holder.binding.getRoot().findViewById(R.id.siteplan_payButton)).setBackground(context.getResources().getDrawable(R.drawable.disabled_gradient_background));
        }

        /*if(lstMyMap.get(position).getReqCreatedDate()!=null){

            ((TextView)holder.binding.getRoot().findViewById(R.id.requestDate)).setText(lstMyMap.get(position).getReqCreatedDate());
        }*/


        if(Boolean.parseBoolean(lstMyMap.get(position).getIsPlanReady()) == true){
            ((ImageView)holder.binding.getRoot().findViewById(R.id.view)).setAlpha(1f);
            ((ImageView)holder.binding.getRoot().findViewById(R.id.view)).setEnabled(true);
        } else {
            ((ImageView)holder.binding.getRoot().findViewById(R.id.view)).setAlpha(.5f);
            ((ImageView)holder.binding.getRoot().findViewById(R.id.view)).setEnabled(false);
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