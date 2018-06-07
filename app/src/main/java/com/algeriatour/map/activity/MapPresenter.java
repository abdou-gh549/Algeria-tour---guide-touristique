package com.algeriatour.map.activity;

import android.util.Log;

import com.algeriatour.uml_class.PlaceInfo;
import com.algeriatour.uml_class.PointInteret;
import com.algeriatour.utils.StaticValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapPresenter implements MapConstraint.PresenterConstraint {
    MapModel mapModel ;
    MapConstraint.ViewConstraint mapView;

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

}
