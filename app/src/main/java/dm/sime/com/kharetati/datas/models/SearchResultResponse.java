package dm.sime.com.kharetati.datas.models;

public class SearchResultResponse {
    private String ParcelId;

    private String ApplicationId;

    private Map Map;

    public String getParcelId ()
    {
        return ParcelId;
    }

    public void setParcelId (String ParcelId)
    {
        this.ParcelId = ParcelId;
    }

    public String getApplicationId ()
    {
        return ApplicationId;
    }

    public void setApplicationId (String ApplicationId)
    {
        this.ApplicationId = ApplicationId;
    }

    public Map getMap ()
    {
        return Map;
    }

    public void setMap (Map Map)
    {
        this.Map = Map;
    }
}
