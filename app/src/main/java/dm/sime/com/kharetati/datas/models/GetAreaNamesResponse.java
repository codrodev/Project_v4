package dm.sime.com.kharetati.datas.models;

public class GetAreaNamesResponse {
    private AreaResponse service_response;

    public AreaResponse getAreaResponse ()
    {
        return service_response;
    }

    public void setAreaResponse (AreaResponse service_response)
    {
        this.service_response = service_response;
    }
}
