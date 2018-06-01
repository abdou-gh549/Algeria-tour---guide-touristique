package com.algeriatour.point;

import android.graphics.Bitmap;

import com.algeriatour.profile.ProfileConstraint;
import com.algeriatour.uml_class.Commentaire;
import com.algeriatour.uml_class.PointInteret;

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
}
