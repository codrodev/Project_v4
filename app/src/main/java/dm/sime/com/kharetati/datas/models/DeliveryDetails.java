package dm.sime.com.kharetati.datas.models;

import androidx.databinding.BaseObservable;

public class DeliveryDetails extends BaseObservable {
    private int emirate;
    private String email_id;
    private String mobile;
    private String name_en;
    private String name_ar;
    private String main_address;
    private String nearest_landmark;
    private String street_address;
    private String makani_no;
    private String building_name;
    private String building_no;
    private int emID;

    public int getEmID() {
        return emID;
    }

    public void setEmID(int emID) {
        this.emID = emID;
    }

    public int getEmirate() {
        return emirate;
    }

    public void setEmirate(int emirate) {
        this.emirate = emirate;
    }

    public String getEmailId() {
        return email_id;
    }

    public void setEmailId(String email_id) {
        this.email_id = email_id;
    }

    public String getMobileNo() {
        return mobile;
    }

    public void setMobileNo(String mobile_no) {
        this.mobile = mobile_no;
    }

    public String getNameEn() {
        return name_en;
    }

    public void setNameEn(String name_en) {
        this.name_en = name_en;
    }

    public String getNameAr() {
        return name_ar;
    }

    public void setNameAr(String name_ar) {
        this.name_ar = name_ar;
    }

    public String getMainAddress() {
        return main_address;
    }

    public void setMainAddress(String main_address) {
        this.main_address = main_address;
    }

    public String getNearestLandmark() {
        return nearest_landmark;
    }

    public void setNearestLandmark(String nearest_landmark) {
        this.nearest_landmark = nearest_landmark;
    }

    public String getStreetAddress() {
        return street_address;
    }

    public void setStreetAddress(String street_address) {
        this.street_address = street_address;
    }

    public String getMakaniNo() {
        return makani_no;
    }

    public void setMakaniNo(String makani_no) {
        this.makani_no = makani_no;
    }

    public String getBldgName() {
        return building_name;
    }

    public void setBldgName(String bldg_name) {
        this.building_name = bldg_name;
    }

    public String getBldgNo() {
        return building_no;
    }

    public void setBldgNo(String bldg_no) {
        this.building_no = bldg_no;
    }
}
