package com.algeriatour.resetpassword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.algeriatour.R;
import com.algeriatour.utils.Networking;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.function.Predicate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class ResetPasswordActivity extends AppCompatActivity implements ResetPasswordConstraint.ViewConstraint {

    @BindView(R.id.resetPassword_email)
    MaterialEditText emailField;

    @BindView(R.id.resetPassword_pseudo)
    MaterialEditText pseudoField;

    ResetPasswordPresenter presenter;

    SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);
        ButterKnife.bind(this);
        presenter = new ResetPasswordPresenter(this);
        Networking.initAndroidNetworking(this);
        progressDialog = new SpotsDialog(this);

        progressDialog.setCancelable(false);
    }

    @OnClick(R.id.resetPassword_sendCodeButton)
    public void onSendButtonClick() {
        presenter.sendPasswordClicked();
    }

    @OnClick(R.id.forgetPassword_backButton)
    void onBackButton(){
        finish();
    }
    @Override
    public String getEmail() {
        return emailField.getText().toString();
    }

    @Override
    public String getPseudo() {
        return pseudoField.getText().toString();
    }

    @Override
    public void showEmailError(String msg) {
        emailField.setError(msg);
    }

    @Override
    public void showPseudoError(String msg) {
        pseudoField.setError(msg);
    }

    @Override
    public void sendPassword(String email, String msg) {
        AlgeriaTourMail.sendResetCode(this, email,msg);
    }

    @Override
    public void showToastError(String msg) {
        Toasty.error(this, msg, Toast.LENGTH_LONG,true).show();
    }

    @Override
    public String getStringFromRessource(int stringId) {
        return getString(stringId);
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.dismiss();
    }
}
