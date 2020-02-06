package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.SerializedName;

public class SerializeMyMapModel {
    @SerializedName("token")
    private String token;
    @SerializedName("locale")
    private String locale;
    @SerializedName("my_id")
    private String my_id;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getMy_id() {
        return my_id;
    }

    public void setMy_id(String my_id) {
        this.my_id = my_id;
    }
}
