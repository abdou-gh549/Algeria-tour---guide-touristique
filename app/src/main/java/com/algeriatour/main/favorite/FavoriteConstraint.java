package com.algeriatour.main.favorite;

import android.graphics.Bitmap;

import com.algeriatour.uml_class.Favorite;

import java.util.ArrayList;

public class FavoriteConstraint {

    public interface ViewConstraint {
        void addFavorite(Favorite favorite);

        void setFavoriteImage(Bitmap image, long position);

        void deleteFavoriteClick(long favoriteId);

        void showProgressRefresh();

        void hideProgressRefresh();

        void showProgressDialog();

        void hideProgressDialog();

        void showInformationText(String msg);

        void hideInformationText();

        void showErrorToast(String msg);

        void showSucessToast(String msg);

        void removeFavorite(long favoriteID);

        void showInformationToast(String s);

        void editFavoriteClick(Favorite favorite);

        void hideEditDialog();
    }

    public interface PresenterConstraint {
        void deleteFavorite(long favoriteID);

        void onDeleteFavortieSuccess(long favoriteId);

        void onDeleteFavoriteFail(String msg);

        void loadFavoriteList();

        void onLoadFavoriteListEmpty();

        void onLoadFavoriteListSuccess(ArrayList<Favorite> favorites);

        void onLoadFavoriteListFail(String msg);

        void updateNoteOfFavorite(long favoriteId, String newNote);

        void onUpdateNoteSucess();

        void onUpdateNoteFail(String msg);

        void onloadPointIneteretImageSuccess(Bitmap bitmap, long pointInetertId);
    }

    public interface ModelConstraint {
        void loadFavoriteList();

        void updateNoteOfFavorite(long favoriteId, String note);

        void deleteFavorite(long FavoriteId);
        void loadPointIneteretImage(long pointInetertId);
    }
}
