package com.uw.fydp.flexeat.flexeat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uw.fydp.flexeat.flexeat.adapters.PagerAdapter;
import com.uw.fydp.flexeat.flexeat.api.Request;
import com.uw.fydp.flexeat.flexeat.model.MenuItem;
import com.uw.fydp.flexeat.flexeat.model.MenuItemInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity
        implements MenuItemInterface, NavigationView.OnNavigationItemSelectedListener {

    ArrayList<MenuItem> selectedItems = new ArrayList<>();
    JSONObject menuResponse = new JSONObject();
    SharedPreferences mPrefs;

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
        mPrefs = this.getSharedPreferences("com.uw.fydp.flexeat.flexeat", MODE_PRIVATE);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set name, email and image in menu
        TextView navUserName = navigationView.getHeaderView(0).findViewById(R.id.userNameText);
        navUserName.setText(mPrefs.getString("userName", ""));
        TextView navUserEmail = navigationView.getHeaderView(0).findViewById(R.id.userEmailText);
        navUserEmail.setText(mPrefs.getString("userEmail", ""));
        String profilePicUrl = mPrefs.getString("userImagePath", "");
        Picasso.with(getApplicationContext()).load(profilePicUrl).fit().into((ImageView) navigationView.getHeaderView(0).findViewById(R.id.profilePic));


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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(android.view.MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit_profile) {
            Intent goToProfileEdit = new Intent(MenuActivity.this, ProfileSetupActivity.class);
            startActivity(goToProfileEdit);
        } else if (id == R.id.nav_logout) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getApplicationContext());
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
