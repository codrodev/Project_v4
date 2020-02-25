package dm.sime.com.kharetati.datas.models;

public class AppSession {
    private String last_login;

    private String token;

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
