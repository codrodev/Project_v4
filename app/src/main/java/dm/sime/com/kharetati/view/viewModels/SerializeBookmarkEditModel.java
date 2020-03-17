package dm.sime.com.kharetati.view.viewModels;

import com.google.gson.annotations.SerializedName;

public class SerializeBookmarkEditModel {
    @SerializedName("UserID")
    private int UserID;
    @SerializedName("ParcelNumber")
    private String ParcelNumber;
    @SerializedName("descriptionEn")
    private String descriptionEn;
    @SerializedName("descriptionAr")
    private String descriptionAr;

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getParcelNumber() {
        return ParcelNumber;
    }

    public void setParcelNumber(String parcelNumber) {
        ParcelNumber = parcelNumber;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionAr() {
        return descriptionAr;
    }

    public void setDescriptionAr(String descriptionAr) {
        this.descriptionAr = descriptionAr;
    }
}
