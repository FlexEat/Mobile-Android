package com.uw.fydp.flexeat.flexeat.adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.uw.fydp.flexeat.flexeat.GenericMenuFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by chaitanyakhanna on 2017-07-20.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    JSONObject menuResponse;

    public PagerAdapter(FragmentManager fm, int numOfTabs, JSONObject menuResponse) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
        this.menuResponse = menuResponse;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        try {
            GenericMenuFragment tab = new GenericMenuFragment();
            bundle.putString("itemsAsString", menuResponse.getJSONArray(menuResponse.names().getString(position)).toString());
            tab.setArguments(bundle);
            return tab;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}