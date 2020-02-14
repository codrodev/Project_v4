package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.SerializedName;

public class SerializeBookmarkModel {
    @SerializedName("UserID")
    private int UserID;
    @SerializedName("ParcelNumber")
    private String ParcelNumber;

    public String getParcelNumber() {
        return ParcelNumber;
    }

    public void setParcelNumber(String parcelNumber) {
        ParcelNumber = parcelNumber;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }
}
