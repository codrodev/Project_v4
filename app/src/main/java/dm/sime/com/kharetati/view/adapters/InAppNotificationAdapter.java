package dm.sime.com.kharetati.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dm.sime.com.kharetati.BR;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.InAppNotifications;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.view.viewModels.HomeViewModel;

public class InAppNotificationAdapter extends RecyclerView.Adapter<InAppNotificationAdapter.GenericViewHolder> {
    private int layoutId;
    private List<InAppNotifications> lstInAppNotifications;
    private HomeViewModel viewModel;
    static Context context;

    public InAppNotificationAdapter(HomeViewModel viewModel, Context context) {
        this.viewModel = viewModel;
        lstInAppNotifications = viewModel.getInAppNotifications();
        this.context = context;
    }

    /*private int getLayoutIdForPosition(int position) {
        return layoutId;
    }*/

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);*/
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_in_app_notifications, parent, false);

        return new InAppNotificationAdapter.GenericViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        //holder.bind(viewModel, position);
        holder.subject.setText(lstInAppNotifications.get(position).getSubject());
    }

    @Override
    public int getItemCount() {
        return  lstInAppNotifications.size();
    }

    public void setInAppNotifications(List<InAppNotifications> lstInAppNotifications) {
        this.lstInAppNotifications = lstInAppNotifications;
    }

    public static class GenericViewHolder extends RecyclerView.ViewHolder{
        private final TextView subject;
        public GenericViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = (TextView) itemView.findViewById(R.id.txtNotification);
        }
    }
}