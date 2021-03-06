package com.uw.fydp.flexeat.flexeat.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chaitanyakhanna on 2017-12-07.
 */

public class FoodRestrictionItem {
    public String name;
    public boolean isCheck;

    public FoodRestrictionItem(String name, boolean isChecked){
        this.name = name;
        this.isCheck = isChecked;
    }

    public FoodRestrictionItem(JSONObject obj){
        try{
            this.name = obj.getString("name");
            this.isCheck = obj.getBoolean("isChecked");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setCheck(boolean check){
        this.isCheck = check;
    }

    public JSONObject getJSONObject(){
        JSONObject obj = new JSONObject();
        try{
            obj.put("name", this.name);
            obj.put("isChecked", this.isCheck);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return obj;
    }
}
