package com.algeriatour.login;

public class LoginConstraint {

    public interface viewConstraint{
        String getEmail();
        String getPassword();
        String getStringFromRessource(int id);
        void showEmailError(String message);
        void showPasswordError(String message);

        void showLoginFailed(String msg);
        void showLoginSucess(String msg);
        void startMainActiviy(String userI);

    }

    public interface modelConstraint{
        boolean userExist(String email, String psw);

    }
}
