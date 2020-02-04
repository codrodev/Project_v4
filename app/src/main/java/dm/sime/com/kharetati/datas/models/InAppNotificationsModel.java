package dm.sime.com.kharetati.datas.models;

import java.util.ArrayList;
import java.util.List;

public class InAppNotificationsModel {
    private List<InAppNotifications> lstNotifications = new ArrayList<InAppNotifications>();

    public InAppNotificationsModel(){

    }

    public List<InAppNotifications> getLstInAppNotifications () {
        populateInAppNotifications();
        return lstNotifications;
    }

    private void populateInAppNotifications(){
        InAppNotifications notifications;
        for (int i = 0; i < 5; i++){
            notifications = new InAppNotifications();
            notifications.setSubject("notify " + String.valueOf(i));
            notifications.setNotificationCode(i);
            notifications.setDate("13/01/2020");

            lstNotifications.add(notifications);
        }
    }
}
