package com.algeriatour.signin;


public class SignInConstraint {
    public interface ViewConstraint{
        void showPseudoFieldError(String msg);
        void showEmailFieldError(String msg);
        void showPasswordFieldError(String msg);
        void showConfirmationPasswordFieldError(String msg);
        void signInFail(String msg);
        void signInSuccess();
        String getPseudo();
        String getEmail();
        String getPassword();
        String getConfirmationPassword();
        String getStringFromResource(int id);
    }

    public interface  ModelConstraint{
        boolean emailAlreadyExist(String Email);
        boolean pseudoAlreadyExist(String Email);
        boolean signIn(String pseudo, String email, String password);

    }
}
