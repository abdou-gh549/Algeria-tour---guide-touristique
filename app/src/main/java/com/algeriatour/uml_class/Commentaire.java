package com.algeriatour.uml_class;

public class Commentaire {
    private String comment;
    private String userName;
    private String date;
    private float ratting;
    private long pointInteretId;
    private long id;
    private long userId;
    public Commentaire(){
        comment="";
        userName = "";
        userId = 0;
        id = 0;
        pointInteretId = 0;
        ratting = 0;

    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getRatting() {
        return ratting;
    }

    public void setRatting(float ratting) {
        this.ratting = ratting;
    }

    public long getPointInteretId() {
        return pointInteretId;
    }

    public void setPointInteretId(long pointInteretId) {
        this.pointInteretId = pointInteretId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
