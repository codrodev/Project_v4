package dm.sime.com.kharetati.view.adapters;

import android.util.SparseArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.fragments.BookmarkFragment;
import dm.sime.com.kharetati.view.fragments.MyMapFragment;
import dm.sime.com.kharetati.view.viewModels.DashboardViewModel;

public class DashboardPagerAdapter extends FragmentPagerAdapter {

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    int pageLimit;
    DashboardViewModel model;

    public DashboardPagerAdapter(FragmentManager fm, int pageLimit){
        super(fm);
        this.pageLimit = pageLimit;
        this.model = model;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment tabFragment;
        if(Global.isUserLoggedIn){
            switch (position) {
                case 0:
                    tabFragment = MyMapFragment.newInstance();
                    break;
                case 1:
                    tabFragment = BookmarkFragment.newInstance();
                    break;
                default:
                    tabFragment = MyMapFragment.newInstance();
                    break;
            }
        } else {
            tabFragment = BookmarkFragment.newInstance();
        }
        return tabFragment;
    }

    @Override
    public int getCount() {
        return pageLimit;
    }
}