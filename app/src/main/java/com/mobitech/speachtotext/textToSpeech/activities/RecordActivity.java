package com.mobitech.speachtotext.textToSpeech.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.MeditationDashBoardActivity;
import com.mobitech.speachtotext.textToSpeech.NavBar.NavDrawerActivity;
import com.mobitech.speachtotext.textToSpeech.TimedMode.Timer_Countdown;
import com.mobitech.speachtotext.textToSpeech.sharedPref.UserSettings;

public class RecordActivity extends AppCompatActivity {

    TextView freestyle_text2;

    UserSettings userSettings ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        freestyle_text2 = findViewById(R.id.freestyle_text2);

        userSettings=UserSettings.getInstance(RecordActivity.this);
        //Initialize and assign footer elements
        BottomNavigationView bottomNavigationView = findViewById(R.id.footer_nav);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.footer_record);

        //Perform Item selected Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.footer_home:
                        startActivity(new Intent(getApplicationContext(), NavDrawerActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.footer_record:
                        return true;
                    case R.id.footer_meditate:
                        startActivity(new Intent(getApplicationContext(), MeditationDashBoardActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.footer_list:
                        startActivity(new Intent(getApplicationContext(), SpeechListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.footer_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }


    public void speech_recording(View v)
    {


        userSettings.set("IsFreeStyle",true);
        Intent intent = new Intent(getApplicationContext(), TextToSpeechActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    public void timed_speech_recording(View v)
    {

        userSettings.set("IsFreeStyle",false);
        Intent intent = new Intent(getApplicationContext(), Timer_Countdown.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}