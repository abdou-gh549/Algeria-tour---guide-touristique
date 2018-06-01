package com.algeriatour.main.favorite;

import com.algeriatour.uml_class.Favorite;

import java.util.ArrayList;

public class FavoritePresenter implements FavoriteConstraint.PresenterConstraint {
    FavoriteConstraint.ViewConstraint favoriteView;

    public FavoritePresenter(FavoriteConstraint.ViewConstraint favoriteView) {
        this.favoriteView = favoriteView;
    }

    @Override
    public void deleteFavorite(long favoriteID) {

    }

    @Override
    public void onDeleteFavortieSuccess() {

    }

    @Override
    public void onDeleteFavoriteFail(String msg) {

    }

    @Override
    public void loadFavoriteList() {

    }

    @Override
    public void onLoadFavoriteListSuccess(ArrayList<Favorite> favorites) {

    }

    @Override
    public void onLoadFavoriteListFail() {

    }

    @Override
    public void updateNoteOfFavorite(long favoriteId, String newNote) {

    }

    @Override
    public void onUpdateNoteSucess() {

    }

    @Override
    public void onUpdateNoteFail(String msg) {

    }
}
