package ae.dsg.happiness;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;



/**
 * Created by dsg216 on 3/21/16.
 */
public class VotingRequest {

    private Header header;
    private User user;
    private Application application;
    private Transaction transaction;
    private Map<String, String> additionalParams;

    public VotingRequest() {
    }

    public Header getHeader() {
        return header;
    }
    /**
     *
     * @return {@link User}
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user {@link User}
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return {@link Application}
     */
    public Application getApplication() {
        return application;
    }

    /**
     *
     * @param application {@link Application}
     */
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
     *
     * @return {@link Transaction}}
     */
    public Transaction getTransaction() {
        return transaction;
    }

    /**
     *
     * @param transaction {@link Transaction}}
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    /**
     *
     * @param header {@link Header}
     */
    public void setHeader(Header header) {
        this.header = header;
    }

    public Map<String, String> getAdditionalParams() {
        return additionalParams;
    }

    /**
     * Set additional parameters
     * @param additionalParams
     * Optional
     */
    public void setAdditionalParams(Map<String, String> additionalParams) {
        this.additionalParams = additionalParams;
    }

    /**
     * log object as json string
     * @return String
     */
    public String toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user",this.getUser().jsonObject());
        jsonObject.put("header",this.getHeader().jsonObject());
        if (this.getApplication() != null)
            jsonObject.put("application",this.getApplication().jsonObject());
        if (this.getTransaction() != null)
            jsonObject.put("transaction",this.getTransaction().jsonObject());
        return jsonObject.toString();
    }
}
