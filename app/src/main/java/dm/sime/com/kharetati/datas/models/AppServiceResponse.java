package dm.sime.com.kharetati.datas.models;

import java.util.List;

public class AppServiceResponse {

    private List<Applications> Applications;

    public List<Applications> getApplications ()
    {
        return Applications;
    }

    public void setApplications (List<Applications> Applications)
    {
        this.Applications = Applications;
    }

}
