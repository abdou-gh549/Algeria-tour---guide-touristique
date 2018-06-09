package com.algeriatour.map.activity;

import android.graphics.Bitmap;

import com.algeriatour.uml_class.PlaceInfo;
import com.algeriatour.utils.AlgeriaTourUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;

public class MapConstraint {

    public interface ViewConstraint{

        void showInfoToast(String msg);
        void showErrorToast(String msg);
        void showPointDetailleView();
        void hidePointDetailleView();
        boolean isClickInAlgeria(LatLng latLng);
        void addPointToMap(PlaceInfo placeInfo);
        Marker addMarker(MarkerOptions markerOptions);
        void setPointInteretImage(Bitmap image);
        void zoomeInto(final LatLng position, float zoom);

        Polyline addPolyline(PolylineOptions polylineOptions);
        void moveCameratoPath(Polyline polyline);

        void showWarnningMessage(String msg);

        void hideLongClickNavigationFab();
        void showLongClickNavigationFab();
        void showRouteActionLayout();
        void hideRouteActionLayout();
        void showProgressDialog();
        void hideProgressDialog();
    }

    public interface PresenterConstraint{
        void loadAllPoint();
    }
    public interface ModelConstraint{
            void loadAllPoint(PresenterCallBack.OnLoadallPointCallBack onLoadallPointCallBack);
            void loadPointIneteretImage(long pointId, AlgeriaTourUtils.NetworkResponseAction action);
        void getPathInformation(LatLng origin, LatLng destination , AlgeriaTourUtils
                .NetworkResponseAction action);
    }

    static public class PresenterCallBack{
        public interface OnLoadallPointCallBack{
            void onLoadAllPointSuccess(JSONArray points) throws JSONException;
            void onLoadAllPointFail(String msg);
        }
    }

}
