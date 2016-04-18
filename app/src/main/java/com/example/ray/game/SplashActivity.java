package com.example.ray.game;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {

    private static int SPLASH_TIMEOUT   =   500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIMEOUT);

    }
}
