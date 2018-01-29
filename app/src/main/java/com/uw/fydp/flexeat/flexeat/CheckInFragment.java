package com.uw.fydp.flexeat.flexeat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class CheckInFragment extends Fragment {

    EditText restaurantCode;
    Button submitButton;
    public CheckInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_check_in, container, false);
        getActivity().setTitle("Check In");
        restaurantCode = (EditText) rootView.findViewById(R.id.check_in_code_field);
        submitButton = (Button) rootView.findViewById(R.id.submit_check_in_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(restaurantCode.getText().toString().equals("1234")){
                    Intent goToMenuScreen = new Intent(getActivity(), MenuActivity.class);
                    startActivity(goToMenuScreen);
                }
                else{
                    Toast.makeText(getActivity(), "Wrong code", Toast.LENGTH_LONG).show();
                }
            }
        });


        return rootView;
    }

}
