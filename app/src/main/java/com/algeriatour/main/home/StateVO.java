package com.algeriatour.main.home;

public class StateVO {
    private String title;
    private boolean selected;

    StateVO(){
        selected =  false;
        title = "item";
    }
    StateVO(String title, boolean selected){
        this.selected =  selected;
        this.title =  title;

    }
    StateVO(String title){
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