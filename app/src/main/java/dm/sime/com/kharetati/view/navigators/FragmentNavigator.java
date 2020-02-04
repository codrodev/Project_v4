package dm.sime.com.kharetati.view.navigators;

import java.util.List;

public interface FragmentNavigator {
    void fragmentNavigator(String fragmentTag, Boolean addToBackStack, List<Object> params);

    void manageActionBar(boolean key);

    void manageBottomBar(boolean key);
}
