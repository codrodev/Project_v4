package dm.sime.com.kharetati.view.viewModels;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import dm.sime.com.kharetati.view.adapters.DashboardPagerAdapter;
import dm.sime.com.kharetati.view.fragments.DashboardFragment;
import dm.sime.com.kharetati.view.navigators.FragmentNavigator;

public class DashboardViewModel extends ViewModel {

    FragmentNavigator frNavigator;
    DashboardPagerAdapter dashboardPagerAdapter;

    public DashboardViewModel(){

    }

    public DashboardPagerAdapter getDashboardPagerAdapter() {
        return dashboardPagerAdapter;
    }

    public void setDashboardPagerAdapter(Fragment fr, int pageLimit) {
        dashboardPagerAdapter = new DashboardPagerAdapter(fr.getChildFragmentManager(), pageLimit);
    }

    public void manageAppBar(Context ctx, boolean status){
        frNavigator = (FragmentNavigator) ctx;
        frNavigator.manageActionBar(status);
    }

    public void manageAppBottomBAtr(Context ctx, boolean status){
        frNavigator = (FragmentNavigator) ctx;
        frNavigator.manageBottomBar(status);
    }

}
