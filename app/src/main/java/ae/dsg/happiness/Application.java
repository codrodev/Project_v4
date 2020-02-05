package ae.dsg.happiness;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsg216 on 3/21/16.
 */
public class Application {


    private String applicationID;
    private String type;
    private String platform;
    private String url;
    private String version;
    private String notes;

    public Application(String applicationID, String url, String type, String platform) {
        this.setApplicationID(applicationID);
        this.setUrl(url);
        this.setType(type);
        this.setPlatform(platform);
    }

    public String getApplicationID() {
        return applicationID;
    }

    /**
     * Application ID, DSG to provide to Entity during Application Registration.
     * @param applicationID
     * Required
     */

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }


    /**
     * Any of
     WEBAPP|SMARTAPP|DESKTOP
     * @param type
     * Required
     */

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


    /**
     * Any of
     IOS|ANDROID|BLACKBERRY|WINDOWS |OTHERS
     * @param platform
     * Conditional - Filed Required when type is SMARTAPP
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatform() {
        return platform;
    }

    public String getUrl() {
        return url;
    }

    /**
     * Web application URL or Mobile Application Store URL.
     *Field is required when type is WEBAPP or SMARTAPP
     * @param url
     * Conditional - *Field is required when type is WEBAPP or SMARTAPP
     */

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    /**
     * Application version
     * @param applicationVersion
     * Required
     */


    public void setVersion(String applicationVersion) {
        this.version = applicationVersion;
    }


    public String getNotes() {
        return notes;
    }

    /**
     * Customer Notes if Present
     * @param notes
     * Optional
     */

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String toJson() throws JSONException {
        return this.jsonObject().toString();
    }

    public JSONObject jsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("applicationID",this.getApplicationID());
        jsonObject.put("type",this.getType());
        jsonObject.put("platform",this.getPlatform());
        jsonObject.put("url",this.getUrl());
        jsonObject.put("notes",this.getNotes());
        jsonObject.put("version",this.getVersion());
        return jsonObject;
    }
}
