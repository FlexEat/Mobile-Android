package com.uw.fydp.flexeat.flexeat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

public class AppetizerFragment extends Fragment {

    ListView appetizerListView;
    ArrayAdapter<String> adapter;
    JSONArray listOfAppetizer;

    public AppetizerFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_appetizer, container, false);
        String allAppetizersAsString = getArguments().getString("appetizersAsString");
        try {
            listOfAppetizer = new JSONArray(allAppetizersAsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] appetizerList = new String[listOfAppetizer.length()];
        for(int i = 0, count = listOfAppetizer.length(); i< count; i++)
        {
            try {
                appetizerList[i] = listOfAppetizer.getString(i);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new ArrayAdapter<>(getContext(), R.layout.item_menu, R.id.item_name, appetizerList);

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
