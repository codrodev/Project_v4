package dm.sime.com.kharetati.datas.models;

import java.util.List;

public class Details {
    private List<LayerDefinition> LayerDefinition;

    private Search Search;

    private String ServiceUrl;

    private String ServiceTokenUrl;

    private String Id;

    private Dimension Dimension;

    private String ServiceTokenPassword;

    private String ServiceTokenUserName;

    public List<LayerDefinition> getLayerDefinition ()
    {
        return LayerDefinition;
    }

    public void setLayerDefinition (List<LayerDefinition> LayerDefinition)
    {
        this.LayerDefinition = LayerDefinition;
    }

    public Search getSearch ()
    {
        return Search;
    }

    public void setSearch (Search Search)
    {
        this.Search = Search;
    }

    public String getServiceUrl ()
    {
        return ServiceUrl;
    }

    public void setServiceUrl (String ServiceUrl)
    {
        this.ServiceUrl = ServiceUrl;
    }

    public String getServiceTokenUrl ()
    {
        return ServiceTokenUrl;
    }

    public void setServiceTokenUrl (String ServiceTokenUrl)
    {
        this.ServiceTokenUrl = ServiceTokenUrl;
    }

    public String getId ()
    {
        return Id;
    }

    public void setId (String Id)
    {
        this.Id = Id;
    }

    public Dimension getDimension ()
    {
        return Dimension;
    }

    public void setDimension (Dimension Dimension)
    {
        this.Dimension = Dimension;
    }

    public String getServiceTokenPassword ()
    {
        return ServiceTokenPassword;
    }

    public void setServiceTokenPassword (String ServiceTokenPassword)
    {
        this.ServiceTokenPassword = ServiceTokenPassword;
    }

    public String getServiceTokenUserName ()
    {
        return ServiceTokenUserName;
    }

    public void setServiceTokenUserName (String ServiceTokenUserName)
    {
        this.ServiceTokenUserName = ServiceTokenUserName;
    }

}
