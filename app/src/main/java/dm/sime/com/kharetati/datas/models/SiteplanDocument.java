package dm.sime.com.kharetati.datas.models;

import androidx.databinding.BaseObservable;

public class SiteplanDocument extends BaseObservable {
    private String doctype;

    private String docid;

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
        return docid;
    }

    public void setDocid (String docid)
    {
        this.docid = docid;
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
