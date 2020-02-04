package dm.sime.com.kharetati.datas.models;

public class Map {
    private String ServiceUrl;

    private String ServiceTokenUrl;

    private String ServiceTokenPassword;

    private String ServiceTokenUserName;

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
