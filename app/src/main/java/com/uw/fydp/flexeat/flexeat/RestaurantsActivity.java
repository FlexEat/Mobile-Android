package com.uw.fydp.flexeat.flexeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.uw.fydp.flexeat.flexeat.api.Request;
import com.uw.fydp.flexeat.flexeat.model.Restaurant;

import java.util.ArrayList;

public class RestaurantsActivity extends AppCompatActivity {

    ListView listOfRestaurants;
    ArrayList<Restaurant> allRestaurants = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        listOfRestaurants = (ListView) findViewById(R.id.list_of_restaurants);


        String endpoint = "/api/restaurants";
        Request.get(getApplicationContext(), endpoint, null, new Request.Callback() {
            @Override
            public void onRespond(boolean success, int code, String res, boolean isRemoteResponse) {

            }

            @Override
            public void onError(boolean success, int code, Exception e) {

            }
        });
    }
}
