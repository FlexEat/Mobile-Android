package com.uw.fydp.flexeat.flexeat.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uw.fydp.flexeat.flexeat.model.Restaurant;

import java.util.ArrayList;

/**
 * Created by chaitanyakhanna on 2018-03-02.
 */

public class RestaurantArrayAdapter extends CustomArrayAdapter<Restaurant> {

    Context context;
    int resource;
    ArrayList<Restaurant> arrayListOfRestaurants;

    public RestaurantArrayAdapter(Context context, int resource, ArrayList<Restaurant> values) {
        super(context, resource, values);
        this.context = context;
        this.resource = resource;
        this.arrayListOfRestaurants = values;
    }

    private class ViewHolder {
        TextView itemName;
        TextView itemPrice;
    }

    @Override
    public Object getViewHolder(View rowView) {
        return null;
    }

    @Override
    public void fillViewHolder(Object viewHolder, Restaurant data) {

    }
}
