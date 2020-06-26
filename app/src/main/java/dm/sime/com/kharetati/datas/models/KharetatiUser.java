package dm.sime.com.kharetati.datas.models;

import androidx.databinding.BaseObservable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hasham on 8/27/2017.
 */

public class KharetatiUser extends BaseObservable {
  public int userID;
  public String deviceID;
  public String EmirateID;
  public String Mobile;

  public boolean isError;
  public String message;
  public String arcgis_token;

  public String gis_token_url;
  public String gis_user_name;
  public String gis_pwd;
  public String community_layerid;
  public String url_plotfinder;
  public String gis_layer_url;
  public String parcelLayerExportUrl_en;
  public String parcelLayerExportUrl_ar;
  public String plot_layerid;
  public String access_token;
  public String aboutus_ar_url;
  public String aboutus_en_url;
  public String terms_ar_url;
  public String terms_en_url;
  @SerializedName("happiness_url")
  @Expose
  private String happinessUrl;
  @SerializedName("happiness_secretkey")
  @Expose
  private String happinessSecretkey;

  public String getHappinessUrl() {
    return happinessUrl;
  }

  public void setHappinessUrl(String happinessUrl) {
    this.happinessUrl = happinessUrl;
  }

  public String getHappinessSecretkey() {
    return happinessSecretkey;
  }

  public void setHappinessSecretkey(String happinessSecretkey) {
    this.happinessSecretkey = happinessSecretkey;
  }

  public String getHappinessServiceprovider() {
    return happinessServiceprovider;
  }

  public void setHappinessServiceprovider(String happinessServiceprovider) {
    this.happinessServiceprovider = happinessServiceprovider;
  }

  public String getHappinessClientid() {
    return happinessClientid;
  }

  public void setHappinessClientid(String happinessClientid) {
    this.happinessClientid = happinessClientid;
  }

  public String getHappinessServicecode() {
    return happinessServicecode;
  }

  public void setHappinessServicecode(String happinessServicecode) {
    this.happinessServicecode = happinessServicecode;
  }

  @SerializedName("happiness_serviceprovider")
  @Expose
  private String happinessServiceprovider;
  @SerializedName("happiness_clientid")
  @Expose
  private String happinessClientid;
  @SerializedName("happiness_servicecode")
  @Expose
  private String happinessServicecode;
  @SerializedName("faq_url")
  @Expose
  private String faq_url;
  @SerializedName("map_hidden_layers")
  @Expose
  private String[] mapHiddenLayers;

  public String[] getMapHiddenLayers() {
    return mapHiddenLayers;
  }

  public void setMapHiddenLayers(String[] mapHiddenLayers) {
    this.mapHiddenLayers = mapHiddenLayers;
  }

  public String getFaq_url() {
    return faq_url;
  }

  public void setFaq_url(String faq_url) {
    this.faq_url = faq_url;
  }

  @SerializedName("plot_dim_layer_id")
  @Expose
  private String plotDimLayerId;
  @SerializedName("plot_highlight_layer_id")
  @Expose
  private String plotHighlightLayerId;
  @SerializedName("plot_layer_parcel_attr_name")
  @Expose
  private String plotLayerParcelAttrName;
  @SerializedName("plot_img_layer_id")
  @Expose
  private String plotImgLayerId;
  @SerializedName("plot_dim_layer_parcel_attr_name")
  @Expose
  private String plotDimLayerParcelAttrName;

  public String getPlotDimLayerId() {
    return plotDimLayerId;
  }

  public void setPlotDimLayerId(String plotDimLayerId) {
    this.plotDimLayerId = plotDimLayerId;
  }

  public String getPlotHighlightLayerId() {
    return plotHighlightLayerId;
  }

  public void setPlotHighlightLayerId(String plotHighlightLayerId) {
    this.plotHighlightLayerId = plotHighlightLayerId;
  }

  public String getPlotLayerParcelAttrName() {
    return plotLayerParcelAttrName;
  }

  public void setPlotLayerParcelAttrName(String plotLayerParcelAttrName) {
    this.plotLayerParcelAttrName = plotLayerParcelAttrName;
  }

  public String getPlotImgLayerId() {
    return plotImgLayerId;
  }

  public void setPlotImgLayerId(String plotImgLayerId) {
    this.plotImgLayerId = plotImgLayerId;
  }

  public String getPlotDimLayerParcelAttrName() {
    return plotDimLayerParcelAttrName;
  }

  public void setPlotDimLayerParcelAttrName(String plotDimLayerParcelAttrName) {
    this.plotDimLayerParcelAttrName = plotDimLayerParcelAttrName;
  }

  public AppMsg appMsg;

  public String auxiliaryServiceUrl;

  public String bookmarks_en_url;

  public String bookmarks_ar_url;

  public String mymaps_en_url;

  public String mymaps_ar_url;

  public String home_en_url;

  public String home_ar_url;

  public String map_en_url;

  public String map_ar_url;

  public String getMap_en_url() {
    return map_en_url;
  }

  public void setMap_en_url(String map_en_url) {
    this.map_en_url = map_en_url;
  }

  public String getMap_ar_url() {
    return map_ar_url;
  }

  public void setMap_ar_url(String map_ar_url) {
    this.map_ar_url = map_ar_url;
  }

  public String getAuxiliaryServiceUrl() {
    return auxiliaryServiceUrl;
  }

  public void setAuxiliaryServiceUrl(String auxiliaryServiceUrl) {
    this.auxiliaryServiceUrl = auxiliaryServiceUrl;
  }

  public String getBookmarks_en_url() {
    return bookmarks_en_url;
  }

  public void setBookmarks_en_url(String bookmarks_en_url) {
    this.bookmarks_en_url = bookmarks_en_url;
  }

  public String getBookmarks_ar_url() {
    return bookmarks_ar_url;
  }

  public void setBookmarks_ar_url(String bookmarks_ar_url) {
    this.bookmarks_ar_url = bookmarks_ar_url;
  }

  public String getMymaps_en_url() {
    return mymaps_en_url;
  }

  public void setMymaps_en_url(String mymaps_en_url) {
    this.mymaps_en_url = mymaps_en_url;
  }

  public String getMymaps_ar_url() {
    return mymaps_ar_url;
  }

  public void setMymaps_ar_url(String mymaps_ar_url) {
    this.mymaps_ar_url = mymaps_ar_url;
  }

  public String getHome_en_url() {
    return home_en_url;
  }

  public void setHome_en_url(String home_en_url) {
    this.home_en_url = home_en_url;
  }

  public String getHome_ar_url() {
    return home_ar_url;
  }

  public void setHome_ar_url(String home_ar_url) {
    this.home_ar_url = home_ar_url;
  }

  public AppMsg getAppMsg() {
    return appMsg;
  }

  public void setAppMsg(AppMsg appMsg) {
    this.appMsg = appMsg;
  }

  public String getAboutus_ar_url() {
    return aboutus_ar_url;
  }

  public void setAboutus_ar_url(String aboutus_ar_url) {
    this.aboutus_ar_url = aboutus_ar_url;
  }

  public String getAboutus_en_url() {
    return aboutus_en_url;
  }

  public void setAboutus_en_url(String aboutus_en_url) {
    this.aboutus_en_url = aboutus_en_url;
  }

  public String getTerms_ar_url() {
    return terms_ar_url;
  }

  public void setTerms_ar_url(String terms_ar_url) {
    this.terms_ar_url = terms_ar_url;
  }

  public String getTerms_en_url() {
    return terms_en_url;
  }

  public void setTerms_en_url(String terms_en_url) {
    this.terms_en_url = terms_en_url;
  }

  public String getArcgis_token() {
    return arcgis_token;
  }

  public void setArcgis_token(String arcgis_token) {
    this.arcgis_token = arcgis_token;
  }

  public boolean isError() {
    return isError;
  }

  public void setError(boolean error) {
    isError = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


  public int getUserID() {
    return userID;
  }

  public void setUserID(int userID) {
    this.userID = userID;
  }

  public String getDeviceID() {
    return deviceID;
  }

  public void setDeviceID(String deviceID) {
    this.deviceID = deviceID;
  }

  public String getEmirateID() {
    return EmirateID;
  }

  public void setEmirateID(String emirateID) {
    EmirateID = emirateID;
  }

  public String getMobile() {
    return Mobile;
  }

  public void setMobile(String mobile) {
    Mobile = mobile;
  }



}
