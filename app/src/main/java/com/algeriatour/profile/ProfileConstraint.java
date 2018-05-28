package com.algeriatour.profile;

public class ProfileConstraint {

    public interface ViewConstrain{
        void showEmailError(String msg);
        void showPasswordError(String msg);
        void showUserNameError(String msg);
        void showToastError(String msg);
        void showToastSuccess(String msg);
        void enableField(int fieldNumber);



        String getEmail();
        String getPassword();
        String getUserName();

        String getStringFromRessource(int stringId);
    }


    public interface ModelConstraint {

        boolean userNameExist(String userName);
        boolean emailExist(String email);

        boolean changePassword(int userId, String psw);
        boolean changeEmail(int userId, String email);
        boolean changePseudo(int userId, String pseudo);

    }
}
