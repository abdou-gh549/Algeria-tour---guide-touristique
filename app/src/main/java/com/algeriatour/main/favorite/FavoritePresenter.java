package com.algeriatour.main.favorite;

import android.graphics.Bitmap;

import com.algeriatour.uml_class.Favorite;

import java.util.ArrayList;

public class FavoritePresenter implements FavoriteConstraint.PresenterConstraint {
    FavoriteConstraint.ViewConstraint favoriteView;
    FavoriteModel favoriteModel;


    public FavoritePresenter(FavoriteConstraint.ViewConstraint favoriteView) {
        this.favoriteView = favoriteView;
        favoriteModel = new FavoriteModel(this);
    }

    @Override
    public void deleteFavorite(long favoriteID) {
        favoriteView.showProgressDialog();
        favoriteModel.deleteFavorite(favoriteID);
    }

    @Override
    public void onDeleteFavortieSuccess(long favoriteID) {
        favoriteView.hideProgressDialog();
        favoriteView.showSucessToast("delete success");
        favoriteView.removeFavorite(favoriteID);

    }

    @Override
    public void onDeleteFavoriteFail(String msg) {
        favoriteView.hideProgressDialog();
        favoriteView.showSucessToast(msg);
    }

    @Override
    public void loadFavoriteList() {
        favoriteView.showProgressRefresh();
        favoriteModel.loadFavoriteList();
    }

    @Override
    public void onLoadFavoriteListEmpty() {
        favoriteView.hideProgressRefresh();
        favoriteView.showInformationText("Empty");
    }

    @Override
    public void onLoadFavoriteListSuccess(ArrayList<Favorite> favorites) {
        for (int i = 0; i < favorites.size(); i++) {
            favoriteView.addFavorite(favorites.get(i));
        }
        favoriteView.hideProgressRefresh();
    }

    @Override
    public void onLoadFavoriteListFail(String msg) {
        favoriteView.hideProgressRefresh();
        favoriteView.showErrorToast(msg);
    }

    @Override
    public void updateNoteOfFavorite(long favoriteId, String newNote) {
        favoriteView.showProgressDialog();
        favoriteModel.updateNoteOfFavorite(favoriteId, newNote);
    }

    @Override
    public void onUpdateNoteSucess() {
        favoriteView.hideProgressDialog();
        favoriteView.showSucessToast("note update success");
        favoriteView.hideEditDialog();
        loadFavoriteList()  ;
    }

    @Override
    public void onUpdateNoteFail(String msg) {
        favoriteView.hideProgressDialog();
        favoriteView.showErrorToast(msg);
    }

    @Override
    public void onloadPointIneteretImageSuccess(Bitmap bitmap, long pointInetertId) {
        favoriteView.setFavoriteImage(bitmap, pointInetertId);
    }
}
