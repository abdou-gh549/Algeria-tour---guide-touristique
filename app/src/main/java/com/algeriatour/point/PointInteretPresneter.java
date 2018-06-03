package com.algeriatour.point;

import android.graphics.Bitmap;
import android.util.Log;

import com.algeriatour.profile.ProfileConstraint;
import com.algeriatour.uml_class.Commentaire;
import com.algeriatour.uml_class.PointInteret;
import com.algeriatour.utils.StaticValue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PointInteretPresneter implements PointIneteretConstraint.PresenterConstraint{
    PointIneteretConstraint.ViewConstraint pointInetertView;
    PointInteretModel pointInteretModel;
    public PointInteretPresneter(PointIneteretConstraint.ViewConstraint pointInetertView) {
        this.pointInetertView = pointInetertView;
        pointInteretModel = new PointInteretModel(this);
    }


    @Override
    public void onloadPointIneteretImageSuccess(Bitmap image) {
        pointInetertView.setPointInteretImage(image);
    }

    @Override
    public void onloadPointIneteretImageFail(String msg) {
        pointInetertView.showToastError(msg);
    }

    @Override
    public void onLoadCommentaireSucess(ArrayList<Commentaire> pointInterets) {
        // todo
        for (int i = 0; i < pointInterets.size(); i++) {
            pointInetertView.addComment(pointInterets.get(i));
        }
        pointInetertView.hideProgressBar();
    }

    @Override
    public void onLoadCommentaireFail(String msg) {
        pointInetertView.hideProgressBar();
        pointInetertView.showTextInDispalyInfor(msg);
    }

    @Override
    public void onLoadEmptyCommentaire() {
        pointInetertView.hideProgressBar();
        pointInetertView.showTextInDispalyInfor("empty ");
    }

    @Override
    public void loadPointIneteretImage(long id) {
        pointInteretModel.loadPointIneteretImage(id);
    }

    @Override
    public void loadCommentaire(long id) {
        pointInetertView.showProgressBar();
        pointInteretModel.loadCommentaire(id);
    }

    @Override
    public void checkIfFavoriteExist(long pointId) {
        pointInetertView.showProgressDialog();
        pointInteretModel.favoriteAlreadyAddedCheck(pointId);
    }

    @Override
    public void addToFavorite(long pointId, String note) {
        pointInetertView.showProgressDialog();
        pointInteretModel.addToFavorite(pointId, note);
    }

    @Override
    public void onFavoriteAlreadyExist() {
        pointInetertView.hideProgressDialog();
        pointInetertView.showNotificationTaost("this point already added to favorite");
    }

    @Override
    public void onFavoriteDoesNoteExist() {
        pointInetertView.hideProgressDialog();
        pointInetertView.showAddFavoriteDialog();
    }

    @Override
    public void onFavoriteCheckFail(String msg) {
        pointInetertView.hideProgressDialog();
        pointInetertView.showToastError(msg);
    }

    @Override
    public void onAddFavoriteResultSuccess(JSONObject response) {

        try {
            if (response.getInt(StaticValue.JSON_NAME_SUCCESS) == 1) {
                pointInetertView.showToastSuccess("point added to favorite");
                pointInetertView.hideAddFavoriteDialog();
            } else {
                pointInetertView.showToastError("server error , try again later");
                Log.d("tixx", "onAddFavoriteResultSuccess -1 : " + response.getString(StaticValue
                        .JSON_NAME_MESSAGE));
            }
        } catch (JSONException e) {
            Log.d("tixx", "onResponse check favorite: catch " + e.getMessage());
            pointInetertView.showToastError("oops something happened ");
        }
        pointInetertView.hideProgressDialog();
    }

    @Override
    public void onAddFavoriteResultfail(String msg) {
        pointInetertView.hideProgressDialog();
        pointInetertView.showToastError(msg);
    }

    @Override
    public void addCommentClicked(long id) {
        pointInetertView.showProgressDialog();
        pointInteretModel.checkIfCommentExistAndAddIt(id);
    }

    @Override
    public void onCheckCommentError(String msg) {
        pointInetertView.hideProgressDialog();
        pointInetertView.showToastError(msg);

    }

    @Override
    public void onCheckCommentExist(String msg) {
        pointInetertView.hideProgressDialog();
        pointInetertView.hideAddCommentDialog();
        pointInetertView.showToastInformation(msg);
    }

    @Override
    public void onAddCommentSuccess(long pointInteretId) {
        pointInetertView.hideProgressDialog();
        pointInetertView.hideAddCommentDialog();
        loadCommentaire(pointInteretId);
        pointInetertView.showToastSuccess("comment added");
    }

    @Override
    public void onAddCommentFail(String msg) {
        pointInetertView.hideProgressDialog();
        pointInetertView.showToastError(msg);
    }

    @Override
    public void showCommentDialog() {
        pointInetertView.showAddCommentDialog();
    }

    @Override
    public void addComment(Commentaire commentaire) {
        pointInteretModel.addCommentaire(commentaire);
    }
}
