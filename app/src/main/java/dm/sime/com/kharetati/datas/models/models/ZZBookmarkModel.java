package dm.sime.com.kharetati.datas.models.models;

import java.util.ArrayList;
import java.util.List;

import dm.sime.com.kharetati.datas.models.ZZBookmark;

public class ZZBookmarkModel {
    private List<ZZBookmark> lstMyMap;

    public ZZBookmarkModel(){

    }
    public List<ZZBookmark> getMyMapList(){
        lstMyMap = new ArrayList<>();
        populateMyMap();
        return lstMyMap;
    }

    private void populateMyMap(){
        ZZBookmark obj;
        for (int x = 0; x < 10; x++){
            obj = new ZZBookmark();

            obj.setPlotNo(String.valueOf(11120 + x));
            obj.setArea("area" + String.valueOf(11120 + x));
            obj.setDate("12/05/2020");

            lstMyMap.add(obj);
        }
    }
}
