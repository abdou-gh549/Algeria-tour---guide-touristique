package com.algeriatour.main.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.algeriatour.R;
import com.algeriatour.uml_class.Ville;
import com.algeriatour.utils.AlgeriaTourUtils;
import com.algeriatour.utils.StaticValue;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.algeriatour.utils.AlgeriaTourUtils.parsImage;

public class HomeFragementModel implements HomeFragmentConstraint.ModelConstraint {
    private final String ville_info_file = "at_get_all_towns.php";
    private final String ville_image_file = "at_get_image_of.php";
    private final String ville_url = StaticValue.MYSQL_SITE + ville_info_file;
    private final String ville_image_url = StaticValue.MYSQL_SITE + ville_image_file;
    HomeFragmentConstraint.PresenterConstraint presenter;

    public HomeFragementModel(HomeFragmentConstraint.PresenterConstraint presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadVilles() {
        Log.d("tixxhome", "loadVilles: ");
        AndroidNetworking.post(ville_url).setTag("loadVille")
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt(StaticValue.JSON_NAME_SUCCESS)) {
                        case 1:
                            JSONArray jsonArrayVilles = response.getJSONArray(StaticValue
                                    .JSON_NAME_TOWNS);
                            JSONObject jsonVille;
                            ArrayList<Ville> villes = new ArrayList<>();

                            for (int i = 0; i < jsonArrayVilles.length(); i++) {
                                jsonVille = (JSONObject) jsonArrayVilles.get(i);
                                Log.d("ville", "onResponse: " +
                                        " id = " + jsonVille.get(StaticValue.JSON_NAME_ID) +
                                        " name = " + jsonVille.get(StaticValue.JSON_NAME_NAME) +
                                        " wilaya = " + jsonVille.get(StaticValue.JSON_NAME_WILAYA) +
                                        " descreption = " + jsonVille.get(StaticValue
                                        .JSON_NAME_DESCREPTION)
                                );
                                villes.add(parsVille(jsonVille));
                            }
                            presenter.onLoadVillesSuccess(villes);
                            // load image for every town
                            for (int i = 0; i < villes.size(); i++) {
                                loadVillesImage(villes.get(i), i);
                            }

                            break;
                        case -1:
                            presenter.onLoadVillesFailed(AlgeriaTourUtils.getString(R.string
                                    .server_error));
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
                Log.d("tixx", "onError: "+ error.getMessage() + " " +error.getErrorDetail());
                presenter.onLoadVillesFailed(AlgeriaTourUtils.getString(R.string
                        .connection_fail));
            }
        });


    }

    @Override
    public void loadVillesImage(Ville ville, int position) {
        Log.d("tix", "loadVillesImage: " + ville.getId());
        AndroidNetworking.post(ville_image_url)
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
                            String imageString = response.getString("image");
                            ville.setImage(parsImage(imageString));
                            presenter.onLoadImageSuccess(ville, position);
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

    private Ville parsVille(JSONObject jsonVille) throws JSONException {
        //town_ratting
        Log.d("tixx", "parsVille: rating  = "+jsonVille.getString(StaticValue
                .JSON_NAME_VILLE_RATING));
        Ville v = new Ville();
        v.setId(jsonVille.getLong(StaticValue.JSON_NAME_ID));
        v.setName(jsonVille.getString(StaticValue.JSON_NAME_NAME));
        v.setWilaya(jsonVille.getString(StaticValue.JSON_NAME_WILAYA));
        v.setDescreption(jsonVille.getString(StaticValue.JSON_NAME_DESCREPTION));
        v.setRate(Float.parseFloat(jsonVille.getString(StaticValue.JSON_NAME_VILLE_RATING)));
        return v;
    }

}
