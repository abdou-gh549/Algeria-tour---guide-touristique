package com.algeriatour.point;

import android.util.Log;

import com.algeriatour.uml_class.Commentaire;
import com.algeriatour.uml_class.PointInteret;
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

public class PointInteretModel implements PointIneteretConstraint.ModelConstraint {
    private final String laodImageFileName = "at_get_image_of.php";
    private final String getCommentFileName = "at_get_opinions_of_point.php";
    private final String getImage_url = StaticValue.MYSQL_SITE + laodImageFileName;
    private final String getComment_url = StaticValue.MYSQL_SITE + getCommentFileName;
    private PointIneteretConstraint.PresenterConstraint presenter;

    public PointInteretModel(PointIneteretConstraint.PresenterConstraint p) {
        presenter = p;
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
                            presenter.onloadPointIneteretImageSuccess(AlgeriaTourUtils.parsImage
                                    (imageString));
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

    @Override
    public void loadCommentaire(long pointInetertId) {
        Log.d("tixx", "load commentaire : " + pointInetertId);
        AndroidNetworking.post(getComment_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_POINT_ID, pointInetertId + "")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt(StaticValue.JSON_NAME_SUCCESS)) {
                        case 1:
                            JSONArray jsonArrayComment = response.getJSONArray(StaticValue
                                    .JSON_NAME_COMMENT);
                            JSONObject jsonComment;
                            ArrayList<Commentaire> commentaires = new ArrayList<>();

                            for (int i = 0; i < jsonArrayComment.length(); i++) {
                                jsonComment = (JSONObject) jsonArrayComment.get(i);
                                Log.d("tixx", jsonComment.toString());
                                commentaires.add(parsComment(jsonComment));
                            }
                            if (commentaires.isEmpty()) {
                                presenter.onLoadEmptyCommentaire();
                            } else {
                                presenter.onLoadCommentaireSucess(commentaires);
                            }
                            break;
                        case -1:
                            Log.d("tixx", "load comment server error " + response.getString
                                    (StaticValue.JSON_NAME_MESSAGE));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "load comment repons  catch  " + e.getMessage() + " " + e.getCause());
                }
            }

            @Override
            public void onError(ANError error) {
                Log.d("tixx", "load comment onError : " + error.getMessage());
            }
        });
    }

    private Commentaire parsComment(JSONObject jsonObject) throws JSONException {
        Commentaire commentaire = new Commentaire();
        commentaire.setUserName( jsonObject.getString(StaticValue.JSON_NAME_USER_NAME));
        commentaire.setComment( jsonObject.getString(StaticValue.JSON_NAME_COMMENT_DESCREPTION));
        commentaire.setDate(jsonObject.getString(StaticValue.JSON_NAME_DATE));
        commentaire.setRatting(jsonObject.getLong(StaticValue.JSON_NAME_RATTING));
        return commentaire;
    }
}
