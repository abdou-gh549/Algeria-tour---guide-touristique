package com.algeriatour.point;

import android.graphics.Bitmap;

import com.algeriatour.uml_class.Commentaire;
import com.algeriatour.uml_class.PointInteret;

import org.json.JSONObject;

import java.util.ArrayList;

public class PointIneteretConstraint {
    public interface ViewConstraint {

        void showProgressBar();

        void hideProgressBar();
        void hideProgressDialog();
        void showProgressDialog();
        void showAddFavoriteDialog();
        void hideAddFavoriteDialog();

        void setPointInteretImage(Bitmap villeImage);

        void showToastInformation(String msg);

        void addComment(Commentaire pointInteret);

        void showToastError(String msg);

        void showTextInDispalyInfor(String msg);

        void showNotificationTaost(String msg);

        void showToastSuccess(String msg);

        void showAddCommentDialog();

        void hideAddCommentDialog();
    }

    public interface ModelConstraint {
        void loadPointIneteretImage(long pointInetertId);

        void loadCommentaire(long pointInetertId);

        void favoriteAlreadyAddedCheck(long pointId);

        void addToFavorite(long pointId, String note);

        void checkIfCommentExistAndAddIt(long pontID);
        void addCommentaire(Commentaire commentaire);
    }

    public interface PresenterConstraint {
        void onloadPointIneteretImageSuccess(Bitmap image);
        void onloadPointIneteretImageFail(String msg);
        void onLoadCommentaireSucess(ArrayList<Commentaire> pointInterets);
        void onLoadCommentaireFail(String msg);
        void onLoadEmptyCommentaire();
        void loadPointIneteretImage(long id);
        void loadCommentaire(long id);

        void checkIfFavoriteExist(long favoriteId);
        void addToFavorite(long pointId, String note);

        void onFavoriteAlreadyExist();

        void onFavoriteDoesNoteExist();

        void onFavoriteCheckFail(String msg);

        void onAddFavoriteResultSuccess(JSONObject response);

        void onAddFavoriteResultfail(String msg);

        void addCommentClicked(long commentaire);


        void onCheckCommentError(String msg);

        void onCheckCommentExist(String msg);

        void onAddCommentSuccess(long pointInteretId);

        void onAddCommentFail(String msg);

        void showCommentDialog();

        void addComment(Commentaire commentaire);
    }
}
