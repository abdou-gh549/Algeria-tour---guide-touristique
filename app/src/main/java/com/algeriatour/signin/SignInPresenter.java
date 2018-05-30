package com.algeriatour.signin;


import com.algeriatour.R;
import com.algeriatour.utils.FormatValidator;

public class SignInPresenter  implements SignInConstraint.PresenterConstraint{
    private SignInConstraint.ViewConstraint signInView;
    private SignInModel signInModel;

    public SignInPresenter(SignInConstraint.ViewConstraint signInView) {
        this.signInView = signInView;
        signInModel = new SignInModel(this);
    }


    public void signIn() {
        if(!checkInput())
            return;
        // signInProcess
        String pseudo = signInView.getPseudo();
        String email = signInView.getEmail();
        String password = signInView.getPassword();
        signInView.showProgressDialog();
        signInModel.signIn(pseudo, email, password);
    }

    private boolean checkInput(){
        // pseudo check
        int inputTocheck = 4;
        int inputValid = 0;
        if( signInView.getPseudo().isEmpty()){
            signInView.showPseudoFieldError( signInView.getStringFromResource(R.string.empy_pseudo_error) );
        }else if( signInView.getPseudo().contains(" ")){
            signInView.showPseudoFieldError( signInView.getStringFromResource(R.string
                    .space_pseudo_error) );
        }else inputValid++;
        // email check
        if(signInView.getEmail().isEmpty()){
            signInView.showEmailFieldError( signInView.getStringFromResource(R.string.empy_pseudo_error) );
        }else if(! FormatValidator.isValidEmail(signInView.getEmail()) ){
            signInView.showEmailFieldError( signInView.getStringFromResource(R.string
                    .invalid_email_format_error) );
        }else inputValid++;
        // password check
        if(signInView.getPassword().isEmpty()){
            signInView.showPasswordFieldError(signInView.getStringFromResource(R.string.empty_password_error));
        }else if(signInView.getPassword().length() < 6){
            signInView.showPasswordFieldError(signInView.getStringFromResource(R.string.short_password_error));
        }else inputValid++;
        // confirmation password check
        if(! signInView.getConfirmationPassword().equals( signInView.getPassword())){
            signInView.showConfirmationPasswordFieldError(signInView.getStringFromResource(R.string
                    .unmatch_password_error));
        }else inputValid++;

        return inputTocheck == inputValid;
    }

    @Override
    public void onSignInSuccess() {
        signInView.hideProgressDialog();
        signInView.signInSuccess();
    }

    @Override
    public void onSignInFail(String msg) {
        signInView.hideProgressDialog();
        signInView.signInFail(msg);
    }
}
