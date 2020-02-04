package dm.sime.com.kharetati.datas.models;

public class MakaniToDLTMResponse {
    private String is_exception;

    private String message;

    private DLTMContainer service_response;

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

    public DLTMContainer getDLTMContainer ()
    {
        return service_response;
    }

    public void setDLTMContainer (DLTMContainer service_response)
    {
        this.service_response = service_response;
    }


}
