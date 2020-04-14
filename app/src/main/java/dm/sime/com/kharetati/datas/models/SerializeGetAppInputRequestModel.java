package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.SerializedName;

public class SerializeGetAppInputRequestModel {
    @SerializedName("TOKEN")
    private String TOKEN;
    @SerializedName("MAKANI")
    private String MAKANI;
    @SerializedName("REMARKS")
    private String REMARKS;
    @SerializedName("parcel_id")
    private int parcel_id;

    public String getMAKANI() {
        return MAKANI;
    }

    public void setMAKANI(String MAKANI) {
        this.MAKANI = MAKANI;
    }

    @SerializedName("IsGuest")
    private boolean IsGuest;

    public boolean isGuest() {
        return IsGuest;
    }

    public void setGuest(boolean guest) {
        IsGuest = guest;
    }

    public int getParcel_id() {
        return parcel_id;
    }

    public void setParcel_id(int parcel_id) {
        this.parcel_id = parcel_id;
    }

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
