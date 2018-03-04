package com.uw.fydp.flexeat.flexeat;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uw.fydp.flexeat.flexeat.adapters.MenuItemArrayAdapter;
import com.uw.fydp.flexeat.flexeat.api.Request;
import com.uw.fydp.flexeat.flexeat.model.FoodMenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {
    int restaurantID = -1;
    int tableNumber = -1;

    ListView orderList;
    MenuItemArrayAdapter adapter;
    ArrayList<FoodMenuItem> arrayOfOrderItems = new ArrayList<>();
    LinearLayout buttonsLayout;
    TextView orderListEmptyTextView;
    Button payViaAppButton;
    Button payViaServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        getSupportActionBar().setTitle(getIntent().getStringExtra("restaurant_name"));

        orderList = (ListView)findViewById(R.id.list_of_orders);
        adapter = new MenuItemArrayAdapter(getApplicationContext(), R.layout.item_menu, arrayOfOrderItems, MenuItemArrayAdapter.SHOW_STATUS);
        orderList.setAdapter(adapter);

        buttonsLayout = (LinearLayout) findViewById(R.id.buttons_layout);

        orderListEmptyTextView = (TextView) findViewById(R.id.order_list_empty_text_view);

        restaurantID = getIntent().getIntExtra("restaurant_id", -1);
        tableNumber = getIntent().getIntExtra("table_number", -1);

        String endpoint = "/api/order";
        JSONObject header = new JSONObject();
        try{
            header.put("restaurant-id", restaurantID);
            header.put("table-number", tableNumber);
        } catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
        }
        Request.get(getApplicationContext(), endpoint, header, new Request.Callback() {
            @Override
            public void onRespond(boolean success, int code, String res, boolean isRemoteResponse) {
                if(success){
                    try{
                        JSONArray response = new JSONArray(res);
                        if (response.length() != 0){
                            orderList.setVisibility(View.VISIBLE);
                            buttonsLayout.setVisibility(View.VISIBLE);
                            for(int i = 0; i < response.length(); i++){
                                arrayOfOrderItems.add(new FoodMenuItem(response.getJSONObject(i)));
                                adapter.notifyDataSetChanged();
                            }
                            orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(OrdersActivity.this);
                                    builder.setMessage("Are you sure you want to cancel this item?")
                                            .setPositiveButton("Yes", null)
                                            .setNegativeButton("No", null)
                                            .show();
                                }
                            });
                        } else {
                            orderListEmptyTextView.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(boolean success, int code, Exception e) {
                e.printStackTrace();
            }
        });

        payViaServer = (Button) findViewById(R.id.waiter_pay_button);

        payViaServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String endpoint = "/api/help";
                JSONObject helpObject = new JSONObject();
                try{
                    helpObject.put("restaurant_id", restaurantID);
                    helpObject.put("table_number", tableNumber);
                    helpObject.put("is_ready_to_pay", true);
                } catch(JSONException e){
                    e.printStackTrace();
                }
                Request.post(getApplicationContext(), endpoint, helpObject, new Request.Callback() {
                    @Override
                    public void onRespond(boolean success, int code, String res, boolean isRemoteResponse) {
                        Toast.makeText(getApplicationContext(), "Server is bringing the machine", Toast.LENGTH_LONG).show();
                        Intent gotoRating = new Intent(OrdersActivity.this, RatingActivity.class);
                        gotoRating.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(gotoRating);
                    }

                    @Override
                    public void onError(boolean success, int code, Exception e) {
                        Toast.makeText(getApplicationContext(), "Something went wrong. Please try again", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        payViaAppButton = (Button) findViewById(R.id.application_pay_button);

        payViaAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoPaymentScreen = new Intent(OrdersActivity.this, PayActivity.class);
                startActivity(gotoPaymentScreen);
            }
        });

    }
}
