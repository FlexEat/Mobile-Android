package com.uw.fydp.flexeat.flexeat.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chaitanyakhanna on 2018-03-02.
 */

public class Restaurant {

    public String name;
    public int id;


    public Restaurant(JSONObject response){
        this.name = response.optString("restaurant_name");
        this.id = response.optInt("restaurant_id");
    }

    public JSONObject getJSONObject(){
        JSONObject obj = new JSONObject();
        try{
            obj.put("restaurant_name", this.name);
            obj.put("restaurant_id", this.id);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj;
    }

}
