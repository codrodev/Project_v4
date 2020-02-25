package dm.sime.com.kharetati.datas.models;

import java.util.List;

public class Tabs {

    private String NameEn;

    private List<Controls> Controls;

    private String Id;

    private String NameAr;

    public String getNameEn() {
        return NameEn;
    }

    public void setNameEn(String nameEn) {
        NameEn = nameEn;
    }

    public List<Controls> getControls() {
        return Controls;
    }

    public void setControls(List<Controls> controls) {
        Controls = controls;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNameAr() {
        return NameAr;
    }

    public void setNameAr(String nameAr) {
        NameAr = nameAr;
    }
}
