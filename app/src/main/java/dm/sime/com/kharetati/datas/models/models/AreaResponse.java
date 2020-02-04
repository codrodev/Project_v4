package dm.sime.com.kharetati.datas.models.models;

import androidx.databinding.BaseObservable;

public class AreaResponse extends BaseObservable {
    private dm.sime.com.kharetati.datas.models.Areas[] Areas;

    public dm.sime.com.kharetati.datas.models.Areas[] getAreas ()
    {
        return Areas;
    }

    public void setAreas (dm.sime.com.kharetati.datas.models.Areas[] Areas)
    {
        this.Areas = Areas;
    }

}
