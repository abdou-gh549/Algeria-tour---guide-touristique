package com.algeriatour.profile;

import com.algeriatour.R;
import com.algeriatour.uml_class.Membre;
import com.algeriatour.utils.FormatValidator;

public class ProfilePresenter implements ProfileConstraint.PresenterConstraint {

    ProfileConstraint.ViewConstrain profile_view;
    ProfileModel profile_model;

    String originaleEmail;
    String originalPassword;

    public ProfilePresenter(ProfileConstraint.ViewConstrain profile_view) {
        this.profile_view = profile_view;

        profile_model = new ProfileModel(this);
    }

    @Override
    public void onSaveClick() {
        if (!checkInput()) {
            return;
        }

        // check if any information have been changed

        String email = profile_view.getEmail();
        String passwrod = profile_view.getPassword();
        String pseudo = profile_view.getPseudo();

        String passwrodToChange = "";
        String emailToChange = "";
        if(originaleEmail.equals(email) && originalPassword.equals(passwrod)){
            profile_view.showToastInformation("nothing to change");
        }else{
            emailToChange = originaleEmail.equals(email) ? "":email;
            passwrodToChange = originalPassword.equals(passwrod) ? "":passwrod;
            profile_view.showProgressDialog();
            profile_model.change(pseudo, emailToChange, passwrodToChange);
        }



        // every thing well
        // find wich data are changed and change it

    }

    @Override
    public void onLoadProfileDataSuccess(Membre membre) {
        originaleEmail = membre.getEmail();
        originalPassword = membre.getPassword();
        profile_view.setPseudo(membre.getPseudo());
        profile_view.setEmail(membre.getEmail());
        profile_view.setPassword(membre.getPassword());
        profile_view.setInscreptionDate(membre.getInscreptionDate());
        profile_view.hideProgressDialog();

    }

    @Override
    public void onLoadProfileDataFail(String msg) {
        profile_view.hideProgressDialog();
        profile_view.showToastError(msg);
        profile_view.finishAcitivity();
    }

    @Override
    public void loadProfileData(String pseudo, String psw) {
        profile_view.showProgressDialog();
        profile_model.loadUserInfo(pseudo, psw);
    }

    @Override
    public void onChangeFail(String msg) {
        profile_view.hideProgressDialog();
        profile_view.showToastError(msg);

    }

    @Override
    public void onChangeSucess(String msg) {
        profile_view.hideProgressDialog();
        profile_view.showToastSuccess(msg);
        profile_view.startMainActivityAfterChange(profile_view.getPseudo(),
                                                  profile_view.getPassword(),
                                                  profile_view.getEmail());
    }

    private boolean checkInput() {
        boolean inputValid = true;
        String email = profile_view.getEmail();
        String psw = profile_view.getPassword();
        if (email.isEmpty()) {
            profile_view.showEmailError(profile_view.getStringFromRessource(R.string.empty_email_error));
            inputValid = false;
        } else if (!FormatValidator.isValidEmail(email)) {
            profile_view.showEmailError(profile_view.getStringFromRessource(R.string.invalid_email_format_error));
            inputValid = false;
        }

        if (psw.isEmpty()) {
            profile_view.showPasswordError(profile_view.getStringFromRessource(R.string.empty_password_error));
            inputValid = false;
        } else if (psw.length() < 6) {
            profile_view.showPasswordError(profile_view.getStringFromRessource(R.string.short_password_error));
            inputValid = false;
        }


        return inputValid;
    }


}
