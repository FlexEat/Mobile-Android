package com.uw.fydp.flexeat.flexeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.uw.fydp.flexeat.flexeat.api.Request;

import org.json.JSONException;
import org.json.JSONObject;


public class CheckInFragment extends Fragment {

    EditText restaurantCode;
    Button submitButton;
    ProgressBar loadingSpinner;
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
        loadingSpinner = (ProgressBar) rootView.findViewById(R.id.loading_spinner);
        submitButton = (Button) rootView.findViewById(R.id.submit_check_in_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(restaurantCode.getText().toString().equals("1234")){
                    getRestaurantMenu();
                }
                else{
                    Toast.makeText(getActivity(), "Wrong code", Toast.LENGTH_LONG).show();
                }
            }
        });


        return rootView;
    }

    private void getRestaurantMenu() {
        loadingSpinner.setVisibility(View.VISIBLE);
        String endpoint = "/api/menus";
        JSONObject restaurantHeader = new JSONObject();
        try {
            restaurantHeader.put("restaurant-id", 1);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        Request.get(getContext(), endpoint, restaurantHeader, new Request.Callback(){

            @Override
            public void onRespond(boolean success, int code, final String res, boolean isRemoteResponse) {
                loadingSpinner.setVisibility(View.GONE);
                getActivity().runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "got something", Toast.LENGTH_LONG).show();
                        Log.d("res", res);
                        Intent goToMenuScreen = new Intent(getActivity(), MenuActivity.class);
                        goToMenuScreen.putExtra("menuAsString", res);
                        startActivity(goToMenuScreen);
                    }
                });
            }

            @Override
            public void onError(boolean success, int code, Exception e) {
                loadingSpinner.setVisibility(View.GONE);
                e.printStackTrace();
                Log.d("error", "request error");
            }
        });
    }

}
