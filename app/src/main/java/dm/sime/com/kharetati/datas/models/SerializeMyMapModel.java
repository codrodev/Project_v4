package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.SerializedName;

public class SerializeMyMapModel {
    @SerializedName("token")
    private String token;
    @SerializedName("locale")
    private String locale;
    @SerializedName("my_id")
    private String my_id;
    @SerializedName("request_id")
    private String request_id;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

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
