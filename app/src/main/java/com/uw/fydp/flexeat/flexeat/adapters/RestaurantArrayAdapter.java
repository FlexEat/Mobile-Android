package com.uw.fydp.flexeat.flexeat.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uw.fydp.flexeat.flexeat.R;
import com.uw.fydp.flexeat.flexeat.model.Restaurant;

import java.text.DecimalFormat;
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
        TextView restaurantName;
        TextView restaurantRating;
        ImageView restaurantRatingStarImage;
    }

    @Override
    public Object getViewHolder(View rowView) {
        final ViewHolder viewHolder = new ViewHolder();

        viewHolder.restaurantName = (TextView) rowView.findViewById(R.id.item_name);
        viewHolder.restaurantRating = (TextView) rowView.findViewById(R.id.item_price);
        viewHolder.restaurantRatingStarImage = (ImageView) rowView.findViewById(R.id.star_image);

        return viewHolder;
    }

    @Override
    public void fillViewHolder(Object viewHolder, Restaurant data) {
        final ViewHolder mViewHolder = (ViewHolder) viewHolder;
        mViewHolder.restaurantName.setText(data.name);
        DecimalFormat df = new DecimalFormat("0.00");
        mViewHolder.restaurantRating.setText(df.format(data.rating));
        mViewHolder.restaurantRatingStarImage.setVisibility(View.VISIBLE);
    }
}
