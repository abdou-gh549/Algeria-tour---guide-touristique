package com.algeriatour.main;

public class MainConstraint {

    interface IViewConstraint{
        void defaultBackPress();
        void closeDrawer();
        void startProfileActivity();
        void startContactUs();
        void exitApp();
        public void disconnect();

        void makeItVisitorDrawer();
        void makeItMembreDrawer();
        void openLoginActivity();
    }

}
