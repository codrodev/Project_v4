package dm.sime.com.kharetati.datas.models.models;

import androidx.databinding.BaseObservable;

public class InAppNotifications extends BaseObservable {

    private int notificationCode;
    private String subject;
    private String date;

    public int getNotificationCode() {
        return notificationCode;
    }

    public void setNotificationCode(int notificationCode) {
        this.notificationCode = notificationCode;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
