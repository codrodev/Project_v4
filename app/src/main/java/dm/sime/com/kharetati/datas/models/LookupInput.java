package dm.sime.com.kharetati.datas.models;

public class LookupInput {

    private String REMARKS;

    private String LkpValue;

    private String LkpId;

    private String TOKEN;

    private boolean isGuest;

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    public String getREMARKS ()
    {
        return REMARKS;
    }

    public void setREMARKS (String REMARKS)
    {
        this.REMARKS = REMARKS;
    }

    public String getLkpValue ()
    {
        return LkpValue;
    }

    public void setLkpValue (String LkpValue)
    {
        this.LkpValue = LkpValue;
    }

    public String getLkpId ()
    {
        return LkpId;
    }

    public void setLkpId (String LkpId)
    {
        this.LkpId = LkpId;
    }

    public String getTOKEN ()
    {
        return TOKEN;
    }

    public void setTOKEN (String TOKEN)
    {
        this.TOKEN = TOKEN;
    }
}
