package com.algeriatour;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.algeriatour.login.LoginActivity;
import com.algeriatour.profile.ProfileActivity;
import com.algeriatour.utils.StaticValue;

public class SplashActivity extends AppCompatActivity {

    private final  int SPALSH_DURATION = 1; // 3 sec
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       /* new Handler().postDelayed(() -> {
            Intent intent = new Intent(this,LoginActivity.class);
            intent.putExtra(StaticValue.START_FROM_SPLASH_TAG, true);
            startActivity(intent);
            finish();
        },SPALSH_DURATION);
        */
       startActivity(new Intent(this, ProfileActivity.class));
       finish();

    }
}
