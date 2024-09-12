package com.mobitech.speachtotext;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputLayout;
import com.mobitech.speachtotext.textToSpeech.NavBar.NavDrawerActivity;
import com.mobitech.speachtotext.textToSpeech.Rlogin;
import com.mobitech.speachtotext.textToSpeech.activities.TextToSpeechActivity;
import com.mobitech.speachtotext.textToSpeech.adapter.AudioListAdapter;
import com.mobitech.speachtotext.textToSpeech.constants.Constants;
import com.mobitech.speachtotext.textToSpeech.constants.URLS;
import com.mobitech.speachtotext.textToSpeech.listeners.ServerRequestListener;
import com.mobitech.speachtotext.textToSpeech.model.RecordedFileModel;
import com.mobitech.speachtotext.textToSpeech.serverRequestHelper.MyServerRequest;
import com.mobitech.speachtotext.textToSpeech.sharedPref.UserSettings;
import com.mobitech.speachtotext.textToSpeech.utils.UtilityFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    EditText edt_email, edt_password;
    Button btn_login;
    TextInputLayout login_email_textfield;
    TextInputLayout login_password_textfield;

    TextView forget;
    ProgressBar progressBar;

    Dialog dialog;
    UserSettings userSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Text Input Layout Typecasting
        login_email_textfield = findViewById(R.id.login_email_textfield);
        login_password_textfield = findViewById(R.id.login_password_textfield);

        dialog = new Dialog(this);
        progressBar = findViewById(R.id.progress);

        userSettings=new UserSettings(Login.this);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.c);
        forget = (TextView) findViewById(R.id.forget);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//startActivity(new Intent(Login.this,ForgetPassword.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (loginvalidation()) {
                    Login();
                }
                /*loginvalidation();
                //  Toast.makeText(Login.this, "Clcicked", Toast.LENGTH_SHORT).show();
            //    Login();


                setvicee();

                finishAffinity();
                Intent i = new Intent(getBaseContext(), NavDrawerActivity.class);
                startActivity(i);*/
            }
        });
    }
    // to go on Registration Activity from login page
    public void perform_action(View v) {
        TextView login_activity_signup_btn = (TextView) findViewById(R.id.loginactivity_signup_btn);
        Intent intent = new Intent(getApplicationContext(), Signup.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    } //end of perform_action method

    public void Signup(View view) {
        startActivity(new Intent(this, Signup.class));
    }

    /*public void Login() {
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
        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
        userSettings.set(Constants.IS_USER_LOGIN, true);
        startActivity(new Intent(Login.this, TextToSpeechActivity.class));
        finish();
        MyServerRequest myServerRequest = new MyServerRequest(Login.this, URLS.LOGIN, jsonObject, new ServerRequestListener() {
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
    }*/

    void setvicee() {
        ClientApi clientApi = Base.getClient().create(ClientApi.class);
        Flogin login = new Flogin(edt_email.getText().toString(), edt_password.getText().toString());
        Call<Rlogin> call = clientApi.signin(login);
        call.enqueue(new Callback<Rlogin>() {
            @Override
            public void onResponse(Call<Rlogin> call, Response<Rlogin> response) {
                if (response.isSuccessful()) {
                    //  Toast.makeText(Login.this, ""+response.code(), Toast.LENGTH_SHORT).show();
                    Rlogin rlogin = response.body();

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("id", rlogin.getId()); // Storing string
                    editor.putString("name", rlogin.getName()); // Storing string
                    editor.putString("phone", rlogin.getPhone()); // Storing string
                    editor.commit();
                    progressBar.setVisibility(View.VISIBLE);
                    finishAffinity();
                    Intent i = new Intent(getBaseContext(), NavDrawerActivity.class);
                    startActivity(i);
                }

            }

            @Override
            public void onFailure(Call<Rlogin> call, Throwable t) {
                openDialog();
              //  Toast.makeText(Login.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
            }
        });


    }


    void Login(){
        String LoginUrl =URLS.LOGIN+"email="+edt_email.getText().toString()+"&password="+edt_password.getText().toString();
        MyServerRequest myServerRequest = new MyServerRequest(Login.this, LoginUrl,
                new ServerRequestListener() {
                    @Override
                    public void onPreResponse() {
                        UtilityFunctions.showProgressDialog(Login.this, true);
                    }

                    @Override
                    public void onPostResponse(JSONObject response, int requestCode) {
                        UtilityFunctions.hideProgressDialog(true);
                        try {
                            JSONObject responseObj= response.getJSONObject("respone");
                            SharedPreferences pref = getSharedPreferences("MyPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            if (responseObj.has("id")) {
                                editor.putString("id",responseObj.getString("id")); // Storing string
                                editor.putString("name", responseObj.getString("name")); // Storing string
                                editor.putString("phone", responseObj.getString("phone"));
                                editor.putBoolean("IsLoggedIn",true);
                                // Storing string
                                editor.apply();
                                Toast.makeText(Login.this, ""+responseObj.getString("responseMsg"), Toast.LENGTH_SHORT).show();
                                 finishAffinity();
                                Intent i = new Intent(Login.this, NavDrawerActivity.class);
                                 startActivity(i);
                            }else{
                                Toast.makeText(Login.this, ""+responseObj.getString("responseMsg"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            UtilityFunctions.hideProgressDialog(true);
                            e.printStackTrace();
                        }


                    }
                });
        myServerRequest.sendLoginGetRequest();
    }


    // edit text field validation
    private boolean loginvalidation() {
        boolean isValid = true;
        //email validation
        String login_email_input = edt_email.getText().toString().trim();
        if (login_email_input.isEmpty())
        {
            login_email_textfield.setError("Please enter email address");
            isValid= true;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(login_email_input).matches())
        {
            login_email_textfield.setError("Please enter a valid email address");
            return false;
        }
        else
        {
            login_email_textfield.setErrorEnabled(false);
        }
        //password field cannot be empty
        if(edt_password.getText().toString().isEmpty())
        {
            login_password_textfield.setError("Please enter the password");
            isValid= true;
        }
        else
        {
            login_password_textfield.setErrorEnabled(false);
        }
        return isValid;
    } // end of validation method
    private void openDialog(){
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

}