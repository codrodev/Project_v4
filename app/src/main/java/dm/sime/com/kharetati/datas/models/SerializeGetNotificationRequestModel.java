package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.SerializedName;

public class SerializeGetNotificationRequestModel {
    @SerializedName("UserId")
    private String UserId;
    @SerializedName("TOKEN")
    private String TOKEN;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }
}
