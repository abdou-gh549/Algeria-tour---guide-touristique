package com.algeriatour.map.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.algeriatour.R;
import com.algeriatour.map.other.CustomClusterRendered;
import com.algeriatour.map.other.MapClusterMarkerItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Locale;

public abstract class MapBaseActivity extends FragmentActivity implements OnMapReadyCallback {
    protected GoogleMap mMap;
    protected ClusterManager<MapClusterMarkerItem> mClusterManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        createGoogleMap();
    }

    protected int getLayoutId() {
        return R.layout.map_activity;
    }

    private void createGoogleMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    protected void setupMap(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap == null) {
            return;
        }
        setUpMapAlgeriaRestriction();
        setUpLang();


        mMap.setBuildingsEnabled(true);
        mMap.setMyLocationEnabled(false);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        UiSettings mUiSettings = mMap.getUiSettings();
        mUiSettings.setMapToolbarEnabled(false);
        mUiSettings.setMyLocationButtonEnabled(false);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);

        // add my marker
    }

    private void setUpMapAlgeriaRestriction() {
        // Add a marker in Sydney and move the camera
        LatLng one = new LatLng(19.569437, 11.297020);
        LatLng two = new LatLng(37.09, -8.563025);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //add them to builder
        builder.include(one);
        builder.include(two);

        LatLngBounds bounds = builder.build();

        //get width and height to current display screen
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        // 5% padding
        int padding = (int) (width * 0.15);

        //set latlong bounds
        mMap.setLatLngBoundsForCameraTarget(bounds);

        //move camera to fill the bound to screen
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

        //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds
        mMap.setMinZoomPreference(mMap.getCameraPosition().zoom + 0.4f);
    }

    private void setUpLang() {
        String languageToLoad = "fr_FR";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }


}