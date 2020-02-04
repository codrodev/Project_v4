package dm.sime.com.kharetati.view.viewModels;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import dm.sime.com.kharetati.view.adapters.DashboardPagerAdapter;

public class DashboardViewModel extends ViewModel {

    DashboardPagerAdapter dashboardPagerAdapter;

    public DashboardViewModel(){

    }

    public DashboardPagerAdapter getDashboardPagerAdapter() {
        return dashboardPagerAdapter;
    }

    public void setDashboardPagerAdapter(Fragment fr, int pageLimit) {
        dashboardPagerAdapter = new DashboardPagerAdapter(fr.getChildFragmentManager(), pageLimit);
    }
}
