package com.uw.fydp.flexeat.flexeat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uw.fydp.flexeat.flexeat.adapters.MenuItemArrayAdapter;
import com.uw.fydp.flexeat.flexeat.model.FoodMenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chaitanyakhanna on 2017-10-23.
 */

public class GenericMenuFragment extends Fragment {

    ListView listView;
    JSONArray itemsJSONArray;
    ArrayList<FoodMenuItem> arrayListOfItems = new ArrayList<>();
    HashMap<Integer, FoodMenuItem> mapOfSelectedItems = new HashMap<>();
    MenuItemArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_generic_menu, container, false);
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

        adapter = new MenuItemArrayAdapter(getContext(), R.layout.item_menu, arrayListOfItems);

        listView = (ListView) rootView.findViewById(R.id.generic_menu_list);
        listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_menu_item_dialog);
                TextView name = (TextView) dialog.findViewById(R.id.food_item_name);
                TextView description = (TextView) dialog.findViewById(R.id.food_item_description);
                Button decreaseQuantity = (Button) dialog.findViewById(R.id.decrease_quantity_button);
                Button increaseQuantity = (Button) dialog.findViewById(R.id.increase_quantity_button);
                final TextView displayQuantity = (TextView) dialog.findViewById(R.id.quantity_display);
                displayQuantity.setText(Integer.toString(arrayListOfItems.get(i).quantity));

                decreaseQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (arrayListOfItems.get(i).quantity > 0){
                            arrayListOfItems.get(i).quantity--;
                        }
                        displayQuantity.setText(Integer.toString(arrayListOfItems.get(i).quantity));
                    }
                });

                increaseQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (arrayListOfItems.get(i).quantity < 10){
                            arrayListOfItems.get(i).quantity++;
                        }
                        displayQuantity.setText(Integer.toString(arrayListOfItems.get(i).quantity));
                    }
                });

                Picasso.with(getActivity())
                        .setLoggingEnabled(true);

                Picasso.with(getActivity())
                        .load(arrayListOfItems.get(i).imageURL)
                        .into((ImageView) dialog.findViewById(R.id.food_item_picture));

                name.setText(arrayListOfItems.get(i).name);
                description.setText(arrayListOfItems.get(i).description);

                Button addToOrderButton = (Button)dialog.findViewById(R.id.add_to_order_button);
                addToOrderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mapOfSelectedItems.put(arrayListOfItems.get(i).foodItemID, arrayListOfItems.get(i));
                        dialog.cancel();
                    }
                });

                dialog.show();

            }
        });
        listView.setAdapter(adapter);

        return rootView;
    }
}
