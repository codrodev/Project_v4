package dm.sime.com.kharetati.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dm.sime.com.kharetati.BR;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.Bookmark;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.models.ZZBookmark;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.viewModels.BookmarkViewModel;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.GenericViewHolder> {
    private int layoutId;
    private BookmarkViewModel viewModel;
    static Context context;
    public List<Bookmark> lstBookmark;


    public BookmarkAdapter(@LayoutRes int layoutId, BookmarkViewModel viewModel,Context context) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
        this.context = context;
        lstBookmark = new ArrayList<>();


    }

    private int getLayoutIdForPosition(int position) {
        return layoutId;
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);

        return new BookmarkAdapter.GenericViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(viewModel, position);
        holder.binding.getRoot().findViewById(R.id.gotomap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Global.isConnected(context)) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(context.getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , context.getResources().getString(R.string.ok), context);
                    else
                        AlertDialogUtil.errorAlertDialog(context.getResources().getString(R.string.lbl_warning), context.getResources().getString(R.string.internet_connection_problem1), context.getResources().getString(R.string.ok), context);
                }
                PlotDetails.isOwner = false;
                PlotDetails.parcelNo=lstBookmark.get(position).ParcelNumber;
                Global.isBookmarks =true;

                ArrayList al= new ArrayList();
                al.add(PlotDetails.parcelNo);
                al.add("");

                ((MainActivity)context).loadFragment(FragmentTAGS.FR_MAP,true,al);



            }
        });
        holder.binding.getRoot().findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Global.bookmarkPlotNo = lstBookmark.get(position).ParcelNumber;
                Bookmark data = lstBookmark.get(position);
                AlertDialogUtil.bookMarksDeleteAlert("",context.getResources().getString(R.string.confirmation_delete),context.getResources().getString(R.string.ok),context.getResources().getString(R.string.cancel),context,data);



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
                else
                    Global.openMakani(lstBookmark.get(position).ParcelNumber,(Activity) context);

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    @Override
    public int getItemCount() {
        return lstBookmark.size();
    }

    public void setBookmark(List<Bookmark> lstBookmark) {
        this.lstBookmark = lstBookmark;
    }

    public static class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ViewDataBinding binding;
        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(BookmarkViewModel viewModel, int position) {
            binding.setVariable(BR.adapterBookmarkVM, viewModel);
            binding.setVariable(BR.position, position);

            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {


        }
    }
}


