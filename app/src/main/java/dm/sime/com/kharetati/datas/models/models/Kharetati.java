package dm.sime.com.kharetati.datas.models.models;

import java.util.List;

public class Kharetati {
    private List<dm.sime.com.kharetati.datas.models.Applications> Applications;

    private dm.sime.com.kharetati.datas.models.Map Map;

    private dm.sime.com.kharetati.datas.models.Notifications[] Notifications;

    public List<dm.sime.com.kharetati.datas.models.Applications> getApplications ()
    {
        return Applications;
    }

    public void setApplications (List<dm.sime.com.kharetati.datas.models.Applications> Applications)
    {
        this.Applications = Applications;
    }

    public dm.sime.com.kharetati.datas.models.Map getMap ()
    {
        return Map;
    }

    public void setMap (dm.sime.com.kharetati.datas.models.Map Map)
    {
        this.Map = Map;
    }

    public dm.sime.com.kharetati.datas.models.Notifications[] getNotifications ()
    {
        return Notifications;
    }

    public void setNotifications (dm.sime.com.kharetati.datas.models.Notifications[] Notifications)
    {
        this.Notifications = Notifications;
    }
}
