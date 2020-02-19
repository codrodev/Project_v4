package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.SerializedName;

public class SerializedModel {
    @SerializedName("token")
    private String token;
    @SerializedName("locale")
    private String locale;
    @SerializedName("my_id")
    private String my_id;
    @SerializedName("request_id")
    private String request_id;
    @SerializedName("is_owner")
    private Boolean is_owner;
    @SerializedName("is_owned_by_person")
    private Boolean is_owned_by_person;
    @SerializedName("doc_id")
    private int doc_id;

    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public Boolean getIs_owner() {
        return is_owner;
    }

    public void setIs_owner(Boolean is_owner) {
        this.is_owner = is_owner;
    }

    public Boolean getIs_owned_by_person() {
        return is_owned_by_person;
    }

    public void setIs_owned_by_person(Boolean is_owned_by_person) {
        this.is_owned_by_person = is_owned_by_person;
    }

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
