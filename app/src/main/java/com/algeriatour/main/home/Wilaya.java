package com.algeriatour.main.home;

public class Wilaya {
    private String title;
    private boolean selected;

    Wilaya(){
        selected =  false;
        title = "item";
    }
    Wilaya(String title, boolean selected){
        this.selected =  selected;
        this.title =  title;

    }
    Wilaya(String title){
        this.title =  title;
        this.selected =  false;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}