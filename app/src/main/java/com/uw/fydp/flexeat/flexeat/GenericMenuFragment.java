package com.uw.fydp.flexeat.flexeat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

/**
 * Created by chaitanyakhanna on 2017-10-23.
 */

public class GenericMenuFragment extends Fragment {

    ListView listView;
    JSONArray itemsJSONArray;
    ArrayList<FoodMenuItem> arrayListOfItems = new ArrayList<>();
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.layout_menu_item_dialog);
                ImageView image = (ImageView)dialog.findViewById(R.id.food_item_picture);
                TextView name = (TextView) dialog.findViewById(R.id.food_item_name);
                TextView ingredients = (TextView) dialog.findViewById(R.id.food_item_ingredients);

                Picasso.with(getContext())
                        .load(arrayListOfItems.get(i).imageURL)
                        .resize(64,64).noFade().into(image);
                name.setText(arrayListOfItems.get(i).name);
                ingredients.setText(arrayListOfItems.get(i).ingredients);



                dialog.show();

            }
        });
        listView.setAdapter(adapter);

        return rootView;
    }
}
