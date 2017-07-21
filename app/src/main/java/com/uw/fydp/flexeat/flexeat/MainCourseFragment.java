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

public class MainCourseFragment extends Fragment {

    ListView mainCourseListView;
    ArrayAdapter<String> adapter;
    JSONArray listOfMainCourse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_course, container, false);

        String allMainCourseAsString = getArguments().getString("mainCoursesAsString");
        try {
            listOfMainCourse = new JSONArray(allMainCourseAsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] mainCourseList = new String[listOfMainCourse.length()];
        for(int i = 0, count = listOfMainCourse.length(); i< count; i++)
        {
            try {
                mainCourseList[i] = listOfMainCourse.getString(i);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new ArrayAdapter<>(getContext(), R.layout.item_menu, R.id.item_name, mainCourseList);

        mainCourseListView = (ListView) rootView.findViewById(R.id.main_course_list);
        mainCourseListView.setAdapter(adapter);

        return rootView;
    }
}
