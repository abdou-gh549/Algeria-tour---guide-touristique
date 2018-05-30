package com.algeriatour.resetpassword;

public class ResetPasswordConstraint {

    public interface ViewConstraint{
            String getEmail();
            String getPseudo();
            void showEmailError(String msg);
            void showPseudoError(String msg);
            void sendPassword(String email, String password);
            void showToastError(String msg);
            String getStringFromRessource(int stringId);

    }
}
