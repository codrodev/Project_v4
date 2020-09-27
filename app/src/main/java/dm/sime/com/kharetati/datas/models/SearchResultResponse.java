package dm.sime.com.kharetati.datas.models;

public class SearchResultResponse {
    private String ParcelId;

    private String ApplicationId;

    private String allowAddToFavorites;

    public String getAllowAddToFavorites() {
        return allowAddToFavorites;
    }

    public void setAllowAddToFavorites(String allowAddToFavorites) {
        this.allowAddToFavorites = allowAddToFavorites;
    }

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
