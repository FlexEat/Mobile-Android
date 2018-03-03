package com.uw.fydp.flexeat.flexeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.uw.fydp.flexeat.flexeat.api.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CheckInFragment extends Fragment {

    EditText restaurantCode;
    Button submitButton;
    ProgressBar loadingSpinner;
    String restaurantName;
    String sessionCode;
    int tableNumber;
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
        restaurantCode.addTextChangedListener(new TextWatcher() {
            int len = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String currentString = restaurantCode.getText().toString();
                len = currentString.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String currentString = restaurantCode.getText().toString();
                if(currentString.length() == 3 && len < currentString.length()){
                    restaurantCode.append("-");
                }
                if(currentString.length() == 3  && len > currentString.length()){
                    currentString = currentString.substring(0, 2);
                    restaurantCode.setText(currentString);
                    restaurantCode.setSelection(currentString.length());
                }
            }
        });

        restaurantCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && i == KeyEvent.KEYCODE_ENTER){
                    validateCode();
                    return true;
                }
                return false;
            }
        });

        loadingSpinner = (ProgressBar) rootView.findViewById(R.id.loading_spinner);
        submitButton = (Button) rootView.findViewById(R.id.submit_check_in_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCode();
            }
        });


        return rootView;
    }


    private void validateCode(){
        String code = restaurantCode.getText().toString();
        if (code.length() != 7){
            Toast.makeText(getContext(), "Enter Valid Code", Toast.LENGTH_LONG).show();
            return;
        }
        sessionCode = code.substring(0,3);
        String strtableNumber = code.substring(4,7);
        tableNumber = Integer.parseInt(strtableNumber);

        if (sessionCode.length() != 3 || strtableNumber.length() != 3){
            Toast.makeText(getContext(), "Enter Valid Code", Toast.LENGTH_LONG).show();
            return;
        }

        loadingSpinner.setVisibility(View.VISIBLE);
        String endpoint = "/api/sessions/validate";
        JSONObject requestBody = new JSONObject();
        try{
            requestBody.put("session_code", sessionCode);
            requestBody.put("table_number", tableNumber);
        } catch (JSONException e){
            e.printStackTrace();
        }

        Request.post(getContext(), endpoint, requestBody, new Request.Callback() {
            @Override
            public void onRespond(boolean success, int code, String res, boolean isRemoteResponse) {
                if(success){
                    int restaurantID = -1;
                    try {
                        JSONArray jsonResponse = new JSONArray(res);
                        if(jsonResponse != null){
                            restaurantID = jsonResponse.getJSONObject(0).getInt("restaurant_id");
                            restaurantName = jsonResponse.getJSONObject(0).getString("restaurant_name");
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    getRestaurantMenu(restaurantID, restaurantName);
                } else{
                    Toast.makeText(getContext(), "Enter Valid Code", Toast.LENGTH_LONG).show();
                }
                loadingSpinner.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(boolean success, int code, Exception e) {
                loadingSpinner.setVisibility(View.INVISIBLE);
                e.printStackTrace();
                Toast.makeText(getContext(), "Enter Valid Code", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getRestaurantMenu(final int restaurantID, final String restaurantName) {
        loadingSpinner.setVisibility(View.VISIBLE);
        String endpoint = "/api/menus/"+ Integer.toString(restaurantID) + "?customer=true";

        Request.get(getContext(), endpoint, null, new Request.Callback(){

            @Override
            public void onRespond(boolean success, int code, final String res, boolean isRemoteResponse) {
                loadingSpinner.setVisibility(View.GONE);
                getActivity().runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        Log.d("res", res);
                        Intent goToMenuScreen = new Intent(getActivity(), MenuActivity.class);
                        goToMenuScreen.putExtra("restaurantName", restaurantName);
                        goToMenuScreen.putExtra("restaurantID", restaurantID);
                        goToMenuScreen.putExtra("tableNumber", tableNumber);
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
