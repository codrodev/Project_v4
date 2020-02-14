package dm.sime.com.kharetati.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dm.sime.com.kharetati.BR;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.Applications;
import dm.sime.com.kharetati.view.viewModels.HomeViewModel;

public class GridMenuAdapter extends RecyclerView.Adapter<GridMenuAdapter.GenericViewHolder> {

    private int layoutId;
    public List<Applications> lstHomeGridMenuItems;
    private HomeViewModel viewModel;
    static Context context;
    static OnMenuSelectedListener listener;
    private int pos;
    private GenericViewHolder viewHolder;

    public GridMenuAdapter(@LayoutRes int layoutId, HomeViewModel viewModel, Context context, OnMenuSelectedListener listener, int parentPosition) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
        lstHomeGridMenuItems = viewModel.getListHomeGridMenuItems(parentPosition);
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

        return new GridMenuAdapter.GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);
        viewHolder = holder;
        pos=position;
//        lstHomeGridMenuItems.get(i).isClick=false;
        if(lstHomeGridMenuItems.get(holder.getAdapterPosition()).isClick)
            holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.border_background));
        else
            holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.borderless_background));


    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    @Override
    public int getItemCount() {
        return  lstHomeGridMenuItems.size();
    }

    public class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ViewDataBinding binding;
        private final CardView cardView;


        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.binding.getRoot().setOnClickListener(this);
            cardView =(CardView)this.binding.getRoot().findViewById(R.id.cardHomeGrid);
        }

        void bind(HomeViewModel viewModel, int position) {
            binding.setVariable(BR.adapterGridMenuVM, viewModel);
            binding.setVariable(BR.position, position);

            binding.executePendingBindings();
        }


        @Override
        public void onClick(View v) {


            if(lstHomeGridMenuItems.get(getAdapterPosition()).isClick){

                lstHomeGridMenuItems.get(getAdapterPosition()).isClick=false;
                notifyDataSetChanged();
            }
            else{
                for(int i=0;i<lstHomeGridMenuItems.size();i++)
                {
                    if(lstHomeGridMenuItems.get(i).isClick)
                    {
                        lstHomeGridMenuItems.get(i).isClick=false;
                    }
                }
                lstHomeGridMenuItems.get(getAdapterPosition()).isClick =true;
                notifyDataSetChanged();

            }

           /* if(isClicked) {
                ((CardView) binding.getRoot().findViewById(R.id.cardHomeGrid)).setBackground(context.getResources().getDrawable(R.drawable.border_background));
                notifyDataSetChanged();
            }
            else{
                ((CardView) binding.getRoot().findViewById(R.id.cardHomeGrid)).setBackground(context.getResources().getDrawable(R.drawable.borderless_background));

            }*/
            listener.onMenuSelected(((TextView) binding.getRoot().findViewById(R.id.txtAppId)).getText().toString());
        }
    }


    public interface OnMenuSelectedListener {
        void onMenuSelected(String menu);

    }
}