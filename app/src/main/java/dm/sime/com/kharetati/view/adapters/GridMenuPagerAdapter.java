package dm.sime.com.kharetati.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.ViewAnimationUtils;
import dm.sime.com.kharetati.view.viewModels.HomeViewModel;

public class GridMenuPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    HomeViewModel model;
    GridMenuAdapter adapterGridMenu;
    GridMenuAdapter.OnMenuSelectedListener listner;

    public GridMenuPagerAdapter(Context context, HomeViewModel model, GridMenuAdapter.OnMenuSelectedListener listner) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.model = model;
        this.listner = listner;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = this.layoutInflater.inflate(R.layout.pager_adapter_grid_menu, container, false);
        model.initializeHomeGridMenu(context, position);
        RecyclerView recycleGridMenu = (RecyclerView)view.findViewById(R.id.recycleGridMenu);
        recycleGridMenu.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        adapterGridMenu = new GridMenuAdapter(R.layout.adapter_grid_menu, model, context, listner, position);
        recycleGridMenu.setAdapter(adapterGridMenu);
        if(Global.isFirstLoad) {
            ViewAnimationUtils.scaleAnimateViewPop(recycleGridMenu);
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return model.getGridPagerSize();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       super.destroyItem((View) container, position, object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View)object);
    }
}
