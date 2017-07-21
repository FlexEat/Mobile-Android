package com.uw.fydp.flexeat.flexeat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

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
            switch(position) {
                case 0:
                    AppetizerFragment tab1 = new AppetizerFragment();
                    bundle.putString("appetizersAsString", menuResponse.getJSONArray("appetizers").toString());
                    tab1.setArguments(bundle);
                    return tab1;
                case 1:
                    MainCourseFragment tab2 = new MainCourseFragment();
                    bundle.putString("mainCoursesAsString", menuResponse.getJSONArray("main course").toString());
                    tab2.setArguments(bundle);
                    return tab2;
                default:
                    return null;
            }
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