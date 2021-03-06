package dm.sime.com.kharetati.view.viewModels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.datas.models.Functions;
import dm.sime.com.kharetati.datas.models.FunctionsOnMap;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.adapters.FunctionOnMapAdapter;
import dm.sime.com.kharetati.view.adapters.GridMenuAdapter;
import dm.sime.com.kharetati.view.navigators.FragmentNavigator;

public class MapFunctionBottomSheetViewModel extends ViewModel {

    private MutableLiveData<List<Functions>> mutableFunctionOnMap = new MutableLiveData<>();
    FunctionOnMapAdapter adapter;
    FragmentNavigator navigator;

    public MapFunctionBottomSheetViewModel(){

    }

    public void initializeViewModel(Context context){
        mutableFunctionOnMap = new MutableLiveData<>();
        mutableFunctionOnMap.setValue(Global.mapSearchResult.getService_response().getMap().getFunctions());
        /*adapter = new FunctionOnMapAdapter(R.layout.adapter_map_function_menu,this, context, listner);*/
    }

    public MutableLiveData<List<Functions>> getMutableFunctionsOnMap(){

        return mutableFunctionOnMap;
    }

    public List<Functions> getFunctionsOnMapList(){
        return mutableFunctionOnMap.getValue();
    }

    public Functions getCurrentFunctionsOnMap(int  position){
        if (mutableFunctionOnMap.getValue() != null ) {
            return mutableFunctionOnMap.getValue().get(position);
        }
        return null;
    }

    public void  setFunctionsOnMapAdapter(List<Functions> lstFunctionsOnMap) {
        /*this.adapter.setFunctionsOnMap(lstFunctionsOnMap);
        this.adapter.notifyDataSetChanged();*/
    }

    public FunctionOnMapAdapter getFunctionsOnMapAdapter() {
        return adapter;
    }

    public void navigate(Context ctx, String fragmentTag) {
        navigator = (FragmentNavigator) ctx;
        navigator.fragmentNavigator(fragmentTag, true, null);
    }
}
