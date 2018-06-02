package com.algeriatour.uml_class;

import java.io.Serializable;

public class PlaceInfo implements Serializable {
    private String name;
    private String wilaya;
    private String ville;
    private String descreption;
    private float rate;
    private long id;
    private String type;

    public PlaceInfo() {
        name = "";
        wilaya = "";
        ville = "";
        descreption = "";
        rate = 3.5f;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public PlaceInfo getPlaceInfo() {
        PlaceInfo placeInfo = new PlaceInfo();
        placeInfo.setName(name);
        placeInfo.setId(id);
        placeInfo.setDescreption(descreption);
        placeInfo.setWilaya(wilaya);
        placeInfo.setRate(rate);
        placeInfo.setType(type);
        placeInfo.setVille(ville);
        return placeInfo;
    }

    public void setPlaceInfo(PlaceInfo placeInfo) {
        name = placeInfo.getName();
        id = placeInfo.getId();
        descreption = placeInfo.getDescreption();
        wilaya = placeInfo.getWilaya();
        rate = placeInfo.getRate();
        type = placeInfo.getType();
        ville = placeInfo.getVille();
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCompletAdress(){
        return ville + " " + wilaya;
    }
}
