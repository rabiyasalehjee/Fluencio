package com.mobitech.speachtotext.textToSpeech.fragments;

import static com.mobitech.speachtotext.textToSpeech.utils.UtilityFunctions.openCustomDialog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.constants.URLS;
import com.mobitech.speachtotext.textToSpeech.listeners.ServerRequestListener;
import com.mobitech.speachtotext.textToSpeech.serverRequestHelper.MyServerRequest;
import com.mobitech.speachtotext.textToSpeech.utils.UtilityFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ProfileFragment extends Fragment {

    EditText full_name, email, contact, password, conf_password;

    TextInputLayout reg_fullname_textfield, reg_contact_textfield, reg_password_textfield, reg_conf_password_textfield;


    Button save;
    SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = view.findViewById(R.id.profile_email);
        full_name = view.findViewById(R.id.profile_fullname);
        contact = view.findViewById(R.id.profile_contact);
        password = view.findViewById(R.id.profile_password);
        conf_password = view.findViewById(R.id.profile_conf_password);

        reg_fullname_textfield = view.findViewById(R.id.reg_fullname_textfield);
        reg_contact_textfield = view.findViewById(R.id.reg_contact_textfield);
        reg_password_textfield = view.findViewById(R.id.reg_password_textfield);
        reg_conf_password_textfield = view.findViewById(R.id.reg_conf_password_textfield);

        save = view.findViewById(R.id.profile_btn);

        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        GetUserData();
        save.setOnClickListener(view1 -> openCustomDialog(view.getContext(),
                "Confirmation", "Are you sure you want to update your Information!!", true,
                () -> {
            if (!validateFullName() | !validateContact() | !validatePassword()| !validateConfPassword()| !validatePasswords()){
//                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//                String currentDateandTime = sdf.format(new Date());
//                Log.d("timecheck", "onViewCreated: "+currentDateandTime);
                return;

            }

                SaveUser(pref.getString("id", "1"),full_name.getText().toString().trim()
                        ,password.getText().toString().trim(),contact.getText().toString().trim());

                }));

    }

    private void GetUserData() {


        String allSpeechURL = URLS.GET_USER_DATA + pref.getString("id", "1");

        Log.i("cheking URL", "GetFIleListApi: " + URLS.GET_FILE_LIST + pref.getString("id", "1"));

        MyServerRequest myServerRequest = new MyServerRequest(requireActivity(), allSpeechURL,
                new ServerRequestListener() {
                    @Override
                    public void onPreResponse() {
                        UtilityFunctions.showProgressDialog(requireActivity(), true);
                    }

                    @Override
                    public void onPostResponse(JSONObject jsonResponse, int requestCode) {

                        UtilityFunctions.hideProgressDialog(true);

                        try {
                            JSONObject response = jsonResponse.getJSONObject("respone");
                            full_name.setText(response.has("name") ? response.getString("name") : "");
                            email.setText(response.has("email") ? response.getString("email") : "");
                            contact.setText(response.has("phone") ? response.getString("phone") : "");
                            password.setText(response.has("password") ? response.getString("password") : "");
                            conf_password.setText(response.has("password") ? response.getString("password") : "");
                        } catch (JSONException e) {
                            Log.d("TAG", "onPostResponse: " + e.getMessage());
                            e.printStackTrace();
                        }

//                UtilityFunctions.hideProgressDialog(true);
//                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Login.this, TextToSpeechActivity.class));
//                finish();
//                try {
//                    if (jsonResponse.getBoolean(AppConstants.HAS_RESPONSE)){
//                        UtilityFunctions.hideProgressDialog(true);
//                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(Login.this,DashboardActivity.class));
//                        finish();
//                    }else{
//                        UtilityFunctions.hideProgressDialog(true);
//                        Toast.makeText(Login.this, " "+jsonResponse.getString(AppConstants.MESSAGE), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    Log.i(TAG, "onPostResponse: "+jsonResponse);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                    }
                });
        myServerRequest.sendGetRequest();
    }
    private boolean validateFullName(){
        String val = reg_fullname_textfield.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            reg_fullname_textfield.setError("Field can not be empty");
            return false;
        }else{
            reg_fullname_textfield.setError(null);
            reg_fullname_textfield.setErrorEnabled(false);
            return true;

        }
    }
    private boolean validateContact(){
        String val = reg_contact_textfield.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            reg_contact_textfield.setError("Field can not be empty");
            return false;
        }else{
            reg_contact_textfield.setError(null);
            reg_contact_textfield.setErrorEnabled(false);
            return true;

        }
    }
    private boolean validatePassword(){
        String val = reg_password_textfield.getEditText().getText().toString().trim();
       if (val.isEmpty()) {
            reg_password_textfield.setError("Field can not be empty");
            return false;
        } else {
            reg_password_textfield.setError(null);
            reg_password_textfield.setErrorEnabled(false);
            return true;

        }
    }

    private boolean validateConfPassword(){
       String val2 = reg_conf_password_textfield.getEditText().getText().toString().trim();
        if (val2.isEmpty()) {
            reg_conf_password_textfield.setError("Field can not be empty");
            return false;
        } else {
            reg_conf_password_textfield.setError(null);
            reg_conf_password_textfield.setErrorEnabled(false);
            return true;

        }
    }

    private boolean validatePasswords(){
        String val = reg_password_textfield.getEditText().getText().toString().trim();
        String val2 = reg_conf_password_textfield.getEditText().getText().toString().trim();
        if (!val.equalsIgnoreCase(val2)) {

            reg_password_textfield.setError("Passwords Doesn't match");
            reg_conf_password_textfield.setError("Passwords Doesn't match");
            return false;
        } else {
            reg_password_textfield.setError(null);
            reg_password_textfield.setErrorEnabled(false);
            reg_conf_password_textfield.setError(null);
            reg_conf_password_textfield.setErrorEnabled(false);
            return true;

        }
    }

    private void SaveUser(String id,String name,String pass,String contact) {


        String allSpeechURL = URLS.UPDATE_USER +"userID="+ id+"&name="+name+"&password="+pass+"&contact="+contact;

        Log.i("cheking URL", "GetFIleListApi: " + URLS.UPDATE_USER +"userID="+ id+"&name="+name+"&password="+pass+"&contact="+contact);

        MyServerRequest myServerRequest = new MyServerRequest(getActivity(), allSpeechURL,
                new ServerRequestListener() {
                    @Override
                    public void onPreResponse() {
                        UtilityFunctions.showProgressDialog(getActivity(), true);
                    }

                    @Override
                    public void onPostResponse(JSONObject jsonResponse, int requestCode) {

                        UtilityFunctions.hideProgressDialog(true);

                        try {
                            boolean response = jsonResponse.getBoolean("respone");

                            if (response){
                                Toast.makeText(getActivity(), "Profile has been updated", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            Log.d("TAG", "onPostResponse: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
        myServerRequest.sendGetRequest();
    }



}