package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessTokenResponse {

  @SerializedName("gis_token_url")
  @Expose
  private String gisTokenUrl;
  @SerializedName("gis_user_name")
  @Expose
  private String gisUserName;
  @SerializedName("gis_pwd")
  @Expose
  private String gisPwd;
  @SerializedName("community_layerid")
  @Expose
  private String communityLayerid;

  @SerializedName("plot_layerid")
  @Expose
  private String plotLayerid;
  @SerializedName("gis_layer_url")
  @Expose
  private String gisLayerUrl;
  @SerializedName("scope")
  @Expose
  private String scope;
  @SerializedName("token_type")
  @Expose
  private String tokenType;
  @SerializedName("refresh_token")
  @Expose
  private String refreshToken;
  @SerializedName("access_token")
  @Expose
  private String accessToken;
  @SerializedName("error")
  @Expose
  private Object error;
  @SerializedName("error_description")
  @Expose
  private Object errorDescription;

  @SerializedName("noctemplateUrl")
  @Expose
  private String noctemplateUrl;
  @SerializedName("forceUserToUpdateBuild")
  @Expose
  private Boolean forceUserToUpdateBuild;
  @SerializedName("forceUserToUpdateBuild_msg_en")
  @Expose
  private String forceUserToUpdateBuildMsgEn;
  @SerializedName("forceUserToUpdateBuild_msg_ar")
  @Expose
  private String forceUserToUpdateBuildMsgAr;
  @SerializedName("CurrentAndroidVersion")
  @Expose
  private String currentAndroidVersion;
  @SerializedName("baseurl_smartsiteplanWs")
  @Expose
  private String baseurlSmartsiteplanWs;
  @SerializedName("smartsiteplanWs_token")
  @Expose
  private String smartsiteplanWsToken;
  @SerializedName("show_landreg_in_menu")
  @Expose
  private Boolean showLandregInMenu;
  @SerializedName("show_landreg_popup")
  @Expose
  private Boolean showLandregPopup;
  @SerializedName("landreg_popup_msg_en")
  @Expose
  private String landregPopupMsgEn;
  @SerializedName("landreg_popup_msg_ar")
  @Expose
  private String landregPopupMsgAr;
  @SerializedName("landreg_popup_msg_heading_en")
  @Expose
  private String landregPopupMsgHeadingEn;
  @SerializedName("landreg_popup_msg_heading_ar")
  @Expose
  private String landregPopupMsgHeadingAr;
  @SerializedName("landreg_url")
  @Expose
  private String landregUrl;
  @SerializedName("aboutus_ar_url")
  @Expose
  private String aboutusArUrl;
  @SerializedName("aboutus_en_url")
  @Expose
  private String aboutusEnUrl;
  @SerializedName("terms_ar_url")
  @Expose
  private String termsArUrl;
  @SerializedName("terms_en_url")
  @Expose
  private String termsEnUrl;
  @SerializedName("appMsg")
  @Expose
  private AppMsg appMsg;
  @SerializedName("happiness_url")
  @Expose
  private String happinessUrl;
  @SerializedName("happiness_secretkey")
  @Expose
  private String happinessSecretkey;
  @SerializedName("happiness_serviceprovider")
  @Expose
  private String happinessServiceprovider;
  @SerializedName("happiness_clientid")
  @Expose
  private String happinessClientid;
  @SerializedName("happiness_servicecode")
  @Expose
  private String happinessServicecode;

  @SerializedName("auxiliaryServiceUrl")
  @Expose
  private String auxiliaryServiceUrl;


  @SerializedName("bookmarks_en_url")
  @Expose
  private String bookmarks_en_url;

  @SerializedName("bookmarks_ar_url")
  @Expose
  private String bookmarks_ar_url;

  @SerializedName("mymaps_en_url")
  @Expose
  private String mymaps_en_url;

  @SerializedName("mymaps_ar_url")
  @Expose
  private String mymaps_ar_url;

  @SerializedName("home_en_url")
  @Expose
  private String home_en_url;

  @SerializedName("home_ar_url")
  @Expose
  private String home_ar_url;

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

  public String getAuxiliaryServiceUrl() {
    return auxiliaryServiceUrl;
  }

  public void setAuxiliaryServiceUrl(String auxiliaryServiceUrl) {
    this.auxiliaryServiceUrl = auxiliaryServiceUrl;
  }

  public String getGisTokenUrl() {
    return gisTokenUrl;
  }

  public void setGisTokenUrl(String gisTokenUrl) {
    this.gisTokenUrl = gisTokenUrl;
  }

  public String getGisUserName() {
    return gisUserName;
  }

  public void setGisUserName(String gisUserName) {
    this.gisUserName = gisUserName;
  }

  public String getGisPwd() {
    return gisPwd;
  }

  public void setGisPwd(String gisPwd) {
    this.gisPwd = gisPwd;
  }

  public String getCommunityLayerid() {
    return communityLayerid;
  }

  public void setCommunityLayerid(String communityLayerid) {
    this.communityLayerid = communityLayerid;
  }



  public String getPlotLayerid() {
    return plotLayerid;
  }

  public void setPlotLayerid(String plotLayerid) {
    this.plotLayerid = plotLayerid;
  }

  public String getGisLayerUrl() {
    return gisLayerUrl;
  }

  public void setGisLayerUrl(String gisLayerUrl) {
    this.gisLayerUrl = gisLayerUrl;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getTokenType() {
    return tokenType;
  }

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Object getError() {
    return error;
  }

  public void setError(Object error) {
    this.error = error;
  }

  public Object getErrorDescription() {
    return errorDescription;
  }

  public void setErrorDescription(Object errorDescription) {
    this.errorDescription = errorDescription;
  }

  public String getNoctemplateUrl() {
    return noctemplateUrl;
  }

  public void setNoctemplateUrl(String noctemplateUrl) {
    this.noctemplateUrl = noctemplateUrl;
  }

  public Boolean getForceUserToUpdateBuild() {
    return forceUserToUpdateBuild;
  }

  public void setForceUserToUpdateBuild(Boolean forceUserToUpdateBuild) {
    this.forceUserToUpdateBuild = forceUserToUpdateBuild;
  }

  public String getForceUserToUpdateBuildMsgEn() {
    return forceUserToUpdateBuildMsgEn;
  }

  public void setForceUserToUpdateBuildMsgEn(String forceUserToUpdateBuildMsgEn) {
    this.forceUserToUpdateBuildMsgEn = forceUserToUpdateBuildMsgEn;
  }

  public String getForceUserToUpdateBuildMsgAr() {
    return forceUserToUpdateBuildMsgAr;
  }

  public void setForceUserToUpdateBuildMsgAr(String forceUserToUpdateBuildMsgAr) {
    this.forceUserToUpdateBuildMsgAr = forceUserToUpdateBuildMsgAr;
  }

  public String getCurrentAndroidVersion() {
    return currentAndroidVersion;
  }

  public void setCurrentAndroidVersion(String currentAndroidVersion) {
    this.currentAndroidVersion = currentAndroidVersion;
  }

  public String getBaseurlSmartsiteplanWs() {
    return baseurlSmartsiteplanWs;
  }

  public void setBaseurlSmartsiteplanWs(String baseurlSmartsiteplanWs) {
    this.baseurlSmartsiteplanWs = baseurlSmartsiteplanWs;
  }

  public String getSmartsiteplanWsToken() {
    return smartsiteplanWsToken;
  }

  public void setSmartsiteplanWsToken(String smartsiteplanWsToken) {
    this.smartsiteplanWsToken = smartsiteplanWsToken;
  }

  public Boolean getShowLandregInMenu() {
    return showLandregInMenu;
  }

  public void setShowLandregInMenu(Boolean showLandregInMenu) {
    this.showLandregInMenu = showLandregInMenu;
  }

  public Boolean getShowLandregPopup() {
    return showLandregPopup;
  }

  public void setShowLandregPopup(Boolean showLandregPopup) {
    this.showLandregPopup = showLandregPopup;
  }

  public String getLandregPopupMsgEn() {
    return landregPopupMsgEn;
  }

  public void setLandregPopupMsgEn(String landregPopupMsgEn) {
    this.landregPopupMsgEn = landregPopupMsgEn;
  }

  public String getLandregPopupMsgAr() {
    return landregPopupMsgAr;
  }

  public void setLandregPopupMsgAr(String landregPopupMsgAr) {
    this.landregPopupMsgAr = landregPopupMsgAr;
  }

  public String getLandregPopupMsgHeadingEn() {
    return landregPopupMsgHeadingEn;
  }

  public void setLandregPopupMsgHeadingEn(String landregPopupMsgHeadingEn) {
    this.landregPopupMsgHeadingEn = landregPopupMsgHeadingEn;
  }

  public String getLandregPopupMsgHeadingAr() {
    return landregPopupMsgHeadingAr;
  }

  public void setLandregPopupMsgHeadingAr(String landregPopupMsgHeadingAr) {
    this.landregPopupMsgHeadingAr = landregPopupMsgHeadingAr;
  }

  public String getLandregUrl() {
    return landregUrl;
  }

  public void setLandregUrl(String landregUrl) {
    this.landregUrl = landregUrl;
  }

  public String getAboutusArUrl() {
    return aboutusArUrl;
  }

  public void setAboutusArUrl(String aboutusArUrl) {
    this.aboutusArUrl = aboutusArUrl;
  }

  public String getAboutusEnUrl() {
    return aboutusEnUrl;
  }

  public void setAboutusEnUrl(String aboutusEnUrl) {
    this.aboutusEnUrl = aboutusEnUrl;
  }

  public String getTermsArUrl() {
    return termsArUrl;
  }

  public void setTermsArUrl(String termsArUrl) {
    this.termsArUrl = termsArUrl;
  }

  public String getTermsEnUrl() {
    return termsEnUrl;
  }

  public void setTermsEnUrl(String termsEnUrl) {
    this.termsEnUrl = termsEnUrl;
  }

  public AppMsg getAppMsg() {
    return appMsg;
  }

  public void setAppMsg(AppMsg appMsg) {
    this.appMsg = appMsg;
  }

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

}



