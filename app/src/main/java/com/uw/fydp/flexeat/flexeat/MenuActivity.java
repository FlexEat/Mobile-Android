package com.uw.fydp.flexeat.flexeat;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements MenuItemInterface{

    ArrayList<MenuItem> selectedItems = new ArrayList<>();
    JSONObject menuResponse = new JSONObject();

    PagerAdapter adapter;
    ArrayList<MenuItem> listOfAppetizer = new ArrayList<>();
    ArrayList<MenuItem> listOfMainCourse = new ArrayList<>();
    ArrayList<MenuItem> listOfDrinks = new ArrayList<>();
    ArrayList<MenuItem> listOfDesserts = new ArrayList<>();

    String[] appetizerNames = {"Spring Rolls", "Samosas", "Chicken Wings", "Nachos", "Garlic Bread", "Chicken Lollipop"};
    String[] mainCourseNames = {"Butter Chicken", "Chili Chicken", "Achari Chicken", "Keema Matar", "Matar Mashroom", "Bindi Masala", "Chana Masala"};
    String[] drinksNames = {"Slippery Nipple", "Rum and coke", "Glenlevit", "Sex on the Beach", "Screwdriver", "Pepsi", "7up", "Barbican", "Lassi"};
    String[] dessertsNames = {"Gulab Jamun", "Ras Malai", "Gajar ka Halwa", "kheer", "Rasgulla", "Kulfi", "Jalebi"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        JSONArray appetizerJSONArray = new JSONArray();
        for (int i = 0 ; i < appetizerNames.length ; i++){
            listOfAppetizer.add(new MenuItem(appetizerNames[i], false));
            appetizerJSONArray.put(listOfAppetizer.get(i).getJSONObject());
        }

        JSONArray mainCourseJSONArray = new JSONArray();
        for (int i = 0 ; i < mainCourseNames.length ; i++){
            listOfMainCourse.add(new MenuItem(mainCourseNames[i], false));
            mainCourseJSONArray.put(listOfMainCourse.get(i).getJSONObject());
        }

        JSONArray drinksJSONArray = new JSONArray();
        for (int i = 0 ; i < drinksNames.length ; i++){
            listOfDrinks.add(new MenuItem(drinksNames[i], false));
            drinksJSONArray.put(listOfDrinks.get(i).getJSONObject());
        }

        JSONArray dessertsJSONArray = new JSONArray();
        for(int i = 0; i < dessertsNames.length; i++){
            listOfDesserts.add(new MenuItem(dessertsNames[i], false));
            dessertsJSONArray.put(listOfDesserts.get(i).getJSONObject());
        }

        try{
            menuResponse.put("appetizers", appetizerJSONArray);
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
        Request request = new Request(selectedItemsJSONArray);
        request.execute();
    }

    @Override
    public void getSelectedItems() {
        for(int i = 0; i<adapter.getCount(); i++){
            GenericMenuFragment currentFragment = (GenericMenuFragment) adapter.getFragment(i);
            //TODO: Fix this when server is back
            try{
                for(int j = 0; j < currentFragment.adapter.getCount(); j++){
                    MenuItem currentItem = currentFragment.adapter.getItem(j);
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
