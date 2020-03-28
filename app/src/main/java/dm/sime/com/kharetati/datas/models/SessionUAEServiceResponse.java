package dm.sime.com.kharetati.datas.models;

public class SessionUAEServiceResponse {
    private UAEPASSDetails UAEPASSDetails;

    private String last_login;

    private String token;

    public UAEPASSDetails getUAEPASSDetails ()
    {
        return UAEPASSDetails;
    }

    public void setUAEPASSDetails (UAEPASSDetails UAEPASSDetails)
    {
        this.UAEPASSDetails = UAEPASSDetails;
    }

    public String getLast_login ()
    {
        return last_login;
    }

    public void setLast_login (String last_login)
    {
        this.last_login = last_login;
    }

    public String getToken ()
    {
        return token;
    }

    public void setToken (String token)
    {
        this.token = token;
    }
}
