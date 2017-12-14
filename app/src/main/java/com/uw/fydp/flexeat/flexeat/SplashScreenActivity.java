package com.uw.fydp.flexeat.flexeat;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.facebook.AccessToken;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        RunAnimation();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (isAuthenticated()) {
                    Intent intent = new Intent(SplashScreenActivity.this, ProfileSetupActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(SplashScreenActivity.this, FbLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 4000);
    }

    private Boolean isAuthenticated(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    private void RunAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.splash_screen_text);
        tv.clearAnimation();
        tv.startAnimation(a);
    }
}
