package com.uw.fydp.flexeat.flexeat.api;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by chaitanyakhanna on 2018-02-09.
 */

public class Request {

    public interface Callback {
        /**
         * Notifies the completion of a successful request
         * @param success
         *          always true
         * @param code
         *          the HTTP response code
         * @param res
         *          the HTTP response
         */
        public abstract void onRespond(boolean success, int code, String res, boolean isRemoteResponse);

        /**
         * Notifies the completion of a failed request
         * @param success
         *          always false
         * @param code
         *          the HTTP response code
         * @param e
         *          the cause of the error
         */
        public abstract void onError(boolean success, int code, Exception e);
    }

    /**
     * Executes a GET request
     * @param context
     *          the current Context, necessary for getting String resources in error messages
     * @param url
     *          a relative URL for the location of the resource
     * @param callback
     *          the callback to call on completion of the response
     */
    public static void get(Context context, String url, JSONObject header, Callback callback) {
        executeAsync(url, "GET", null, context, header, callback);
    }

    /**
     * Executes a POST request
     * @param context
     *          the current Context, necessary for getting String resources in error messages
     * @param url
     *          a relative URL for the location of the resource
     * @param parameter
     *          the body to POST in JSON format
     * @param callback
     *          the callback to call on completion of the response
     */
    public static void post(Context context, String url, JSONObject parameter, Callback callback) {
        executeAsync(url, "POST", parameter, context, null, callback);
    }

    /**
     * Executes a PUT request
     * @see Request#post
     */
    public static void put(Context context, String url, JSONObject parameter, Callback callback) {
        executeAsync(url, "PUT", parameter, context, null, callback);
    }

    /**
     * Executes a DELETE request
     * @param context
     *          the current Context, necessary for getting String resources in error messages
     * @param url
     *          a relative URL for the location of the resource
     * @param callback
     *          the callback to call on completion of the response
     */
    public static void delete(Context context, String url, Callback callback) {
        executeAsync(url, "DELETE", null, context, null, callback);
    }

    /**
     * Executes the request. Method to be deprecate (instantiate RequestAsync object from calling class on UI thread).
     */
    private static void executeAsync(final String url, final String method, final JSONObject parameter, final Context context, final JSONObject headerData, final Callback callback) {

        RequestBase request = new RequestBase(url, method, parameter, context, headerData, new RequestBase.Callback() {
            @Override
            public void onSuccess(int code, String res, boolean isRemoteResponse) {
                if (callback != null) {
                    callback.onRespond(true, code, res, isRemoteResponse);
                }
            }
            @Override
            public void onError(int code, String res) {
                if (callback != null) {
                    callback.onError(false, code, new Exception(res));
                }
            }
        });
        request.execute();

    }

}
