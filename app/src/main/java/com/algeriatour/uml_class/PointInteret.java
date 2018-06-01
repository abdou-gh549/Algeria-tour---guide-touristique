package com.algeriatour.uml_class;

import android.graphics.Bitmap;

public class PointInteret extends PlaceInfo{

    private Bitmap image;
    public PointInteret() {
        image = null;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


}
