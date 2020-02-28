package dm.sime.com.kharetati.datas.models;

public class LicenceDocs {
    private String doc_desc_en;
    private String doc_type;
    private String doc_format;
    private int doc_id;
    private String doc;
    private String doc_name;

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getDoc_desc_en() {
        return doc_desc_en;
    }

    public void setDoc_desc_en(String doc_desc_en) {
        this.doc_desc_en = doc_desc_en;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public String getDoc_format() {
        return doc_format;
    }

    public void setDoc_format(String doc_format) {
        this.doc_format = doc_format;
    }

    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }
}
