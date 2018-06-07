package com.algeriatour.map.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.algeriatour.R;
import com.algeriatour.map.other.DirectionsParser;
import com.algeriatour.map.other.MapClusterMarkerItem;
import com.algeriatour.map.other.MapUtils;
import com.algeriatour.map.other.RouteRequestResult;
import com.algeriatour.point.PointIntereActivity;
import com.algeriatour.uml_class.PlaceInfo;
import com.algeriatour.utils.StaticValue;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class MapActivity extends MapBaseActivity implements
        MapConstraint.ViewConstraint,
        ClusterManager.OnClusterItemClickListener,
        ClusterManager.OnClusterClickListener<MapClusterMarkerItem>,
        GoogleMap.OnMapClickListener,
        MapPointViewDetaille.OnMapPointViewDetailleAction,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener

{

    private static int countTry = 0;
    private static int maxTry = 2;
    private final String TAG = "tixx";

    @BindView(R.id.map_point_detaille)
    View pointDettailleView;
    MapPointViewDetaille mapPointViewDetaille;
    MapPresenter mapPresenter;
    ArrayList<PlaceInfo> allPlaceInfo;
    private boolean moveCamtoPath = false;
    private Polyline polyline;

    private PlaceInfo placeToNavigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        allPlaceInfo = new ArrayList<>();
        mapPresenter = new MapPresenter(this);
        mapPointViewDetaille = new MapPointViewDetaille(pointDettailleView, this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setupMap(googleMap);
        mMap.setOnMapClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterClickListener(this);

        setUpLoaction();

        loadMapData();
    }

    @OnClick(R.id.map_my_location)
    void onMyLocationClick() {
        if (currentLoaction == null) {
            Toasty.warning(this, "can't find position info please try to move and try again",
                    Toast.LENGTH_SHORT, true).show();
            return;
        }
        LatLng currentPosition = new LatLng(currentLoaction.getLatitude(), currentLoaction.getLongitude());
        myLastLocationMarker.setPosition(currentPosition);
        zoomeInto(currentPosition, 15);
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

    private void zoomeInto(final LatLng position, float zoom) {
        new Handler().postDelayed(() -> {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
        }, 300);
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

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("tixx", "onLocationChanged: " + location.toString());
                // hide init location whene we get real location
                currentLoaction = location;
                if (myLastLocationMarker != null) {
                    LatLng currentPosition = new LatLng(currentLoaction.getLatitude(), currentLoaction
                            .getLongitude());
                    myLastLocationMarker.setPosition(currentPosition);
                }
                // draw the new route if there is place navigation
                if (placeToNavigate != null) {
                    if (polyline != null) {
                        polyline.remove();
                        polyline = null;
                    }
                    LatLng currentPosition = new LatLng(currentLoaction.getLatitude(), currentLoaction
                            .getLongitude());

                    traceWay(currentPosition, placeToNavigate.getLatLng(), false);

                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };
        // get init location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // return;
        }
        currentLoaction = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (currentLoaction == null)
            return;

        LatLng currentPosition = new LatLng(currentLoaction.getLatitude(), currentLoaction.getLongitude());
        if (myLastLocationMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("my position");
            markerOptions.position(currentPosition);
            myLastLocationMarker = mMap.addMarker(markerOptions);
        } else {
            myLastLocationMarker.setPosition(currentPosition);
        }
        // add init location to map
        // todo not sur of it

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 5, locationListener);
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
    public void showPointDetailleView() {
        mapPointViewDetaille.showMapPointViewDetaille();
        Log.d(TAG, "showPointDetailleView: view hight  = " + mapPointViewDetaille.getHeight());
        mMap.setPadding(0, 0, 0, mapPointViewDetaille.getHeight());
    }

    @Override
    public void hidePointDetailleView() {
        mMap.setPadding(0, 0, 0, 0);
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
        mClusterManager.addItem(new MapClusterMarkerItem(placeInfo.getLatLng(), placeInfo.getName()));
        mClusterManager.cluster();
    }

    @Override
    public boolean onClusterItemClick(ClusterItem clusterItem) {
        zoomeInto(clusterItem.getPosition(), 15.5f);
        int placeInfoPosition = getPlaceInfoFromPosition(clusterItem.getPosition());
        if (placeInfoPosition == -1) {
            return false;
        }
        mapPointViewDetaille.setData(allPlaceInfo.get(placeInfoPosition));
        showPointDetailleView();
        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (pointDettailleView.getVisibility() == View.VISIBLE) {
            hidePointDetailleView();
        }
        if (myLastLocationMarker != null)
            myLastLocationMarker.hideInfoWindow();
    }

    @Override
    public void onNavigationClick(PlaceInfo placeInfo) {

        if (polyline != null) {
            polyline.remove();
            polyline = null;
        }
        if (currentLoaction == null) {
            showErrorToast("can't find user location !!");
            return;
        }
        LatLng currentPosition = new LatLng(currentLoaction.getLatitude(), currentLoaction
                .getLongitude());

        traceWay(currentPosition, placeInfo.getLatLng(), true);
        placeToNavigate = placeInfo;
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


    private void traceWay(final LatLng origin, final LatLng destination, Boolean focusCamera) {
        if (focusCamera != null)
            moveCamtoPath = focusCamera;
        String requestUrl = MapUtils.getRequestUrl(origin, destination);
        AndroidNetworking.cancel("route");
        AndroidNetworking.get(requestUrl).setTag("route")
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                // do parsing
                TaskParser taskParser = new TaskParser();

                try {
                    JSONObject jsRespons = new JSONObject(response);
                    if ("ZERO_RESULTS".equals(jsRespons.getString("status"))) {
                        // todo zero result query
                        Toast.makeText(MapActivity.this, "no direction found", Toast.LENGTH_SHORT).show();
                        placeToNavigate = null;
                        return;
                    }
                } catch (JSONException e) {
                    placeToNavigate = null;
                    e.printStackTrace();
                    Log.d(TAG, "onResponse: trace way msg -> " + e.getMessage());
                }
                taskParser.execute(new RouteRequestResult(origin, destination, response));
            }

            @Override
            public void onError(ANError anError) {
                placeToNavigate = null;
                Log.d(TAG, "onError: " + anError.getMessage());
            }
        });
    }

    private void moveCameratoPath() {
        if (!moveCamtoPath) {
            return;
        }
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
        zoomeInto(cluster.getPosition(), 8);
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public class TaskParser extends AsyncTask<RouteRequestResult, Void, List<List<HashMap<String, String>>>> {
        private RouteRequestResult m_routeRequestResult;

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(RouteRequestResult... routeRequestResult) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            m_routeRequestResult = routeRequestResult[0];
            try {
                jsonObject = new JSONObject(m_routeRequestResult.getRequestResult());
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map
            //todo : draw PolylineOptions
            PolylineOptions polylineOptions = MapUtils.convertToPolyLineOption(lists);

            if (polylineOptions != null) {
                polyline = mMap.addPolyline(polylineOptions);
                countTry = 0;
                hidePointDetailleView();
                moveCameratoPath();

            } else {
                if (countTry < maxTry) {
                    countTry++;
                    new Handler().postDelayed(
                            () -> traceWay(m_routeRequestResult.getOrigin(), m_routeRequestResult
                                    .getDestination(), null), 5000);
                } else {
                    countTry = 0;
                    Toast.makeText(getApplicationContext(), "can't load direction for the moment",
                            Toast.LENGTH_SHORT).show();
                    placeToNavigate = null;
                }
            }

        }
    }
}
