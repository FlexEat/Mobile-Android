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

import java.util.ArrayList;

public class MainCourseFragment extends Fragment {

    ListView mainCourseListView;
    JSONArray mainCourseJSONArray;
    ArrayList<MenuItem> arrayListOfMainCourse = new ArrayList<>();
    MenuItemArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_course, container, false);

        String allMainCourseAsString = getArguments().getString("mainCoursesAsString");
        try {
            mainCourseJSONArray = new JSONArray(allMainCourseAsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0, count = mainCourseJSONArray.length(); i< count; i++)
        {
            try {
                arrayListOfMainCourse.add(new MenuItem(mainCourseJSONArray.getJSONObject(i)));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new MenuItemArrayAdapter(getContext(), R.layout.item_menu, arrayListOfMainCourse);

        mainCourseListView = (ListView) rootView.findViewById(R.id.main_course_list);
        mainCourseListView.setAdapter(adapter);

        return rootView;
    }
}
