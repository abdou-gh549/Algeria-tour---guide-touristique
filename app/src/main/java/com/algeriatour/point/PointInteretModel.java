package com.algeriatour.point;

import android.util.Log;

import com.algeriatour.R;
import com.algeriatour.uml_class.Commentaire;
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

public class PointInteretModel implements PointIneteretConstraint.ModelConstraint {
    private final String laodImageFileName = "at_get_image_of.php";
    private final String getCommentFileName = "at_get_opinions_of_point.php";
    private final String favoriteExistCheckFileName = "at_has_user_favorite.php";
    private final String addToFavoriteFileName = "at_add_favorite.php";
    private final String checkCommentExistFileName = "at_has_user_comment.php";
    private final String addCommentFileName = "at_insert_comment.php";
    private final String getImage_url = StaticValue.MYSQL_SITE + laodImageFileName;
    private final String getComment_url = StaticValue.MYSQL_SITE + getCommentFileName;
    private final String favoriteCheck_url = StaticValue.MYSQL_SITE + favoriteExistCheckFileName;
    private final String addToFavorite_url = StaticValue.MYSQL_SITE + addToFavoriteFileName;
    private final String checkComment_url = StaticValue.MYSQL_SITE + checkCommentExistFileName;
    private final String addComment_url= StaticValue.MYSQL_SITE + addCommentFileName;

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
                presenter.onLoadCommentaireFail(AlgeriaTourUtils.getString(R.string.loading_comment_error));
                Log.d("tixx", "load comment onError : " + error.getMessage());
            }
        });
    }

    @Override
    public void favoriteAlreadyAddedCheck(long pointId) {
        AndroidNetworking.post(favoriteCheck_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_USER_ID, User.getMembre().getId() + "")
                .addBodyParameter(StaticValue.PHP_POINT_ID, pointId + "")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt(StaticValue.JSON_NAME_SUCCESS) == 1) {
                        if (response.getInt("has") == 1) { // true
                            presenter.onFavoriteAlreadyExist();
                        } else { // false
                            presenter.onFavoriteDoesNoteExist();
                        }
                    } else {
                        presenter.onFavoriteCheckFail(AlgeriaTourUtils.getString(R.string.server_error));
                    }
                } catch (JSONException e) {
                    Log.d("tixx", "onResponse check favorite: catch " + e.getMessage());
                    presenter.onFavoriteCheckFail(AlgeriaTourUtils.getString(R.string.server_error));
                }
            }

            @Override
            public void onError(ANError error) {
                Log.d("tixx", "load comment onError : " + error.getMessage());
                presenter.onFavoriteCheckFail(AlgeriaTourUtils.getString(R.string.connection_fail));
            }
        });
    }

    @Override
    public void addToFavorite(long pointId, String note) {
        AndroidNetworking.post(addToFavorite_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_USER_ID, User.getMembre().getId() + "")
                .addBodyParameter(StaticValue.PHP_POINT_ID, pointId + "")
                .addBodyParameter(StaticValue.PHP_NOTE, note)
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                presenter.onAddFavoriteResultSuccess(response);
            }

            @Override
            public void onError(ANError error) {
                Log.d("tixx", "load comment onError : " + error.getMessage());
                presenter.onAddFavoriteResultfail(AlgeriaTourUtils.getString(R.string.connection_fail));
            }
        });
    }

    @Override
    public void checkIfCommentExistAndAddIt(long pointId) {
        AndroidNetworking.post(checkComment_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_USER_ID, User.getMembre().getId() + "")
                .addBodyParameter(StaticValue.PHP_POINT_ID, pointId + "")
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt(StaticValue.JSON_NAME_SUCCESS) == 1) {
                        if (response.getInt("has") == 1) { // true
                            presenter.onCheckCommentExist(AlgeriaTourUtils.getString(R.string.comment_already_exist));
                        } else { // false
                            presenter.showCommentDialog();
                        }
                    } else {
                        presenter.onCheckCommentError(AlgeriaTourUtils.getString(R.string.server_error));
                    }
                } catch (JSONException e) {
                    Log.d("tixx", "onResponse checkIfCommentExistAndAddIt : catch " + e
                            .getMessage());
                    presenter.onCheckCommentError(AlgeriaTourUtils.getString(R.string.server_error));
                }
            }
            @Override
            public void onError(ANError error) {
                Log.d("tixx", "checkIfCommentExistAndAddIt onError : " + error.getMessage());
                presenter.onCheckCommentError(AlgeriaTourUtils.getString(R.string.connection_fail));

            }
        });
    }

    @Override
    public void addCommentaire(Commentaire commentaire) {
        AndroidNetworking.post(addComment_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_USER_ID, commentaire.getUserId() + "")
                .addBodyParameter(StaticValue.PHP_POINT_ID, commentaire.getPointInteretId() + "")
                .addBodyParameter(StaticValue.PHP_RATING, commentaire.getRatting()+"")
                .addBodyParameter(StaticValue.PHP_COMMENTAIRE, commentaire.getComment())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getInt(StaticValue.JSON_NAME_SUCCESS) == 1){

                        presenter.onAddCommentSuccess(commentaire.getPointInteretId());
                    }
                    else{
                        Log.d("tixx", "onResponse: -1 " + commentaire.getRatting()+" " + response
                                .getString(StaticValue
                                .JSON_NAME_MESSAGE));
                        presenter.onAddCommentFail(AlgeriaTourUtils.getString(R.string.error_while_adding_comment));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "onResponse exp : " + e.getMessage());

                    presenter.onAddCommentFail(AlgeriaTourUtils.getString(R.string.error_while_adding_comment));

                }
            }

            @Override
            public void onError(ANError error) {
                Log.d("tixx", "load comment onError : " + error.getMessage());
                presenter.onAddCommentFail(AlgeriaTourUtils.getString(R.string.connection_fail));
            }
        });

    }
    private Commentaire parsComment(JSONObject jsonObject) throws JSONException {
        Commentaire commentaire = new Commentaire();
        commentaire.setUserName(jsonObject.getString(StaticValue.JSON_NAME_USER_NAME));
        commentaire.setComment(jsonObject.getString(StaticValue.JSON_NAME_COMMENT_DESCREPTION));
        commentaire.setDate(jsonObject.getString(StaticValue.JSON_NAME_DATE));
        commentaire.setRatting(Float.parseFloat(jsonObject.getString(StaticValue
                .JSON_NAME_RATING)));
        commentaire.setId(jsonObject.getLong(StaticValue.JSON_NAME_ID));
        commentaire.setUserId(jsonObject.getLong(StaticValue.JSON_NAME_USER_ID));
        commentaire.setPointInteretId(jsonObject.getLong(StaticValue.JSON_NAME_POINT_ID));
        return commentaire;
    }
}
