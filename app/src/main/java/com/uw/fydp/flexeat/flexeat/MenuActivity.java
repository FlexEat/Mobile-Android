package com.uw.fydp.flexeat.flexeat;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MenuActivity extends AppCompatActivity implements MenuItemInterface{

    ArrayList<MenuItem> selectedItems = new ArrayList<>();
    JSONObject menuResponse = new JSONObject();

    PagerAdapter adapter;
    ArrayList<MenuItem> listOfAppetizer = new ArrayList<>();
    ArrayList<MenuItem> listOfMainCourse = new ArrayList<>();

    String[] appetizerNames = {"Spring Rolls", "Samosas", "Chicken Wings", "Nachos", "Garlic Bread", "Chicken Lollipop"};
    String[] mainCourseNames = {"Butter Chicken", "Chili Chicken", "Achari Chicken", "Keema Matar", "Matar Mashroom", "Bindi Masala", "Chana Masala"};
    String[] listOfDrinks = {"Slippery Nipple", "Rum and coke", "Glenlevit", "Sex on the Beach", "Screwdriver", "Pepsi", "7up", "Barbican", "Lassi"};
    String[] listOfDesserts = {"Gulab Jamun", "Ras Malai", "Gajar ka Halwa", "kheer", "Rasgulla", "Kulfi", "Jalebi"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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

        try{
            menuResponse.put("appetizers", appetizerJSONArray);
            menuResponse.put("main course", mainCourseJSONArray);
            menuResponse.put("drinks", new JSONArray(Arrays.asList(listOfDrinks)));
            menuResponse.put("desserts", new JSONArray(Arrays.asList(listOfDesserts)));
        }catch (JSONException e){
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Appetizer"));
        tabLayout.addTab(tabLayout.newTab().setText("Main Course"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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
    }

    @Override
    public void getSelectedItems() {
        for(int i = 0; i<adapter.getCount(); i++){
            Fragment currentFragment = adapter.getFragment(i);

            if(currentFragment instanceof AppetizerFragment){
                AppetizerFragment temp = (AppetizerFragment) currentFragment;
                for(int j = 0 ; j < temp.adapter.getCount(); j++){
                    MenuItem currentItem = temp.adapter.getItem(j);
                    if(currentItem.isCheck){
                        selectedItems.add(currentItem);
                    }
                }
            }else if(currentFragment instanceof MainCourseFragment){
                MainCourseFragment temp = (MainCourseFragment) currentFragment;
                for(int j = 0 ; j < temp.adapter.getCount(); j++){
                    MenuItem currentItem = temp.adapter.getItem(j);
                    if(currentItem.isCheck){
                        selectedItems.add(currentItem);
                    }
                }
            }

        }
    }
}
