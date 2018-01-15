package com.uw.fydp.flexeat.flexeat.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chaitanyakhanna on 2017-07-20.
 */

public class FoodMenuItem {
    public String name;
    public boolean isCheck;

    public FoodMenuItem(String name, boolean isChecked){
        this.name = name;
        this.isCheck = isChecked;
    }

    public FoodMenuItem(JSONObject obj){
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
