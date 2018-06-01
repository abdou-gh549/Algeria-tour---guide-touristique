package com.algeriatour.point;

import android.graphics.Bitmap;

import com.algeriatour.uml_class.Commentaire;
import com.algeriatour.uml_class.PointInteret;

import java.util.ArrayList;

public class PointIneteretConstraint {
    public interface ViewConstraint {

        void showProgressBar();

        void hideProgressBar();

        void setPointInteretImage(Bitmap villeImage);

        void showToastInformation(String msg);

        void addComment(Commentaire pointInteret);

        void showToastError(String msg);

        void showTextInDispalyInfor(String msg);
    }

    public interface ModelConstraint {
        void loadPointIneteretImage(long pointInetertId);

        void loadCommentaire(long pointInetertId);
    }

    public interface PresenterConstraint {
        void onloadPointIneteretImageSuccess(Bitmap image);
        void onloadPointIneteretImageFail(String msg);
        void onLoadCommentaireSucess(ArrayList<Commentaire> pointInterets);
        void onLoadCommentaireFail(String msg);
        void onLoadEmptyCommentaire();
        void loadPointIneteretImage(long id);
        void loadCommentaire(long id);
    }
}
