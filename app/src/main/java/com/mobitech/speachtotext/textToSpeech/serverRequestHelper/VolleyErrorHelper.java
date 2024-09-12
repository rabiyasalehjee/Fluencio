package com.mobitech.speachtotext.textToSpeech.serverRequestHelper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.listeners.CustomInfoDialogListener;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class VolleyErrorHelper {
    /**
     * Returns appropriate message which is to be displayed to the user
     * against the specified error object.
     *
     * @param error
     * @param context
     * @return
     */
    public static void ShowError(Object error, Context context, CustomInfoDialogListener listener) {
        if (error instanceof TimeoutError) {
            Toast.makeText(context, R.string.server_down, Toast.LENGTH_SHORT).show();
        } else if (isServerProblem(error)) {
            handleServerError(error, context, listener);
        } else if (isNetworkProblem(error)) {
            Toast.makeText(context, R.string.no_internet_connection_found, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.generic_error, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Determines whether the error is related to network
     *
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    /**
     * Determines whether the error is related to server
     *
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    /**
     * Handles the server error, tries to determine whether to show a stock message or to
     * show a message retrieved from the server.
     *
     * @param err
     * @param context
     * @return
     */
    private static void handleServerError(Object err, Context context, CustomInfoDialogListener listener) {
        VolleyError error = (VolleyError) err;
        NetworkResponse response = error.networkResponse;
        if (response != null) {
            if (response.statusCode >= HttpURLConnection.HTTP_BAD_REQUEST && response.statusCode <= HttpURLConnection.HTTP_INTERNAL_ERROR) {
                try {
                    // server might return error like this { "error": "Some error occured" }
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    Log.d("VolleyError", "parseVolleyError: " + responseBody);
                    JSONObject data = new JSONObject(responseBody);
                    if (data.has("message") && !data.isNull("message")) {
                            Toast.makeText(context,data.getString("message"),Toast.LENGTH_SHORT).show();

                    } else if (data.has("error") && !data.isNull("error")) {
                        Toast.makeText(context,data.getString("message"),Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.generic_error, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, R.string.generic_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
