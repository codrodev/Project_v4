package dm.sime.com.kharetati.datas.models;

import androidx.databinding.BaseObservable;

public class Docs extends BaseObservable {
    private String doctype;

    private String docId;

    private String docformat;

    public String getDoctype ()
    {
        return doctype;
    }

    public void setDoctype (String doctype)
    {
        this.doctype = doctype;
    }

    public String getDocid ()
    {
        return docId;
    }

    public void setDocid (String docId)
    {
        this.docId = docId;
    }

    public String getDocformat ()
    {
        return docformat;
    }

    public void setDocformat (String docformat)
    {
        this.docformat = docformat;
    }

}
