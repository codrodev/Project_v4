package dm.sime.com.kharetati.datas.models;

import java.util.List;

public class Kharetati {
    private List<Applications> Applications;

    private Map Map;

    private Notifications[] Notifications;

    public List<Applications> getApplications ()
    {
        return Applications;
    }

    public void setApplications (List<Applications> Applications)
    {
        this.Applications = Applications;
    }

    public Map getMap ()
    {
        return Map;
    }

    public void setMap (Map Map)
    {
        this.Map = Map;
    }

    public Notifications[] getNotifications ()
    {
        return Notifications;
    }

    public void setNotifications (Notifications[] Notifications)
    {
        this.Notifications = Notifications;
    }
}
