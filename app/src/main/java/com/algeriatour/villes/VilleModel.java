package com.algeriatour.villes;

import android.util.Log;

import com.algeriatour.uml_class.PointInteret;
import com.algeriatour.uml_class.Ville;
import com.algeriatour.utils.AlgeriaTourUtils;
import com.algeriatour.utils.StaticValue;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.algeriatour.utils.AlgeriaTourUtils.parsImage;

public class VilleModel implements VilleConstraint.ModelConstraint {
    private final String getPointFileName = "at_get_points_of_town.php";
    private final String laodImageFileName = "at_get_image_of.php";
    private final String pointInteret_url = StaticValue.MYSQL_SITE + getPointFileName;
    private final String getImage_url = StaticValue.MYSQL_SITE + laodImageFileName;
    VilleConstraint.PresenterConstraint presenter;

    public VilleModel(VilleConstraint.PresenterConstraint presenterConstraint) {
        this.presenter = presenterConstraint;
    }

    @Override
    public void loadPointInteret(long villeId) {
        AndroidNetworking.post(pointInteret_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_TOWN_ID, villeId + "")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt(StaticValue.JSON_NAME_SUCCESS)) {
                        case 1:
                            JSONArray jsonArrayPoints = response.getJSONArray(StaticValue
                                    .JSON_NAME_POINTS);
                            JSONObject jsonPoint;
                            ArrayList<PointInteret> pointInterets = new ArrayList<>();

                            for (int i = 0; i < jsonArrayPoints.length(); i++) {
                                jsonPoint = (JSONObject) jsonArrayPoints.get(i);

                                pointInterets.add(parstPointInteret(jsonPoint));
                            }
                            if (pointInterets.isEmpty()) {
                                presenter.onLoadEmptyPointInteret();
                            } else {
                                presenter.onLoadPointIntereSuccess(pointInterets);
                                for (int i = 0; i < pointInterets.size(); i++) {
                                    loadPointIneteretImage(pointInterets.get(i), i);
                                }
                            }

                            break;
                        case -1:
                            Log.d("tixx", "reponse -1 error : " + response.getInt(StaticValue
                                    .JSON_NAME_MESSAGE));
                            presenter.onLoadPointIntereFail("server error !");
                            break;
                    }
                    Log.d("tixx", "reponse " + response.getInt(StaticValue.JSON_NAME_SUCCESS));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "reponst catch" + e.getMessage());
                }
            }

            @Override
            public void onError(ANError error) {
                Log.d("tixx", "error " + error.getMessage());
                presenter.onLoadPointIntereFail("verfier votre connection ");
            }
        });

    }

    @Override
    public void loadPointIneteretImage(PointInteret pointInteret, int position) {
        Log.d("tixx", "loadPointIneteretImage: " + pointInteret.getId());
        AndroidNetworking.post(getImage_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_WHAT, StaticValue.PHP_POINT)
                .addBodyParameter(StaticValue.PHP_ID, pointInteret.getId() + "")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt(StaticValue.JSON_NAME_SUCCESS)) {
                        case 1:
                            String imageString = response.getString("image");
                            pointInteret.setImage(parsImage(imageString));
                            presenter.onLoadPointInteretImageSucess(pointInteret, position);
                            break;
                        case -1:
                            Log.d("tixx", "load image server error " + response.getString(StaticValue
                                    .JSON_NAME_MESSAGE));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "load image reponst catch 1 " + e.getMessage() + " " + e
                            .getCause());
                }
            }

            @Override
            public void onError(ANError error) {
                Log.d("tixx", "load image onError : " + error.getMessage());
            }
        });
    }

    @Override
    public void loadVilleImage(Ville ville) {
        AndroidNetworking.post(getImage_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_WHAT, StaticValue.PHP_TOWN)
                .addBodyParameter(StaticValue.PHP_ID, ville.getId() + "")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt(StaticValue.JSON_NAME_SUCCESS)) {
                        case 1:
                            String imageString = response.getString(StaticValue.JSON_NAME_IMAGE);
                            presenter.onLoadVilleImageSuccess(AlgeriaTourUtils.parsImage(imageString));
                            break;
                        case -1:
                            presenter.onLoadVilleImageFail("can't load ville image");
                            break;
                    }
                    Log.d("tixx", "reponse load ville image" + response.getInt(StaticValue
                            .JSON_NAME_SUCCESS));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "reponst catch" + e.getMessage());

                }
            }

            @Override
            public void onError(ANError error) {
                presenter.onLoadVilleImageFail("can't load ville image check your connection");
                Log.d("tixx", "error " + error.getMessage());
            }
        });

    }

    private PointInteret parstPointInteret(JSONObject jsonObject) throws JSONException {
        Log.d("unicode", "parstPointInteret: " + jsonObject.getString(StaticValue.JSON_NAME_DESCREPTION));

        PointInteret pointInteret = new PointInteret();
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
