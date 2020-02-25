package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.SerializedName;

public class SearchParameterInput {
    @SerializedName("ApplicationId")
    private String ApplicationId;

    @SerializedName("SearchValue")
    private String SearchValue;

    @SerializedName("TabId")
    private String TabId;

    @SerializedName("TOKEN")
    private String TOKEN;

    @SerializedName("REMARKS")
    private String REMARKS;

    public String getApplicationId() {
        return ApplicationId;
    }

    public void setApplicationId(String applicationId) {
        ApplicationId = applicationId;
    }

    public String getSearchValue() {
        return SearchValue;
    }

    public void setSearchValue(String searchValue) {
        SearchValue = searchValue;
    }

    public String getTabId() {
        return TabId;
    }

    public void setTabId(String tabId) {
        TabId = tabId;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }
}
