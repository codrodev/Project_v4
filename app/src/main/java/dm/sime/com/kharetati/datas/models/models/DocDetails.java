package dm.sime.com.kharetati.datas.models.models;

import androidx.databinding.BaseObservable;

public class DocDetails extends BaseObservable {
    private String doctype;

    private String docformat;

    private String doc;

    public String getDoctype ()
    {
        return doctype;
    }

    public void setDoctype (String doctype)
    {
        this.doctype = doctype;
    }

    public String getDocformat ()
    {
        return docformat;
    }

    public void setDocformat (String docformat)
    {
        this.docformat = docformat;
    }

    public String getDoc ()
    {
        return doc;
    }

    public void setDoc (String doc)
    {
        this.doc = doc;
    }

}
