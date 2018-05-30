package com.algeriatour.login;

import android.util.Log;

import com.algeriatour.utils.StaticValue;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

public class LoginModel implements LoginConstraint.modelConstraint {
    private final String register_url = "https://algeriatour.000webhostapp.com/at_login.php";
    LoginConstraint.PresenterConstraint presenter;

    public LoginModel(LoginConstraint.PresenterConstraint presenter) {
        this.presenter = presenter;
    }

    @Override
    public void doLogin(String pseudo, String psw) {
        AndroidNetworking.post(register_url)
                .addBodyParameter("target", "external")
                .addBodyParameter("username", pseudo)
                .addBodyParameter("password", psw)
                .setPriority(Priority.MEDIUM)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt("success")){
                        case 1 :
                            JSONObject user = (JSONObject) response.get("user");
                            String email = user.getString("email");
                            presenter.onLoginSucess(pseudo, psw, email);
                            break;
                        case 0:
                            presenter.onLoginFail("utilisateur n'exist pas !");
                            break;
                        case -1:
                            presenter.onLoginFail("problem dans serveur ");
                            break;
                    }
                    Log.d("tixx", "reponse " + response.getInt("success"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "reponst catch" + e.getMessage());
                }
            }

            @Override
            public void onError(ANError error) {
                presenter.onLoginFail("verfier votre connection ");
                Log.d("tixx", "error " + error.getErrorDetail());
                Log.d("tixx", "error " + error.getMessage());
            }
        });
    }
}
