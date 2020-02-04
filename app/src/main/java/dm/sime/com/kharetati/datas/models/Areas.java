package dm.sime.com.kharetati.datas.models;

import androidx.databinding.BaseObservable;

public class Areas extends BaseObservable {
    private String AREA_ID;

    private String AREA_NAME_AR;

    private String AREA_NAME_EN;

    public String getAreaID ()
    {
        return AREA_ID;
    }

    public void setAreaID (String AREA_ID)
    {
        this.AREA_ID = AREA_ID;
    }

    public String getAreaNameAR ()
    {
        return AREA_NAME_AR;
    }

    public void setAreaNameAR (String AREA_NAME_AR)
    {
        this.AREA_NAME_AR = AREA_NAME_AR;
    }

    public String getAreaNameEN ()
    {
        return AREA_NAME_EN;
    }

    public void setAreaNameEN (String AREA_NAME_EN)
    {
        this.AREA_NAME_EN = AREA_NAME_EN;
    }
}
