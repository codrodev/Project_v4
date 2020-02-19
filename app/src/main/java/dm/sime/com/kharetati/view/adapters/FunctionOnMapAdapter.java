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
import dm.sime.com.kharetati.datas.models.FunctionsOnMap;
import dm.sime.com.kharetati.view.viewModels.MapFunctionBottomSheetViewModel;

public class FunctionOnMapAdapter extends RecyclerView.Adapter<FunctionOnMapAdapter.GenericViewHolder> {
    private int layoutId;
    private List<FunctionsOnMap> lstFunctionsOnMap;
    private MapFunctionBottomSheetViewModel viewModel;
    static Context context;
    static OnMenuSelectedListener listener;

    public FunctionOnMapAdapter(@LayoutRes int layoutId, MapFunctionBottomSheetViewModel viewModel, Context context, OnMenuSelectedListener listener) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
        lstFunctionsOnMap = viewModel.getFunctionsOnMapList();
        this.context = context;
        this.listener = listener;
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
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    @Override
    public int getItemCount() {
        return  lstFunctionsOnMap.size();
    }

    public void setFunctionsOnMap(List<FunctionsOnMap> lstFunctionsOnMap) {
        this.lstFunctionsOnMap = lstFunctionsOnMap;
    }

    public static class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ViewDataBinding binding;
        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        void bind(MapFunctionBottomSheetViewModel viewModel, int position) {
            binding.setVariable(BR.adapterMapFunctionVM, viewModel);
            binding.setVariable(BR.position, position);

            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            listener.onMenuSelected(((TextView) binding.getRoot().findViewById(R.id.txtFunctionNAme)).getText().toString());
        }
    }

    public interface OnMenuSelectedListener {
        void onMenuSelected(String menu);
    }
}