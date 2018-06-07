package com.algeriatour.map.other;

import com.algeriatour.uml_class.PlaceInfo;
import com.algeriatour.uml_class.PointInteret;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MapClusterMarkerItem implements ClusterItem {
    LatLng position;
    String title;

    public MapClusterMarkerItem(LatLng position, String title) {
        this.position = position;
        this.title = title;
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


}

