package dm.sime.com.kharetati.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentDashboardBinding;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.viewModels.DashboardViewModel;

import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_BOOKMARK;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_DASHBOARD;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_DELIVERY;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_MYMAP;

public class DashboardFragment extends Fragment implements ViewPager.OnPageChangeListener{

    private static String ARG_PARAM1;
    DashboardViewModel model;
    FragmentDashboardBinding binding;
    public static FragmentDashboardBinding dshboardBinding;
    private View mRootView;
    private Tracker mTracker;
    public static Boolean sortDescending=true;
    private int fragmenntPosition;

    public static DashboardFragment newInstance(int param1){
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(DashboardViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.current_fragment_id = FragmentTAGS.FR_DASHBOARD;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        binding.setFragmentDashboardVM(model);
        fragmenntPosition = getArguments().getInt(ARG_PARAM1);
        dshboardBinding =binding;
        mRootView = binding.getRoot();
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_DASHBOARD);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        initializePage();
        setRetainInstance(true);
        return binding.getRoot();
    }

    private void initializePage(){
        Global.FragmentTagForDashboardHelpUrl = 0;
        Global.isDashboard=true;
        if(!Global.isUserLoggedIn){
            guestUserUI();
            binding.layoutParent.setWeightSum(1);
            model.setDashboardPagerAdapter(this, 1);
        } else {
            binding.layoutParent.setWeightSum(2);
            binding.layoutParent.setPaddingRelative(48,8,200,8);
            model.setDashboardPagerAdapter(this, 2);
        }
        binding.viewPagerCreatePackage.addOnPageChangeListener(this);
        binding.viewPagerCreatePackage.setAdapter(model.getDashboardPagerAdapter());

        binding.viewPagerCreatePackage.setCurrentItem(fragmenntPosition, true);
        model.manageAppBar(getActivity(), true);
        model.manageAppBottomBAtr(getActivity(), true);

        binding.imgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sortDescending)
                    binding.imgSort.setRotationX(180);
                else
                    binding.imgSort.setRotationX(0);


                if(Global.current_fragment_id.equals(FragmentTAGS.FR_BOOKMARK)){
                    BookmarkFragment.bmModel.bookMarksNavigator.sortBookmarks(sortDescending);

                }
                else if(Global.current_fragment_id.equals(FragmentTAGS.FR_MYMAP)){
                    MyMapFragment.myMapModel.myMapNavigator.sortSiteplans(sortDescending);
                }


            }
        });

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

    private void guestUserUI(){
        binding.layoutCardMyMap.setVisibility(View.GONE);
        binding.txtBookmark.setTextColor(getResources().getColor(R.color.black));
        binding.layoutBookmark.setBackgroundColor(getResources().getColor(R.color.white));
        binding.imgBookMark.setImageDrawable(getResources().getDrawable(R.drawable.favourites));
        ((MainActivity)getActivity()).setScreenName(getActivity().getResources().getString(R.string.bookmark));
        binding.layoutParent.setPaddingRelative(0,5,0,5);
    }

    private void changeBookmarkColor(){
        binding.txtBookmark.setTextColor(getResources().getColor(R.color.black));
        binding.layoutBookmark.setBackground(getResources().getDrawable(R.drawable.capsule_white_bg));
        binding.imgBookMark.setImageDrawable(getResources().getDrawable(R.drawable.favourites));
        ((MainActivity)getActivity()).setScreenName(getActivity().getResources().getString(R.string.bookmark));

        binding.txtMyMap.setTextColor(getResources().getColor(R.color.white));
        binding.layoutMyMap.setBackground(getResources().getDrawable(R.drawable.capsule_bg));
        binding.imgMyMap.setImageDrawable(getResources().getDrawable(R.drawable.map_white));
        Global.HelpUrl = Global.CURRENT_LOCALE.equals("en")?Global.bookmarks_en_url:Global.bookmarks_ar_url;
        Global.current_fragment_id = FR_BOOKMARK;
    }

    private void changeMyMapColor(){
        binding.txtBookmark.setTextColor(getResources().getColor(R.color.white));
        binding.layoutBookmark.setBackground(getResources().getDrawable(R.drawable.capsule_bg));
        binding.imgBookMark.setImageDrawable(getResources().getDrawable(R.drawable.favourites_white));

        binding.txtMyMap.setTextColor(getResources().getColor(R.color.black));
        binding.layoutMyMap.setBackground(getResources().getDrawable(R.drawable.capsule_white_bg));
        binding.imgMyMap.setImageDrawable(getResources().getDrawable(R.drawable.map));
        ((MainActivity)getActivity()).setScreenName(getActivity().getResources().getString(R.string.mymaps));
        Global.HelpUrl = Global.CURRENT_LOCALE.equals("en")?Global.mymaps_en_url:Global.mymaps_ar_url;
        Global.current_fragment_id = FR_MYMAP;
    }

    private int getNext() {
        return binding.viewPagerCreatePackage.getCurrentItem() + 1;
    }

    private int getPrevious() {
        return binding.viewPagerCreatePackage.getCurrentItem() - 1;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Global.FragmentTagForDashboardHelpUrl = position;
        if(Global.isUserLoggedIn) {
            if (position == 1) {
                changeBookmarkColor();
            } else {
                changeMyMapColor();
            }
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
