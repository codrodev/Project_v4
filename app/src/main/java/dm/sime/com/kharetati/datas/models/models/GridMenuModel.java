package dm.sime.com.kharetati.datas.models.models;

import java.util.ArrayList;
import java.util.List;

import dm.sime.com.kharetati.datas.models.Applications;
import dm.sime.com.kharetati.datas.models.Kharetati;
import dm.sime.com.kharetati.datas.models.SearchForm;

public class GridMenuModel {
    private Kharetati kharetati;
    List<Applications> lstApplications;
    List<SearchForm> lstSearchForm;
    String[] arrayApp = {"Request Site Plan", "Makani Search", "Land Registration", "Land Registration","Land Registration","Land Registration","Land Registration","Land Registration","Land Registration","Land Registration","Land Registration","Land Registration","Makani Search", "Makani Search", "Makani Search", "Makani Search", "Makani Search", "Makani Search", "Makani Search", "Makani Search", "Makani Search","Makani Search"};

    public Kharetati getKharetati ()
    {
        kharetati = new Kharetati();
        kharetati.setApplications(populateApplication());
        return kharetati;
    }

    public void setKharetati (Kharetati kharetati)
    {
        this.kharetati = kharetati;
    }

    private List<Applications> populateApplication(){
        lstApplications = new ArrayList<>();
        for (int i =1; i<14; i++) {
            Applications obj = new Applications();
            obj.setId(String.valueOf(i));
            obj.setNameEn(arrayApp[i - 1]);
            if(i == 2) {
                obj.setIsNative("0");
                obj.setLaunchUrl("https://www.google.com");
            } else {
                obj.setIsNative("1");
            }
            obj.setSearchForm(populateSearchForm());
            lstApplications.add(obj);
        }
        return lstApplications;
    }

    private List<SearchForm> populateSearchForm(){
        lstSearchForm = new ArrayList<>();
        for(int i =1; i < 3; i++){
            SearchForm form = new SearchForm();
            form.setId(String.valueOf(i));
            form.setNameEn("Plot Number");
            form.setType("text");
            form.setPlaceHolderEn("Enter your Plot Number");
            form.setDescriptionEn("This input is required for requesting the site plan");
            form.setIsMandatory("1");
            lstSearchForm.add(form);
        }
        return lstSearchForm;
    }
}
