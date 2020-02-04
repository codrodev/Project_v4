package dm.sime.com.kharetati.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.SearchForm;
import dm.sime.com.kharetati.utility.ViewAnimationUtils;
import dm.sime.com.kharetati.view.viewModels.HomeViewModel;

public class RuntimeViewPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    HomeViewModel model;
    int pagerCount;
    List<SearchForm> lstForms;

    public RuntimeViewPagerAdapter(Context context, HomeViewModel model, List<SearchForm> lstForms, int pagerCount) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.model = model;
        this.lstForms = lstForms;
        this.pagerCount = pagerCount;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = this.layoutInflater.inflate(R.layout.pager_adapter_runtime, container, false);
        SearchForm form = lstForms.get(position);
        LinearLayout layoutParent = (LinearLayout)view.findViewById(R.id.layoutParent);
        EditText x = new EditText(context);
        x.setHint(form.getPlaceHolderEn());
        layoutParent.addView(x);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return pagerCount;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View)object);
    }
}