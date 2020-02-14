package dm.sime.com.kharetati.datas.models;

public class RetrieveDocStreamResponse {
    private DocDetails doc_details;

    private String message_ar;

    private String message_en;

    private String status;


    public DocDetails getDoc_details ()
    {
        return doc_details;
    }

    public void setDoc_details (DocDetails doc_details)
    {
        this.doc_details = doc_details;
    }

    public String getMessage_ar ()
    {
        return message_ar;
    }

    public void setMessage_ar (String message_ar)
    {
        this.message_ar = message_ar;
    }

    public String getMessage_en ()
    {
        return message_en;
    }

    public void setMessage_en (String message_en)
    {
        this.message_en = message_en;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }


}
