package com.mobitech.speachtotext.textToSpeech.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mobitech.speachtotext.Login;
import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.Signup;


public class letscreateprofile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letscreateprofile);
        addListenerOnButton();

    }

    private void addListenerOnButton() {

        final Context context = this;
        Button signup_btn = findViewById(R.id.letscreateprofile_signup);
        Button login_btn = findViewById(R.id.letscreateprofile_login);
        signup_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, Signup.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }

        });
        login_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }

        });

    }

}