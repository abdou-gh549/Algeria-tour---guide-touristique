package com.algeriatour.villes;

import android.graphics.Bitmap;

public class VilleConstraint {
    public interface ViewConstraint{
        void setVilleImage(Bitmap image);
        void setVilleName(String name);
        void setVilleDescreption(String descreption);
        void addCentreInteret(/* class de type CentreInteret*/);
    }
    public interface ModelConstrain{
        void getVilleInformation(int villeId);
    }

}
