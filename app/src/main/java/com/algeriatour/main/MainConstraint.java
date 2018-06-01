package com.algeriatour.main;

public class MainConstraint {

    interface IViewConstraint{
        void defaultBackPress();
        void closeDrawer();
        void startProfileActivity();
        void startContactUs();
        void exitApp();
        void disconnect();

        void makeItVisitorDrawer();
        void makeItMembreDrawer();
        void openLoginActivity();
        void setDrawerEmail(String email);
        void setDrawerPseudo(String pseudo);
    }

}
