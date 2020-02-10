package dm.sime.com.kharetati.datas.models;

import java.util.ArrayList;
import java.util.List;

public class ZZBookmarkModel {
    private List<ZZBookmark> lstBookmark;

    public ZZBookmarkModel(){

    }
    public List<ZZBookmark> getMyMapList(){
        lstBookmark = new ArrayList<>();
        populateMyMap();
        return lstBookmark;
    }

    private void populateMyMap(){
        ZZBookmark obj;
        for (int x = 0; x < lstBookmark.size(); x++){

            obj = new ZZBookmark();
            obj.setPlotNo(lstBookmark.get(x).getPlotNo());
            obj.setArea(lstBookmark.get(x).getArea());
            obj.setDate(lstBookmark.get(x).getDate());

            lstBookmark.add(obj);
        }
    }
}
