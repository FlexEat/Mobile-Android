package com.uw.fydp.flexeat.flexeat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.uw.fydp.flexeat.flexeat.adapters.MenuItemArrayAdapter;
import com.uw.fydp.flexeat.flexeat.model.FoodMenuItem;
import com.uw.fydp.flexeat.flexeat.model.FoodRestrictionItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chaitanyakhanna on 2017-10-23.
 */

public class GenericMenuFragment extends Fragment {

    ListView listView;
    ProgressBar loadingSpinner;
    JSONArray itemsJSONArray;
    ArrayList<FoodMenuItem> arrayListOfItems = new ArrayList<>();
    HashMap<Integer, FoodMenuItem> mapOfSelectedItems = new HashMap<>();
    MenuItemArrayAdapter adapter;
    SharedPreferences mPrefs;
    ArrayList<FoodRestrictionItem> listOfSelectedRestrictions;
    boolean hasFoodRestrictions = false;
    ArrayList<String> containsRestrictions = new ArrayList<>();
    ArrayList<String> categoryRestrictions = new ArrayList<>();
    String userID = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_generic_menu, container, false);
        loadingSpinner = (ProgressBar)rootView.findViewById(R.id.loading_spinner);
        // make facebook request
        getUserIDFromFacebook();

        String allItemsAsString = getArguments().getString("itemsAsString");
        try {
            itemsJSONArray = new JSONArray(allItemsAsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i< itemsJSONArray.length(); i++)
        {
            try {
                arrayListOfItems.add(new FoodMenuItem(itemsJSONArray.getJSONObject(i)));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new MenuItemArrayAdapter(getContext(), R.layout.item_menu, arrayListOfItems, MenuItemArrayAdapter.SHOW_PRICE);

        listView = (ListView) rootView.findViewById(R.id.generic_menu_list);
        listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final FoodMenuItem currentItem = arrayListOfItems.get(i);

                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_menu_item_dialog);
                TextView name = (TextView) dialog.findViewById(R.id.food_item_name);
                TextView description = (TextView) dialog.findViewById(R.id.food_item_description);
                TextView price = (TextView) dialog.findViewById(R.id.food_item_price);
                Button decreaseQuantity = (Button) dialog.findViewById(R.id.decrease_quantity_button);
                Button increaseQuantity = (Button) dialog.findViewById(R.id.increase_quantity_button);
                LinearLayout foodRestrictionContainer = (LinearLayout)dialog.findViewById(R.id.food_restriction_container);
                TextView foodRestrictionReason = (TextView)dialog.findViewById(R.id.food_restriction_reason);

                final TextView displayQuantity = (TextView) dialog.findViewById(R.id.quantity_display);
                displayQuantity.setText(Integer.toString(currentItem.quantity));

                decreaseQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currentItem.quantity > 0){
                            currentItem.quantity--;
                        }
                        displayQuantity.setText(Integer.toString(currentItem.quantity));
                    }
                });

                increaseQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currentItem.quantity < 10){
                            currentItem.quantity++;
                        }
                        displayQuantity.setText(Integer.toString(currentItem.quantity));
                    }
                });

                Picasso.with(getActivity())
                        .load(currentItem.imageURL)
                        .into((ImageView) dialog.findViewById(R.id.food_item_picture));

                name.setText(currentItem.name);
                description.setText(currentItem.description);
                DecimalFormat df = new DecimalFormat("0.00");
                price.setText("$".concat(df.format(currentItem.price)));

                Button addToOrderButton = (Button)dialog.findViewById(R.id.add_to_order_button);
                addToOrderButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                addToOrderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(currentItem.quantity > 0) {
                            mapOfSelectedItems.put(currentItem.foodItemID, currentItem);
                        }
                        dialog.cancel();
                    }
                });

                while (userID == ""){
                    // show loading spinner
                    loadingSpinner.setVisibility(View.VISIBLE);
                }

                if(userID != ""){
                    // hide loading spinner
                    loadingSpinner.setVisibility(View.GONE);
                    // fb has returned and we have current user's restrictions
                    hasFoodRestrictions = false;
                    isAnyFoodRestrictionMet(currentItem);
                    if(hasFoodRestrictions) {
                        foodRestrictionContainer.setVisibility(View.VISIBLE);
                        foodRestrictionReason.setText(setFoodRestrictionReasonString());
                    } else{
                        foodRestrictionContainer.setVisibility(View.GONE);
                    }
                }

                dialog.show();

            }
        });
        listView.setAdapter(adapter);

        return rootView;
    }

    public void isAnyFoodRestrictionMet(final FoodMenuItem currentItem){
        categoryRestrictions.clear();
        containsRestrictions.clear();
        if (listOfSelectedRestrictions.size() > 0){
            // have food restrictions. lets see if they match any of the restrictions on current item
            for(int i = 0; i < listOfSelectedRestrictions.size(); i++){
                switch (listOfSelectedRestrictions.get(i).name){
                    case "Gluten":
                        if (!currentItem.isGlutenFree){
                            hasFoodRestrictions = true;
                            containsRestrictions.add("gluten");
                        }
                        break;
                    case "Peanuts":
                        if(currentItem.containsPeanuts){
                            hasFoodRestrictions = true;
                            containsRestrictions.add("peanuts");
                        }
                        break;
                    case "Fish":
                        if(currentItem.containsFish){
                            hasFoodRestrictions = true;
                            containsRestrictions.add("fish");
                        }
                        break;
                    case "Soy":
                        if(currentItem.containsSoy){
                            hasFoodRestrictions = true;
                            containsRestrictions.add("soy");
                        }
                        break;
                    case "Halal":
                        if(!currentItem.isHalal){
                            hasFoodRestrictions = true;
                            categoryRestrictions.add("halal");
                        }
                        break;
                    case "Vegetarian":
                        if(!currentItem.isVegetarian){
                            hasFoodRestrictions = true;
                            categoryRestrictions.add("vegetarian");
                        }
                        break;
                    case "Vegan":
                        if(!currentItem.isVegan){
                            hasFoodRestrictions = true;
                            categoryRestrictions.add("vegan");
                        }
                        break;
                }

            }
        }else {
            hasFoodRestrictions = false;
        }

    }

    public String setFoodRestrictionReasonString(){
        String toBeReturned = "";
        if(containsRestrictions.size() > 0){
            toBeReturned = "This item contains ";
            for(int i = 0; i < containsRestrictions.size(); i++){
                toBeReturned = toBeReturned.concat(containsRestrictions.get(i));
                if(i == containsRestrictions.size() - 1){
                    toBeReturned = toBeReturned.concat(". ");
                } else if ( i == containsRestrictions.size() - 2){
                    toBeReturned = toBeReturned.concat(" and ");
                } else {
                    toBeReturned = toBeReturned.concat(", ");
                }
            }
        }

        if(categoryRestrictions.size() > 0){
            toBeReturned = toBeReturned.concat("This item is not ");
            for(int i = 0; i < categoryRestrictions.size(); i++){
                toBeReturned = toBeReturned.concat(categoryRestrictions.get(i));
                if(i == categoryRestrictions.size() - 1){
                    toBeReturned = toBeReturned.concat(". ");
                } else if (i == categoryRestrictions.size() - 2){
                    toBeReturned = toBeReturned.concat(" or ");
                } else {
                    toBeReturned = toBeReturned.concat(", ");
                }
            }
        }
        return toBeReturned;
    }

    public void getUserIDFromFacebook(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    userID = (String) object.get("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // get all food restrictions from sharedprefs
                mPrefs = getActivity().getSharedPreferences("com.uw.fydp.flexeat.flexeat", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = mPrefs.getString(userID, "");
                Type foodRestrictionItemType = new TypeToken<ArrayList<FoodRestrictionItem>>() {
                }.getType();

                if (gson.fromJson(json, foodRestrictionItemType) != null)
                    listOfSelectedRestrictions = gson.fromJson(json, foodRestrictionItemType);
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
