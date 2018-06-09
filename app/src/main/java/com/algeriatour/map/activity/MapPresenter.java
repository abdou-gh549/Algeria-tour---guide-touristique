package com.algeriatour.map.activity;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.algeriatour.map.other.DirectionsParser;
import com.algeriatour.map.other.MapRoute;
import com.algeriatour.map.other.MapUtils;
import com.algeriatour.map.other.RouteRequestResult;
import com.algeriatour.uml_class.PlaceInfo;
import com.algeriatour.uml_class.PointInteret;
import com.algeriatour.utils.AlgeriaTourUtils;
import com.algeriatour.utils.StaticValue;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class MapPresenter implements MapConstraint.PresenterConstraint {
    private MapModel mapModel ;
    private MapConstraint.ViewConstraint mapView;

    private static int countTry = 0;
    private static int maxTry = 2;


    public MapPresenter(MapConstraint.ViewConstraint mapView) {
        this.mapView = mapView;
        mapModel = new MapModel();
    }

    @Override
    public void loadAllPoint() {
        mapModel.loadAllPoint(new MapConstraint.PresenterCallBack.OnLoadallPointCallBack() {
            @Override
            public void onLoadAllPointSuccess(JSONArray points) throws JSONException {
                for (int i = 0; i < points.length(); i++) {
                    mapView.addPointToMap( parstPointInteret(points.getJSONObject(i)));
                }
            }
            @Override
            public void onLoadAllPointFail(String msg) {
                mapView.showErrorToast(msg);
            }
        });
    }


    private PlaceInfo parstPointInteret(JSONObject jsonObject) throws JSONException {
        Log.d("unicode", "parstPointInteret: " +jsonObject.getString(StaticValue.JSON_NAME_DESCREPTION));

        PlaceInfo pointInteret = new PointInteret();
        pointInteret.setId(jsonObject.getLong(StaticValue.JSON_NAME_ID));
        pointInteret.setName(jsonObject.getString(StaticValue.JSON_NAME_NAME));
        pointInteret.setType(jsonObject.getString(StaticValue.JSON_NAME_TYPE));
        pointInteret.setDescreption(jsonObject.getString(StaticValue.JSON_NAME_DESCREPTION));
        pointInteret.setRate(Float.parseFloat(jsonObject.getString(StaticValue
                .JSON_NAME_POINT_RATING)));
        pointInteret.setLatitude(jsonObject.getDouble(StaticValue.JSON_NAME_LATITUDE));
        pointInteret.setLongitude(jsonObject.getDouble(StaticValue.JSON_NAME_LONGITUDE));
        pointInteret.setWilaya(jsonObject.getString(StaticValue.JSON_NAME_WILAYA));
        pointInteret.setVille(jsonObject.getString("ville_name"));
        return pointInteret;
    }

    public void onMapLongClick(LatLng clickPosition) {
        if( ! mapView.isClickInAlgeria(clickPosition)){
            return;
        }
        Marker marker;
        if(mapModel.getLongClickMarker() == null){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(clickPosition);
            marker = mapView.addMarker(markerOptions);
            mapModel.setLongClickMarker(marker);
        }else{
            mapModel.getLongClickMarker().setPosition(clickPosition);
        }
        mapView.showLongClickNavigationFab();
        mapView.hidePointDetailleView();
        mapModel.getLongClickMarker().setVisible(true);
    }

    public void onMapClick(LatLng clickPosition) {
        if(! mapView.isClickInAlgeria(clickPosition))
            return;

        if(mapModel.getLongClickMarker() != null){
            mapModel.getLongClickMarker().setVisible(false);
            mapView.hideLongClickNavigationFab();
        }
    }

    public void loadPointImage(long id) {
        mapModel.loadPointIneteretImage(id, new AlgeriaTourUtils.NetworkResponseAction() {
            @Override
            public void onSuccess(Object rep) {
                JSONObject response = (JSONObject) rep;
                try {
                    if( response.getInt(StaticValue.JSON_NAME_SUCCESS) == 1 ){
                        String imageString = response.getString("image");
                        mapView.setPointInteretImage(AlgeriaTourUtils.parsImage(imageString));
                    }else{
                        Log.d("tixx", "load image server error " + response.getString(StaticValue
                                .JSON_NAME_MESSAGE));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "load image reponst catch  " + e.getMessage() + " " + e
                            .getCause());
                }
            }

            @Override
            public void onFail(String msg) { }
        });
    }

    public void traceWay(final LatLng origin, final LatLng destination) {
        mapView.showProgressDialog();
        removePolylineFromMap();
        mapModel.getPathInformation(origin, destination, new AlgeriaTourUtils.NetworkResponseAction() {
            @Override
            public void onSuccess(Object response) {
                MapPresenter.TaskParser taskParser = new MapPresenter.TaskParser();
                try {
                    JSONObject jsRespons = new JSONObject((String) response);
                    if ("ZERO_RESULTS".equals(jsRespons.getString("status"))) {
                        // zero result query
                        mapView.hideProgressDialog();
                        mapView.showErrorToast("no direction found");
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "onResponse: trace way msg -> " + e.getMessage());
                }

                taskParser.execute(new RouteRequestResult(destination, origin, (String) response));
            }
            @Override
            public void onFail(String msg) {
                mapView.hideProgressDialog();
                mapView.showErrorToast("connection error");
            }
        });

    }

    public void onLocationChanged(LatLng latLng) {
        setCurrentPosition(latLng);
    }

    public void setCurrentPosition(LatLng currentPosition) {
        mapModel.setCurrentPosition(currentPosition);
        if(mapModel.getMyLocationMarker() == null){
            Log.d("tixxmap", "setCurrentPosition: " + "creat my locaiton marker");
            Marker marker = mapView.addMarker( new MarkerOptions()
                    .title("mon position")
                    .position(currentPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mapModel.setMyLocationMarker(marker);
        }else{
            Log.d("tixxmap", "setCurrentPosition: " + "update my location marker");
            mapModel.getMyLocationMarker().setPosition(currentPosition);
            mapModel.getMyLocationMarker().setVisible(true);
        }
    }

    public void onNavigationClick(LatLng latLng) {
        if(mapModel.getCurrentPosition() == null ){
            mapView.showErrorToast("can't find your location");
            return;
        }
        traceWay(mapModel.getCurrentPosition(),latLng);
    }


    public void removePolylineFromMap(){
        if (mapModel.getRoute() != null) {
            mapModel.getRoute().getPolyline().remove();
            mapModel.setRoute(null);
            mapView.hideRouteActionLayout();
        }
    }

    public void onMyLocationClick() {
        if (mapModel.getCurrentPosition() == null) {
            mapView.showWarnningMessage("can't find position info please try to move and try " +
                    "again");
            return;
        }
        mapView.zoomeInto(mapModel.getCurrentPosition(), 10);
    }

    public void traceWayToLongClickMarker() {
        if(mapModel.getCurrentPosition() == null){
            mapView.showErrorToast("can't find user location");
            return;
        }
        mapView.hideLongClickNavigationFab();
        traceWay(mapModel.getCurrentPosition(), mapModel.getLongClickMarker().getPosition());
    }

    public void cancelRoute() {
        removePolylineFromMap();
        mapView.hideRouteActionLayout();
        if(mapModel.getLongClickMarker() != null)
            mapModel.getLongClickMarker().setVisible(false);
    }

    public void onFullViewRoutClick() {
        if(mapModel.getRoute() != null){
            mapView.moveCameratoPath(mapModel.getRoute().getPolyline());
        }else{
            mapView.hideRouteActionLayout();
        }
    }

    public void onRefreshRootClicked() {
        if(mapModel.getRoute() != null ){
            if(mapModel.getCurrentPosition() == null){
                mapView.showErrorToast("can't find the new location");
            }else{
                traceWay( mapModel.getCurrentPosition(), mapModel.getRoute().getDestination());
            }
        }else{
            mapView.hideRouteActionLayout();
        }
    }

    public class TaskParser extends AsyncTask<RouteRequestResult, Void, List<List<HashMap<String, String>>>> {
        private RouteRequestResult m_routeRequestResult;

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(RouteRequestResult... routeRequestResult) {
            JSONObject jsonObject ;
            List<List<HashMap<String, String>>> routes = null;
            m_routeRequestResult = routeRequestResult[0];
            try {
                jsonObject = new JSONObject(m_routeRequestResult.getRequestResult());
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MapRoute mapRoute = new MapRoute();
            mapRoute.setDestination(routeRequestResult[0].getDestination());
            mapRoute.setOrigin(routeRequestResult[0].getOrigin());
            mapModel.setRoute(mapRoute);
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map
            PolylineOptions polylineOptions = MapUtils.convertToPolyLineOption(lists);

            if (polylineOptions != null) {
                Polyline polyline = mapView.addPolyline(polylineOptions);
                mapModel.getRoute().setPolyline(polyline);
                countTry = 0;
                mapView.hidePointDetailleView();
                mapView.moveCameratoPath(mapModel.getRoute().getPolyline());
                mapView.showRouteActionLayout();
            } else {
                if (countTry < maxTry) {
                    countTry++;
                    new Handler().postDelayed(
                            () -> traceWay(m_routeRequestResult.getOrigin(), m_routeRequestResult
                                    .getDestination()), 5000);
                } else {
                    countTry = 0;
                    mapView.showErrorToast("can't load direction for the moment");
                }
            }
            mapView.hideProgressDialog();

        }
    }


}
