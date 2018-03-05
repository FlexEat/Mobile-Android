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
    public int orderID;
    public boolean isGlutenFree;
    public boolean containsPeanuts;
    public boolean containsFish;
    public boolean containsSoy;
    public boolean isHalal;
    public boolean isVegetarian;
    public boolean isVegan;

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
        this.isGlutenFree = obj.optBoolean("isglutenfree", false);
        this.containsPeanuts = obj.optBoolean("containspeanuts", false);
        this.containsFish = obj.optBoolean("containsfish", false);
        this.containsSoy = obj.optBoolean("containssoy", false);
        this.isHalal = obj.optBoolean("ishalal", false);
        this.isVegan = obj.optBoolean("isvegan", false);
        this.isVegetarian = obj.optBoolean("isvegatarian", false);
    }

    public FoodMenuItem(JSONObject obj, boolean isOrdered){
        this.name = obj.optString("item_name", "");
        this.foodItemID = obj.optInt("item_id", -1);
        this.restaurantID = obj.optInt("restaurant_id", -1);
        this.price = obj.optString("price", "");
        this.status = obj.optString("status", "");
        this.orderID = obj.optInt("order_id", -1);
        this.quantity = obj.optInt("item_quantity", -1);
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

    public JSONObject getOrderedJSONObject(){
        JSONObject obj = new JSONObject();
        try{
            obj.put("item_name", this.name);
            obj.put("item_quantity", this.quantity);
            obj.put("item_id", this.foodItemID);
            obj.put("restaurant_id", this.restaurantID);
            obj.put("status", this.status);
            obj.put("order_id", this.orderID);

        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj;
    }

}
