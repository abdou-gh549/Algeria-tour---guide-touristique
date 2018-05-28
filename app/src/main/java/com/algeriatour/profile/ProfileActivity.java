package com.algeriatour.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.algeriatour.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity implements ProfileConstraint.ViewConstrain {
    @BindView(R.id.profile_userNameField)
    MaterialEditText userNameField;

    @BindView(R.id.profile_passwordField)
    MaterialEditText passwordField;

    @BindView(R.id.profile_emailField)
    MaterialEditText emailField;

    @BindView(R.id.profile_dateInscription)
    TextView dateInscreptionTextView;

    ProfilePresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        ButterKnife.bind(this);
        presenter = new ProfilePresenter(this);

        loadData();
    }

    private void loadData() {
        emailField.setText("abdellah.tst@gmail.com");
        userNameField.setText("tixx");
        passwordField.setText("abcdefghijklm");
        dateInscreptionTextView.setText("12/10/2018");
    }


    @OnClick(R.id.profile_back_Button)
    public void onBackButtonClicked() {
        finish();
    }

    @OnClick(R.id.profile_editUserNameImgV)
    public void onEditUserNameImgVClicked() {
        userNameField.setEnabled(true);
        emailField.setEnabled(false);
        passwordField.setEnabled(false);
        userNameField.requestFocus();
    }

    @OnClick(R.id.profile_editEmailImgV)
    public void onEditEmailImgVClicked() {
        emailField.setEnabled(true);
        userNameField.setEnabled(false);
        passwordField.setEnabled(false);
        emailField.requestFocus();
    }

    @OnClick(R.id.profile_editPasswordImgV)
    public void onEditPasswordImgVClicked() {
        passwordField.setEnabled(true);
        userNameField.setEnabled(false);
        emailField.setEnabled(false);
        passwordField.requestFocus();
    }

    @OnClick(R.id.profile_saveChangeBtn)
    public void onSaveChangeClick(){
        presenter.onSaveClick();
    }

    @OnClick(R.id.profile_cancelBtn)
    public void onCanclClick(){
        finish();
    }

    @Override
    public void showEmailError(String msg) {
        emailField.setError(msg);
    }

    @Override
    public void showPasswordError(String msg) {
        passwordField.setError(msg);
    }

    @Override
    public void showUserNameError(String msg) {
        userNameField.setError(msg);
    }

    @Override
    public void showToastError(String msg) {
        Toasty.error(this, msg, Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void showToastSuccess(String msg) {
        Toasty.success(this, msg, Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void enableField(int fieldNumber) {
        MaterialEditText fields[] = new MaterialEditText[]{userNameField, emailField, passwordField};
        if (fieldNumber > 0 && fieldNumber < 4) {
            for (int i = 1; i <= 3; i++) {
                if (i == fieldNumber) {
                    fields[i].setEnabled(true);
                    fields[i].requestFocus();
                } else {
                    fields[i].setEnabled(false);
                }
            }
        }
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
    public String getUserName() {
        return userNameField.getText().toString();
    }

    @Override
    public String getStringFromRessource(int stringId) {
        return getString(stringId);
    }


}
