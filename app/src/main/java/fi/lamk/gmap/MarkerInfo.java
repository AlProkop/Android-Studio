package fi.lamk.gmap;

import java.io.Serializable;

public class MarkerInfo implements Serializable {
    private String title;
    private Double lat;
    private Double lng;

    public MarkerInfo(String _title, Double _lat, Double _lng){
        title = _title;
        lat = _lat;
        lng = _lng;
    }
}
