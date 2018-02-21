package com.uw.fydp.flexeat.flexeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class FbLoginActivity extends AppCompatActivity {

    LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_login);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Successful Login", Toast.LENGTH_LONG).show();
                Intent goToProfileActivity = new Intent(FbLoginActivity.this, ProfileSetupActivity.class);
                startActivity(goToProfileActivity);
                finish();
            }

            @Override
            public void onCancel() {
                Log.d("test", "cancelled login");
                Toast.makeText(getApplicationContext(), "Cancelled Login", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("test", "errored login");
                Toast.makeText(getApplicationContext(), "Error Login", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
