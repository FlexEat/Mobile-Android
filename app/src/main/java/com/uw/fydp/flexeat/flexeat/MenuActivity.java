package com.uw.fydp.flexeat.flexeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MenuActivity extends AppCompatActivity {

    JSONObject menuResponse = new JSONObject();
    String[] listOfAppetizer = {"Spring Rolls", "Samosas", "Chicken Wings", "Nachos", "Garlic Bread", "Chicken Lollipop"};
    String[] listOfMainCourse = {"Butter Chicken", "Chili Chicken", "Achari Chicken", "Keema Matar", "Matar Mashroom", "Bindi Masala", "Chana Masala"};
    String[] listOfDrinks = {"Slippery Nipple", "Rum and coke", "Glenlevit", "Sex on the Beach", "Screwdriver", "Pepsi", "7up", "Barbican", "Lassi"};
    String[] listOfDesserts = {"Gulab Jamun", "Ras Malai", "Gajar ka Halwa", "kheer", "Rasgulla", "Kulfi", "Jalebi"};

    ListView appetizerList;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        try{
            menuResponse.put("appetizers", new JSONArray(Arrays.asList(listOfAppetizer)));
            menuResponse.put("main course", new JSONArray(Arrays.asList(listOfMainCourse)));
            menuResponse.put("drinks", new JSONArray(Arrays.asList(listOfDrinks)));
            menuResponse.put("desserts", new JSONArray(Arrays.asList(listOfDesserts)));
        }catch (JSONException e){
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<>(this, R.layout.item_menu, R.id.item_name, listOfAppetizer);

        appetizerList = (ListView) findViewById(R.id.appetizer_list);
        appetizerList.setAdapter(adapter);

    }
}
