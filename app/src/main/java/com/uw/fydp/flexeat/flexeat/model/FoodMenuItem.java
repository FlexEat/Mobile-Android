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

    public FoodMenuItem(String name, boolean isChecked){
        this.name = name;
    }

    public FoodMenuItem(JSONObject obj){
        try{
            this.name = obj.getString("item_name");
            this.foodItemID = obj.getInt("item_id");
            this.restaurantID = obj.getInt("restaurant_id");
            this.category = obj.getString("category");
            this.description = obj.getString("description");
            this.price = obj.getString("price");
            this.imageURL = obj.getString("image_url");
            this.isShownInMenu = obj.getBoolean("is_shown_in_menu");

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setCheck(boolean check){
        this.quantity = 0;
    }

    public JSONObject getJSONObject(){
        JSONObject obj = new JSONObject();
        try{
            obj.put("item_name", this.name);
            obj.put("quantity", this.quantity);
            obj.put("item_id", this.foodItemID);
            obj.put("restaurant_id", this.restaurantID);
            obj.put("category", this.category);
            obj.put("ingredients", this.description);
            obj.put("price", this.price);
            obj.put("image_url", this.imageURL);
            obj.put("is_shown_in_menu", this.isShownInMenu);

        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj;
    }
}
