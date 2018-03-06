package com.uw.fydp.flexeat.flexeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.uw.fydp.flexeat.flexeat.adapters.RestaurantArrayAdapter;
import com.uw.fydp.flexeat.flexeat.api.Request;
import com.uw.fydp.flexeat.flexeat.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class RestaurantsActivity extends AppCompatActivity {

    ListView listOfRestaurants;
    ArrayList<Restaurant> allRestaurants = new ArrayList<>();
    JSONArray JSONArrayofRestaurants;
    RestaurantArrayAdapter adapter;
    ProgressBar loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        listOfRestaurants = (ListView) findViewById(R.id.list_of_restaurants);
        loadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);

        String endpoint = "/api/restaurants";
        loadingSpinner.setVisibility(View.VISIBLE);
        Request.get(getApplicationContext(), endpoint, null, new Request.Callback() {
            @Override
            public void onRespond(boolean success, int code, String res, boolean isRemoteResponse) {
                if(success) {
                    loadingSpinner.setVisibility(View.GONE);
                    try{
                        JSONArrayofRestaurants = new JSONArray(res);
                        for (int i = 0 ; i < JSONArrayofRestaurants.length() ; i++){
                            Restaurant currentRestaurant = new Restaurant(JSONArrayofRestaurants.getJSONObject(i));
                            allRestaurants.add(currentRestaurant);
                            adapter = new RestaurantArrayAdapter(getApplicationContext(), R.layout.item_menu, allRestaurants);
                            listOfRestaurants = (ListView) findViewById(R.id.list_of_restaurants);
                            listOfRestaurants.setAdapter(adapter);
                        }
                    } catch (JSONException e){
                        loadingSpinner.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(boolean success, int code, Exception e) {
                e.printStackTrace();
                loadingSpinner.setVisibility(View.GONE);
            }
        });
    }
}
