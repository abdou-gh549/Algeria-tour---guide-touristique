package com.algeriatour.map.other;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

public class MapRoute {
    private Polyline polyline;
    private LatLng origin;
    private LatLng destination;
    private Marker marker;

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        // 0.00002
        // change position for make originale marker clickable
        LatLng latLng = marker.getPosition();
        double latitude = latLng.latitude - 0.00003;
        double longitude = latLng.longitude - 0.00002;
        marker.setPosition( new LatLng(latitude, longitude));
        this.marker = marker;
    }
}
