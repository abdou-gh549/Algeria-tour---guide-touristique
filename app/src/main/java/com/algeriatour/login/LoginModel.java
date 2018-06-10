package com.algeriatour.login;

import android.util.Log;

import com.algeriatour.R;
import com.algeriatour.uml_class.Membre;
import com.algeriatour.utils.AlgeriaTourUtils;
import com.algeriatour.utils.StaticValue;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginModel implements LoginConstraint.modelConstraint {
    private final String fileName = "at_login.php";
    private final String login_url = StaticValue.MYSQL_SITE + fileName;
    LoginConstraint.PresenterConstraint presenter;

    public LoginModel(LoginConstraint.PresenterConstraint presenter) {
        this.presenter = presenter;
    }

    @Override
    public void doLogin(String pseudo, String psw) {
        AndroidNetworking.post(login_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_PSEUDO, pseudo)
                .addBodyParameter(StaticValue.PHP_PASSWORD, psw)
                .setPriority(Priority.MEDIUM)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt(StaticValue.JSON_NAME_SUCCESS)) {
                        case 1:
                            JSONObject user = (JSONObject) response.get(StaticValue.JSON_NAME_USER);
                            presenter.onLoginSucess(parseMembre(user));
                            break;
                        case 0:
                            presenter.onLoginFail(AlgeriaTourUtils.getString(R.string.user_not_exist_error));
                            break;
                        case -1:
                            presenter.onLoginFail(AlgeriaTourUtils.getString(R.string.server_error));
                            break;
                    }
                    Log.d("tixx", "reponse " + response.getInt(StaticValue.JSON_NAME_SUCCESS));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "reponst catch" + e.getMessage());
                    presenter.onLoginFail(AlgeriaTourUtils.getString(R.string.server_error));
                }
            }

            @Override
            public void onError(ANError error) {
                presenter.onLoginFail(AlgeriaTourUtils.getString(R.string.connection_fail));
                Log.d("tixx", "error " + error.getErrorDetail());
                Log.d("tixx", "error " + error.getMessage());
            }
        });
    }

    private Membre parseMembre(JSONObject jsonObject) throws JSONException {
        Membre membre = new Membre();
        membre.setId( jsonObject.getLong(StaticValue.JSON_NAME_ID));
        membre.setPseudo(jsonObject.getString(StaticValue.JSON_NAME_PSEUDO));
        membre.setPassword(jsonObject.getString(StaticValue.JSON_NAME_PASSWORD));
        membre.setEmail(jsonObject.getString(StaticValue.JSON_NAME_EMAIL));
        membre.setInscreptionDate(jsonObject.getString(StaticValue.JSON_NAME_JOIN_DATE));
        return membre;
    }
}
