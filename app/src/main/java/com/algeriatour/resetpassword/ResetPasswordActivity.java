package com.algeriatour.resetpassword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.algeriatour.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.function.Predicate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ResetPasswordActivity extends AppCompatActivity implements ResetPasswordConstraint.ViewConstraint {

    @BindView(R.id.resetPassword_email)
    MaterialEditText emailField;

    @BindView(R.id.resetPassword_pseudo)
    MaterialEditText pseudoField;

    ResetPasswordPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);
        ButterKnife.bind(this);
        presenter = new ResetPasswordPresenter(this);
    }

    @OnClick(R.id.resetPassword_sendCodeButton)
    public void onSendButtonClick() {
        presenter.sendPasswordClicked();
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
    public void sendPassword(String email, String password) {
        AlgeriaTourMail.sendResetCode(this, email,password);
    }

    @Override
    public void showToastError(String msg) {
        Toasty.error(this, msg, Toast.LENGTH_LONG,true).show();
    }

    @Override
    public String getStringFromRessource(int stringId) {
        return getString(stringId);
    }
}
