package dm.sime.com.kharetati.view.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dm.sime.com.kharetati.BR;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.Bookmark;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.models.ZZBookmark;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.fragments.BookmarkFragment;
import dm.sime.com.kharetati.view.fragments.MapFragment;
import dm.sime.com.kharetati.view.viewModels.BookmarkViewModel;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.GenericViewHolder> {
    private int layoutId;
    private BookmarkViewModel viewModel;
    static Activity context;
    private List<Bookmark> data;
    public static BookmarkAdapter BmAdapter;
    public static List<Bookmark> lstBookmark;
    private ArrayList<String> plotNumbers;
    private List<Bookmark> filteredData;



    public BookmarkAdapter(@LayoutRes int layoutId, BookmarkViewModel viewModel,Activity context) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
        this.context = context;
        lstBookmark = new ArrayList<>();
        plotNumbers = new ArrayList<String>();

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

        if(lstBookmark.get(position).date!=null){
        Locale locale;
        SimpleDateFormat format;
        format= Global.CURRENT_LOCALE.equals("en")? new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa",new Locale("en")):new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa ", new Locale("en"));
        ((TextView)holder.binding.getRoot().findViewById(R.id.dateBookmark)).setText(format.format(lstBookmark.get(position).date));
        ((TextView)holder.binding.getRoot().findViewById(R.id.plotNo)).setText(lstBookmark.get(position).ParcelNumber.trim());
        ((TextView)holder.binding.getRoot().findViewById(R.id.community)).setText(Global.CURRENT_LOCALE.equals("en")?lstBookmark.get(position).Community:lstBookmark.get(position).CommunityAr);
        }
        if(Global.CURRENT_LOCALE.compareToIgnoreCase("en") == 0){
            if(lstBookmark.get(position).descriptionEn != null && lstBookmark.get(position).descriptionEn.length() > 0||lstBookmark.get(position).descriptionAr != null && lstBookmark.get(position).descriptionAr.length() > 0) {
                holder.txtDescription.setVisibility(View.VISIBLE);
                if(lstBookmark.get(position).descriptionEn!=null && !lstBookmark.get(position).descriptionEn.isEmpty())
                    holder.txtDescription.setText(lstBookmark.get(position).descriptionEn);
                else if(lstBookmark.get(position).descriptionAr!=null && !lstBookmark.get(position).descriptionAr.isEmpty())
                    holder.txtDescription.setText(lstBookmark.get(position).descriptionAr);

            } else {
                holder.txtDescription.setVisibility(View.GONE);
            }
        } else {
            if(lstBookmark.get(position).descriptionAr != null && lstBookmark.get(position).descriptionAr.length() > 0||lstBookmark.get(position).descriptionEn != null && lstBookmark.get(position).descriptionEn.length() > 0) {
                holder.txtDescription.setVisibility(View.VISIBLE);
                if(lstBookmark.get(position).descriptionAr!=null && !lstBookmark.get(position).descriptionAr.isEmpty())
                    holder.txtDescription.setText(lstBookmark.get(position).descriptionAr);
                else if(lstBookmark.get(position).descriptionEn!=null && !lstBookmark.get(position).descriptionEn.isEmpty())
                    holder.txtDescription.setText(lstBookmark.get(position).descriptionEn);
            } else {
                holder.txtDescription.setVisibility(View.GONE);
            }
        }

        holder.binding.getRoot().findViewById(R.id.gotomap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Global.isConnected(context)) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(context.getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , context.getResources().getString(R.string.ok), context);
                    else
                        AlertDialogUtil.errorAlertDialog(context.getResources().getString(R.string.lbl_warning), context.getResources().getString(R.string.internet_connection_problem1), context.getResources().getString(R.string.ok), context);
                }

                if(lstBookmark.get(position).ParcelNumber != null && !lstBookmark.get(position).ParcelNumber.isEmpty()) {
                    PlotDetails.isOwner = false;
                    PlotDetails.parcelNo=lstBookmark.get(position).ParcelNumber.trim();
                    Global.isBookmarks =true;
                    Global.isSaveAsBookmark =false;

                    ArrayList al = new ArrayList();
                    al.add(PlotDetails.parcelNo.trim());
                    //al.add("");

                    ((MainActivity) context).loadFragment(FragmentTAGS.FR_MAP, true, al);
                    //MapFragment.mapVM.mapNavigator.findParcelForBookmarks(PlotDetails.parcelNo);


                }

            }
        });

        holder.binding.getRoot().findViewById(R.id.rowBookmark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Global.isConnected(context)) {

                    if(Global.appMsg!=null)
                        AlertDialogUtil.errorAlertDialog(context.getResources().getString(R.string.lbl_warning),Global.CURRENT_LOCALE.equals("en")?Global.appMsg.getInternetConnCheckEn():Global.appMsg.getInternetConnCheckAr() , context.getResources().getString(R.string.ok), context);
                    else
                        AlertDialogUtil.errorAlertDialog(context.getResources().getString(R.string.lbl_warning), context.getResources().getString(R.string.internet_connection_problem1), context.getResources().getString(R.string.ok), context);
                }

                if(lstBookmark.get(position).ParcelNumber != null && !lstBookmark.get(position).ParcelNumber.isEmpty()) {
                    PlotDetails.isOwner = false;
                    PlotDetails.parcelNo=lstBookmark.get(position).ParcelNumber.trim();
                    Global.isBookmarks =true;
                    Global.isSaveAsBookmark =false;

                    ArrayList al = new ArrayList();
                    al.add(PlotDetails.parcelNo.trim());
                    //al.add("");

                    ((MainActivity) context).loadFragment(FragmentTAGS.FR_MAP, true, al);
                    //MapFragment.mapVM.mapNavigator.findParcelForBookmarks(PlotDetails.parcelNo);


                }

            }
        });
        holder.binding.getRoot().findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lstBookmark.get(position).ParcelNumber != null && !lstBookmark.get(position).ParcelNumber.isEmpty()) {
                    Global.bookmarkPlotNo = lstBookmark.get(position).ParcelNumber.trim();
                    Bookmark data = lstBookmark.get(position);
                    AlertDialogUtil.bookMarksEditAlert(context, data);
                }
            }
        });
        holder.binding.getRoot().findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lstBookmark.get(position).ParcelNumber != null && !lstBookmark.get(position).ParcelNumber.isEmpty()) {
                    Global.bookmarkPlotNo = lstBookmark.get(position).ParcelNumber.trim();
                    Bookmark data = lstBookmark.get(position);
                    AlertDialogUtil.bookMarksDeleteAlert("", context.getResources().getString(R.string.confirmation_delete), context.getResources().getString(R.string.ok), context.getResources().getString(R.string.cancel), context, data, position);
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
                    if(lstBookmark.get(position).ParcelNumber != null && !lstBookmark.get(position).ParcelNumber.isEmpty()) {
                        Global.openMakani(lstBookmark.get(position).ParcelNumber.trim(), (Activity) context);
                    }
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
        return lstBookmark.size();
    }

    public void setBookmark(List<Bookmark> lstBookmark) {
        this.lstBookmark = lstBookmark;
    }
    public List<Bookmark> getBookMarks(){
        return lstBookmark;
    }

    public void remove(Bookmark data){
        lstBookmark.remove(data);
        //notifyDataSetChanged();

    }

    public void filterList(ArrayList<Bookmark> filterdNames) {

        setBookmark(filterdNames);
        BookmarkFragment.bmModel.bookMarksNavigator.updateAdapter();

        notifyDataSetChanged();
    }

    public static class GenericViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ViewDataBinding binding;
        private final TextView txtDescription;
        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            txtDescription = (TextView) this.binding.getRoot().findViewById(R.id.txtDescriptionEn);
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


