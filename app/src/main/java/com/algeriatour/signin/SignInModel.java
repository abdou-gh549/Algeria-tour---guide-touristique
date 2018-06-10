package com.algeriatour.signin;

import android.util.Log;

import com.algeriatour.R;
import com.algeriatour.utils.AlgeriaTourUtils;
import com.algeriatour.utils.StaticValue;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInModel implements SignInConstraint.ModelConstraint {
    private final String fileName = "at_register.php";
    private final String register_url = StaticValue.MYSQL_SITE + fileName;

    SignInConstraint.PresenterConstraint presenter;

    public SignInModel(SignInConstraint.PresenterConstraint presenter) {
        this.presenter = presenter;
    }
    /*
     *  -1 database error / Register failed
     *   0 user already exist
     *   1 registre success
     *
     */

    @Override
    public void signIn(String pseudo, String email, String password) {
        AndroidNetworking.post(register_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_EMAIL, email)
                .addBodyParameter(StaticValue.PHP_PSEUDO, pseudo)
                .addBodyParameter(StaticValue.PHP_PASSWORD, password)
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt(StaticValue.JSON_NAME_SUCCESS)) {
                        case 1:
                            presenter.onSignInSuccess();
                            break;
                        case 0:
                            Log.d("tixx", "change onResponse: pseudo = " + pseudo);
                            presenter.onSignInFail(AlgeriaTourUtils.getString(R.string
                                    .user_not_exist_error));
                            break;
                        case -1:
                            presenter.onSignInFail(AlgeriaTourUtils.getString(R.string
                                    .server_error));
                            break;
                    }
                    Log.d("tixx", "reponse " + response.getInt(StaticValue.JSON_NAME_SUCCESS));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "reponst catch" + e.getMessage());

                    presenter.onSignInFail(AlgeriaTourUtils.getString(R.string.server_error));
                }
            }

            @Override
            public void onError(ANError error) {
                Log.d("tixx", "error " + error.getMessage());
                presenter.onSignInFail(AlgeriaTourUtils.getString(R.string
                        .connection_fail));
            }
        });
    }
}
