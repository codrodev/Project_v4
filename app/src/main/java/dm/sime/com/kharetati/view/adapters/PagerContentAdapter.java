package dm.sime.com.kharetati.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.viewModels.MainViewModel;
import dm.sime.com.kharetati.view.viewModels.MyViewModel;


public class PagerContentAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    MainViewModel viewModel;
    private Context context;

    public PagerContentAdapter(Context context, MainViewModel viewModel){
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.viewModel = viewModel;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = this.layoutInflater.inflate(R.layout.fragment_content, container, false);

        TextView txtHeader = (TextView)view.findViewById(R.id.txtHeader);
        TextView txtMessage = (TextView)view.findViewById(R.id.txtMessage);
        TextView txtDate = (TextView)view.findViewById(R.id.txtDate);
        LinearLayout root = (LinearLayout) view.findViewById(R.id.rootLayout);
        ImageView cancel = (ImageView)view.findViewById(R.id.notification_cancel);
        viewModel.setDotPosition(position);
        txtHeader.setText(Global.CURRENT_LOCALE.equals("en")?Global.notificationResponse.getServiceResponse().getGeneralNotifications().get(position).getNameEn():Global.notificationResponse.getServiceResponse().getGeneralNotifications().get(position).getNameAr());
        txtMessage.setText(Global.CURRENT_LOCALE.equals("en")?Global.notificationResponse.getServiceResponse().getGeneralNotifications().get(position).getDescriptionEn():Global.notificationResponse.getServiceResponse().getGeneralNotifications().get(position).getDescriptionAr());
        txtDate.setText(Global.CURRENT_LOCALE.equals("en")?Global.notificationResponse.getServiceResponse().getGeneralNotifications().get(position).getDate():Global.notificationResponse.getServiceResponse().getGeneralNotifications().get(position).getDate());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.cancelNotification();
            }
        });
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Global.notificationResponse.getServiceResponse().getGeneralNotifications().get(position).getUrlEn() != null || Global.notificationResponse.getServiceResponse().getGeneralNotifications().get(position).getUrlAr() != null) {


                    if (!Global.notificationResponse.getServiceResponse().getGeneralNotifications().get(position).getUrlEn().isEmpty() || !Global.notificationResponse.getServiceResponse().getGeneralNotifications().get(position).getUrlAr().isEmpty()) {
                        viewModel.loadWebView(Global.CURRENT_LOCALE.equals("en")?Global.notificationResponse.getServiceResponse().getGeneralNotifications().get(position).getUrlEn():Global.notificationResponse.getServiceResponse().getGeneralNotifications().get(position).getUrlAr(),null);
                        viewModel.cancelNotification();
                    }
                }
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Global.notificationResponse.getServiceResponse().getGeneralNotifications().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View)object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

}
