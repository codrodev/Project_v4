package dm.sime.com.kharetati.datas.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Imran on 3/5/2018.
 */

public class BuildingViolationResponse implements  java.io.Serializable{
    public boolean allowplan;
    public String allowplanmsgar;
    public String allowplanmsgen;
    public String status;
    public List<BuildingViolation> violationsArray=new ArrayList<>();
    public String[] violations;
}

