package dm.sime.com.kharetati.datas.models;

import androidx.databinding.BaseObservable;

public class Session extends BaseObservable {
    private String token;

    public String getToken ()
    {
        return token;
    }

    public void setToken (String token)
    {
        this.token = token;
    }
}
