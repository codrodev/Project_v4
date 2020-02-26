package dm.sime.com.kharetati.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentBottomsheetMapFunctionBinding;
import dm.sime.com.kharetati.datas.models.Functions;
import dm.sime.com.kharetati.datas.models.FunctionsOnMap;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.adapters.FunctionOnMapAdapter;
import dm.sime.com.kharetati.view.viewModels.MapFunctionBottomSheetViewModel;

public class MapFunctionBottomSheetFragment extends BottomSheetDialogFragment implements FunctionOnMapAdapter.OnMenuSelectedListener {

    MapFunctionBottomSheetViewModel model;
    FragmentBottomsheetMapFunctionBinding binding;
    MapFunctionWebViewBottomSheetFragment webBottomSheet;
    private View mRootView;
    static OnFunctionMenuSelectedListener listener;

    public static MapFunctionBottomSheetFragment newInstance(OnFunctionMenuSelectedListener listeners) {
        MapFunctionBottomSheetFragment f = new MapFunctionBottomSheetFragment();
        listener = listeners;
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(MapFunctionBottomSheetViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottomsheet_map_function, container, false);
        binding.setFragmentBottomSheetMapFunction(model);
        model.initializeViewModel(getActivity(), this);

        model.getMutableFunctionsOnMap().observe(getActivity(), new Observer<List<Functions>>() {
            @Override
            public void onChanged(List<Functions> lstFunctionsOnMap) {

                //model.loading.set(View.GONE);
                if (Global.mapSearchResult.getService_response().getMap().getFunctions().size() > 0) {
                    model.setFunctionsOnMapAdapter(Global.mapSearchResult.getService_response().getMap().getFunctions());
                }
            }
        });

        mRootView = binding.getRoot();
        return binding.getRoot();
    }

    @Override
    public void onMenuSelected(String menu, int position) {
        listener.onFunctionMenuSelected(position);
        /*Functions fun = model.getCurrentFunctionsOnMap(position);
        if(fun.getInternalFunctions() != null && fun.getInternalFunctions().length() > 0){
            if (fun.getInternalFunctions().equals(FragmentTAGS.FR_REQUEST_SITE_PLAN)){
                model.navigate(getActivity(), FragmentTAGS.FR_REQUEST_SITE_PLAN);
            }
        } else if (fun.getLaunchUrl() != null && fun.getLaunchUrl().length() > 0 &&
                fun.getLaunchUrl().contains("http")){
            webBottomSheet = new MapFunctionWebViewBottomSheetFragment(fun.getLaunchUrl());
            webBottomSheet.show(getActivity().getSupportFragmentManager(), webBottomSheet.getTag());
        }*/
        this.dismiss();

    }

    public interface OnFunctionMenuSelectedListener {
        void onFunctionMenuSelected(int position);
    }
}