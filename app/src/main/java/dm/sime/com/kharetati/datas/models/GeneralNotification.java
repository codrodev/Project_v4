package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeneralNotification {

    @SerializedName("NameEn")
    @Expose
    private String nameEn;
    @SerializedName("NameAr")
    @Expose
    private String nameAr;
    @SerializedName("DescriptionEn")
    @Expose
    private String descriptionEn;
    @SerializedName("DescriptionAr")
    @Expose
    private String descriptionAr;
    @SerializedName("Show")
    @Expose
    private Boolean show;
    @SerializedName("Order")
    @Expose
    private Integer order;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("UrlEn")
    @Expose
    private String urlEn;
    @SerializedName("UrlAr")
    @Expose
    private String urlAr;

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
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

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlEn() {
        return urlEn;
    }

    public void setUrlEn(String urlEn) {
        this.urlEn = urlEn;
    }

    public String getUrlAr() {
        return urlAr;
    }

    public void setUrlAr(String urlAr) {
        this.urlAr = urlAr;
    }

}
