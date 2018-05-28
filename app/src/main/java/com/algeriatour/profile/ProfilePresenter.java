package com.algeriatour.profile;

import com.algeriatour.R;
import com.algeriatour.utils.FormatValidator;

public class ProfilePresenter {

    ProfileConstraint.ViewConstrain profile_view;
    ProfileModel profile_model;

    public ProfilePresenter(ProfileConstraint.ViewConstrain profile_view) {
        this.profile_view = profile_view;

        profile_model = new ProfileModel();
    }

    public void onSaveClick() {
        if (! checkInput()) {
            return;
        }

        // check if any information have been changed

        String email = profile_view.getEmail();
        String passwrod = profile_view.getPassword();
        String userName = profile_view.getUserName();
        // check if email exist
        if (profile_model.emailExist(email)) {
            profile_view.showToastError("email already exist");
            return;
        } else if (profile_model.userNameExist(userName)) {
            profile_view.showToastError("user name already exist");
            return;
        }

        // every thing well
        // find wich data are changed and change it

        changeEmail(0, "new Email");
        changePassword(0, "new password");
        changePseudo(0, "new pseudo");
    }

    private boolean checkInput() {
        boolean inputValid = true;
        String email = profile_view.getEmail();
        String userName = profile_view.getUserName();
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

        if (userName.isEmpty()) {
            profile_view.showUserNameError(profile_view.getStringFromRessource(R.string.empty_email_error));
            inputValid = false;
        } else if (userName.contains(" ")) {
            profile_view.showUserNameError(profile_view.getStringFromRessource(R.string.space_pseudo_error));
            inputValid = false;
        }

        return inputValid;
    }

    private void changePassword(int userId, String newPsw){
        if(profile_model.changePassword(userId, newPsw)){
            profile_view.showToastSuccess("password changed success");
        }else{
            profile_view.showToastError("password changed fail");
        }
    }

    private void changeEmail(int userId, String newEmail){
        if(profile_model.changeEmail(userId, newEmail)){
            profile_view.showToastSuccess("email changed success");
        }else{
            profile_view.showToastError("email changed fail");
        }
    }

    private void changePseudo(int userId, String newPseudo){
        if(profile_model.changePseudo(userId, newPseudo)){
            profile_view.showToastSuccess("pseudo changed success");
        }else{
            profile_view.showToastError("pseudo changed fail");
        }
    }
}
