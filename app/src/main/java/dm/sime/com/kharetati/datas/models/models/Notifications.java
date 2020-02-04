package dm.sime.com.kharetati.datas.models.models;

import androidx.databinding.BaseObservable;

/**
 * Created by Imran on 9/25/2017.
 */

public class Notifications extends BaseObservable {
    private String Description;

    private String NameEn;

    private String LaunchUrl;

    private String Id;

    private String NameAr;

    public String getDescription ()
    {
        return Description;
    }

    public void setDescription (String Description)
    {
        this.Description = Description;
    }

    public String getNameEn ()
    {
        return NameEn;
    }

    public void setNameEn (String NameEn)
    {
        this.NameEn = NameEn;
    }

    public String getLaunchUrl ()
    {
        return LaunchUrl;
    }

    public void setLaunchUrl (String LaunchUrl)
    {
        this.LaunchUrl = LaunchUrl;
    }

    public String getId ()
    {
        return Id;
    }

    public void setId (String Id)
    {
        this.Id = Id;
    }

    public String getNameAr ()
    {
        return NameAr;
    }

    public void setNameAr (String NameAr)
    {
        this.NameAr = NameAr;
    }
}
