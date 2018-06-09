package com.algeriatour.map.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import com.algeriatour.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapUtils {


    static public String getRequestUrl(LatLng origin, LatLng dest) {
        //Value of origin
        String str_org = "origin=" + origin.latitude + "," + origin.longitude;
        //Value of destination
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String mode = "mode=driving";
        //Build the full param
        String param = str_org + "&" + str_dest + "&" + sensor + "&" + mode;
        //Output format
        String output = "json";
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
    }

    static public PolylineOptions convertToPolyLineOption(List<List<HashMap<String, String>>> lists) {
        PolylineOptions polylineOptions = null;
        ArrayList points = null;
        for (List<HashMap<String, String>> path : lists) {
            points = new ArrayList();
            polylineOptions = new PolylineOptions();

            for (HashMap<String, String> point : path) {
                double lat = Double.parseDouble(point.get("lat"));
                double lon = Double.parseDouble(point.get("lon"));

                points.add(new LatLng(lat, lon));
            }

            polylineOptions.addAll(points);
            polylineOptions.color(Color.GRAY);
            polylineOptions.width(8);
        }
        return polylineOptions;
    }

    static public Bitmap getIcon(Context context, String type) {

        int height = 85;
        int width = 55;
        int imageId = 0;
        if(type.toLowerCase().equals("port")){
            imageId = R.drawable.ic_marker_port;
        }else if(type.toLowerCase().equals("plage")) {
            imageId = R.drawable.ic_marker_plage;
        }else if(type.toLowerCase().equals("mémorial")){
            imageId = R.drawable.ic_marker_musume;
        }else{
            imageId = R.drawable.ic_marker_default;
        }

        return ((BitmapDrawable) context.getResources().getDrawable(imageId)).getBitmap();
    }

}
