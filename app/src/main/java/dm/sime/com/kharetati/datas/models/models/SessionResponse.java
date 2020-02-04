package dm.sime.com.kharetati.datas.models.models;

import androidx.databinding.BaseObservable;

import dm.sime.com.kharetati.datas.models.Session;

public class SessionResponse extends BaseObservable {
    private String is_exception;

    private String message;

    private Session service_response;

    public String getIs_exception ()
    {
        return is_exception;
    }

    public void setIs_exception (String is_exception)
    {
        this.is_exception = is_exception;
    }

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public Session getSession ()
    {
        return service_response;
    }

    public void setSession (Session service_response)
    {
        this.service_response = service_response;
    }

}