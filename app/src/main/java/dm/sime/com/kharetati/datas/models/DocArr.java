package dm.sime.com.kharetati.datas.models;

public class DocArr {
    public String docFormat;
    public String docKey;
    public String docPath;
    public String docName;
    public String docDta;
    public int docId;
    public String docDesc;

    public String getDocDesc() {
        return docDesc;
    }

    public void setDocDesc(String docDesc) {
        this.docDesc = docDesc;
    }

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