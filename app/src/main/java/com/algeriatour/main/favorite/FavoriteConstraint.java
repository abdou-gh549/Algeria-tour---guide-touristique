package com.algeriatour.main.favorite;

import com.algeriatour.uml_class.Favorite;

import java.util.ArrayList;

public class FavoriteConstraint {

    public interface ViewConstraint{
        void showProgressBar();
        void hideProgressBar();
        void showInformationText(String msg);
        void hideInformationText();
        void addFavorite(Favorite favorite);
        void showErrorToast(String msg);
        void showSucessToast(String msg);
    }

    public interface PresenterConstraint{
        void deleteFavorite(long favoriteID);
        void onDeleteFavortieSuccess();
        void onDeleteFavoriteFail(String msg);
        void loadFavoriteList();
        void onLoadFavoriteListSuccess(ArrayList<Favorite> favorites);
        void onLoadFavoriteListFail();
        void updateNoteOfFavorite(long favoriteId, String newNote);
        void onUpdateNoteSucess();
        void onUpdateNoteFail(String msg);
    }

    public interface ModelConstraint{
        void loadFavoriteList();
        void updateNoteOfFavorite(long favoriteId, String note);
        void deleteFavorite(long FavoriteId);
    }
}
