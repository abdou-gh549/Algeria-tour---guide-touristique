package com.algeriatour.map.other;

import com.algeriatour.uml_class.PlaceInfo;
import com.algeriatour.uml_class.PointInteret;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MapClusterMarkerItem implements ClusterItem {
    private LatLng position;
    private String title;
    private String type;

    public MapClusterMarkerItem(LatLng position, String title, String type) {
        this.position = position;
        this.title = title;
        this.type = type;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return null;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

