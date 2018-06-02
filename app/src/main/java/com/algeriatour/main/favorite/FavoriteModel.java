package com.algeriatour.main.favorite;

import android.util.Log;

import com.algeriatour.uml_class.Favorite;
import com.algeriatour.uml_class.PointInteret;
import com.algeriatour.utils.AlgeriaTourUtils;
import com.algeriatour.utils.StaticValue;
import com.algeriatour.utils.User;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoriteModel implements FavoriteConstraint.ModelConstraint {
    private final String loadFavoriteListFileName = "at_get_favorites_of_user.php";
    private final String updateNoteOfFavoriteFileName = "at_update_favorite.php";
    private final String deleteFavoriteFileName = "at_delete_favorite.php";
    private final String laodImageFileName = "at_get_image_of.php";
    private final String loadFavoriteList_url = StaticValue.MYSQL_SITE + loadFavoriteListFileName;
    private final String updateNoteFavorite_url = StaticValue.MYSQL_SITE + updateNoteOfFavoriteFileName;
    private final String deleteFavorite_url = StaticValue.MYSQL_SITE + deleteFavoriteFileName;
    private final String getImage_url = StaticValue.MYSQL_SITE + laodImageFileName;
    FavoriteConstraint.PresenterConstraint presenter;

    public FavoriteModel(FavoriteConstraint.PresenterConstraint presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadFavoriteList() {

        AndroidNetworking.post(loadFavoriteList_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_USER_ID, User.getMembre().getId() + "")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("tixx", "onResponse favorite : reponse = " + response.getInt(StaticValue.JSON_NAME_SUCCESS) +
                            " msg = " + response.toString());
                    if (response.getInt(StaticValue.JSON_NAME_SUCCESS) == 1) {
                        JSONArray jsonfavoriteArray = response.getJSONArray(StaticValue
                                .JSON_NAME_FAVORITE);
                        JSONObject jsonfavorite;
                        ArrayList<Favorite> favorites = new ArrayList<>();
                        if (jsonfavoriteArray.length() == 0) {
                            presenter.onLoadFavoriteListEmpty();
                            return;
                        }
                        // else
                        for (int i = 0; i < jsonfavoriteArray.length(); i++) {
                            jsonfavorite = jsonfavoriteArray.getJSONObject(i);
                            favorites.add(parsFavorite(jsonfavorite));
                        }

                        presenter.onLoadFavoriteListSuccess(favorites);

                        for (int i = 0; i < favorites.size(); i++) {
                            loadPointIneteretImage(favorites.get(i).getPointInteret().getId());
                        }
                    } else {
                        presenter.onLoadFavoriteListFail("server Error can't load favorite list");
                    }
                } catch (JSONException e) {
                    presenter.onLoadFavoriteListFail(" opps somthing happend");
                    Log.d("tixx", "onResponse error : in catch :  " + e.getMessage());
                }
            }

            @Override
            public void onError(ANError error) {

            }
        });

    }

    @Override
    public void updateNoteOfFavorite(long favoriteId, String note) {
        AndroidNetworking.post(updateNoteFavorite_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_NOTE, note)
                .addBodyParameter(StaticValue.PHP_ID, favoriteId + "")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("tixx", "onResponse favorite : reponse = " + response.getInt(StaticValue.JSON_NAME_SUCCESS) +
                            " msg = " + response.toString());
                    if (response.getInt(StaticValue.JSON_NAME_SUCCESS) == 1) {
                        presenter.onUpdateNoteSucess();
                    } else {
                        presenter.onUpdateNoteFail("server Error can't update favorite");
                    }
                } catch (JSONException e) {
                    presenter.onUpdateNoteFail(" opps somthing happend");
                    Log.d("tixx", "onResponse error : in catch :  " + e.getMessage());
                }
            }

            @Override
            public void onError(ANError error) {
                presenter.onUpdateNoteFail("can't save change ... check your connection");
            }
        });
    }

    @Override
    public void deleteFavorite(long favoriteId) {
        AndroidNetworking.post(deleteFavorite_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_ID, favoriteId + "")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt(StaticValue.JSON_NAME_SUCCESS) == 1) {
                        presenter.onDeleteFavortieSuccess(favoriteId);
                    } else {
                        presenter.onDeleteFavoriteFail("server error can't delete");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    presenter.onDeleteFavoriteFail("can't delete ... something happened");
                }
            }

            @Override
            public void onError(ANError error) {
                presenter.onDeleteFavoriteFail("can't delete check your connection");
            }
        });
    }

    @Override
    public void loadPointIneteretImage(long pointInetertId) {
        Log.d("tixx", "loadPointIneteretImage: " + pointInetertId);
        AndroidNetworking.post(getImage_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_WHAT, StaticValue.PHP_POINT)
                .addBodyParameter(StaticValue.PHP_ID, pointInetertId + "")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt(StaticValue.JSON_NAME_SUCCESS)) {
                        case 1:
                            String imageString = response.getString("image");
                            presenter.onloadPointIneteretImageSuccess(AlgeriaTourUtils.parsImage(imageString), pointInetertId);
                            break;
                        case -1:
                            Log.d("tixx", "load image server error " + response.getString(StaticValue
                                    .JSON_NAME_MESSAGE));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "load image reponst catch  " + e.getMessage() + " " + e
                            .getCause());
                }
            }

            @Override
            public void onError(ANError error) {
                Log.d("tixx", "load image onError : " + error.getMessage());
            }
        });
    }

    private Favorite parsFavorite(JSONObject jsonObject) throws JSONException {
        PointInteret pointInteret = new PointInteret();
        Favorite favorite = new Favorite();
        pointInteret.setId(jsonObject.getLong(StaticValue.JSON_NAME_POINT_ID));
        pointInteret.setName(jsonObject.getString(StaticValue.JSON_NAME_POINT));
        pointInteret.setType(jsonObject.getString(StaticValue.JSON_NAME_TYPE));
        pointInteret.setDescreption(jsonObject.getString(StaticValue.JSON_NAME_DESCREPTION));
        pointInteret.setWilaya(jsonObject.getString(StaticValue.JSON_NAME_WILAYA));
        pointInteret.setVille(jsonObject.getString(StaticValue.JSON_NAME_TOWN));
        favorite.setFavoriteId(jsonObject.getLong(StaticValue.JSON_NAME_ID));

        favorite.setNote(jsonObject.getString(StaticValue.JSON_NAME_NOT));
        favorite.setPointInteret(pointInteret);
        return favorite;
    }
}
