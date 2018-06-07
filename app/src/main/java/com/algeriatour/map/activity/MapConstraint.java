package com.algeriatour.map.activity;

import com.algeriatour.uml_class.PlaceInfo;
import com.algeriatour.uml_class.PointInteret;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapConstraint {

    public interface ViewConstraint{

        void showInfoToast(String msg);
        void showErrorToast(String msg);
        void showPointDetailleView();
        void hidePointDetailleView();
        boolean isClickInAlgeria(LatLng latLng);
        void addPointToMap(PlaceInfo placeInfo);
    }

    public interface PresenterConstraint{
        void loadAllPoint();
    }
    public interface ModelConstraint{
            void loadAllPoint(PresenterCallBack.OnLoadallPointCallBack onLoadallPointCallBack);
    }

    static public class PresenterCallBack{
        public interface OnLoadallPointCallBack{
            void onLoadAllPointSuccess(JSONArray points) throws JSONException;
            void onLoadAllPointFail(String msg);
        }
    }

}
