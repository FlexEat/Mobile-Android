package com.uw.fydp.flexeat.flexeat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by chaitanyakhanna on 2017-10-23.
 */

public class GenericFragment extends Fragment {

    ListView listView;
    JSONArray itemsJSONArray;
    ArrayList<MenuItem> arrayListOfItems = new ArrayList<>();
    MenuItemArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_generic, container, false);
        String allItemsAsString = getArguments().getString("itemsAsString");
        try {
            itemsJSONArray = new JSONArray(allItemsAsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i< itemsJSONArray.length(); i++)
        {
            try {
                arrayListOfItems.add(new MenuItem(itemsJSONArray.getJSONObject(i)));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new MenuItemArrayAdapter(getContext(), R.layout.item_menu, arrayListOfItems);

        listView = (ListView) rootView.findViewById(R.id.generic_list);
        listView.setAdapter(adapter);

        return rootView;
    }
}
