package com.algeriatour.profile;

import android.util.Log;

import com.algeriatour.uml_class.Membre;
import com.algeriatour.utils.StaticValue;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileModel implements ProfileConstraint.ModelConstraint {
    private final String login_url = "https://algeriatour.000webhostapp.com/at_login.php";
    private final String update_url = "https://algeriatour.000webhostapp.com/at_updateuser.php";

    ProfileConstraint.PresenterConstraint presenter;

    public ProfileModel(ProfileConstraint.PresenterConstraint presenterConstraint) {
        this.presenter = presenterConstraint;
    }

    @Override
    public void change(String pseudo, String email, String password) {
        AndroidNetworking.post(update_url)
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
                            presenter.onChangeSucess(response.getString("message"));
                            break;
                        case 0:
                            Log.d("tixx", "change onResponse: pseudo = " + pseudo);
                            presenter.onLoadProfileDataFail("utilisateur n'exist pas !");
                            break;
                        case -1:
                            presenter.onLoadProfileDataFail("problem dans serveur ");
                            break;
                    }
                    Log.d("tixx", "reponse " + response.getInt("success"));
                } catch (JSONException e) {
                    presenter.onChangeFail("getting information error");
                    e.printStackTrace();
                    Log.d("tixx", "reponst catch" + e.getMessage());
                }
            }

            @Override
            public void onError(ANError error) {
                Log.d("tixx", "error " + error.getMessage());
                presenter.onChangeFail("verfier votre connection ");
            }
        });
    }

    @Override
    public void loadUserInfo(String pseudo, String password) {
        AndroidNetworking.post(login_url)
                .addBodyParameter("target", "external")
                .addBodyParameter("username", pseudo)
                .addBodyParameter("password", password)
                .setPriority(Priority.MEDIUM)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt(StaticValue.JSON_NAME_SUCESS)) {
                        case 1:
                            Membre membre = new Membre();
                            JSONObject user = (JSONObject) response.get(StaticValue.JSON_NAME_USER);
                            membre.setEmail(user.getString(StaticValue.JSON_NAME_EMAIL));
                            membre.setPseudo(user.getString(StaticValue.JSON_NAME_PSEUDO));
                            membre.setInscreptionDate(user.getString(StaticValue.JSON_NAME_JOIN_DATE));
                            membre.setPassword(user.getString(StaticValue.JSON_NAME_PASSWORD));
                            presenter.onLoadProfileDataSuccess(membre);
                            break;
                        case 0:
                            presenter.onLoadProfileDataFail("utilisateur n'exist pas !");
                            break;
                        case -1:
                            presenter.onLoadProfileDataFail("problem dans serveur ");
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
                presenter.onLoadProfileDataFail("verfier votre connection ");
                Log.d("tixx", "error " + error.getErrorDetail());
                Log.d("tixx", "error " + error.getMessage());
            }
        });
    }
}
