package com.mobitech.speachtotext.textToSpeech.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.listeners.CustomInfoDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by WeMartDevelopers .
 */
public class UtilityFunctions {

    private static LoadingDialog spinnerProgressDialog;

    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                }
            } else {

                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        // connected to the internet
                        if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            Log.i("update_status", "Network is available : true and network type WIFI");
                            return true;
                        } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            Log.i("update_status", "Network is available : true and network type DATA");
                            return true;
                        } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                            Log.i("update_status", "Network is available : true and network type ETHERNET");
                            return true;
                        }
                    }
                } catch (Exception e) {
                    Log.i("update_statut", "" + e.getMessage());
                }
            }
        }
        Log.i("update_statut", "Network is available : FALSE ");
        return false;
    }

    public static boolean locationValidation(Location location) {
        boolean isValidationTrue = true;

        if (location == null || (location.getLatitude() == 0 || location.getLongitude() == 0)) isValidationTrue = false;

        return isValidationTrue;
    }
    public static String JSONValidator(JSONObject jsonObject, String key) {
        String value = "-";
        try {
            if (jsonObject.has(key) && !jsonObject.getString(key).equals("") && !jsonObject.isNull(key)) {
                value = jsonObject.getString(key);
            }
        } catch (JSONException e) {
            Log.e("JSONException : ", e.getMessage());
        }
        return value;
    }


    /**
     * Show spinner on context
     *
     * @param context context
     */
    public static void showProgressDialog(Context context, Boolean showHud) {
        if (showHud) {
            if (spinnerProgressDialog == null) {
                spinnerProgressDialog = new LoadingDialog(context);
                spinnerProgressDialog.setCanceledOnTouchOutside(false);
                spinnerProgressDialog.show();
            } else {
                spinnerProgressDialog.show();
            }
        }
    }


    public static void openCustomDialog(Context context, String heading, String description, boolean wantYesNoBtns
            ,  CustomInfoDialogListener listener){

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialog_btn_ok = dialog.findViewById(R.id.dialog_btn_ok);
        Button dialog_btn_yes = dialog.findViewById(R.id.dialog_btn_yes);
        Button dialog_btn_no = dialog.findViewById(R.id.dialog_btn_no);
        LinearLayoutCompat dialog_ll = dialog.findViewById(R.id.dialog_ll_yes_no);
        TextView tv_hd = dialog.findViewById(R.id.textView9);
        TextView sub_title = dialog.findViewById(R.id.textView7);
        if (wantYesNoBtns) {
            dialog_ll.setVisibility(View.VISIBLE);
            dialog_btn_ok.setVisibility(View.GONE);
        }else{
            dialog_ll.setVisibility(View.GONE);
            dialog_btn_ok.setVisibility(View.VISIBLE);
        }

        tv_hd.setText(heading);
        sub_title.setText(description);
        dialog_btn_no.setOnClickListener(v -> dialog.dismiss());
        dialog_btn_ok.setOnClickListener(v -> {
            dialog.dismiss();
            listener.onOkClick();
        });
        dialog_btn_yes.setOnClickListener(v -> {
            dialog.dismiss();
            listener.onOkClick();
        });
        dialog.show();
    }
    /**
     * hide spinner on activity
     */
    public static void hideProgressDialog(Boolean showHud) {
        if (showHud) {
            if (spinnerProgressDialog != null) {
                try {
                    spinnerProgressDialog.dismiss();
                } catch (IllegalArgumentException e) {
                    Log.i("IllegalArgumentExcep...", "Spinner not on window");
                }
                spinnerProgressDialog = null;
            }
        }
    }

    public static String getFormattedTimeStamp(String timeStampApp) {
        Date date;
        String formattedDate = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(timeStampApp);
            // format the java.util.Date object to the desired format
            formattedDate = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault()).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String getFormattedTimeStampForCamp(String timeStampApp) {
        Date date;
        String formattedDate = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(timeStampApp);
            // format the java.util.Date object to the desired format
            formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String changeStringDateFormat(String inputDate, String inputFormat, String outputFormat) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.US);
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.US);

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return outputDate;
    }

    public static void datePicker(Context context, EditText editText, long maxDate, long minDate) {
        final Calendar mCurrentDate = Calendar.getInstance();
        int mYear = mCurrentDate.get(Calendar.YEAR);
        int mMonth = mCurrentDate.get(Calendar.MONTH);
        int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(context, (datepicker, selectedyear, selectedmonth, selectedday) -> {

            mCurrentDate.set(Calendar.YEAR, selectedyear);
            mCurrentDate.set(Calendar.MONTH, selectedmonth);
            mCurrentDate.set(Calendar.DAY_OF_MONTH, selectedday);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

            editText.setText(sdf.format(mCurrentDate.getTime()));
        }, mYear, mMonth, mDay);

        if (minDate > 0) {
            mDatePicker.getDatePicker().setMinDate(minDate);
        }
        if (maxDate > 0){
            mDatePicker.getDatePicker().setMaxDate(maxDate);
        }
        mDatePicker.show();
    }

}
