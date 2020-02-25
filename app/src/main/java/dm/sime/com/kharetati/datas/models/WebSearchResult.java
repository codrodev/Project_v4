package dm.sime.com.kharetati.datas.models;

public class WebSearchResult {
    private String message_ar;

    private String is_exception;

    private String message;

    private WebSearchResultResponse service_response;

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

    public WebSearchResultResponse getService_response ()
    {
        return service_response;
    }

    public void setService_response (WebSearchResultResponse service_response)
    {
        this.service_response = service_response;
    }
}
