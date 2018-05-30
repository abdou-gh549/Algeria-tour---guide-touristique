package com.algeriatour.login;

public class LoginConstraint {

    public interface viewConstraint{
        String getPseudo();
        String getPassword();
        String getStringFromRessource(int id);
        void showPseudoError(String message);
        void showPasswordError(String message);

        void showLoginFailed(String msg);
        void showLoginSucess(String msg);
        void startMainActiviy(String userPseudo, String psw, String email);
        void startMainActiviy();
        void showProgressDialog();
        void hideProgressDialog();

    }

    public interface modelConstraint{
        void doLogin(String pseudo, String psw);
    }

    public interface PresenterConstraint{
        void doAuthentification();
        void onLoginSucess(String pseudo, String psw, String email);
        void onLoginFail(String errorMessage);
    }
}
