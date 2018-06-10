package com.algeriatour.map.other;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import java.util.concurrent.ExecutionException;

public class SelectedMarker {
    private Marker marker;
    private Bitmap defaultBitmap;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Bitmap getDefaultBitmap() {
        return defaultBitmap;
    }

    public void setDefaultBitmap(Bitmap defaultBitmap) {
        this.defaultBitmap = defaultBitmap;
    }

    public void resetIcon(){
        try {
             marker.setIcon(BitmapDescriptorFactory.fromBitmap(defaultBitmap));
        }catch (Exception exp){
            exp.getStackTrace();
        }
    }
}
