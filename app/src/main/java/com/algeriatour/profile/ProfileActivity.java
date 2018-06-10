package com.algeriatour.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.algeriatour.R;
import com.algeriatour.main.MainActivity;
import com.algeriatour.utils.Networking;
import com.algeriatour.utils.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class ProfileActivity extends AppCompatActivity implements ProfileConstraint.ViewConstrain {
    @BindView(R.id.profile_pseudo_textView)
    TextView pseudoTextView;

    @BindView(R.id.profile_passwordField)
    MaterialEditText passwordField;

    @BindView(R.id.profile_emailField)
    MaterialEditText emailField;

    @BindView(R.id.profile_dateInscription)
    TextView dateInscreptionTextView;

    ProfilePresenter presenter;
    private SpotsDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        ButterKnife.bind(this);
        presenter = new ProfilePresenter(this);
        Networking.initAndroidNetworking(this);
        progressDialog = new SpotsDialog(this);
        progressDialog.setCancelable(false);
        loadData();
    }

    private void loadData() {

        try {
            presenter.loadProfileData(User.getMembre().getPseudo(), User.getMembre().getPassword());
        } catch (Exception exp) {
            showToastError(getString(R.string.cant_get_profile_info_msg));
            finish();
        }
    }

    @OnClick(R.id.profile_back_Button)
    public void onBackButtonClicked() {
        finish();
    }


    @OnClick(R.id.profile_editEmailImgV)
    public void onEditEmailImgVClicked() {
        emailField.setEnabled(true);
        passwordField.setEnabled(false);
        emailField.requestFocus();
    }

    @OnClick(R.id.profile_editPasswordImgV)
    public void onEditPasswordImgVClicked() {
        passwordField.setEnabled(true);
        emailField.setEnabled(false);
        passwordField.requestFocus();
    }

    @OnClick(R.id.profile_saveChangeBtn)
    public void onSaveChangeClick() {
        presenter.onSaveClick();
    }

    @OnClick(R.id.profile_cancelBtn)
    public void onCanclClick() {
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
    public void showToastError(String msg) {
        Toasty.error(this, msg, Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void showToastSuccess(String msg) {
        Toasty.success(this, msg, Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void showToastInformation(String msg) {
        Toasty.info(this, msg, Toast.LENGTH_LONG, true).show();

    }

    @Override
    public void setInscreptionDate(String inscreptionDate) {
        dateInscreptionTextView.setText(inscreptionDate);
    }

    @Override
    public String getEmail() {
        return emailField.getText().toString();
    }

    @Override
    public void setEmail(String email) {
        emailField.setText(email);
    }

    @Override
    public String getPassword() {
        return passwordField.getText().toString();
    }

    @Override
    public void setPassword(String psw) {
        passwordField.setText(psw);
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
    public String getPseudo() {
        return pseudoTextView.getText().toString();
    }

    @Override
    public void setPseudo(String pseudo) {
        pseudoTextView.setText(pseudo);
    }

    @Override
    public String getStringFromRessource(int stringId) {
        return getString(stringId);
    }

    @Override
    public void finishAcitivity() {
        finish();
    }

    @Override
    public void startMainActivityAfterChange() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
