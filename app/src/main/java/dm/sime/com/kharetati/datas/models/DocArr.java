package dm.sime.com.kharetati.datas.models;

public class DocArr {
    String docFormat;
    String docKey;
    String docPath;
    String docName;
    String docDta;
    int docId;

    public String getDocName() {
        return docName;
    }

    public String getDocDta() {
        return docDta;
    }

    public void setDocDta(String docDta) {
        this.docDta = docDta;
    }


    public String getDocFormat() {
        return docFormat;
    }

    public void setDocFormat(String docFormat) {
        this.docFormat = docFormat;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }
}