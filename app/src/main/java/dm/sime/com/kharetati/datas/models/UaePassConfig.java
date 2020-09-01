package dm.sime.com.kharetati.datas.models;

public class UaePassConfig {
    public String getAuthCodeUAEID_url;
    public String getAccessTokenUAEID_url;
    public String UAEID_callback_url;
    public String UAEID_clientid;
    public String UAEID_secret;
    public String UAE_PASS_ENVIRONMENT;
    public boolean disableMyId;
    public boolean hideDeliveryDetails;

    public boolean isHideDeliveryDetails() {
        return hideDeliveryDetails;
    }

    public void setHideDeliveryDetails(boolean hideDeliveryDetails) {
        this.hideDeliveryDetails = hideDeliveryDetails;
    }

    public String UAE_PASS_SCOPE;
    public String UAE_PASS_ACR_VALUES_WEBVIEW;
    public String UAE_PASS_ACR_VALUES_MOBILE;
    public String auxiliaryServiceUrl;
    public String getAuxiliaryServiceUrl() {
        return auxiliaryServiceUrl;
    }

    public void setAuxiliaryServiceUrl(String auxiliaryServiceUrl) {
        this.auxiliaryServiceUrl = auxiliaryServiceUrl;
    }


    public String getUAE_PASS_SCOPE() {
        return UAE_PASS_SCOPE;
    }

    public void setUAE_PASS_SCOPE(String UAE_PASS_SCOPE) {
        this.UAE_PASS_SCOPE = UAE_PASS_SCOPE;
    }

    public String getUAE_PASS_ACR_VALUES_WEBVIEW() {
        return UAE_PASS_ACR_VALUES_WEBVIEW;
    }

    public void setUAE_PASS_ACR_VALUES_WEBVIEW(String UAE_PASS_ACR_VALUES_WEBVIEW) {
        this.UAE_PASS_ACR_VALUES_WEBVIEW = UAE_PASS_ACR_VALUES_WEBVIEW;
    }

    public String getUAE_PASS_ACR_VALUES_MOBILE() {
        return UAE_PASS_ACR_VALUES_MOBILE;
    }

    public void setUAE_PASS_ACR_VALUES_MOBILE(String UAE_PASS_ACR_VALUES_MOBILE) {
        this.UAE_PASS_ACR_VALUES_MOBILE = UAE_PASS_ACR_VALUES_MOBILE;
    }

    public String getUAE_PASS_ENVIRONMENT() {
        return UAE_PASS_ENVIRONMENT;
    }

    public void setUAE_PASS_ENVIRONMENT(String UAE_PASS_ENVIRONMENT) {
        this.UAE_PASS_ENVIRONMENT = UAE_PASS_ENVIRONMENT;
    }

    public boolean isDisableMyId() {
        return disableMyId;
    }

    public String getGetAuthCodeUAEID_url() {
        return getAuthCodeUAEID_url;
    }

    public void setGetAuthCodeUAEID_url(String getAuthCodeUAEID_url) {
        this.getAuthCodeUAEID_url = getAuthCodeUAEID_url;
    }

    public String getGetAccessTokenUAEID_url() {
        return getAccessTokenUAEID_url;
    }

    public void setGetAccessTokenUAEID_url(String getAccessTokenUAEID_url) {
        this.getAccessTokenUAEID_url = getAccessTokenUAEID_url;
    }

    public String getUAEID_callback_url() {
        return UAEID_callback_url;
    }

    public void setUAEID_callback_url(String UAEID_callback_url) {
        this.UAEID_callback_url = UAEID_callback_url;
    }

    public String getUAEID_clientid() {
        return UAEID_clientid;
    }

    public void setUAEID_clientid(String UAEID_clientid) {
        this.UAEID_clientid = UAEID_clientid;
    }

    public String getUAEID_secret() {
        return UAEID_secret;
    }

    public void setUAEID_secret(String UAEID_secret) {
        this.UAEID_secret = UAEID_secret;
    }

    public boolean getDisableMyId() {
        return disableMyId;
    }

    public void setDisableMyId(boolean disableMyId) {
        this.disableMyId = disableMyId;
    }
}
