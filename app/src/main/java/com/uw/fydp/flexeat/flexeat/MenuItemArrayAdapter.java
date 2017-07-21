package com.uw.fydp.flexeat.flexeat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chaitanyakhanna on 2017-07-20.
 */

public class MenuItemArrayAdapter extends CustomArrayAdapter<MenuItem> {

    Context context;
    ArrayList<MenuItem> arrayListOfMenuItems;
    int resource;

    public MenuItemArrayAdapter(Context context, int resource, ArrayList<MenuItem> values) {
        super(context, resource, values);
        this.context = context;
        this.arrayListOfMenuItems = values;
        this.resource = resource;
    }

    private class ViewHolder {
        TextView itemName;
        CheckBox isChecked;
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

        final MenuItem data = getItem(position); // get data
        viewHolder.isChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setCheck(isChecked);
            }
        });
        fillViewHolder(viewHolder, data); // Populate View Holder

        return rowView; // Return view (required by super class)
    }


    @Override
    public Object getViewHolder(View rowView) {
        final ViewHolder viewHolder = new ViewHolder();

        viewHolder.itemName = (TextView) rowView.findViewById(R.id.item_name);
        viewHolder.isChecked = (CheckBox) rowView.findViewById(R.id.item_selection);

        return viewHolder;
    }

    @Override
    public void fillViewHolder(Object viewHolder, MenuItem data) {
        final ViewHolder mViewHolder = (ViewHolder) viewHolder;
        if (data.name != null) {
            mViewHolder.itemName.setText(data.name);
        }
        mViewHolder.isChecked.setSelected(data.isCheck);
    }
}
