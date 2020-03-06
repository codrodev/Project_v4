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
import dm.sime.com.kharetati.datas.models.Functions;
import dm.sime.com.kharetati.datas.models.FunctionsOnMap;
import dm.sime.com.kharetati.view.viewModels.MapFunctionBottomSheetViewModel;

public class FunctionOnMapAdapter extends RecyclerView.Adapter<FunctionOnMapAdapter.GenericViewHolder> {
    private List<Functions> lstFunctionsOnMap;
    private MapFunctionBottomSheetViewModel viewModel;
    static Context context;
    static OnMenuSelectedListener listener;

    public FunctionOnMapAdapter(MapFunctionBottomSheetViewModel viewModel, Context context, OnMenuSelectedListener listener,
                                List<Functions> lstFunctions) {
        this.viewModel = viewModel;
        lstFunctionsOnMap = lstFunctions;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_map_function_menu, parent, false);

        return new FunctionOnMapAdapter.GenericViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
          holder.functionName.setText(lstFunctionsOnMap.get(position).getNameEn());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMenuSelected(holder.functionName.getText().toString(), position);
            }
        });
    }

    /*@Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }
*/
    @Override
    public int getItemCount() {
        return  lstFunctionsOnMap.size();
    }

   /* public void setFunctionsOnMap(List<Functions> lstFunctionsOnMap) {
        this.lstFunctionsOnMap = lstFunctionsOnMap;
    }*/

    public static class GenericViewHolder extends RecyclerView.ViewHolder{
        private final TextView functionName;
        View container;
        public GenericViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView;
            functionName = (TextView) itemView.findViewById(R.id.txtFunctionNAme);
        }


        /*@Override
        public void onClick(View v) {
            //listener.onMenuSelected(((TextView) binding.getRoot().findViewById(R.id.txtFunctionNAme)).getText().toString(), getAdapterPosition());
        }*/
    }

    public interface OnMenuSelectedListener {
        void onMenuSelected(String menu, int position);
    }
}