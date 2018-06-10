package com.algeriatour.login;

import com.algeriatour.uml_class.Membre;

public class LoginConstraint {

    public interface viewConstraint{
        String getPseudo();
        String getPassword();
        String getStringFromRessource(int id);
        void showPseudoError(String message);
        void showPasswordError(String message);

        void showLoginFailed(String msg);
        void showLoginSucess(String msg);
        void startMainActiviy(Membre membre);
        void startMainActiviy();
        void showProgressDialog();
        void hideProgressDialog();

    }

    public interface modelConstraint{
        void doLogin(String pseudo, String psw);
    }

    public interface PresenterConstraint{
        void doAuthentification();
        void onLoginSucess(Membre membre);
        void onLoginFail(String errorMessage);
    }
}
