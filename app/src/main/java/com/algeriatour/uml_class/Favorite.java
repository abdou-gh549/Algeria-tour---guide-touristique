package com.algeriatour.uml_class;

public class Favorite {
    long favoriteId;
    private PointInteret pointInteret;
    private String note;

    public Favorite() {
        pointInteret = new PointInteret();
        note = "";
    }

    public long getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(long favoriteId) {
        this.favoriteId = favoriteId;
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
