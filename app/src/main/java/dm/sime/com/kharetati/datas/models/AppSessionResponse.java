package dm.sime.com.kharetati.datas.models;

public class AppSessionResponse {
    private String message_ar;

    private String is_exception;

    private String message;

    private AppSession service_response;

    public String getMessage_ar ()
    {
        return message_ar;
    }

    public void setMessage_ar (String message_ar)
    {
        this.message_ar = message_ar;
    }

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

    public AppSession getService_response ()
    {
        return service_response;
    }

    public void setService_response (AppSession service_response)
    {
        this.service_response = service_response;
    }
}
