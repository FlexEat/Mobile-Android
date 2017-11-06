package com.uw.fydp.flexeat.flexeat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by chaitanyakhanna on 2017-11-05.
 */

public class FoodRestrictionsGridAdapter extends BaseAdapter {

    private Context mContext;
    private String[] listOfFoodRestrictions;

    public FoodRestrictionsGridAdapter(Context c, String[] list){
        mContext = c;
        listOfFoodRestrictions = list;
    }

    @Override
    public int getCount() {
        return listOfFoodRestrictions.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder {
        TextView foodLabel;
        CheckBox isChecked;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        ViewHolder vh = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        grid = inflater.inflate(R.layout.cell_single_food, null);

        vh.isChecked = (CheckBox) grid.findViewById(R.id.food_restriction_item_selection);
        vh.foodLabel = (TextView) grid.findViewById(R.id.food_restriction_item_label);

        vh.foodLabel.setText(listOfFoodRestrictions[position]);

        return grid;
    }
}
