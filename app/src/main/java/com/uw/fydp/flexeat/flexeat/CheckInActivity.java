package com.uw.fydp.flexeat.flexeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CheckInActivity extends AppCompatActivity {

    EditText restaurantCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        restaurantCode = (EditText) findViewById(R.id.check_in_code_field);
    }

    public void onSubmitClick(View view) {
        if(restaurantCode.getText().toString().equals("1234")){
            Intent goToMenuScreen = new Intent(CheckInActivity.this, MenuActivity.class);
            startActivity(goToMenuScreen);
        }
        else{
            Toast.makeText(getApplicationContext(), "Wrong code", Toast.LENGTH_LONG).show();
        }
    }
}
