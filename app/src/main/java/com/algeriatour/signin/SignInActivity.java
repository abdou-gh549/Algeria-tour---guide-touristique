package com.algeriatour.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.algeriatour.R;
import com.algeriatour.utils.StaticValue;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class SignInActivity extends AppCompatActivity implements SignInConstraint.ViewConstraint {

    /// views
    @BindView(R.id.signin_pseudo_inp)
    MaterialEditText pseudoField;
    @BindView(R.id.signin_email_inp)
    MaterialEditText emailField;
    @BindView(R.id.signin_password_inp)
    MaterialEditText passwordField;
    @BindView(R.id.signin_confirmation_psw_inp)
    MaterialEditText confirmationPasswordField;


    //other declaration

    SignInPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);
        ButterKnife.bind(this);
        presenter = new SignInPresenter(this);
    }

    @OnClick(R.id.signin_back_btn)
    public void onBackButtonClicked() {
        finish();
    }

    @OnClick(R.id.signin_signin_button)
    void onSignINButtonClicked() {
        presenter.signIn();
    }

    @Override
    public String getStringFromResource(int id) {
        return getString(id);
    }

    @Override
    public void showPseudoFieldError(String msg) {
        pseudoField.setError(msg);
    }

    @Override
    public void showEmailFieldError(String msg) {
        emailField.setError(msg);
    }

    @Override
    public void showPasswordFieldError(String msg) {
        passwordField.setError(msg);
    }

    @Override
    public void showConfirmationPasswordFieldError(String msg) {
        confirmationPasswordField.setError(msg);
    }

    @Override
    public void signInFail(String msg) {
        Toasty.error(this, msg, Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void signInSuccess() {
        Toasty.success(this, getString(R.string.signIn_sucess), Toast.LENGTH_LONG, true).show();
        Intent intent = new Intent();
        intent.putExtra(StaticValue.EMAIL_TAGE, getEmail());
        setResult(StaticValue.SIGN_IN_RESULT_TAGE, intent);
        finish();
    }

    @Override
    public String getPseudo() {
        return pseudoField.getText().toString();
    }

    @Override
    public String getEmail() {
        return emailField.getText().toString();
    }

    @Override
    public String getPassword() {
        return passwordField.getText().toString();
    }

    @Override
    public String getConfirmationPassword() {
        return confirmationPasswordField.getText().toString();
    }
}
