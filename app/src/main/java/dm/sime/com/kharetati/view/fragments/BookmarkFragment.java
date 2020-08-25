package dm.sime.com.kharetati.view.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.R;
import dm.sime.com.kharetati.databinding.FragmentBookmarkBinding;
import dm.sime.com.kharetati.datas.models.Bookmark;
import dm.sime.com.kharetati.datas.models.PlotDetails;
import dm.sime.com.kharetati.datas.network.ApiFactory;
import dm.sime.com.kharetati.datas.network.NetworkConnectionInterceptor;
import dm.sime.com.kharetati.datas.repositories.BookMarkRepository;
import dm.sime.com.kharetati.utility.AlertDialogUtil;
import dm.sime.com.kharetati.utility.FontChangeCrawler;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.activities.MainActivity;
import dm.sime.com.kharetati.view.navigators.BookMarksNavigator;
import dm.sime.com.kharetati.view.viewModels.BookmarkViewModel;
import dm.sime.com.kharetati.view.viewmodelfactories.BookMarkViewModelFactory;

import static dm.sime.com.kharetati.utility.constants.AppConstants.PARCEL_NUMBER;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_BOOKMARK;
import static dm.sime.com.kharetati.utility.constants.FragmentTAGS.FR_DELIVERY;

public class BookmarkFragment extends Fragment implements BookMarksNavigator {

    FragmentBookmarkBinding binding;
    BookmarkViewModel model;
    private View mRootView;
    private BookMarkRepository repository;
    private BookMarkViewModelFactory factory;
    public static BookmarkViewModel bmModel;
    private Tracker mTracker;
    private boolean isDesending;

    public static BookmarkFragment newInstance(){
        BookmarkFragment fragment = new BookmarkFragment();
        return fragment;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "Dubai-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.getView());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            repository = new BookMarkRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        factory = new BookMarkViewModelFactory(getActivity(),repository);
        model = ViewModelProviders.of(this,factory).get(BookmarkViewModel.class);
        bmModel = model;
        model.bookMarksNavigator =this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Global.current_fragment_id = FragmentTAGS.FR_BOOKMARK;
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false);
        binding.setFragmentBookmarkVM(model);
        mRootView = binding.getRoot();
        Global.HelpUrl = Global.CURRENT_LOCALE.equals("en")?Global.bookmarks_en_url:Global.bookmarks_ar_url;
        mTracker = KharetatiApp.getInstance().getDefaultTracker();
        mTracker.setScreenName(FR_BOOKMARK);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        MainActivity.firebaseAnalytics.setCurrentScreen(getActivity(),FR_BOOKMARK , null /* class override */);
        initializePage();
        setRetainInstance(true);
        Global.enableClearTextInEditBox(binding.fragmentBookmarksPlotnumber,getActivity());

        /*binding.sortOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.sortLayout.getVisibility()!=View.VISIBLE)
                    binding.sortLayout.setVisibility(View.VISIBLE);
                else
                    binding.sortLayout.setVisibility(View.GONE);
            }
        });*/

        binding.sortDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isDesending = true;
               sortBookmarks(isDesending);

            }
        });
        binding.sortAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isDesending = false;


                sortBookmarks(isDesending);

            }
        });

        binding.fragmentBookmarksBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.fragmentBookmarksPlotnumber.getText().toString().trim().length()==0){
                    AlertDialogUtil.errorAlertDialog(getString(R.string.lbl_warning), getString(R.string.PLEASE_ENTER_PLOTNUMBER), getString(R.string.ok), getActivity());
                }
                else{
                    search(binding.fragmentBookmarksPlotnumber.getText().toString());
                }

                Global.hideSoftKeyboard(getActivity());
            }
        });
        binding.fragmentBookmarksPlotnumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    search(binding.fragmentBookmarksPlotnumber.getText().toString());
                    Global.hideSoftKeyboard(getActivity());
                    return true;
                }
                return false;
            }
        });
        binding.fragmentBookmarksPlotnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                 filter(s.toString());

            }
        });
        binding.fragmentBookmarksPlotnumber.setImeOptions(EditorInfo.IME_ACTION_DONE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, Global.getParcelNumbersFromHistory(getActivity()));
        binding.fragmentBookmarksPlotnumber.setAdapter(adapter);
        return binding.getRoot();

    }

    public void search(String plotno){
        if(!Global.isConnected(getContext())){
            AlertDialogUtil.errorAlertDialog(getString(R.string.lbl_warning), getString(R.string.internet_connection_problem1), getString(R.string.ok), getActivity());
            return;
        }
        PlotDetails.isOwner = false;
        if( Global.isUserLoggedIn){
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Search Parcel")
                    .setAction("["+Global.getUser(getActivity()).getUsername() +" ] - "+ PARCEL_NUMBER+"- [ " + plotno +" ]")
                    .build());
        }else{
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Search Parcel")
                    .setAction("Guest - DeviceID = [" +Global.deviceId+ "] "+ PARCEL_NUMBER+"- [ " + plotno +" ]")
                    .build());
        }
        PlotDetails.clearCommunity();
        plotno = plotno.replaceAll("\\s+","");
        plotno = plotno.replaceAll("_","");
        if(plotno.trim().length()>0){
            Global.isBookmarks =true;
        Global.isSaveAsBookmark =false;
        PlotDetails.parcelNo =plotno;

        ArrayList al = new ArrayList();
        al.add(PlotDetails.parcelNo.trim());
        //al.add("");

        ((MainActivity) getActivity()).loadFragment(FragmentTAGS.FR_MAP, true, al);}

    }

    private void initializePage(){
        model.initializeBookmarkViewModel(getActivity());
        //model.getAllBookMarks();

        model.getMutableBookmark().observe(getActivity(), new Observer<List<Bookmark>>() {
            @Override
            public void onChanged(List<Bookmark> lstBookmark) {
               if(lstBookmark!=null){
                if (lstBookmark.size() > 0) {
                    model.setBookmarkAdapter(lstBookmark);
                    binding.recyclerBookMarks.setAdapter(model.getBookmarkAdapter());
                    binding.recyclerBookMarks.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.recyclerBookMarks.setHasFixedSize(true);

                }
            }}
        });
    }
    public void intializeAdapter(){
        model.getMutableBookmark().observe(getActivity(), new Observer<List<Bookmark>>() {
            @Override
            public void onChanged(List<Bookmark> lstBookmark) {
                if(lstBookmark!=null){
                    if (lstBookmark.size() > 0) {
                        model.setBookmarkAdapter(lstBookmark);
                        binding.recyclerBookMarks.setAdapter(model.getBookmarkAdapter());
                        binding.recyclerBookMarks.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.recyclerBookMarks.setHasFixedSize(true);

                    }
                }}
        });
    }

    @Override
    public void onStarted() {
        AlertDialogUtil.showProgressBar(getActivity(),true);
    }

    @Override
    public void onSuccess() {
        binding.textHeading.setVisibility(View.GONE);
        AlertDialogUtil.showProgressBar(getActivity(),false);
        binding.recyclerBookMarks.setAdapter(model.getBookmarkAdapter());

    }

    @Override
    public void onEmpty(String Msg) {
        AlertDialogUtil.showProgressBar(getActivity(),false);
        binding.textHeading.setVisibility(View.VISIBLE);
        binding.textHeading.setText(Msg);
    }

    @Override
    public void onFailure(String Msg) {
        binding.textHeading.setVisibility(View.GONE);
        if(getActivity()!=null){
        AlertDialogUtil.showProgressBar(getActivity(),false);

        AlertDialogUtil.errorAlertDialog("",Msg,getActivity().getResources().getString(R.string.ok),getActivity());}

    }

    @Override
    public void onDeleteSuccess(List<Bookmark> lstBookmark) {
        AlertDialogUtil.showProgressBar(getActivity(),false);
        //model.getBookmarkAdapter().notifyDataSetChanged();


        model.getBookmarkAdapter().notifyDataSetChanged();
        /*if(lstBookmark.size()==0)
            onFailure(getActivity().getResources().getString(R.string.NO_FAVOURITE_PLOTS_FOUND));*/

    }

    @Override
    public void removeData(Bookmark data) {
        //model.getBookMarks().remove(data);
        //model.getBookmarkAdapter().notifyDataSetChanged();
    }
    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<Bookmark> filterdNames = new ArrayList<Bookmark>();


        //looping through existing elements
        if(model.getMutableBookmark().getValue()!=null){
        for (Bookmark s : (model.getMutableBookmark().getValue())) {
            //if the existing elements contains the search input
            if (s.ParcelNumber.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
                binding.textHeading.setVisibility(View.GONE);
            }
            else{
                if(filterdNames.size()==0){
                binding.textHeading.setVisibility(View.VISIBLE);
                binding.textHeading.setText(getActivity().getResources().getString(R.string.no_result_found));}
            }
        }
        model.getBookmarkAdapter().setBookmark(filterdNames);
        model.getBookmarkAdapter().notifyDataSetChanged();
        }

        //calling a method of the adapter class and passing the filtered list

    }

    @Override
    public void updateAdapter() {
        intializeAdapter();
        model.getBookmarkAdapter().notifyDataSetChanged();
    }

    @Override
    public void sortBookmarks(boolean descending) {
        if(descending)
        {
           if(model.getBookMarks()!=null){
            Collections.sort(model.getBookMarks(), new Comparator<Bookmark>() {
                @Override
                public int compare(Bookmark bookmark1, Bookmark bookmark2) {
                    if(bookmark1.date==null || bookmark2.date==null) return 0;
                    return bookmark1.date.compareTo(bookmark2.date);
                }
            });
           }
        }
        else{
            //txtHeading.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.asc, 0);
            if(model.getBookMarks()!=null){
            Collections.sort(model.getBookMarks(), new Comparator<Bookmark>() {
                @Override
                public int compare(Bookmark bookmark1, Bookmark bookmark2) {
                    if(bookmark1.date==null || bookmark2.date==null) return 0;
                    return bookmark1.date.compareTo(bookmark2.date)>=0?-1:0;
                }
            });
            }
        }
        DashboardFragment.sortDescending=!descending;

        if(descending)
        {


        }
        else{

        }
        if(model.getBookmarkAdapter()!=null)
        model.getBookmarkAdapter().notifyDataSetChanged();

    }



}
