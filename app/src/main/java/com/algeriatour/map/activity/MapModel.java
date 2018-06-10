package com.algeriatour.map.activity;

import android.util.Log;

import com.algeriatour.R;
import com.algeriatour.map.other.SelectedMarker;
import com.algeriatour.map.other.MapRoute;
import com.algeriatour.map.other.MapUtils;
import com.algeriatour.utils.AlgeriaTourUtils;
import com.algeriatour.utils.StaticValue;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapModel implements MapConstraint.ModelConstraint {
    private final String getAllPointsFileName = "at_get_all_point_for_map.php";
    private final String laodImageFileName = "at_get_image_of.php";
    private final String loadAllPoint_url = StaticValue.MYSQL_SITE + getAllPointsFileName;
    private final String getImage_url = StaticValue.MYSQL_SITE + laodImageFileName;

    private Marker myLocationMarker;
    private Marker longClickMarker;
    private LatLng currentPosition;
    private MapRoute route;
    private SelectedMarker clickedMarker;

    @Override
    public void loadAllPoint(MapConstraint.PresenterCallBack.OnLoadallPointCallBack onLoadallPointCallBack) {

        AndroidNetworking.post(loadAllPoint_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt(StaticValue.JSON_NAME_SUCCESS) == 1) {

                        JSONArray jsonArrayPoints = response.getJSONArray(StaticValue.JSON_NAME_POINTS);
                        onLoadallPointCallBack.onLoadAllPointSuccess(jsonArrayPoints);

                    } else {

                        Log.d("tixx", "reponse -1 error : " + response.getInt(StaticValue
                                .JSON_NAME_MESSAGE));

                        onLoadallPointCallBack.onLoadAllPointFail(AlgeriaTourUtils.getString(R
                                .string.server_error));
                    }

                    Log.d("tixx", "reponse " + response.getInt(StaticValue.JSON_NAME_SUCCESS));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "reponst catch" + e.getMessage());
                    onLoadallPointCallBack.onLoadAllPointFail(AlgeriaTourUtils.getString(R
                            .string.server_error));

                }
            }

            @Override
            public void onError(ANError error) {
                Log.d("tixx", "error " + error.getMessage());
                onLoadallPointCallBack.onLoadAllPointFail(AlgeriaTourUtils.getString(R.string.connection_fail));
            }
        });
    }

    @Override
    public void loadPointIneteretImage(long pointInetertId,AlgeriaTourUtils.NetworkResponseAction action) {
        Log.d("tixx", "loadPointIneteretImage: " + pointInetertId);
        AndroidNetworking.post(getImage_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_WHAT, StaticValue.PHP_POINT)
                .addBodyParameter(StaticValue.PHP_ID, pointInetertId + "")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                action.onSuccess(response);
            }
            @Override
            public void onError(ANError error) {
                Log.d("tixx", "load image onError : " + error.getMessage());
                action.onFail(AlgeriaTourUtils.getString(R.string.load_image_error));
            }
        });
    }

    @Override
    public void getPathInformation(LatLng origin, LatLng destination, AlgeriaTourUtils
            .NetworkResponseAction action){
        String requestUrl = MapUtils.getRequestUrl(origin, destination);
        AndroidNetworking.cancel("route");
        AndroidNetworking.get(requestUrl).setTag("route")
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                action.onSuccess(response);

            }

            @Override
            public void onError(ANError anError) {
                Log.d("tixx", "onError: " + anError.getMessage());
                action.onFail(AlgeriaTourUtils.getString(R.string.connection_fail));
            }
        });

    }

    public Marker getMyLocationMarker() {
        return myLocationMarker;
    }

    public void setMyLocationMarker(Marker myLocationMarker) {
        this.myLocationMarker = myLocationMarker;
    }

    public LatLng getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(LatLng currentPosition) {
        this.currentPosition = currentPosition;
    }


    public Marker getLongClickMarker() {
        return longClickMarker;
    }

    public void setLongClickMarker(Marker longClickMarker) {
        this.longClickMarker = longClickMarker;
    }

    public MapRoute getRoute() {
        return route;
    }

    public void setRoute(MapRoute route) {
        this.route = route;
    }

    public SelectedMarker getClickedMarker() {
        return clickedMarker;
    }

    public void setClickedMarker(SelectedMarker clickedMarker) {
        this.clickedMarker = clickedMarker;
    }
}
