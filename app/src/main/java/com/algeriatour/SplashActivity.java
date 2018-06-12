package com.algeriatour;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.algeriatour.login.LoginActivity;
import com.algeriatour.main.MainActivity;
import com.algeriatour.map.activity.MapActivity;
import com.algeriatour.uml_class.Membre;
import com.algeriatour.utils.AlgeriaTourUtils;
import com.algeriatour.utils.StaticValue;
import com.algeriatour.utils.User;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;

public class SplashActivity extends AppCompatActivity {

    private final int SPALSH_DURATION = 1; // 3 sec


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // init networking

        OkHttpClient httpConfiguration = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(this, httpConfiguration);

        //
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences
                (StaticValue.LOGIN_SHARED_PEFERENCE,
                        MODE_PRIVATE);
        String pseudo = sharedPreferences.getString(StaticValue.PSEUDO_TAGE, "");
        String password = sharedPreferences.getString(StaticValue.PASSWORD_TAGE, "");
        Log.d("tixx", "splash screen onCreate: pseudo = " + pseudo + " psw = " + password);
        if (pseudo.isEmpty() || password.isEmpty()) {
            onLoginFail(3000);
        } else {
            doLogin(pseudo, password);
        }

    }


    private void doLogin(String pseudo, String psw) {
        final String fileName = "at_login.php";
        final String login_url = StaticValue.MYSQL_SITE + fileName;
        AndroidNetworking.post(login_url)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_PSEUDO, pseudo)
                .addBodyParameter(StaticValue.PHP_PASSWORD, psw)
                .setPriority(Priority.MEDIUM)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("tixx", "reponse " + response.getInt(StaticValue.JSON_NAME_SUCCESS));
                    if (response.getInt(StaticValue.JSON_NAME_SUCCESS) == 1) {
                        JSONObject user = (JSONObject) response.get(StaticValue.JSON_NAME_USER);
                        User.connect(parseMembre(user));
                        onLoginSucess();

                    } else {
                        onLoginFail();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tixx", "reponst catch" + e.getMessage());
                }
            }

            @Override
            public void onError(ANError error) {
                onLoginFail();
                Log.d("tixx", "error " + error.getErrorDetail());
                Log.d("tixx", "error " + error.getMessage());
            }
        });
    }

    private void onLoginFail() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(StaticValue.START_FROM_SPLASH_TAG, true);
        startActivity(intent);
        finish();
    }

    private void onLoginFail(int duration) {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(StaticValue.START_FROM_SPLASH_TAG, true);
            startActivity(intent);
            finish();
        }, duration);
    }

    private void onLoginSucess() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private Membre parseMembre(JSONObject jsonObject) throws JSONException {
        Membre membre = new Membre();
        membre.setId(jsonObject.getLong(StaticValue.JSON_NAME_ID));
        membre.setPseudo(jsonObject.getString(StaticValue.JSON_NAME_PSEUDO));
        membre.setPassword(jsonObject.getString(StaticValue.JSON_NAME_PASSWORD));
        membre.setEmail(jsonObject.getString(StaticValue.JSON_NAME_EMAIL));
        membre.setInscreptionDate(jsonObject.getString(StaticValue.JSON_NAME_JOIN_DATE));
        return membre;
    }
}
