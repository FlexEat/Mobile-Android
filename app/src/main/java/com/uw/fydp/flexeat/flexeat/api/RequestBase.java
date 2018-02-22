package com.uw.fydp.flexeat.flexeat.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.common.io.ByteStreams;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by chaitanyakhanna on 2017-07-21.
 */

//public class RequestBase extends AsyncTask<String, Void, String>{
//
//    JSONArray JsonArray = new JSONArray();
//
//    public RequestBase(JSONArray JsonArray){
//        this.JsonArray = JsonArray;
//    }
//
//    @Override
//    protected String doInBackground(String... params) {
//        try {
//            URL postURL = new URL("http://192.168.2.211:3000/api/order/");
//            HttpURLConnection connection = (HttpURLConnection) postURL.openConnection();
//            connection.setConnectTimeout(15000);
//            connection.setRequestMethod("POST");
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//
//            OutputStream os = connection.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//            writer.write(getPostDataString(this.JsonArray));
//            writer.flush();
//            writer.close();
//            os.close();
//            int responseCode = connection.getResponseCode();
//            if(responseCode == HttpURLConnection.HTTP_OK){
//                Log.d("Response Code", "SUCCESS");
//            }else{
//                Log.d("Response Code", "FAIL");
//            }
//        }catch (MalformedURLException e){
//            e.printStackTrace();
//        }catch (IOException e){
//            e.printStackTrace();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return "abc";
//    }
//
//    private String getPostDataString(JSONArray selectedItemsJSONArray) {
//        StringBuilder JSONArrayAsString = new StringBuilder();
//
//        try {
//            boolean first = true;
//            for(int i = 0 ; i < selectedItemsJSONArray.length() ; i++){
//                JSONObject obj = (JSONObject) selectedItemsJSONArray.get(i);
//                Iterator<String> itr = obj.keys();
//
//                while(itr.hasNext()){
//
//                    String key= itr.next();
//                    if (key.equals("name")){
//                        Object value = obj.get(key);
//
//                        if (first)
//                            first = false;
//                        else
//                            JSONArrayAsString.append("&");
//
//                        JSONArrayAsString.append(URLEncoder.encode(key, "UTF-8"));
//                        JSONArrayAsString.append("=");
//                        JSONArrayAsString.append(URLEncoder.encode(value.toString(), "UTF-8"));
//                    }
//                }
//            }
//
//        }catch (JSONException e){
//            e.printStackTrace();
//        }catch (UnsupportedEncodingException e){
//            e.printStackTrace();
//        }
//
//        return JSONArrayAsString.toString();
//    }
//}

public class RequestBase extends AsyncTask<String, Void, Boolean> {

    private String mURL;
    private String mMethod;
    private JSONObject mParams;
    private JSONObject mHeaderInfo;
    private Context mContext;
    private Callback mCallback;
    private int mResponseCode;
    private String mResponseBody;
    private boolean mSuccess;

    private static final String BASE_URL = "https://flex-eat.herokuapp.com";

    public interface Callback {
        /**
         * Notifies the completion of a successful request
         * @param code
         *          the HTTP response code
         * @param res
         *          the HTTP response
         */
        public abstract void onSuccess(int code, String res, boolean isRemoteResponse);

        /**
         * Notifies the completion of a failed request
         * @param code
         *          the HTTP response code
         * @param res
         *          the HTTP response
         */
        public abstract void onError(int code, String res);
    }

    public RequestBase(String url, String method, JSONObject params, Context context, JSONObject extraHeaderInfo, Callback callback){
        mURL = BASE_URL + url;
        mMethod = method;
        mParams = params;
        mHeaderInfo = extraHeaderInfo;
        mContext = context;
        mCallback = callback;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os;

        try {

            URL urlEndpoint = new URL(mURL);
            connection = (HttpURLConnection) urlEndpoint.openConnection();

            // connection setup
            connection.setRequestMethod(mMethod);
            connection.setConnectTimeout(15000);
            connection.setDoInput(true);
            connection.setDoOutput(mMethod.equals("POST") || mMethod.equals("PUT"));
            connection.setRequestProperty("Content-type", "application/json");
            if (mHeaderInfo != null){
                for(int i =0; i < mHeaderInfo.length(); i++){
                    Log.d("headerKey", mHeaderInfo.names().getString(i));
                    connection.setRequestProperty(mHeaderInfo.names().getString(i), mHeaderInfo.getString(mHeaderInfo.names().getString(i)));
                }
            }

            connection.connect();

            // Write request
            if (connection.getDoOutput() && mMethod != null) {
                os = connection.getOutputStream();
                os.write(mParams.toString().getBytes("UTF-8"));
                os.close();
            }

            mResponseCode = connection.getResponseCode();
            mResponseBody = "";

            if (mResponseCode == 200) { // status 200
                mSuccess = true;
                // Convert ByteStream to string

                is = connection.getInputStream();
                mResponseBody = is != null ? new String(ByteStreams.toByteArray(is), "UTF-8") : "";

            } else {
                mSuccess= false;
                mResponseBody = "Something went wrong";
            }

        }catch (MalformedURLException e){
            mSuccess = false;
            e.printStackTrace();
        }catch (IOException e){
            mSuccess = false;
            e.printStackTrace();
        }catch (Exception e){
            mSuccess = false;
            e.printStackTrace();
        }finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return mSuccess;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(Boolean success) {

        // Callback

        if (success) {
            mCallback.onSuccess(mResponseCode, mResponseBody, true);
        }
        else {
            mCallback.onError(mResponseCode, mResponseBody);
        }

    }

}
