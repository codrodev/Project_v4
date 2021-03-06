package dm.sime.com.kharetati.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.activities.MainActivity;
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

        return new GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);
        viewHolder = holder;
        pos=position;
        if(Global.CURRENT_LOCALE.equals("en"))
            holder.txtMenuName.setText(lstHomeGridMenuItems.get(position).getNameEn());
        else
            holder.txtMenuName.setText(lstHomeGridMenuItems.get(position).getNameAr());
        if(viewModel.getSelectedApplication() != null && Global.isAppSelected) {
            if (viewModel.getSelectedApplication().getId().equals(lstHomeGridMenuItems.get(position).getId())) {
                holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.border_background));
                holder.txtMenuName.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.theme_background));
                holder.txtMenuName.setTextColor(context.getResources().getColor(R.color.black));
            }
        }

        holder.imgIcon.setImageBitmap(viewModel.getIconImage(position));
        /*if(Global.isFirstLoad||Global.isRecreate){
            if(position == 0) {
                holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.border_background));
                holder.txtMenuName.setTextColor(context.getResources().getColor(R.color.white));
                listener.onMenuSelected(lstHomeGridMenuItems.get(0).getId(), true);
            }
            if(position == lstHomeGridMenuItems.size() - 1){
                listener.onMenuSelected(lstHomeGridMenuItems.get(0).getId(), true);
            }
            Global.isRecreate =false;
        }*/

    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    @Override
    public int getItemCount() {
        return  lstHomeGridMenuItems.size();
    }

    public class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ViewDataBinding binding;
        private final ImageView imgIcon;
        private final CardView cardView;
        private final TextView txtMenuName, txtAppId;


        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.binding.getRoot().setOnClickListener(this);
            imgIcon = (ImageView) this.binding.getRoot().findViewById(R.id.imgMenuIcon);
            cardView = (CardView) this.binding.getRoot().findViewById(R.id.cardHomeGrid);
            txtMenuName = (TextView) this.binding.getRoot().findViewById(R.id.txtMenuName);
            txtAppId = (TextView) this.binding.getRoot().findViewById(R.id.txtAppId);
        }

        void bind(HomeViewModel viewModel, int position) {
            binding.setVariable(BR.adapterGridMenuVM, viewModel);
            binding.setVariable(BR.position, position);

            binding.executePendingBindings();
        }


        @Override
        public void onClick(View v) {
            Global.hideSoftKeyboard((MainActivity)context);
            listener.onMenuSelected(((TextView) binding.getRoot().findViewById(R.id.txtAppId)).getText().toString(), false);
            notifyDataSetChanged();
        }
    }


    public interface OnMenuSelectedListener {
        void onMenuSelected(String menu, boolean isAnimation);

    }
}