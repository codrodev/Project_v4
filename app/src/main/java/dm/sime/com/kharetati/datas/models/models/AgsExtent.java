package dm.sime.com.kharetati.datas.models.models;

import androidx.databinding.BaseObservable;

/**
 * Created by Imran on 8/19/2017.
 */

public class AgsExtent extends BaseObservable {
    public double xmin;
    public double ymin;
    public double xmax;
    public double ymax;
    public  AgsExtent(double xmin,double ymin,double xmax,double ymax)
    {
        this.xmin=xmin;
        this.xmax=xmax;
        this.ymin=ymin;
        this.ymax=ymax;
    }
}
