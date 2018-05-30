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
        void showProgressDialog();
        void hideProgressDialog();
    }

    public interface  ModelConstraint{
        void signIn(String pseudo, String email, String password);
    }
    public interface PresenterConstraint{
        void onSignInSuccess();
        void onSignInFail(String mgs);

    }
}
