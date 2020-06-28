package dm.sime.com.kharetati.datas.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceResponse {

    @SerializedName("GeneralNotifications")
    @Expose
    private List<GeneralNotification> generalNotifications = null;

    public List<GeneralNotification> getGeneralNotifications() {
        return generalNotifications;
    }

    public void setGeneralNotifications(List<GeneralNotification> generalNotifications) {
        this.generalNotifications = generalNotifications;
    }

}
