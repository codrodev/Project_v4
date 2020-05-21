package dm.sime.com.kharetati.view.viewModels;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dm.sime.com.kharetati.KharetatiApp;
import dm.sime.com.kharetati.datas.repositories.MainRepository;
import dm.sime.com.kharetati.utility.Global;
import dm.sime.com.kharetati.utility.constants.FragmentTAGS;
import dm.sime.com.kharetati.view.navigators.MainNavigator;

public class MainViewModel extends ViewModel {

    private KharetatiApp kharetatiApp;
    Activity activity;
    MainRepository repository;
    public MainNavigator mainNavigator;

    /*public MainViewModel(){

    }*/
    public MainViewModel(Activity context, MainRepository repository){
        this.activity = context;
        this.repository = repository;

        kharetatiApp = KharetatiApp.create(activity);
    }

    public MutableLiveData<String> sampleText = new MutableLiveData<String>();

    public MainViewModel(Context context) {
        sampleText = new MutableLiveData<String>();
    }

    public void initialize(){
        sampleText.setValue("sample text");
    }

    public String getValue(){
        return sampleText.getValue();
    }

    public String bottomNavigationTAG(int position){
        switch (position) {
            case 1:
                return FragmentTAGS.FR_DASHBOARD;
            case 2:
                return FragmentTAGS.FR_HAPPINESS;
            case 3:
                return FragmentTAGS.FR_HOME;
            case 4:
                return FragmentTAGS.FR_CONTACT_US;
                case 5:
                return FragmentTAGS.FR_BOTTOMSHEET;
            default:
                return "";
        }
    }
    public int getHeaderHieght(){
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            return dp2Px((Global.getScreenHeight(activity)/100)*15);
        } else {
            // In portrait
            return dp2Px((Global.getScreenHeight(activity)/100)*15);

        }

    }
    private int sp2Px(Float sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }
    private int dp2Px(Float dp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
