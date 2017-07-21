package com.uw.fydp.flexeat.flexeat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class AppetizerFragment extends Fragment {

    ListView appetizerListView;
    JSONArray appetizerJSONArray;
    ArrayList<MenuItem> arrayListOfAppetizers = new ArrayList<>();
    MenuItemArrayAdapter adapter;

    public AppetizerFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_appetizer, container, false);
        String allAppetizersAsString = getArguments().getString("appetizersAsString");
        try {
            appetizerJSONArray = new JSONArray(allAppetizersAsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0, count = appetizerJSONArray.length(); i< count; i++)
        {
            try {
                arrayListOfAppetizers.add(new MenuItem(appetizerJSONArray.getJSONObject(i)));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new MenuItemArrayAdapter(getContext(), R.layout.item_menu, arrayListOfAppetizers);

        appetizerListView = (ListView) rootView.findViewById(R.id.appetizer_list);
        appetizerListView.setAdapter(adapter);

        return rootView;
    }

    public void getSeletedItems(){
        for ( int i=0; i < appetizerListView.getAdapter().getCount(); i++) {
            appetizerListView.getAdapter();
        }
    }
}
