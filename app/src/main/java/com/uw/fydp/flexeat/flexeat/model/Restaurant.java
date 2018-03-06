package com.uw.fydp.flexeat.flexeat.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chaitanyakhanna on 2018-03-02.
 */

public class Restaurant {

    public String name;
    public int id;
    public double rating;

    public Restaurant(JSONObject response){
        this.name = response.optString("restaurant_name", "");
        this.id = response.optInt("restaurant_id", -1);
        this.rating = response.optDouble("rating", 5.0);
    }
}
