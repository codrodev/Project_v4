package ae.dsg.happiness;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dsg216 on 3/21/16.
 */
public class User {

    private String source;
    private String username;
    private String email;
    private String mobile;


    public User() {
        this.setSource("ANONYMOUS");
    }

    public String getSource() {
        return source;
    }

    /**
     * Any of
     *LOCAL | MYID | ANONYMOUS
     *Where LOCAL to be used with Departments Local User Profile.
     * @param source
     * Required
     */
    public void setSource(String source) {
        this.source = source;
    }

    public String getUserName() {
        return username;
    }

    /**
     * Username in case available
     * @param userName
     * Conditional - username is required when source is LOCAL
     */

    public void setUserName(String userName) {
        this.username = userName;
    }
    public String getEmail() {
        return email;
    }

    /**
     * User Email in case available.
     * @param email
     * Optional
     */

    public void setEmail(String email) {
        this.email = email;
    }
    public String getMobile() {
        return mobile;
    }

    /**
     * User Mobile in case available
     *Format “9715X XXXXXXX”
     * @param mobile
     * Optional
     */

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String toJson() throws JSONException {
        return this.jsonObject().toString();
    }

    public JSONObject jsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("source",this.getSource());
        jsonObject.put("username",this.getUserName());
        jsonObject.put("email",this.getEmail());
        jsonObject.put("mobile",this.getMobile());
        return jsonObject;
    }
}
