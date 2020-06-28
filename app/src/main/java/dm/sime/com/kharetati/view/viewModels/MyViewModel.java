package dm.sime.com.kharetati.view.viewModels;



import android.app.Activity;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dm.sime.com.kharetati.R;

public class MyViewModel {

    List<String> lstHeader;
    List<String> lstBody;
    List<String> lstDate;


    public MyViewModel(){

    }

    public void initializeMode(Activity activity){
        lstHeader = new ArrayList<>();
        lstBody = new ArrayList<>();
        lstDate = new ArrayList<>();

        lstHeader = Arrays.asList(activity.getResources().getStringArray(R.array.arrHeader));
        lstBody = Arrays.asList(activity.getResources().getStringArray(R.array.arrBody));
        lstDate = Arrays.asList(activity.getResources().getStringArray(R.array.arrDate));

    }

    public List<String> getLstHeader(){
        return lstHeader;
    }

    public List<String> getLstDate(){
        return lstDate;
    }

    public List<String> getLstBody(){
        return lstBody;
    }

    public String getCurrentHeader(int position){
        return lstHeader.get(position);
    }

    public String getCurrentDate(int position){
        return lstDate.get(position);
    }

    public String getCurrentBody(int position){
        return lstBody.get(position);
    }
}
