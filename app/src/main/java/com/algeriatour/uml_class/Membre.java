package com.algeriatour.uml_class;

public class Membre {
    private long   id;
    private String pseudo;
    private String email;
    private String password;
    private String inscreptionDate;

    public Membre() {
        id = 0;
        pseudo = "";
        email  = "";
        password = "";
        inscreptionDate = "";
    }

    public Membre(String pseudo, String email, String password, String inscreptionDate, long id) {
        this.pseudo = pseudo;
        this.email = email;
        this.password = password;
        this.inscreptionDate = inscreptionDate;
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInscreptionDate() {
        return inscreptionDate;
    }

    public void setInscreptionDate(String inscreptionDate) {
        this.inscreptionDate = inscreptionDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
