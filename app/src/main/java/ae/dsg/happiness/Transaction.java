package ae.dsg.happiness;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsg216 on 3/21/16.
 */
public class Transaction {

    private String transactionID;
    private String gessEnabled;
    private String serviceCode;
    private String serviceDescription;
    private String channel;
    private String notes;

    public Transaction() {

    }

    public String getTransactionID() {
        return transactionID;
    }

    /**
     * Service Provider unique Transaction ID
     * @param transactionID
     * Required
     */

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getGessEnabled() {
        return gessEnabled;
    }

    /**
     * true or false based on if the Service is registered in the GESS System.
     * @param gessEnabled
     * Required
     */
    public void setGessEnabled(String gessEnabled) {
        this.gessEnabled = gessEnabled;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    /**
     * Service code (as registered in GESS System)
     * @param serviceCode
     * Optional
     */

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    /**
     * Service Description
     * @param serviceDescription
     * Required
     */

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getChannel() {
        return channel;
    }

    /**
     * Any of
     * WEB|SMARTAPP|KIOSK|IVR|SMS
     * @param channel
     * Required
     */

    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Customer Notes if Present
     * @param notes
     * Optional
     */

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public String toJson() throws JSONException {
        return this.jsonObject().toString();
    }

    public JSONObject jsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("transactionID",this.getTransactionID());
        jsonObject.put("gessEnabled",this.getGessEnabled());
        jsonObject.put("serviceCode",this.getServiceCode());
        jsonObject.put("serviceDescription",this.getServiceDescription());
        jsonObject.put("channel",this.getChannel());
        jsonObject.put("notes",this.getNotes());
        return jsonObject;
    }

}
