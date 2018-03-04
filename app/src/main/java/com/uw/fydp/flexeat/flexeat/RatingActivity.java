package com.uw.fydp.flexeat.flexeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RatingActivity extends AppCompatActivity {

    Button notNow;
    Button submitRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        getSupportActionBar().setTitle("Rate Your Experience");

        notNow = (Button) findViewById(R.id.no_rating);
        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoHomeFragment = new Intent(RatingActivity.this, BaseActivity.class);
                gotoHomeFragment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(gotoHomeFragment);
            }
        });

        submitRating = (Button) findViewById(R.id.submit_rating);
        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoHomeFragment = new Intent(RatingActivity.this, BaseActivity.class);
                gotoHomeFragment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(gotoHomeFragment);
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent goToMainFragment = new Intent(RatingActivity.this, BaseActivity.class);
        goToMainFragment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToMainFragment);

    }
}
