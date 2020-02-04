package dm.sime.com.kharetati.view.viewModels;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import dm.sime.com.kharetati.view.navigators.FragmentNavigator;

public class ParentSiteplanViewModel extends ViewModel {

    FragmentNavigator frNavigator;

    public ParentSiteplanViewModel(){

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
