package dm.sime.com.kharetati.datas.models;

public class Dimension {
    private String ColumnName;

    private String ColumnType;

    private String LayerId;

    private String LayerName;

    public String getColumnName ()
    {
        return ColumnName;
    }

    public void setColumnName (String ColumnName)
    {
        this.ColumnName = ColumnName;
    }

    public String getColumnType ()
    {
        return ColumnType;
    }

    public void setColumnType (String ColumnType)
    {
        this.ColumnType = ColumnType;
    }

    public String getLayerId ()
    {
        return LayerId;
    }

    public void setLayerId (String LayerId)
    {
        this.LayerId = LayerId;
    }

    public String getLayerName ()
    {
        return LayerName;
    }

    public void setLayerName (String LayerName)
    {
        this.LayerName = LayerName;
    }
}
