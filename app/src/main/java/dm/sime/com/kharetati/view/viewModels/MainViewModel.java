package dm.sime.com.kharetati.view.viewModels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dm.sime.com.kharetati.utility.constants.FragmentTAGS;

public class MainViewModel extends ViewModel {

    public MainViewModel(){

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
            default:
                return "";
        }
    }
}
