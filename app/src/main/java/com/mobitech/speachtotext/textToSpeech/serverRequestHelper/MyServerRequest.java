package com.mobitech.speachtotext.textToSpeech.serverRequestHelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.NavBar.NavDrawerActivity;
import com.mobitech.speachtotext.textToSpeech.constants.Constants;
import com.mobitech.speachtotext.textToSpeech.listeners.CustomInfoDialogListener;
import com.mobitech.speachtotext.textToSpeech.listeners.ServerRequestListener;
import com.mobitech.speachtotext.textToSpeech.model.UserModel;
import com.mobitech.speachtotext.textToSpeech.sharedPref.UserSettings;
import com.mobitech.speachtotext.textToSpeech.utils.UtilityFunctions;

import org.json.JSONException;
import org.json.JSONObject;


public class MyServerRequest {

    private String url;
    private JSONObject jsonObject;
    private int requestCode = 0;
    private Context context;
    private RequestQueue queue;
    private CustomInfoDialogListener listener;
    private UserSettings userSettings;
    private ServerRequestListener serverRequestListener;

    public MyServerRequest(Context context, String url, JSONObject jsonObject, int requestCode, ServerRequestListener serverRequestListener) {
        this.url = url;
        this.jsonObject = jsonObject;
        this.requestCode = requestCode;
        this.context = context;
        this.serverRequestListener = serverRequestListener;
        queue = VolleySingleton.getInstance(context).getReq_queue();
        userSettings = UserSettings.getInstance(context);
    }

    public MyServerRequest(Context context, String url, JSONObject jsonObject, ServerRequestListener serverRequestListener) {
        this.url = url;
        this.jsonObject = jsonObject;
        this.context = context;
        this.serverRequestListener = serverRequestListener;
        queue = VolleySingleton.getInstance(context).getReq_queue();
        userSettings = UserSettings.getInstance(context);
    }

    public MyServerRequest(Context context, String url, int requestCode, ServerRequestListener serverRequestListener) {
        this.url = url;
        this.context = context;
        this.requestCode = requestCode;
        this.serverRequestListener = serverRequestListener;
        queue = VolleySingleton.getInstance(context).getReq_queue();
        userSettings = UserSettings.getInstance(context);
    }

    public MyServerRequest(Context context, String url, ServerRequestListener serverRequestListener) {
        this.url = url;
        this.context = context;
        this.serverRequestListener = serverRequestListener;
        queue = VolleySingleton.getInstance(context).getReq_queue();
        userSettings = UserSettings.getInstance(context);
    }


    public void sendRequest() {

        if (UtilityFunctions.isNetworkAvailable(context)) {
            serverRequestListener.onPreResponse();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
//                try {
                    UtilityFunctions.hideProgressDialog(true);
                    serverRequestListener.onPostResponse(response, requestCode);
//                    if (response.getBoolean(AppConstants.HAS_RESPONSE)) {
////                        context.startActivity(new Intent(context,LoginActivity.class));
//
//
//                    } else {
//                        UtilityFunctions.hideProgressDialog(true);
//                        Toast.makeText(context, " "+response.getString(AppConstants.MESSAGE), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    UtilityFunctions.hideProgressDialog(true);
//
//                }

            }, error -> {
                UtilityFunctions.hideProgressDialog(true);
                VolleyLog.e("TransactionRequest", "Error: " + error);
                Log.e("TransactionRequest", "Error: " + error);
                VolleyErrorHelper.ShowError(error, context, listener);
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjectRequest);
        } else {
            UtilityFunctions.hideProgressDialog(true);

            Toast.makeText(context, "No internet connection found please check your wifi or network state", Toast.LENGTH_SHORT).show();
            ((Activity) context).finish();
           
        }

    }

    public void sendGetRequest() {

        if (UtilityFunctions.isNetworkAvailable(context)) {
            serverRequestListener.onPreResponse();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                serverRequestListener.onPostResponse(response, requestCode);

            }, error -> {
                UtilityFunctions.hideProgressDialog(true);
                VolleyLog.e("TransactionRequest", "Error: " + error);
                Log.e("TransactionRequest", "Error: " + error);
                VolleyErrorHelper.ShowError(error, context, listener);
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjectRequest);
        } else {
            
            Toast.makeText(context, "No internet connection found please check your wifi or network state", Toast.LENGTH_SHORT).show();
            ((Activity) context).finish();
        }

    }
    public void sendLoginGetRequest() {

        if (UtilityFunctions.isNetworkAvailable(context)) {
            serverRequestListener.onPreResponse();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                serverRequestListener.onPostResponse(response, requestCode);

            }, error -> {
                UtilityFunctions.hideProgressDialog(true);
                VolleyLog.e("TransactionRequest", "Error: " + error);
                Log.e("TransactionRequest", "Error: " + error);
//                VolleyErrorHelper.ShowError(error, context, listener);
                openDialog(context);
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjectRequest);
        } else {

            Toast.makeText(context, "No internet connection found please check your wifi or network state", Toast.LENGTH_SHORT).show();
            ((Activity) context).finish();
        }

    }
    private void openDialog(Context context){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialog_btn_ok = dialog.findViewById(R.id.dialog_btn_ok);
        dialog_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void sendLoginRequest() {

        if (UtilityFunctions.isNetworkAvailable(context)) {
            serverRequestListener.onPreResponse();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
                serverRequestListener.onPostResponse(response, requestCode);
                userSettings.set(Constants.IS_USER_LOGIN,true);
                UserModel userModel = new UserModel();
//                userModel.setUser_id(response.getJSONArray(AppConstants.RESPONSE).getJSONObject(0).getInt("id"));
//                userModel.setUser_name(response.getJSONArray(AppConstants.RESPONSE).getJSONObject(0).getString("name"));
//                userModel.setUser_phn_no(response.getJSONArray(AppConstants.RESPONSE).getJSONObject(0).getString("number"));
//                userModel.setUser_email(response.getJSONArray(AppConstants.RESPONSE).getJSONObject(0).getString("email"));
//                userModel.setUser_image(response.getJSONArray(AppConstants.RESPONSE).getJSONObject(0).getString("img"));
                userSettings.createUserSession(userModel);
//                try {
//                    if (response.getBoolean(AppConstants.HAS_RESPONSE)) {
//                        serverRequestListener.onPostResponse(response, requestCode);
//                        userSettings.set(AppConstants.IS_USER_LOGIN,true);
//                        UserModel userModel = new UserModel();
//                        userModel.setUser_id(response.getJSONArray(AppConstants.RESPONSE).getJSONObject(0).getInt("id"));
//                        userModel.setUser_name(response.getJSONArray(AppConstants.RESPONSE).getJSONObject(0).getString("name"));
//                        userModel.setUser_phn_no(response.getJSONArray(AppConstants.RESPONSE).getJSONObject(0).getString("number"));
//                        userModel.setUser_email(response.getJSONArray(AppConstants.RESPONSE).getJSONObject(0).getString("email"));
//                        userModel.setUser_image(response.getJSONArray(AppConstants.RESPONSE).getJSONObject(0).getString("img"));
//                        userSettings.createUserSession(userModel);
//                    } else {
//                        UtilityFunctions.hideProgressDialog(true);
//                        Toast.makeText(context, response.getString(AppConstants.MESSAGE), Toast.LENGTH_SHORT).show();
//
//                    }
//                } catch (JSONException e) {
//
//                }

            }, error -> {

                VolleyLog.e("TransactionRequest", "Error: " + error);
                Log.e("TransactionRequest", "Error: " + error);
                VolleyErrorHelper.ShowError(error, context, listener);
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjectRequest);
        } else {
            
            Toast.makeText(context, "No internet connection found please check your wifi or network state", Toast.LENGTH_SHORT).show();
            ((Activity) context).finish();
        }



    }


    public void setCustomDialogListener(CustomInfoDialogListener listener) {
        this.listener = listener;
    }
}
