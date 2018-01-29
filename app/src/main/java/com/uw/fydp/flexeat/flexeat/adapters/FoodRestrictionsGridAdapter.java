package com.uw.fydp.flexeat.flexeat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.uw.fydp.flexeat.flexeat.R;
import com.uw.fydp.flexeat.flexeat.model.FoodRestrictionItem;

import java.util.ArrayList;

/**
 * Created by chaitanyakhanna on 2017-11-05.
 */

public class FoodRestrictionsGridAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<FoodRestrictionItem> arrayListOfFoodRestrictionItem;

    public FoodRestrictionsGridAdapter(Context c, ArrayList<FoodRestrictionItem> list){
        mContext = c;
        arrayListOfFoodRestrictionItem = list;
    }

    public class ViewHolder {
        TextView foodLabel;
        CheckBox isChecked;
    }

    @Override
    public int getCount() {
        return arrayListOfFoodRestrictionItem.size();
    }

    @Override
    public FoodRestrictionItem getItem(int i) {
        return arrayListOfFoodRestrictionItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid = convertView;
        ViewHolder vh = new ViewHolder();
        if (grid == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.cell_single_food, parent, false);
            vh.isChecked = (CheckBox) grid.findViewById(R.id.food_restriction_item_selection);
            vh.foodLabel = (TextView) grid.findViewById(R.id.food_restriction_item_label);
            grid.setTag(vh);
        } else {
            vh = (ViewHolder) grid.getTag();
        }

        final FoodRestrictionItem data = getItem(position);
        vh.isChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                data.setCheck(isChecked);
            }
        });

        vh.foodLabel.setText(arrayListOfFoodRestrictionItem.get(position).name);
        vh.isChecked.setChecked(data.isCheck);

        return grid;
    }
}
