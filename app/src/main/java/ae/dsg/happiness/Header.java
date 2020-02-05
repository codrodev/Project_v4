package ae.dsg.happiness;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsg216 on 3/21/16.
 */
public class Header {


    private String timeStamp;
    private String serviceProvider;
    private String microApp;
    private String microAppDisplay;
    private String themeColor;


    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * Set UTC timestamp with format : dd/MM/yyyy HH:mm:ss
     * @param timeStamp
     * Required
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    /**
     * Set service provider e.g: DSG
     * @param serviceProvider
     * Required
     */
    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getMicroApp() {
        return microApp;
    }

    /**
     * Set the service name
     * @param microApp
     * Optional
     */
    public void setMicroApp(String microApp) {
        this.microApp = microApp;
    }

    public String getThemeColor() {
        return themeColor;
    }

    /**
     * Modify the theme color. Both hex and color values are acceptable e.g: red or #FFFFFF
     * @param themeColor
     * Optional
     */
    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }


    public String getMicroAppDisplay() {
        return microAppDisplay;
    }

    /**
     * Set the service name for display only. English or Arabic according to user language
     * @param microAppDisplay
     * Optional
     */

    public void setMicroAppDisplay(String microAppDisplay) {
        this.microAppDisplay = microAppDisplay;
    }

    public String toJson() throws JSONException {
        return this.jsonObject().toString();
    }


    public JSONObject jsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp",this.getTimeStamp());
        jsonObject.put("serviceProvider",this.getServiceProvider());
        jsonObject.put("themeColor",this.getThemeColor());
        jsonObject.put("microApp",this.getMicroApp());
        jsonObject.put("microAppDisplay",this.getMicroAppDisplay());
        return jsonObject;
    }

}
