package com.uw.fydp.flexeat.flexeat;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.uw.fydp.flexeat.flexeat.adapters.PagerAdapter;
import com.uw.fydp.flexeat.flexeat.api.Request;
import com.uw.fydp.flexeat.flexeat.api.RequestBase;
import com.uw.fydp.flexeat.flexeat.model.FoodMenuItem;
import com.uw.fydp.flexeat.flexeat.model.MenuItemInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements MenuItemInterface {

    ArrayList<FoodMenuItem> selectedItems = new ArrayList<>();
    JSONObject menuResponse = new JSONObject();

    PagerAdapter adapter;
    ArrayList<FoodMenuItem> listOfAppetizer = new ArrayList<>();
    ArrayList<FoodMenuItem> listOfMainCourse = new ArrayList<>();
    ArrayList<FoodMenuItem> listOfDrinks = new ArrayList<>();
    ArrayList<FoodMenuItem> listOfDesserts = new ArrayList<>();

    String[] appetizerNames = {"Spring Rolls", "Samosas", "Chicken Wings", "Nachos", "Garlic Bread", "Chicken Lollipop"};
    String[] mainCourseNames = {"Butter Chicken", "Chili Chicken", "Achari Chicken", "Keema Matar", "Matar Mashroom", "Bindi Masala", "Chana Masala"};
    String[] drinksNames = {"Slippery Nipple", "Rum and coke", "Glenlevit", "Sex on the Beach", "Screwdriver", "Pepsi", "7up", "Barbican", "Lassi"};
    String[] dessertsNames = {"Gulab Jamun", "Ras Malai", "Gajar ka Halwa", "kheer", "Rasgulla", "Kulfi", "Jalebi"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //String menuFromAPI = null;
        String menuFromAPI = getIntent().getStringExtra("menuAsString");
        String restaurantName = getIntent().getStringExtra("restaurantName");


        Log.d("menu", menuFromAPI);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(restaurantName);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        JSONArray appetizerJSONArray = new JSONArray();
        JSONArray mainCourseJSONArray = new JSONArray();
        JSONArray drinksJSONArray = new JSONArray();
        JSONArray dessertsJSONArray = new JSONArray();

        // uncomment the next line to use the mock menu data
        //menuFromAPI = null;

        if (menuFromAPI == null) {
            // default menu
            for (int i = 0; i < appetizerNames.length; i++) {
                listOfAppetizer.add(new FoodMenuItem(appetizerNames[i], false));
                appetizerJSONArray.put(listOfAppetizer.get(i).getJSONObject());
            }

            for (int i = 0; i < mainCourseNames.length; i++) {
                listOfMainCourse.add(new FoodMenuItem(mainCourseNames[i], false));
                mainCourseJSONArray.put(listOfMainCourse.get(i).getJSONObject());
            }

            for (int i = 0; i < drinksNames.length; i++) {
                listOfDrinks.add(new FoodMenuItem(drinksNames[i], false));
                drinksJSONArray.put(listOfDrinks.get(i).getJSONObject());
            }

            for (int i = 0; i < dessertsNames.length; i++) {
                listOfDesserts.add(new FoodMenuItem(dessertsNames[i], false));
                dessertsJSONArray.put(listOfDesserts.get(i).getJSONObject());
            }

        } else{
            // menu came from API
            JSONArray fullMenu;

            try{
                fullMenu = new JSONArray(menuFromAPI);
                for (int i = 0; i < fullMenu.length(); i++){
                    FoodMenuItem currentItem = new FoodMenuItem(fullMenu.getJSONObject(i));
                    switch (currentItem.category) {
                        case "Appetizer":
                            listOfAppetizer.add(currentItem);
                            appetizerJSONArray.put(fullMenu.getJSONObject(i));
                            break;
                        case "Main Course":
                            listOfMainCourse.add(currentItem);
                            mainCourseJSONArray.put(fullMenu.getJSONObject(i));
                            break;
                        case "Drink":
                            listOfDrinks.add(currentItem);
                            drinksJSONArray.put(fullMenu.getJSONObject(i));
                            break;
                        case "Dessert":
                            listOfDesserts.add(currentItem);
                            dessertsJSONArray.put(fullMenu.getJSONObject(i));
                            break;
                    }
                }

            } catch (JSONException e){
                e.printStackTrace();
            }
        }

        try{
            menuResponse.put("Appetizer", appetizerJSONArray);
            menuResponse.put("main course", mainCourseJSONArray);
            menuResponse.put("drinks", drinksJSONArray);
            menuResponse.put("desserts", dessertsJSONArray);

            for(int i = 0; i < menuResponse.length(); i++){
                tabLayout.addTab(tabLayout.newTab().setText(menuResponse.names().getString(i)));
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), menuResponse);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void onSubmitOrderClick(View view) {
        selectedItems.clear();
        getSelectedItems();
        JSONArray selectedItemsJSONArray = new JSONArray();
        for (int i = 0; i<selectedItems.size(); i++){
            selectedItemsJSONArray.put(selectedItems.get(i).getJSONObject());
        }
        JSONObject selectedItems = new JSONObject();
        try{
            selectedItems.put("selectedItems", selectedItemsJSONArray);
        } catch (JSONException e){
            e.printStackTrace();
        }
        String endpoint = "/api/order";
        Request.post(getApplicationContext(), endpoint, selectedItems, new Request.Callback() {
            @Override
            public void onRespond(boolean success, int code, String res, boolean isRemoteResponse) {
                if (success)
                    finish();
            }

            @Override
            public void onError(boolean success, int code, Exception e) {
                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void getSelectedItems() {
        for(int i = 0; i<adapter.getCount(); i++){
            GenericMenuFragment currentFragment = (GenericMenuFragment) adapter.getFragment(i);
            //TODO: Fix this when server is back
            try{
                for(int j = 0; j < currentFragment.adapter.getCount(); j++){
                    FoodMenuItem currentItem = currentFragment.adapter.getItem(j);
                    if(currentItem.isCheck){
                        selectedItems.add(currentItem);
                    }
                }
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }
}
