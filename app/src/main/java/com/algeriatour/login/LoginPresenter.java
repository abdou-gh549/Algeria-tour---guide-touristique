package com.algeriatour.login;

import com.algeriatour.R;
import com.algeriatour.uml_class.Membre;

public class LoginPresenter implements LoginConstraint.PresenterConstraint{

    private LoginConstraint.viewConstraint loginView;
    private LoginModel loginModel;

    public LoginPresenter(LoginConstraint.viewConstraint loginVeiw) {
        this.loginView = loginVeiw;
        loginModel = new LoginModel(this);
    }

    @Override
    public void doAuthentification() {
        String pseudo = loginView.getPseudo();
        String psw = loginView.getPassword();

        // if input are not valid quit function
        if( ! checkInput(pseudo, psw) )
            return;
        loginView.showProgressDialog();
        loginModel.doLogin(pseudo, psw);
    }



    @Override
    public void onLoginSucess(Membre membre) {
        loginView.hideProgressDialog();
        loginView.showLoginSucess( "Login Success !");
        loginView.startMainActiviy(membre);
    }

    @Override
    public void onLoginFail(String errorMsg) {
        loginView.hideProgressDialog();
        loginView.showLoginFailed(errorMsg);
    }


    private boolean checkInput(String pseudo, String psw) {

        boolean inputValid = true;
        if (pseudo.isEmpty()) {
            loginView.showPseudoError(loginView.getStringFromRessource(R.string.empy_pseudo_error));
            inputValid = false;
        } else if (pseudo.contains(" ")) {
            loginView.showPseudoError(loginView.getStringFromRessource(R.string.space_pseudo_error));
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
