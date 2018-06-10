package com.algeriatour.villes;

import android.graphics.Bitmap;

import com.algeriatour.R;
import com.algeriatour.uml_class.PointInteret;
import com.algeriatour.uml_class.Ville;
import com.algeriatour.utils.AlgeriaTourUtils;

import java.util.ArrayList;

public class VillePresenter implements VilleConstraint.PresenterConstraint {


    private VilleConstraint.ViewConstraint ville_view;
    private VilleModel villeModel;

    public VillePresenter(VilleConstraint.ViewConstraint ville_view) {
        this.ville_view = ville_view;
        villeModel = new VilleModel(this);
    }

    public void loadPointIntere(long villeId) {
        ville_view.hideRefreshLayout();
        villeModel.loadPointInteret(villeId);
    }

    public void loadVilleImage(Ville ville){
        villeModel.loadVilleImage(ville);
    }

    @Override
    public void onLoadPointIntereSuccess(ArrayList<PointInteret> pointInterets) {
        for (int i = 0; i < pointInterets.size(); i++) {
            ville_view.addPointInteret(pointInterets.get(i));
        }
        ville_view.showRefreshLayout();
    }

    @Override
    public void onLoadPointIntereFail(String msg) {
        ville_view.showRefreshLayout();
        ville_view.showToastError(msg);
    }

    @Override
    public void onLoadPointInteretImageSucess(PointInteret pointInteret, int position) {
            ville_view.setPointInteretImage(pointInteret, position);
    }

    @Override
    public void onLoadVilleImageSuccess(Bitmap image) {
        ville_view.setVilleImage(image);
    }

    @Override
    public void onLoadVilleImageFail(String msg) {
        ville_view.showToastError(msg);
    }

    @Override
    public void onLoadEmptyPointInteret() {
        ville_view.showTextInDispalyInfor(AlgeriaTourUtils.getString(R.string.empty));
    }
}
