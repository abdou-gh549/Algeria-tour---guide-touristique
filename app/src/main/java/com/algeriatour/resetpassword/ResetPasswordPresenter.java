package com.algeriatour.resetpassword;

import android.util.Log;
import android.widget.Toast;

import com.algeriatour.R;
import com.algeriatour.utils.AlgeriaTourUtils;
import com.algeriatour.utils.StaticValue;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordPresenter {
    ResetPasswordConstraint.ViewConstraint resetPsw_view;
    private final String resetPasswordFile = "at_forgot_password.php";
    private final String resetPAssword_url = StaticValue.MYSQL_SITE + resetPasswordFile;

    public ResetPasswordPresenter(ResetPasswordConstraint.ViewConstraint resetPsw_view) {
        this.resetPsw_view = resetPsw_view;
    }


    public void sendPasswordClicked() {
        String email = resetPsw_view.getEmail();
        String pseudo = resetPsw_view.getPseudo();
        if( ! checkInput(email, pseudo))
            return;

        AndroidNetworking.post(resetPAssword_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_EMAIL, email)
                .addBodyParameter(StaticValue.PHP_PSEUDO, pseudo)
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getInt(StaticValue.JSON_NAME_SUCCESS) == 1){
                        String msg = "hello " + pseudo + "your password is : "+
                                response.getString(StaticValue.JSON_NAME_PASSWORD);
                        resetPsw_view.sendPassword(email, msg);

                    }else if (response.getInt(StaticValue.JSON_NAME_SUCCESS) == 0){
                        resetPsw_view.showToastError("user doesn't exist");
                    }else{
                        // server error
                        resetPsw_view.showToastError("server error try again later");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    resetPsw_view.showToastError("ops something happened");
                }
                resetPsw_view.hideProgressDialog();
            }

            @Override
            public void onError(ANError error) {
                resetPsw_view.hideProgressDialog();
                Log.d("tixx", "onError: " + error.getMessage());
                resetPsw_view.showToastError("check your connection");
                resetPsw_view.hideProgressDialog();
            }
        });
    }

    private boolean checkInput(String email, String pseudo){
        boolean inputValid = true;
        if (email.isEmpty()) {
            resetPsw_view.showEmailError(resetPsw_view.getStringFromRessource(R.string.empty_email_error));
            inputValid = false;
        } else if (!AlgeriaTourUtils.isValidEmail(email)) {
            resetPsw_view.showEmailError(resetPsw_view.getStringFromRessource(R.string.invalid_email_format_error));
            inputValid = false;
        }

        if (pseudo.isEmpty()) {
            resetPsw_view.showPseudoError(resetPsw_view.getStringFromRessource(R.string.empty_email_error));
            inputValid = false;
        } else if (pseudo.contains(" ")) {
            resetPsw_view.showPseudoError(resetPsw_view.getStringFromRessource(R.string.space_pseudo_error));
            inputValid = false;
        }

        return inputValid;
    }
}
