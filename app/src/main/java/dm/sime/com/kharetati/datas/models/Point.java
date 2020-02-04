package dm.sime.com.kharetati.datas.models;

import androidx.databinding.BaseObservable;

/**
 * Created by Imran on 8/19/2017.
 */

public class Point extends BaseObservable {
    public double x;
    public double y;

    public Point(double x,double y)
    {
        this.x=x;
        this.y=y;
    }
}
