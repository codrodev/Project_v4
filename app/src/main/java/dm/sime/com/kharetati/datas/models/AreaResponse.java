package dm.sime.com.kharetati.datas.models;

import androidx.databinding.BaseObservable;

public class AreaResponse extends BaseObservable {
    private Areas[] Areas;

    public Areas[] getAreas ()
    {
        return Areas;
    }

    public void setAreas (Areas[] Areas)
    {
        this.Areas = Areas;
    }

}
