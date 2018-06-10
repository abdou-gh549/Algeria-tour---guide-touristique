package com.algeriatour.profile;

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

public class ProfileModel implements ProfileConstraint.ModelConstraint {
    private final String loginFileName = "at_login.php";
    private final String updateFileName = "at_update_user.php";

    private final String login_url = StaticValue.MYSQL_SITE + loginFileName;
    private final String update_url = StaticValue.MYSQL_SITE + updateFileName;

    ProfileConstraint.PresenterConstraint presenter;

    public ProfileModel(ProfileConstraint.PresenterConstraint presenterConstraint) {
        this.presenter = presenterConstraint;
    }

    @Override
    public void change(String pseudo, String email, String password) {
        AndroidNetworking.post(update_url)
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
                            presenter.onChangeSucess(response.getString(StaticValue
                                    .JSON_NAME_MESSAGE));
                            break;
                        case 0:
                            Log.d("tixx", "change onResponse: pseudo = " + pseudo);
                            presenter.onLoadProfileDataFail(AlgeriaTourUtils.getString(R.string
                                    .user_not_exist_error));
                            break;
                        case -1:
                            presenter.onLoadProfileDataFail(AlgeriaTourUtils.getString(R.string
                                    .server_error));
                            break;
                    }
                    Log.d("tixx", "reponse " + response.getInt(StaticValue.JSON_NAME_SUCCESS));
                } catch (JSONException e) {
                    presenter.onChangeFail(AlgeriaTourUtils.getString(R.string.change_info_error));
                    e.printStackTrace();
                    Log.d("tixx", "reponst catch" + e.getMessage());
                }
            }

            @Override
            public void onError(ANError error) {
                Log.d("tixx", "error " + error.getMessage());
                presenter.onChangeFail(AlgeriaTourUtils.getString(R.string.connection_fail));
            }
        });
    }

    @Override
    public void loadUserInfo(String pseudo, String password) {
        AndroidNetworking.post(login_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_PSEUDO, pseudo)
                .addBodyParameter(StaticValue.PHP_PASSWORD, password)
                .setPriority(Priority.MEDIUM)

                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt(StaticValue.JSON_NAME_SUCCESS)) {
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
                            presenter.onLoadProfileDataFail(AlgeriaTourUtils.getString(R.string
                                    .user_not_exist_error));
                            break;
                        case -1:
                            presenter.onLoadProfileDataFail(AlgeriaTourUtils.getString(R.string
                                    .server_error));
                            break;
                    }
                    Log.d("tixx", "reponse " + response.getInt(StaticValue.JSON_NAME_SUCCESS));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "reponst catch" + e.getMessage());
                }
            }

            @Override
            public void onError(ANError error) {
                presenter.onLoadProfileDataFail(AlgeriaTourUtils.getString(R.string
                        .connection_fail));
                Log.d("tixx", "error " + error.getErrorDetail());
                Log.d("tixx", "error " + error.getMessage());
            }
        });
    }
}
