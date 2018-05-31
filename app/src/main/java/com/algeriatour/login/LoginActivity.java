package com.algeriatour.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.algeriatour.R;
import com.algeriatour.main.MainActivity;
import com.algeriatour.signin.SignInActivity;
import com.algeriatour.utils.Networking;
import com.algeriatour.utils.StaticValue;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements LoginConstraint.viewConstraint {

    private final int SIGN_IN_TAG = 11;

    @BindView(R.id.login_email_inp)
    MaterialEditText emailField;

    @BindView(R.id.login_password_inp)
    MaterialEditText passwordFiled;

    private LoginPresenter presenter;
    private SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        presenter = new LoginPresenter(this);
        /* init networking*/
        Networking.initAndroidNetworking(this);
        /*---------------*/
        progressDialog = new SpotsDialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_TAG) {
            if (data == null)
                return;
            Bundle bundle = data.getExtras();
            String pseudo = "";
            if (bundle != null) {
                pseudo = data.getExtras().getString(StaticValue.PSEUDO_TAGE, "");
            }

            if (!pseudo.isEmpty()) {
                emailField.setText(pseudo);
                passwordFiled.requestFocus();
            }
        }
    }

    //
    @OnClick(R.id.login_signin_btn)
    public void onSignInButtonCLicked() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivityForResult(intent, SIGN_IN_TAG);
    }

    @OnClick(R.id.login_exit_imageview)
    public void onExitImageViewClicked() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getBoolean(StaticValue.START_FROM_SPLASH_TAG, false)) {
            startMainActiviy();
        }

        finish();
    }

    @OnClick(R.id.login_login_btn)
    public void onLoginButtonClicked() {
        presenter.doAuthentification();
    }

    @OnClick(R.id.login_forgetPassword_textView)
    public void onForgetPasswordClicked() {
        // TODO : forget password stuff
    }


    @Override
    public String getPseudo() {
        return emailField.getText().toString();
    }

    @Override
    public String getPassword() {
        return passwordFiled.getText().toString();
    }

    @Override
    public String getStringFromRessource(int id) {
        return getString(id);
    }

    @Override
    public void showPseudoError(String message) {
        emailField.setError(message);
    }

    @Override
    public void showPasswordError(String message) {
        passwordFiled.setError(message);
    }

    @Override
    public void showLoginFailed(String msg) {
        Toasty.error(this, msg, Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void showLoginSucess(String msg) {
        Toasty.success(this, msg, Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void startMainActiviy(String pseudo, String psw, String email) {
        Intent intent = new Intent(this, MainActivity.class);
        if (pseudo != null && psw != null) {
            intent.putExtra(StaticValue.PSEUDO_TAGE, pseudo);
            intent.putExtra(StaticValue.PASSWORD_TAGE, psw);
            intent.putExtra(StaticValue.EMAIL_TAGE, email);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    @Override
    public void startMainActiviy() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.hide();
    }

    @Override
    public void saveLoginTosharedPreference(String pseudo, String psw) {
        SharedPreferences sharedPreferences = getPreferences( MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StaticValue.PSEUDO_TAGE, pseudo);
        editor.putString(StaticValue.PASSWORD_TAGE, psw);
        editor.apply();
    }

}
