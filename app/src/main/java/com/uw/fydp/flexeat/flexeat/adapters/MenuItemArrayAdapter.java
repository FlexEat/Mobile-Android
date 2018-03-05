package com.uw.fydp.flexeat.flexeat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.uw.fydp.flexeat.flexeat.model.FoodMenuItem;
import com.uw.fydp.flexeat.flexeat.R;

import java.util.ArrayList;

/**
 * Created by chaitanyakhanna on 2017-07-20.
 */

public class MenuItemArrayAdapter extends CustomArrayAdapter<FoodMenuItem> {

    public final static int SHOW_QUANTITY = 0;
    public final static int SHOW_PRICE = 1;
    public final static int SHOW_STATUS = 2;

    Context context;
    ArrayList<FoodMenuItem> arrayListOfFoodMenuItems;
    int resource;
    int displayType;

    public MenuItemArrayAdapter(Context context, int resource, ArrayList<FoodMenuItem> values, int typeOfDisplay) {
        super(context, resource, values);
        this.context = context;
        this.arrayListOfFoodMenuItems = values;
        this.resource = resource;
        this.displayType = typeOfDisplay;
    }

    private class ViewHolder {
        TextView itemName;
        TextView itemPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        // Conditionally inflate view
        ViewHolder viewHolder = new ViewHolder();
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(resource, parent, false);
            viewHolder = (ViewHolder) getViewHolder(rowView); // setup View Holder
            rowView.setTag(viewHolder); // set tag for recycling
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        final FoodMenuItem data = getItem(position); // get data

        fillViewHolder(viewHolder, data); // Populate View Holder

        return rowView; // Return view (required by super class)
    }


    @Override
    public Object getViewHolder(View rowView) {
        final ViewHolder viewHolder = new ViewHolder();

        viewHolder.itemName = (TextView) rowView.findViewById(R.id.item_name);
        viewHolder.itemPrice = (TextView) rowView.findViewById(R.id.item_price);

        return viewHolder;
    }

    @Override
    public void fillViewHolder(Object viewHolder, FoodMenuItem data) {
        final ViewHolder mViewHolder = (ViewHolder) viewHolder;
        if (data.name != null) {
            mViewHolder.itemName.setText(data.name);
            if(this.displayType == SHOW_STATUS){
                mViewHolder.itemPrice.setText(data.status);
            } else if (this.displayType == SHOW_PRICE){
                mViewHolder.itemPrice.setText("$".concat(data.price));
            } else if (this.displayType == SHOW_QUANTITY){
                mViewHolder.itemPrice.setText(Integer.toString(data.quantity));
            }

        }
    }
}
