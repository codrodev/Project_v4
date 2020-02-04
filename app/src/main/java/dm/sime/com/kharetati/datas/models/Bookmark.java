package dm.sime.com.kharetati.datas.models;

import androidx.databinding.BaseObservable;

import java.util.Date;

/**
 * Created by Imran on 9/7/2017.
 */

public class Bookmark extends BaseObservable {
    public String ParcelNumber;
    public String Community;
    public String CommunityAr;
    public String Area;
    public boolean isParcelExistInSitePlan;
    public Date date;
    public String descriptionEn;
    public String descriptionAr;
}
