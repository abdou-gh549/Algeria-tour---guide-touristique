package com.algeriatour.map.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.algeriatour.R;
import com.algeriatour.map.other.CustomClusterRendered;
import com.algeriatour.map.other.MapClusterMarkerItem;
import com.algeriatour.map.other.MapUtils;
import com.algeriatour.point.PointIntereActivity;
import com.algeriatour.uml_class.PlaceInfo;
import com.algeriatour.utils.AlgeriaTourUtils;
import com.algeriatour.utils.StaticValue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class MapActivity extends MapBaseActivity implements
        MapConstraint.ViewConstraint,
        ClusterManager.OnClusterItemClickListener,
        ClusterManager.OnClusterClickListener<MapClusterMarkerItem>,
        GoogleMap.OnMapClickListener,
        MapPointViewDetaille.OnMapPointViewDetailleAction,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener

{

    private final String TAG = "tixx";

    private final int GPS_REQUEST = 199;

    @BindView(R.id.map_point_detaille)
    View pointDettailleView;

    @BindView(R.id.map_navigation_fab)
    FloatingActionButton longClickNavigationFab;

    @BindView(R.id.map_route_action_layout)
    View routeActionLayout;

    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    MapPointViewDetaille mapPointViewDetaille;
    MapPresenter mapPresenter;
    ArrayList<PlaceInfo> allPlaceInfo;
    SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        // check if gps enabled
        checkGpsStatus();

        progressDialog = new SpotsDialog(this);
        progressDialog.setCancelable(false);
        allPlaceInfo = new ArrayList<>();
        mapPresenter = new MapPresenter(this);
        mapPointViewDetaille = new MapPointViewDetaille(pointDettailleView, this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        setupMap(googleMap);
        setUpClusterManager();
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.getMarkerCollection().setOnMarkerClickListener(this);


        setUpLoaction();

        loadMapData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK && !googleApiClient.isConnected()) {
                googleApiClient.connect();
            }
        } else if (requestCode == GPS_REQUEST) {
            int locationMode = 0;
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            if (locationMode == Settings.Secure.LOCATION_MODE_OFF) {
                showErrorToast(getString(R.string.map_gps_off_error));
                finish();
            }

        }
    }

    @OnClick(R.id.map_my_location)
    void onMyLocationClick() {
        mapPresenter.onMyLocationClick();
    }

    // to long click marker
    @OnClick(R.id.map_navigation_fab)
    void onNavigationFabClicked() {
        mapPresenter.traceWayToLongClickMarker();
    }

    @OnClick(R.id.map_route_action_cancel)
    void onCancelRouteClick() {
        mapPresenter.cancelRoute();
    }

    @OnClick(R.id.map_route_action_fullView)
    void onFullRouteViewClick() {
        mapPresenter.onFullViewRoutClick();
    }

    @OnClick(R.id.map_route_action_refresh)
    void onRefreshRouteClicked() {
        mapPresenter.onRefreshRootClicked();
    }

    private void setUpClusterManager() {
        mClusterManager = new ClusterManager<>(this, mMap);
        CustomClusterRendered customClusterRendered = new CustomClusterRendered(this, mMap
                , mClusterManager, mapPresenter);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setRenderer(customClusterRendered);
        mClusterManager.setAnimation(true);
    }

    private void loadMapData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String source = bundle.getString(StaticValue.MAP_SOURCE_TAG, "");
            Log.d(TAG, "loadMapData: " + source);
            if (StaticValue.POINT.equals(source)) {
                PlaceInfo placeInfo = (PlaceInfo) getIntent().getSerializableExtra(StaticValue.POINT_TAG);
                Log.d(TAG, "loadMapData: place info = " + placeInfo.getLatLng().toString());
                addPointToMap(placeInfo);
                mapPointViewDetaille.setData(allPlaceInfo.get(0));
                showPointDetailleView();
                zoomeInto(placeInfo.getLatLng(), 15.5f);
                return;
            }
        }
        // else
        mapPresenter.loadAllPoint();
    }

    private int getPlaceInfoFromPosition(LatLng position) {
        for (int i = 0; i < allPlaceInfo.size(); i++) {
            if (allPlaceInfo.get(i).getLatitude() == position.latitude
                    && allPlaceInfo.get(i).getLongitude() == position.longitude) {
                return i;
            }
        }
        return -1;
    }

    private void setUpLoaction() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.open_gps_messge)
                .setCancelable(false)
                .setPositiveButton("Oui", (dialog, id) -> startActivityForResult(new Intent
                        (Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_REQUEST))
                .setNegativeButton("Non", (dialog, id) -> {
                    showErrorToast(AlgeriaTourUtils.getString(R.string.map_gps_off_error));
                    MapActivity.this.finish();
                    dialog.cancel();
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void checkGpsStatus() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager == null) {
            showErrorToast(getString(R.string.cant_get_location_msg));
            finish();
            return;
        }
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    @Override
    public void zoomeInto(final LatLng position, float zoom) {
        new Handler().postDelayed(() -> {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
        }, 300);
    }


    @Override
    public Polyline addPolyline(PolylineOptions polylineOptions) {
        return mMap.addPolyline(polylineOptions);
    }

    @Override
    public void showInfoToast(String msg) {
        Toasty.info(this, msg, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void showErrorToast(String msg) {
        Toasty.error(this, msg, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void showWarnningMessage(String msg) {
        Toasty.warning(this, msg, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void hideLongClickNavigationFab() {
        longClickNavigationFab.setVisibility(View.GONE);
    }

    @Override
    public void showLongClickNavigationFab() {
        longClickNavigationFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRouteActionLayout() {
        routeActionLayout.setVisibility(View.VISIBLE);
        mMap.setPadding(0, 0, 0, routeActionLayout.getHeight() + 5);
    }

    @Override
    public void hideRouteActionLayout() {
        routeActionLayout.setVisibility(View.GONE);
        mMap.setPadding(0, 0, 0, 0);
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.dismiss();
    }


    @Override
    public void showPointDetailleView() {
        mapPointViewDetaille.showMapPointViewDetaille();
        Log.d(TAG, "showPointDetailleView: view hight  = " + mapPointViewDetaille.getHeight());
        mMap.setPadding(0, 0, 0, mapPointViewDetaille.getHeight());
    }

    @Override
    public void hidePointDetailleView() {
        if (routeActionLayout.getVisibility() == View.VISIBLE) {
            mMap.setPadding(0, 0, 0, routeActionLayout.getHeight() + 5);
        } else {
            mMap.setPadding(0, 0, 0, 0);
        }
        mapPointViewDetaille.hideMapPointViewDetaille();
    }

    @Override
    public boolean isClickInAlgeria(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                String countryCode = addresses.get(0).getCountryCode();
                if ("DZ".equals(countryCode)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void addPointToMap(PlaceInfo placeInfo) {
        allPlaceInfo.add(placeInfo);
        mClusterManager.addItem(new MapClusterMarkerItem(placeInfo.getLatLng(), placeInfo.getName
                (), placeInfo.getType()));
        mClusterManager.cluster();
    }

    @Override
    public Marker addMarker(MarkerOptions markerOptions) {
        return mMap.addMarker(markerOptions);
    }

    @Override
    public void setPointInteretImage(Bitmap image) {
        mapPointViewDetaille.setPointImage(image);
    }

    @Override
    public boolean onClusterItemClick(ClusterItem clusterItem) {
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (pointDettailleView.getVisibility() == View.VISIBLE) {
            hidePointDetailleView();
        }
        mapPresenter.onMapClick(latLng);
    }

    @Override
    public void onNavigationClick(PlaceInfo placeInfo) {
        mapPresenter.onNavigationClick(placeInfo.getLatLng());
    }

    @Override
    public void onMoreInformationClick(PlaceInfo placeInfo) {
        Bundle bulndle = getIntent().getExtras();
        if (bulndle != null && StaticValue.MAIN.equals(bulndle.getString(StaticValue.MAP_SOURCE_TAG, ""))) {
            Intent intent = new Intent(MapActivity.this, PointIntereActivity.class);
            intent.putExtra(StaticValue.POINT_SOURCE_TAGE, StaticValue.MAP);
            intent.putExtra(StaticValue.POINT_TAG, placeInfo);
            startActivity(intent);
        } else {
            finish();
        }
    }

    @Override
    public void moveCameratoPath(Polyline polyline) {
        boolean hasPoints = false;
        Double maxLat = null, minLat = null, minLon = null, maxLon = null;

        if (polyline != null && polyline.getPoints() != null) {
            List<LatLng> pts = polyline.getPoints();
            for (LatLng coordinate : pts) {
                // Find out the maximum and minimum latitudes & longitudes
                // Latitude
                maxLat = maxLat != null ? Math.max(coordinate.latitude, maxLat) : coordinate.latitude;
                minLat = minLat != null ? Math.min(coordinate.latitude, minLat) : coordinate.latitude;

                // Longitude
                maxLon = maxLon != null ? Math.max(coordinate.longitude, maxLon) : coordinate.longitude;
                minLon = minLon != null ? Math.min(coordinate.longitude, minLon) : coordinate.longitude;

                hasPoints = true;
            }
        }

        if (hasPoints) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(maxLat, maxLon));
            builder.include(new LatLng(minLat, minLon));
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 80));
        }
    }


    @Override
    public boolean onClusterClick(Cluster<MapClusterMarkerItem> cluster) {

        zoomeInto(cluster.getPosition(), mMap.getCameraPosition().zoom + 2f);

        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toasty.error(this, "permission problem", Toast.LENGTH_SHORT).show();
            return;
        }
        // check if gps enable
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            mapPresenter.setCurrentPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }
        // start update
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toasty.error(MapActivity.this, getString(R.string.connecting_to_location_service_fail), Toast.LENGTH_SHORT, true).show();
        try {
            connectionResult.startResolutionForResult(MapActivity.this, 1000);

        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null)
            return;

        Log.d("tixx", "onLocationChanged: " + location.toString());
        // hide init location whene we get real location
        mapPresenter.onLocationChanged(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mapPresenter.onMapLongClick(latLng);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        zoomeInto(marker.getPosition(), 15.5f);
        int placeInfoPosition = getPlaceInfoFromPosition(marker.getPosition());
        if (placeInfoPosition == -1) {
            return false;
        }
        mapPresenter.setSelectedIconToMarker(marker,
                MapUtils.getIcon(this, "selected"),
                MapUtils.getIcon(this, allPlaceInfo.get(placeInfoPosition).getType()));

        mapPointViewDetaille.setData(allPlaceInfo.get(placeInfoPosition));
        mapPresenter.loadPointImage(allPlaceInfo.get(placeInfoPosition).getId());
        showPointDetailleView();
        return true;
    }
}
