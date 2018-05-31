package com.algeriatour.uml_class;

import android.graphics.Bitmap;

public class Ville {
    private String name;
    private String wilaya;
    private String descreption;
    private float rate;
    private Bitmap image;
    private long id;

    public Ville() {
        name = "";
        wilaya = "";
        descreption = "";
        rate = 3.5f;
        image = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWilaya() {
        return wilaya;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }

    public String getDescreption() {
        return descreption;
    }

    public void setDescreption(String descreption) {
        this.descreption = descreption;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
