package com.algeriatour.uml_class;

public class Favorite {
    private PointInteret pointInteret;
    private String note;

    public Favorite() {
        pointInteret = new PointInteret();
        note = "";
    }

    public PointInteret getPointInteret() {
        return pointInteret;
    }

    public void setPointInteret(PointInteret pointInteret) {
        this.pointInteret = pointInteret;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
