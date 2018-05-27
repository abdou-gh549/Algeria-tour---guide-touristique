package com.algeriatour.login;

import com.algeriatour.R;
import com.algeriatour.utils.FormatValidator;

public class LoginPresenter {

    private LoginConstraint.viewConstraint loginView;
    private LoginModel loginModel;

    public LoginPresenter(LoginConstraint.viewConstraint loginVeiw) {
        this.loginView = loginVeiw;
        loginModel = new LoginModel();
    }

    void authentificationButtonClicked() {
        String email = loginView.getEmail();
        String psw = loginView.getEmail();

        // if input are not valid quit function
        if( ! checkInput(email, psw) )
            return;

        if( loginModel.userExist(email, psw) ){
            loginView.showLoginSucess( "Login Success !");
            loginView.startMainActiviy(email);
        }else{
            loginView.showLoginFailed("User doesn't exist !");
        }

    }

    private boolean checkInput(String email, String psw) {

        boolean inputValid = true;
        if (email.isEmpty()) {
            loginView.showEmailError(loginView.getStringFromRessource(R.string.empty_email_error));
            inputValid = false;
        } else if (!FormatValidator.isValidEmail(email)) {
            loginView.showEmailError(loginView.getStringFromRessource(R.string.invalid_email_format_error));
            inputValid = false;
        }

        if (psw.isEmpty()) {
            loginView.showPasswordError(loginView.getStringFromRessource(R.string.empty_password_error));
            inputValid = false;
        } else if (psw.length() < 6) {
            loginView.showPasswordError(loginView.getStringFromRessource(R.string.short_password_error));
            inputValid = false;
        }
        return inputValid;
    }
}
