package dm.sime.com.kharetati.datas.models;

import java.util.List;

public class LookupResponse {
    private List<LookupValue> Lkp;

    public List<LookupValue> getLkp ()
    {
        return Lkp;
    }

    public void setLkp (List<LookupValue> Lkp)
    {
        this.Lkp = Lkp;
    }
}
