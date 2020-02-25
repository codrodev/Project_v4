package dm.sime.com.kharetati.datas.models;

import java.util.List;

public class Applications {
    private String SearchUrl;

    private String IsNative;

    private String IsDisabled;

    private boolean HasMap;

    private String IconBase64;

    private String DescAr;

    private String IconUrl;

    private String DescEn;

    private String IsPublic;

    private String NameEn;

    private String Id;

    private List<SearchForm> SearchForm;

    private String NameAr;

    public String getSearchUrl() {
        return SearchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        SearchUrl = searchUrl;
    }

    public String getIsNative() {
        return IsNative;
    }

    public void setIsNative(String isNative) {
        IsNative = isNative;
    }

    public String getIsDisabled() {
        return IsDisabled;
    }

    public void setIsDisabled(String isDisabled) {
        IsDisabled = isDisabled;
    }

    public boolean getHasMap() {
        return HasMap;
    }

    public void setHasMap(boolean hasMap) {
        HasMap = hasMap;
    }

    public String getIconBase64() {
        return IconBase64;
    }

    public void setIconBase64(String iconBase64) {
        IconBase64 = iconBase64;
    }

    public String getDescAr() {
        return DescAr;
    }

    public void setDescAr(String descAr) {
        DescAr = descAr;
    }

    public String getIconUrl() {
        return IconUrl;
    }

    public void setIconUrl(String iconUrl) {
        IconUrl = iconUrl;
    }

    public String getDescEn() {
        return DescEn;
    }

    public void setDescEn(String descEn) {
        DescEn = descEn;
    }

    public String getIsPublic() {
        return IsPublic;
    }

    public void setIsPublic(String isPublic) {
        IsPublic = isPublic;
    }

    public String getNameEn() {
        return NameEn;
    }

    public void setNameEn(String nameEn) {
        NameEn = nameEn;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public List<SearchForm> getSearchForm() {
        return SearchForm;
    }

    public void setSearchForm(List<SearchForm> searchForm) {
        SearchForm = searchForm;
    }

    public String getNameAr() {
        return NameAr;
    }

    public void setNameAr(String nameAr) {
        NameAr = nameAr;
    }
}
