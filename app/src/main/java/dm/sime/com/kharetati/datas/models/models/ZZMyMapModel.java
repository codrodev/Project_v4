package dm.sime.com.kharetati.datas.models.models;

import java.util.ArrayList;
import java.util.List;

public class ZZMyMapModel {
    private List<ZZMyMap> lstMyMap;

    public ZZMyMapModel(){

    }
    public List<ZZMyMap> getMyMapList(){
        lstMyMap = new ArrayList<>();
        populateMyMap();
        return lstMyMap;
    }

    private void populateMyMap(){
        ZZMyMap obj;
        for (int x = 0; x < 10; x++){
            obj = new ZZMyMap();

            obj.setPlotNo(String.valueOf(11120 + x));
            obj.setArea("area" + String.valueOf(x));
            obj.setDate("12/05/2020");

            lstMyMap.add(obj);
        }
    }
}
