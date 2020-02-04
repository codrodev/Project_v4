package dm.sime.com.kharetati.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dm.sime.com.kharetati.BR;
import dm.sime.com.kharetati.datas.models.InAppNotifications;
import dm.sime.com.kharetati.view.viewModels.HomeViewModel;

public class InAppNotificationAdapter extends RecyclerView.Adapter<InAppNotificationAdapter.GenericViewHolder> {
    private int layoutId;
    private List<InAppNotifications> lstInAppNotifications;
    private HomeViewModel viewModel;
    static Context context;

    public InAppNotificationAdapter(@LayoutRes int layoutId, HomeViewModel viewModel, Context context) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
        this.context = context;
    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);

        return new InAppNotificationAdapter.GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    @Override
    public int getItemCount() {
        return  lstInAppNotifications.size();
    }

    public void setInAppNotifications(List<InAppNotifications> lstInAppNotifications) {
        this.lstInAppNotifications = lstInAppNotifications;
    }

    public static class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ViewDataBinding binding;
        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(HomeViewModel viewModel, int position) {
            binding.setVariable(BR.adapterNotificationHomeVM, viewModel);
            binding.setVariable(BR.position, position);

            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {

        }
    }
}