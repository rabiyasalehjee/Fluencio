package com.mobitech.speachtotext.textToSpeech.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.MeditationDashBoardActivity;
import com.mobitech.speachtotext.textToSpeech.NavBar.NavDrawerActivity;
import com.mobitech.speachtotext.textToSpeech.TimedMode.Timer_Countdown;
import com.mobitech.speachtotext.textToSpeech.sharedPref.UserSettings;

public class InterviewCategory extends AppCompatActivity {

    TextView freestyle_text2;
    CardView generalCard, competencyCard, personalCard;
    RelativeLayout generalLayout, competencyLayout, personalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_category);

        generalCard = findViewById(R.id.generalCard);
        generalLayout = findViewById(R.id.generalLayout);

        competencyCard = findViewById(R.id.competencyCard);
        competencyLayout = findViewById(R.id.competencyLayout);

        personalCard = findViewById(R.id.personalCard);
        personalLayout = findViewById(R.id.personalLayout);
        UserSettings userSettings=UserSettings.getInstance(getApplicationContext());

        generalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSettings.set("level","basic");
                startActivity(new Intent(getApplicationContext(), InterviewRecord.class));

            }
        });
        generalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userSettings.set("level","basic");
                startActivity(new Intent(getApplicationContext(), InterviewRecord.class));

            }
        });
        competencyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userSettings.set("level","advance");
                startActivity(new Intent(getApplicationContext(), InterviewRecord.class));

            }
        });
        competencyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userSettings.set("level","advance");
                startActivity(new Intent(getApplicationContext(), InterviewRecord.class));


            }
        });
        personalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSettings.set("level","advance");
                startActivity(new Intent(getApplicationContext(), InterviewRecord.class));
            }
        });
        personalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSettings.set("level","advance");
                startActivity(new Intent(getApplicationContext(), InterviewRecord.class));
            }
        });


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
    public void interview_recording(View v)
    {
        Intent intent = new Intent(getApplicationContext(), InterviewRecord.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}