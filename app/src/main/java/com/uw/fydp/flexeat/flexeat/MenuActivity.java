package com.uw.fydp.flexeat.flexeat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.uw.fydp.flexeat.flexeat.adapters.MenuItemArrayAdapter;
import com.uw.fydp.flexeat.flexeat.adapters.PagerAdapter;
import com.uw.fydp.flexeat.flexeat.api.Request;
import com.uw.fydp.flexeat.flexeat.model.FoodMenuItem;
import com.uw.fydp.flexeat.flexeat.model.MenuItemInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuActivity extends AppCompatActivity implements MenuItemInterface {

    ArrayList<FoodMenuItem> listOfSelectedItems = new ArrayList<>();
    JSONObject menuResponse = new JSONObject();

    PagerAdapter adapter;
    ArrayList<FoodMenuItem> listOfAppetizer = new ArrayList<>();
    ArrayList<FoodMenuItem> listOfMainCourse = new ArrayList<>();
    ArrayList<FoodMenuItem> listOfDrinks = new ArrayList<>();
    ArrayList<FoodMenuItem> listOfDesserts = new ArrayList<>();

    int tableNumber;
    int restaurantID;
    String restaurantName;

    Dialog reviewOrderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //String menuFromAPI = null;
        String menuFromAPI = getIntent().getStringExtra("menuAsString");
        restaurantName = getIntent().getStringExtra("restaurantName");
        tableNumber = getIntent().getIntExtra("tableNumber", -1);
        restaurantID = getIntent().getIntExtra("restaurantID", -1);

        Log.d("menu", menuFromAPI);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(restaurantName);

        LinearLayout layoutInToolbar = (LinearLayout)toolbar.findViewById(R.id.toolbar_item_container);
        ImageButton waiterButton = new ImageButton(this);
        ImageButton orderStatusButton = new ImageButton(this);
        waiterButton.setImageDrawable(getResources().getDrawable(R.drawable.waiter));
        orderStatusButton.setImageDrawable(getResources().getDrawable(R.drawable.current_order));
        LinearLayout.LayoutParams paramsToolbar = new LinearLayout.LayoutParams(150, 150);
        paramsToolbar.gravity = Gravity.RIGHT;
        paramsToolbar.gravity = Gravity.END;
        waiterButton.setBackgroundColor(Color.TRANSPARENT);
        waiterButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        waiterButton.setLayoutParams(paramsToolbar);
        waiterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String endpoint = "/api/help";
                JSONObject helpObject = new JSONObject();
                try{
                    helpObject.put("restaurant_id", restaurantID);
                    helpObject.put("table_number", tableNumber);
                } catch(JSONException e){
                    e.printStackTrace();
                }
                Request.post(getApplicationContext(), endpoint, helpObject, new Request.Callback() {
                    @Override
                    public void onRespond(boolean success, int code, String res, boolean isRemoteResponse) {
                        Toast.makeText(getApplicationContext(), "Help is on the way", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(boolean success, int code, Exception e) {
                        Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        orderStatusButton.setBackgroundColor(Color.TRANSPARENT);
        orderStatusButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        orderStatusButton.setLayoutParams(paramsToolbar);
        orderStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToOrderScreen = new Intent(MenuActivity.this, OrdersActivity.class);
                goToOrderScreen.putExtra("restaurant_id", restaurantID);
                goToOrderScreen.putExtra("table_number", tableNumber);
                goToOrderScreen.putExtra("restaurant_name", restaurantName);
                startActivity(goToOrderScreen);
            }
        });

        layoutInToolbar.addView(waiterButton);
        layoutInToolbar.addView(orderStatusButton);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        JSONArray appetizerJSONArray = new JSONArray();
        JSONArray mainCourseJSONArray = new JSONArray();
        JSONArray drinksJSONArray = new JSONArray();
        JSONArray dessertsJSONArray = new JSONArray();

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
                    case "Drinks":
                        listOfDrinks.add(currentItem);
                        drinksJSONArray.put(fullMenu.getJSONObject(i));
                        break;
                    case "Desserts":
                        listOfDesserts.add(currentItem);
                        dessertsJSONArray.put(fullMenu.getJSONObject(i));
                        break;
                }
            }

        } catch (JSONException e){
            e.printStackTrace();
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

    public void onReviewOrderClick(View view) {
        listOfSelectedItems.clear();
        getSelectedItems();

        if(listOfSelectedItems.size() > 0) {

            reviewOrderDialog = new Dialog(this);
            reviewOrderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            reviewOrderDialog.setContentView(R.layout.layout_review_order_dialog);
            ListView listForReviewOrder = (ListView) reviewOrderDialog.findViewById(R.id.review_order_list);
            Button submitOrder = (Button) reviewOrderDialog.findViewById(R.id.submit_order_button);
            submitOrder.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            MenuItemArrayAdapter reviewOrder = new MenuItemArrayAdapter(getApplicationContext(), R.layout.item_menu, listOfSelectedItems, MenuItemArrayAdapter.SHOW_QUANTITY);
            listForReviewOrder.setAdapter(reviewOrder);
            submitOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSubmitOrder(view);
                }
            });
            reviewOrderDialog.show();
        } else {
            Toast.makeText(getApplicationContext(), "Please add items to your order", Toast.LENGTH_SHORT).show();
        }

    }

    public void onSubmitOrder(View view){
        JSONArray selectedItemsJSONArray = new JSONArray();
        for (int i = 0; i< listOfSelectedItems.size(); i++){
            selectedItemsJSONArray.put(listOfSelectedItems.get(i).getJSONObject());
        }
        JSONObject selectedItems = new JSONObject();
        try{
            selectedItems.put("selected_items", selectedItemsJSONArray);
            selectedItems.put("restaurant_id", restaurantID);
            selectedItems.put("table_number", tableNumber);
        } catch (JSONException e){
            e.printStackTrace();
        }
        String endpoint = "/api/order";
        Request.post(getApplicationContext(), endpoint, selectedItems, new Request.Callback() {
            @Override
            public void onRespond(boolean success, int code, String res, boolean isRemoteResponse) {
                if (success)
                    Toast.makeText(getApplicationContext(), "Order Submitted", Toast.LENGTH_SHORT).show();
                for(int i = 0; i < adapter.getInitiatedFragmentCount(); i++){
                    GenericMenuFragment currentFragment = (GenericMenuFragment)adapter.getFragment(i);
                    currentFragment.mapOfSelectedItems.clear();
                }
                reviewOrderDialog.cancel();
            }

            @Override
            public void onError(boolean success, int code, Exception e) {
                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void getSelectedItems() {
        HashMap<Integer, FoodMenuItem> mapOfAllSelectedItems = new HashMap<>();
        for(int i = 0; i < adapter.getInitiatedFragmentCount(); i++){
            GenericMenuFragment currentFragment = (GenericMenuFragment) adapter.getFragment(i);
            try{
                mapOfAllSelectedItems.putAll(currentFragment.mapOfSelectedItems);
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        listOfSelectedItems.addAll(mapOfAllSelectedItems.values());
    }
}
