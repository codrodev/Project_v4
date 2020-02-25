package dm.sime.com.kharetati.datas.models;

import java.util.List;

public class Map {
    private Details Details;

    private List<Functions> Functions;

    public Details getDetails ()
    {
        return Details;
    }

    public void setDetails (Details Details)
    {
        this.Details = Details;
    }

    public List<Functions> getFunctions ()
    {
        return Functions;
    }

    public void setFunctions (List<Functions> Functions)
    {
        this.Functions = Functions;
    }

}
