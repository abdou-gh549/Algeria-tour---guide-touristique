package com.algeriatour.villes;

import android.graphics.Bitmap;

import com.algeriatour.uml_class.PointInteret;
import com.algeriatour.uml_class.Ville;

import java.util.ArrayList;

public class VilleConstraint {
    public interface ViewConstraint {

        void showProgressBar();

        void hideProgressBar();

        void setVilleImage(Bitmap villeImage);

        void addPointInteret(PointInteret pointInteret);
        void setPointInteretImage(PointInteret pointInteret, int position);
        void showToastError(String msg);
        void showTextInDispalyInfor(String msg);
    }

    public interface ModelConstraint {
        void loadPointInteret(long villeId);

        void loadPointIneteretImage(PointInteret pointInteret, int position);

        void loadVilleImage(Ville ville);
    }

    public interface PresenterConstraint {
        void onLoadPointIntereSuccess(ArrayList<PointInteret> pointInterets);

        void onLoadPointIntereFail(String msg);

        void onLoadPointInteretImageSucess(PointInteret pointInteret, int position);

        void onLoadVilleImageSuccess(Bitmap image);

        void onLoadVilleImageFail(String msg);

        void onLoadEmptyPointInteret();
    }
}
