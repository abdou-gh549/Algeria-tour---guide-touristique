package com.algeriatour.uml_class;

import android.graphics.Bitmap;


public class Ville extends PlaceInfo{

    private Bitmap image;
    public Ville() {

        image = null;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


}
