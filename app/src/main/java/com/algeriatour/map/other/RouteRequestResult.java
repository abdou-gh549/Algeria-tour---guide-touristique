package com.algeriatour.map.other;

import com.google.android.gms.maps.model.LatLng;

public class RouteRequestResult {
    private LatLng destination;
    private LatLng origin;
    private String requestResult;

    public RouteRequestResult(LatLng destination, LatLng origin, String requestResult) {
        this.destination = destination;
        this.origin = origin;
        this.requestResult = requestResult;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public LatLng getOrigin() {
        return origin;
    }

    public void setOrigin(LatLng origin) {
        this.origin = origin;
    }

    public String getRequestResult() {
        return requestResult;
    }

    public void setRequestResult(String requestResult) {
        this.requestResult = requestResult;
    }
}
