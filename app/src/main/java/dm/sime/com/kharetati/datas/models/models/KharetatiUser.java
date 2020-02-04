package dm.sime.com.kharetati.datas.models.models;

import androidx.databinding.BaseObservable;

import dm.sime.com.kharetati.datas.models.AppMsg;

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
  public AppMsg appMsg;

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
