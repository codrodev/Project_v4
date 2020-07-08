package dm.sime.com.kharetati.view.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import dm.sime.com.kharetati.datas.models.Functions;
import dm.sime.com.kharetati.datas.models.FunctionsOnMap;
import dm.sime.com.kharetati.view.viewModels.MapFunctionBottomSheetViewModel;
import dm.sime.com.kharetati.view.viewModels.MapViewModel;

public class FunctionOnMapAdapter extends RecyclerView.Adapter<FunctionOnMapAdapter.GenericViewHolder> {
    private List<Functions> lstFunctionsOnMap;
    private MapViewModel viewModel;
    static Context context;
    static OnMenuSelectedListener listener;

    public FunctionOnMapAdapter(MapViewModel viewModel, Context context, OnMenuSelectedListener listener,
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
          holder.icon.setImageBitmap(getIconImage(lstFunctionsOnMap.get(position).getIconBase64()));


        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMenuSelected(holder.functionName.getText().toString(), position);

                    /*if (holder.functionName.getText().toString().equals(lstFunctionsOnMap.get(position).getNameEn())) {
                        holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.maroon_light));
                        holder.functionName.setTextColor(context.getResources().getColor(R.color.white));
                    } else {
                        holder.cardView.setBackgroundColor(Color.parseColor("#FCFCFC"));
                        holder.functionName.setTextColor(context.getResources().getColor(R.color.black));
                    }*/if (holder.functionName.getText().toString().equals(lstFunctionsOnMap.get(position).getNameEn())) {
                        holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.map_option_background));
                        holder.functionName.setTextColor(context.getResources().getColor(R.color.white));
                    } else {
                        holder.cardView.setBackground(context.getResources().getDrawable(R.drawable.theme_background));
                        holder.functionName.setTextColor(context.getResources().getColor(R.color.black));
                    }

            }
        });
    }
    public Bitmap getIconImage(String string){
        //Applications app = getCurrentHomeGridMenuItems(position);
        Bitmap icon= null;
        try {
            /*InputStream stream = new ByteArrayInputStream(Base64.decode(app.getIconBase64().getBytes(), Base64.URL_SAFE|Base64.DEFAULT|Base64.NO_WRAP));
            icon = BitmapFactory.decodeStream(stream);*/

            byte[] decodedString = Base64.decode(string, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            icon = decodedByte;

        } catch (Exception ex){
            /*Drawable d =  activity.getResources().getDrawable(R.drawable.bookmark);
            icon = ((BitmapDrawable)d).getBitmap();*/
        }
        return icon;
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
        private final ImageView icon;
        private final CardView cardView;

        View container;
        public GenericViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView;
            functionName = (TextView) itemView.findViewById(R.id.txtFunctionNAme);
            icon = (ImageView) itemView.findViewById(R.id.imgMenuIcon);
            cardView = (CardView) itemView.findViewById(R.id.mapcard);
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