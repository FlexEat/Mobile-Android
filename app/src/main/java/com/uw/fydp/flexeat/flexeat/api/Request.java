package com.uw.fydp.flexeat.flexeat.api;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by chaitanyakhanna on 2017-07-21.
 */

public class Request extends AsyncTask<String, Void, String>{

    JSONArray JsonArray = new JSONArray();

    public Request(JSONArray JsonArray){
        this.JsonArray = JsonArray;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL postURL = new URL("http://192.168.2.211:3000/api/order/");
            HttpURLConnection connection = (HttpURLConnection) postURL.openConnection();
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(this.JsonArray));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                Log.d("Response Code", "SUCCESS");
            }else{
                Log.d("Response Code", "FAIL");
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "abc";
    }

    private String getPostDataString(JSONArray selectedItemsJSONArray) {
        StringBuilder JSONArrayAsString = new StringBuilder();

        try {
            boolean first = true;
            for(int i = 0 ; i < selectedItemsJSONArray.length() ; i++){
                JSONObject obj = (JSONObject) selectedItemsJSONArray.get(i);
                Iterator<String> itr = obj.keys();

                while(itr.hasNext()){

                    String key= itr.next();
                    if (key.equals("name")){
                        Object value = obj.get(key);

                        if (first)
                            first = false;
                        else
                            JSONArrayAsString.append("&");

                        JSONArrayAsString.append(URLEncoder.encode(key, "UTF-8"));
                        JSONArrayAsString.append("=");
                        JSONArrayAsString.append(URLEncoder.encode(value.toString(), "UTF-8"));
                    }
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        return JSONArrayAsString.toString();
    }
}
