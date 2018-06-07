package com.algeriatour.map.activity;

import android.util.Log;

import com.algeriatour.utils.StaticValue;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapModel implements MapConstraint.ModelConstraint {
    private final String getAllPointsFileName = "at_get_all_point_for_map.php";
    private final String laodImageFileName = "at_get_image_of.php";
    private final String loadAllPoint_url = StaticValue.MYSQL_SITE + getAllPointsFileName;
    private final String getImage_url = StaticValue.MYSQL_SITE + laodImageFileName;

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

                        onLoadallPointCallBack.onLoadAllPointFail("server error");
                    }

                    Log.d("tixx", "reponse " + response.getInt(StaticValue.JSON_NAME_SUCCESS));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "reponst catch" + e.getMessage());
                    onLoadallPointCallBack.onLoadAllPointFail("parsing data error !");

                }
            }

            @Override
            public void onError(ANError error) {
                Log.d("tixx", "error " + error.getMessage());
                onLoadallPointCallBack.onLoadAllPointFail(error.getMessage());
            }
        });
    }
}
