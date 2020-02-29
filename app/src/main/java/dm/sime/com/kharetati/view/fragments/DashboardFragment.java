package dm.sime.com.kharetati.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentDashboardBinding;
import dm.sime.com.kharetati.view.viewModels.DashboardViewModel;

public class DashboardFragment extends Fragment implements ViewPager.OnPageChangeListener{

    DashboardViewModel model;
    FragmentDashboardBinding binding;
    private View mRootView;

    public static DashboardFragment newInstance(){
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(DashboardViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        binding.setFragmentDashboardVM(model);
        mRootView = binding.getRoot();
        initializePage();
        return binding.getRoot();
    }

    private void initializePage(){
        model.setDashboardPagerAdapter(this, 2);
        binding.viewPagerCreatePackage.addOnPageChangeListener(this);
        binding.viewPagerCreatePackage.setAdapter(model.getDashboardPagerAdapter());

        model.manageAppBar(getActivity(), true);
        model.manageAppBottomBAtr(getActivity(), true);

        binding.layoutBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.viewPagerCreatePackage.setCurrentItem(getNext(), true);
            }
        });
        binding.layoutMyMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.viewPagerCreatePackage.setCurrentItem(getPrevious(), true);
            }
        });
    }

    private void changeBookmarkColor(){
        binding.txtBookmark.setTextColor(getResources().getColor(R.color.white));
        binding.layoutBookmark.setBackgroundColor(getResources().getColor(R.color.maroon_dark));
        binding.imgBookMark.setImageDrawable(getResources().getDrawable(R.drawable.ic_book_white_24dp));

        binding.txtMyMap.setTextColor(getResources().getColor(R.color.black));
        binding.layoutMyMap.setBackgroundColor(getResources().getColor(R.color.white));
        binding.imgMyMap.setImageDrawable(getResources().getDrawable(R.drawable.ic_pin_drop_24dp));
    }

    private void changeMyMapColor(){
        binding.txtBookmark.setTextColor(getResources().getColor(R.color.black));
        binding.layoutBookmark.setBackgroundColor(getResources().getColor(R.color.white));
        binding.imgBookMark.setImageDrawable(getResources().getDrawable(R.drawable.ic_book_black_24dp));

        binding.txtMyMap.setTextColor(getResources().getColor(R.color.white));
        binding.layoutMyMap.setBackgroundColor(getResources().getColor(R.color.maroon_dark));
        binding.imgMyMap.setImageDrawable(getResources().getDrawable(R.drawable.ic_pin_drop_white_24dp));
    }

    private int getNext() {
        return binding.viewPagerCreatePackage.getCurrentItem() + 1;
    }

    private int getPrevious() {
        return binding.viewPagerCreatePackage.getCurrentItem() - 1;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(position == 1){
            changeBookmarkColor();
        } else {
            changeMyMapColor();
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
