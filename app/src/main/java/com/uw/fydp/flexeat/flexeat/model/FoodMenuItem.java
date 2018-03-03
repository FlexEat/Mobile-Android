package com.uw.fydp.flexeat.flexeat.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chaitanyakhanna on 2017-07-20.
 */

public class FoodMenuItem {
    public String name;
    public int foodItemID;
    public int restaurantID;
    public String category;
    public int quantity = 0;
    public String description;
    public String price;
    public String imageURL;
    public boolean isShownInMenu;
    public String status;

    public FoodMenuItem(String name, boolean isChecked){
        this.name = name;
    }

    public FoodMenuItem(JSONObject obj){
        this.name = obj.optString("item_name", "");
        this.foodItemID = obj.optInt("item_id", -1);
        this.restaurantID = obj.optInt("restaurant_id", -1);
        this.category = obj.optString("category", "");
        this.description = obj.optString("description", "");
        this.price = obj.optString("price", "");
        this.imageURL = obj.optString("image_url", "");
        this.isShownInMenu = obj.optBoolean("is_shown_in_menu", true);
        this.status = obj.optString("status", "");
    }

    public void setCheck(boolean check){
        this.quantity = 0;
    }

    public JSONObject getJSONObject(){
        JSONObject obj = new JSONObject();
        try{
            obj.put("item_name", this.name);
            obj.put("item_quantity", this.quantity);
            obj.put("item_id", this.foodItemID);
            obj.put("restaurant_id", this.restaurantID);
            obj.put("category", this.category);
            obj.put("ingredients", this.description);
            obj.put("price", this.price);
            obj.put("image_url", this.imageURL);
            obj.put("is_shown_in_menu", this.isShownInMenu);
            obj.put("status", this.status);

        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj;
    }
}
