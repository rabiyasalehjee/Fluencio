package com.mobitech.speachtotext;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.mobitech.speachtotext.textToSpeech.NavBar.NavDrawerActivity;
import com.mobitech.speachtotext.textToSpeech.constants.URLS;
import com.mobitech.speachtotext.textToSpeech.listeners.ServerRequestListener;
import com.mobitech.speachtotext.textToSpeech.serverRequestHelper.MyServerRequest;
import com.mobitech.speachtotext.textToSpeech.utils.UtilityFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {
    EditText edt_email, edt_password, edt_contact, edt_fullname;
    Button btn;
    //textinput declaration to display error message of validation
    TextInputLayout reg_fullname_textfield;
    TextInputLayout reg_email_textfield;
    TextInputLayout reg_contact_textfield;
    TextInputLayout reg_password_textfield;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Text Input Layout
        reg_fullname_textfield = findViewById(R.id.reg_fullname_textfield);
        reg_email_textfield = findViewById(R.id.reg_email_textfield);
        reg_contact_textfield = findViewById(R.id.reg_contact_textfield);
        reg_password_textfield = findViewById(R.id.reg_password_textfield);

        progressBar = findViewById(R.id.progress);

        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_contact = (EditText) findViewById(R.id.signup_contact);
        edt_fullname = (EditText) findViewById(R.id.signup_fullname);
        btn = (Button) findViewById(R.id.signup_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (signupvalidation()) {

                    SignUpRequest(edt_fullname.getText().toString(),edt_email.getText().toString(), edt_password.getText().toString(), edt_contact.getText().toString());
                }
//                signupvalidation();
//               request(edt_fullname.getText().toString(),edt_email.getText().toString(), edt_password.getText().toString(), edt_contact.getText().toString());
            //    Login();

            }
        });

    }

/*

    public void Login() {
        String number = edt_email.getText().toString().trim();
        String pass = edt_password.getText().toString().trim();
//        loginRepository.Login(new UserModel(number, pass));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Number", number);
            jsonObject.put("Password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(Signup.this, "Register Successful", Toast.LENGTH_SHORT).show();
//        userSettings.set(Constants.IS_USER_LOGIN, true);
        startActivity(new Intent(Signup.this, Login.class));
        finish();
        MyServerRequest myServerRequest = new MyServerRequest(Signup.this, URLS.LOGIN, jsonObject, new ServerRequestListener() {
            @Override
            public void onPreResponse() {
//                UtilityFunctions.showProgressDialog(Login.this,true);
            }

            @Override
            public void onPostResponse(JSONObject jsonResponse, int requestCode) {

//                UtilityFunctions.hideProgressDialog(true);
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
        myServerRequest.sendLoginRequest();
    }
*/

    void request(String edt_fullname, String edt_email, String edt_password, String edt_contact) {
        //Toast.makeText(this, "Confirm", Toast.LENGTH_SHORT).show();

        ClientApi clientApi = Base.getClient().create(ClientApi.class);
        FSignup login = new FSignup(edt_fullname, edt_email, edt_contact, edt_password);
        Call<String> call = clientApi.signup(login);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(ConfirmOTP.this, "Sign up Successfuly", Toast.LENGTH_SHORT).show();
                    signupvalidation();
                    progressBar.setVisibility(View.VISIBLE);
                    finishAffinity();
                     startActivity(new Intent(Signup.this, Login.class));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                 Toast.makeText(Signup.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

/*
                finishAffinity();
                startActivity(new Intent(Signup.this, Login.class));*/
            }
        });


    }




    void SignUpRequest(String edt_fullname, String edt_email, String edt_password, String edt_contact){
        String SignUpUrl =URLS.REGISTERATION+"name="+edt_fullname+"&email="+edt_email+"&password="+edt_password+"&phone="+edt_contact;
        MyServerRequest myServerRequest = new MyServerRequest(Signup.this, SignUpUrl,
                new ServerRequestListener() {
                    @Override
                    public void onPreResponse() {
                        UtilityFunctions.showProgressDialog(Signup.this, true);
                    }

                    @Override
                    public void onPostResponse(JSONObject jsonResponse, int requestCode) {
                        UtilityFunctions.hideProgressDialog(true);
                        try {

                            if (jsonResponse.getBoolean("respone")) {
                                Toast.makeText(Signup.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                finishAffinity();
                                startActivity(new Intent(Signup.this, Login.class));
                            }else{
                                Toast.makeText(Signup.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            UtilityFunctions.hideProgressDialog(true);
                            e.printStackTrace();
                        }



                    }
                });
        myServerRequest.sendGetRequest();
    }



    // to go on login Activity from signup activity
    public void perform_action (View v){

        TextView register_activity_login_btn = (TextView) findViewById(R.id.registeractivity_login_btn);
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    } // end of perform_action method

    // validation of edit text fields
    private boolean signupvalidation() {
        //full name field can't be empty
        boolean isValid = true;
        if (edt_fullname.getText().toString().isEmpty()) {
            reg_fullname_textfield.setError("Please enter full name");
            isValid = true;
        } else {
            reg_fullname_textfield.setErrorEnabled(false);
        }
        //email validation
        String reg_email_input = edt_email.getText().toString().trim();
        if (reg_email_input.isEmpty()) {
            reg_email_textfield.setError("Please enter email address");
            isValid = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(reg_email_input).matches()) {
            reg_email_textfield.setError("Please enter a valid email address");
            return false;
        } else {
            reg_email_textfield.setErrorEnabled(false);
        }
        //password field cannot be empty
        String reg_contact_input = edt_contact.getText().toString().trim();
        if (reg_contact_input.isEmpty()) {
            reg_contact_textfield.setError("Please enter the contact number");
            isValid = true;
        } else {
            reg_contact_textfield.setErrorEnabled(false);
        }

        //contact field cannot be empty
        String reg_password_input = edt_password.getText().toString().trim();
        if (reg_password_input.isEmpty()) {
            reg_password_textfield.setError("Please enter the password");
            isValid = true;
        } else {
            reg_password_textfield.setErrorEnabled(false);
        }


        return isValid;
    } // end of validation method on button click

}