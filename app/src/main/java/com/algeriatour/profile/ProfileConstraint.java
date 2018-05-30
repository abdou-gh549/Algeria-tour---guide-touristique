package com.algeriatour.profile;

import com.algeriatour.uml_class.Membre;

public class ProfileConstraint {

    public interface ViewConstrain{
        void showEmailError(String msg);
        void showPasswordError(String msg);
        void showToastError(String msg);
        void showToastSuccess(String msg);
        void showToastInformation(String msg);
        void setPseudo(String pseudo);
        void setInscreptionDate(String inscreptionDate);
        void setEmail(String email);
        void setPassword(String psw);
        void showProgressDialog();
        void hideProgressDialog();

        String getEmail();
        String getPassword();
        String getPseudo();

        String getStringFromRessource(int stringId);

        void finishAcitivity();

        void startMainActivityAfterChange(String pseudo, String password, String email);
    }


    public interface ModelConstraint {
        void change(String pseudo, String email, String password);
        void loadUserInfo(String pseudo, String password);
    }
    public interface PresenterConstraint {
        void onSaveClick();
        void onLoadProfileDataSuccess(Membre membre);
        void onLoadProfileDataFail(String msg);
        void loadProfileData(String pseudo, String psw);
        void onChangeFail(String msg);
        void onChangeSucess(String msg);

    }
}
