package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.SerializedName;

public class SerializeGetAppInputRequestModel {
    @SerializedName("TOKEN")
    private String TOKEN;
    @SerializedName("REMARKS")
    private String REMARKS;

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }
}
