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


        int imageId = 0;

        String pointType = type.toLowerCase();
        if (pointType.equals("port")) {
            imageId = R.drawable.ic_marker_port;
        } else if (pointType.equals("plage")) {
            imageId = R.drawable.ic_marker_plage;
        } else if (pointType.equals("mémorial") || pointType.equals("monument")
         || pointType.equals("musée")) {
            imageId = R.drawable.ic_marker_musume;
        } else if (pointType.equals("selected")) {
            imageId = R.drawable.ic_marker_selected;
        }else if(pointType.equals("mosquée")) {
            imageId = R.drawable.ic_marker_mosque;
        }else if (pointType.equals("basilique")) {
            imageId = R.drawable.ic_marker_basilique;
        }else if (pointType.equals("lieu historique") || pointType.equals("site historique")) {
            imageId = R.drawable.ic_marker_historique;
        }else if(pointType.equals("zoo")) {
            imageId = R.drawable.ic_marker_zoo;
        }else if(pointType.equals("parc")) {
            imageId = R.drawable.ic_marker_parc;
        }else if (pointType.equals("jardin")){
            imageId = R.drawable.ic_marker_jardin;
        } else {
            imageId = R.drawable.ic_marker_default;
        }

        return ((BitmapDrawable) context.getResources().getDrawable(imageId)).getBitmap();
    }

}
