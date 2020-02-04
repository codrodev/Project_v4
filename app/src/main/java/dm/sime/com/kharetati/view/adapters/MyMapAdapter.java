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

import java.util.ArrayList;
import java.util.List;

import dm.sime.com.kharetati.BR;
import dm.sime.com.kharetati.datas.models.MyMapResults;
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

        return new MyMapAdapter.GenericViewHolder(binding);
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