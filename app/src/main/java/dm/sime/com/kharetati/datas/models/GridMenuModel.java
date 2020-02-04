package dm.sime.com.kharetati.datas.models;

import java.util.ArrayList;
import java.util.List;

public class GridMenuModel {
    private Kharetati kharetati;
    List<Applications> lstApplications;
    List<SearchForm> lstSearchForm;
    List<FunctionsOnMap> lstFunctionsOnMap;
    String[] arrayApp = {"Request Site Plan", "Makani Search", "Land Registration", "Land Registration","Land Registration","Land Registration","Land Registration","Land Registration","Land Registration","Land Registration","Land Registration","Land Registration","Makani Search", "Makani Search", "Makani Search", "Makani Search", "Makani Search", "Makani Search", "Makani Search", "Makani Search", "Makani Search","Makani Search"};
    String[] arrayFunctions = {"Request Site Plan", "Zoning Regulation", "Zoning Violation"};

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
            obj.setFunctionsOnMap(populateFunctionsOnMap());
            lstApplications.add(obj);
        }
        return lstApplications;
    }

    private List<SearchForm> populateSearchForm(){
        lstSearchForm = new ArrayList<>();
        for(int i =1; i < 4; i++){
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

    private List<FunctionsOnMap> populateFunctionsOnMap(){
        lstFunctionsOnMap = new ArrayList<>();
        for(int i =0; i < 3; i++){
            FunctionsOnMap form = new FunctionsOnMap();
            form.setNameEn(arrayFunctions[i]);
            form.setNameAr("Plot Number");
            form.setLaunchUrl("https://www.google.com");
            lstFunctionsOnMap.add(form);
        }
        return lstFunctionsOnMap;
    }
}
