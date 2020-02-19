package dm.sime.com.kharetati.view.adapters;



import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.utility.Files.DialogConfigs;
import dm.sime.com.kharetati.utility.Files.DialogProperties;
import dm.sime.com.kharetati.utility.Files.FileListItem;
import dm.sime.com.kharetati.utility.Files.MarkedItemList;
import dm.sime.com.kharetati.view.customview.MaterialCheckbox;
import dm.sime.com.kharetati.view.customview.NotifyItemChecked;
import dm.sime.com.kharetati.view.customview.OnCheckedChangeListener;


public class FileListAdapter extends BaseAdapter {
    private ArrayList<FileListItem> listItem;
    private Context context;
    private DialogProperties properties;
    private NotifyItemChecked notifyItemChecked;

    public FileListAdapter(ArrayList<FileListItem> listItem, Context context, DialogProperties properties) {
        this.listItem = listItem;
        this.context = context;
        this.properties = properties;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public FileListItem getItem(int i) {
        return listItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_file_list_item, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else
        {   holder = (ViewHolder)view.getTag();
        }
        final FileListItem item = listItem.get(i);
        if (MarkedItemList.hasItem(item.getLocation())) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.marked_item_animation);
            view.setAnimation(animation);
        }
        else {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.unmarked_item_animation);
            view.setAnimation(animation);
        }
        if (item.isDirectory()) {
            holder.type_icon.setImageResource(R.drawable.ic_type_folder);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.type_icon.setColorFilter(context.getResources().getColor(R.color.colorHeader,context.getTheme()));
            }
            else
            {   holder.type_icon.setColorFilter(context.getResources().getColor(R.color.colorHeader));
            }
            if(properties.selection_type == DialogConfigs.FILE_SELECT)
            {   holder.fmark.setVisibility(View.INVISIBLE);
            }
            else
            {   holder.fmark.setVisibility(View.VISIBLE);
            }
        }
        else {
            holder.type_icon.setImageResource(R.drawable.ic_type_file);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.type_icon.setColorFilter(context.getResources().getColor(R.color.maroon_light,context.getTheme()));
            }
            else
            {   holder.type_icon.setColorFilter(context.getResources().getColor(R.color.colorAccent));
            }
            if(properties.selection_type == DialogConfigs.DIR_SELECT)
            {   holder.fmark.setVisibility(View.INVISIBLE);
            }
            else
            {   holder.fmark.setVisibility(View.VISIBLE);
            }
        }
        holder.type_icon.setContentDescription(item.getFilename());
        holder.name.setText(item.getFilename());
        SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat stime = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        Date date = new Date(item.getTime());
        if(i==0&&item.getFilename().startsWith(context.getString(R.string.label_parent_dir))) {
            holder.type.setText(R.string.label_parent_directory);
        }
        else {
            holder.type.setText(context.getString(R.string.last_edit) + sdate.format(date) + ", " + stime.format(date));
        }
        if(holder.fmark.getVisibility()== View.VISIBLE) {
            if(i==0&&item.getFilename().startsWith(context.getString(R.string.label_parent_dir)))
            {   holder.fmark.setVisibility(View.INVISIBLE);
            }
            if (MarkedItemList.hasItem(item.getLocation())) {
                holder.fmark.setChecked(true);
            }
            else {
                holder.fmark.setChecked(false);
            }
        }
        
        holder.fmark.setOnCheckedChangedListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MaterialCheckbox checkbox, boolean isChecked) {
                item.setMarked(isChecked);
                if (item.isMarked()) {
                    if(properties.selection_mode == DialogConfigs.MULTI_MODE) {
                        MarkedItemList.addSelectedItem(item);
                    }
                    else {
                        MarkedItemList.addSingleFile(item);
                    }
                }
                else {
                    MarkedItemList.removeSelectedItem(item.getLocation());
                }
                notifyItemChecked.notifyCheckBoxIsClicked();
            }
        });
        return view;
    }

    private class ViewHolder
    {   ImageView type_icon;
        TextView name,type;
        MaterialCheckbox fmark;

        ViewHolder(View itemView) {
            name= itemView.findViewById(R.id.fname);
            type= itemView.findViewById(R.id.ftype);
            type_icon= itemView.findViewById(R.id.image_type);
            fmark= itemView.findViewById(R.id.file_mark);
        }
    }

    public void setNotifyItemCheckedListener(NotifyItemChecked notifyItemChecked) {
        this.notifyItemChecked = notifyItemChecked;
    }
}
