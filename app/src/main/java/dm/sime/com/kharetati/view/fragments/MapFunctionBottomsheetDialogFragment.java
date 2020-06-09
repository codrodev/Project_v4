package dm.sime.com.kharetati.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentBottomsheetMapFunctionBinding;
import dm.sime.com.kharetati.datas.models.Functions;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.view.adapters.FunctionOnMapAdapter;
import dm.sime.com.kharetati.view.viewModels.MapFunctionBottomSheetViewModel;

public class MapFunctionBottomsheetDialogFragment extends BottomSheetDialogFragment implements FunctionOnMapAdapter.OnMenuSelectedListener  {
    MapFunctionBottomSheetViewModel model;
    FragmentBottomsheetMapFunctionBinding binding;
    private View mRootView;
    static OnFunctionMenuSelectedListener listener;
    FunctionOnMapAdapter adapter;
    RecyclerView recycleMapFunction;

    public static  MapFunctionBottomsheetDialogFragment newInstance(OnFunctionMenuSelectedListener list){
        MapFunctionBottomsheetDialogFragment f = new MapFunctionBottomsheetDialogFragment();
        listener = list;
        return f;
    }

    //Bottom Sheet Callback
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }


        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    /*@Override
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
    }*/

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View contentView = View.inflate(getContext(), R.layout.fragment_bottomsheet_map_function, null);
        recycleMapFunction = (RecyclerView)contentView.findViewById(R.id.recycleMapFunction);
        dialog.setContentView(contentView);
        //model.initializeViewModel(getActivity());
       /* if(Global.mapSearchResult.getService_response().getMap().getFunctions() != null){
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);

            adapter = new FunctionOnMapAdapter(model, getActivity(),this, Global.mapSearchResult.getService_response().getMap().getFunctions());
            recycleMapFunction.setAdapter(adapter);
            recycleMapFunction.setLayoutManager(layoutManager);
            adapter.notifyDataSetChanged();
            
        }*/



        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        //Set callback
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    @Override
    public void onMenuSelected(String menu, int position) {
        listener.onFunctionMenuSelected(position);
        this.dismiss();

    }

    public interface OnFunctionMenuSelectedListener {
        void onFunctionMenuSelected(int position);
    }
}