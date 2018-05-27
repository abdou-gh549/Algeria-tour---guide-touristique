package com.algeriatour.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.algeriatour.R;
import com.algeriatour.main.MainActivity;
import com.algeriatour.signin.SignInActivity;
import com.algeriatour.utils.FormatValidator;
import com.algeriatour.utils.StaticValue;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements LoginConstraint.viewConstraint{

    private final int SIGN_IN_TAG = 11;

    @BindView(R.id.login_email_inp)
    MaterialEditText emailField;

    @BindView(R.id.login_password_inp)
    MaterialEditText passwordFiled;

    private LoginPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        presenter = new LoginPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_TAG) {
            Bundle bundle = data.getExtras();
            String email = "";
            if (bundle != null) {
                email = data.getExtras().getString(StaticValue.EMAIL_TAGE, "");
            }

            if (!email.isEmpty()) {
                emailField.setText(email);
                passwordFiled.requestFocus();
            }
        }
    }

    @OnClick(R.id.login_signin_btn)
    public void onSignInButtonCLicked() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivityForResult(intent, SIGN_IN_TAG);
    }

    @OnClick(R.id.login_exit_imageview)
    public void onExitImageViewClicked() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        boolean startFromSplashScreen = bundle.getBoolean(StaticValue.START_FROM_SPLASH_TAG, false);
        if (startFromSplashScreen) {
            startMainActiviy(null);
        }
        finish();
    }

    @OnClick(R.id.login_login_btn)
    public void onLoginButtonClicked() {
        presenter.authentificationButtonClicked();
    }

    @OnClick(R.id.login_forgetPassword_textView)
    public void onForgetPasswordClicked() {
        // TODO : forget password stuff
    }


    @Override
    public String getEmail() {
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
    public void showEmailError(String message) {
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
    public void startMainActiviy(String userEmail) {
        Intent intent = new Intent(this, MainActivity.class);
        if (userEmail != null){
            intent.putExtra(StaticValue.EMAIL_TAGE, userEmail);
        }
        startActivity(intent);

    }

}
