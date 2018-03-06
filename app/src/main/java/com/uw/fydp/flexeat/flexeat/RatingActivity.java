package com.uw.fydp.flexeat.flexeat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.uw.fydp.flexeat.flexeat.api.Request;

import org.json.JSONException;
import org.json.JSONObject;

public class RatingActivity extends AppCompatActivity {

    Button notNow;
    Button submitRating;
    RatingBar ratings;
    int restaurantID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        getSupportActionBar().setTitle("Rate Your Experience");

        restaurantID = getIntent().getIntExtra("restaurantID", -1);

        ratings = (RatingBar) findViewById(R.id.ratingBar);

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
                sendRatingToServer();
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

    public void sendRatingToServer(){
        String endpoint = "/api/rating";
        JSONObject sendingRating = new JSONObject();
        try{
            sendingRating.put("rating", ratings.getRating());
            sendingRating.put("restaurant_id", restaurantID);
        }catch (JSONException e){
            e.printStackTrace();
        }
        Request.post(getApplicationContext(), endpoint, sendingRating, new Request.Callback() {
            @Override
            public void onRespond(boolean success, int code, String res, boolean isRemoteResponse) {
                if(success){
                    Toast.makeText(getApplicationContext(), "Rating Sent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(boolean success, int code, Exception e) {

            }
        });
    }
}
