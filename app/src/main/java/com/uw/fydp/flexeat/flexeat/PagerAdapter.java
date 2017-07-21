package com.uw.fydp.flexeat.flexeat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by chaitanyakhanna on 2017-07-20.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    JSONObject menuResponse;

    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();

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

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getFragment(final int position) {
        final WeakReference<Fragment> wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr.get();
        } else {
            return null;
        }
    }
}