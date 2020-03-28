package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SerializedCreateAndUpdateModel {

    @SerializedName("token")
    private String token;
    @SerializedName("my_id")
    private String my_id;
    @SerializedName("request_id")
    private String request_id;
    @SerializedName("request_source")
    private String request_source;
    @SerializedName("parcel_id")
    private long parcel_id;
    @SerializedName("is_hard_copy_reqd")
    private String is_hard_copy_reqd;
    @SerializedName("applicant_email_id")
    private String applicant_email_id;
    @SerializedName("applicant_mobile")
    private String applicant_mobile;
    @SerializedName("is_owner")
    private Boolean is_owner;
    @SerializedName("is_owned_by_person")
    private Boolean is_owned_by_person;
    @SerializedName("delivery_details")
    private DeliveryDetails delivery_details;
    @SerializedName("payment_type")
    private String payment_type;
    @SerializedName("passport_docs")
    public List<PassportDocs> passport_docs;
    @SerializedName("license")
    public List<LicenceDocs> license;
    @SerializedName("noc_docs")
    public List<NocDocs> noc_docs;
    @SerializedName("locale")
    private String locale;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMy_id() {
        return my_id;
    }

    public void setMy_id(String my_id) {
        this.my_id = my_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getRequest_source() {
        return request_source;
    }

    public void setRequest_source(String request_source) {
        this.request_source = request_source;
    }

    public long getParcel_id() {
        return parcel_id;
    }

    public void setParcel_id(long parcel_id) {
        this.parcel_id = parcel_id;
    }

    public String getIs_hard_copy_reqd() {
        return is_hard_copy_reqd;
    }

    public void setIs_hard_copy_reqd(String is_hard_copy_reqd) {
        this.is_hard_copy_reqd = is_hard_copy_reqd;
    }

    public String getApplicant_email_id() {
        return applicant_email_id;
    }

    public void setApplicant_email_id(String applicant_email_id) {
        this.applicant_email_id = applicant_email_id;
    }

    public String getApplicant_mobile() {
        return applicant_mobile;
    }

    public void setApplicant_mobile(String applicant_mobile) {
        this.applicant_mobile = applicant_mobile;
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

    public DeliveryDetails getDelivery_details() {
        return delivery_details;
    }

    public void setDelivery_details(DeliveryDetails delivery_details) {
        this.delivery_details = delivery_details;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public List<PassportDocs> getPassport_docs() {
        return passport_docs;
    }

    public void setPassport_docs(List<PassportDocs> passport_docs){
        this.passport_docs = passport_docs;
    }

    public List<LicenceDocs> getLicense() {
        return license;
    }

    public void setLicense(List<LicenceDocs> license) {
        this.license = license;
    }

    public List<NocDocs> getNoc_docs() {
        return noc_docs;
    }

    public void setNoc_docs(List<NocDocs> noc_docs) {
        this.noc_docs = noc_docs;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }


}
