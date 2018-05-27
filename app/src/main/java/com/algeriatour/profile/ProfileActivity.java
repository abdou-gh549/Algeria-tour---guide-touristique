package com.algeriatour.profile;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.algeriatour.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.profile_userNameField)
    MaterialEditText userNameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.profile_back_Button)
    public void onBackButtonClicked() {
        finish();
    }

    @OnClick(R.id.profile_editUserNameImgV)
    public void onEditUserNameImgVClicked() {
        userNameField.setEnabled(true);
        userNameField.requestFocus();
    }

    @OnClick(R.id.profile_editEmailImgV)
    public void onEditEmailImgVClicked() {


    }

    @OnClick(R.id.profile_editPasswordImgV)
    public void onEditPasswordImgVClicked() {

    }
}
