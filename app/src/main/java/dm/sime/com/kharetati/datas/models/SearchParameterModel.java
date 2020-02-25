package dm.sime.com.kharetati.datas.models;

import com.google.gson.annotations.SerializedName;

public class SearchParameterModel {
    @SerializedName("inputJson")
    private SearchParameterInput inputJson;

    public SearchParameterInput getInputJson() {
        return inputJson;
    }

    public void setInputJson(SearchParameterInput inputJson) {
        this.inputJson = inputJson;
    }
}
