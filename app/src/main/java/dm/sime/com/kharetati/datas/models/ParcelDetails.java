package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.SerializedName;

public class ParcelDetails {
    @SerializedName("CommNo")
    private int CommNo;
    @SerializedName("CommNameAr")
    private String CommNameAr;
    @SerializedName("CommNameEn")
    private String CommNameEn;
    @SerializedName("AreaInSqMt")
    private long AreaInSqMt;

    public int getCommNo() {
        return CommNo;
    }

    public void setCommNo(int commNo) {
        CommNo = commNo;
    }

    public String getCommNameAr() {
        return CommNameAr;
    }

    public void setCommNameAr(String commNameAr) {
        CommNameAr = commNameAr;
    }

    public String getCommNameEn() {
        return CommNameEn;
    }

    public void setCommNameEn(String commNameEn) {
        CommNameEn = commNameEn;
    }

    public long getAreaInSqMt() {
        return AreaInSqMt;
    }

    public void setAreaInSqMt(long areaInSqMt) {
        AreaInSqMt = areaInSqMt;
    }
}
