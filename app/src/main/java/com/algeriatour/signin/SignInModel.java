package com.algeriatour.signin;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInModel implements SignInConstraint.ModelConstraint {
    private final String register_url = "https://algeriatour.000webhostapp.com/at_register.php";

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
                .addBodyParameter("target", "external")
                .addBodyParameter("username", pseudo)
                .addBodyParameter("password", password)
                .addBodyParameter("email", email)
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt("success")) {
                        case 1:
                                presenter.onSignInSuccess();
                            break;
                        case 0:
                            Log.d("tixx", "change onResponse: pseudo = " + pseudo);
                            presenter.onSignInFail("user already exist");
                            break;
                        case -1:
                            presenter.onSignInFail("server error");
                            break;
                    }
                    Log.d("tixx", "reponse " + response.getInt("success"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "reponst catch" + e.getMessage());

                    presenter.onSignInFail("getting information error");

                }
            }

            @Override
            public void onError(ANError error) {
                Log.d("tixx", "error " + error.getMessage());
                presenter.onSignInFail("verfier votre connection ");
            }
        });    }
}
