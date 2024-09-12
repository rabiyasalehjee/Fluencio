package com.mobitech.speachtotext.textToSpeech.MeditationFeature;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.CATEGORIES.Cate_Anxiety_Release;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.CATEGORIES.Cate_Five_Minute_Exercises;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.CATEGORIES.Cate_GuidedMindfulness;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.CATEGORIES.Cate_Nervousness;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.CATEGORIES.Cate_Relaxing_Music;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.CATEGORIES.Cate_Stressed;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.CATEGORIES.Cate_ten_min_meditation;
import com.mobitech.speachtotext.textToSpeech.NavBar.NavDrawerActivity;
import com.mobitech.speachtotext.textToSpeech.activities.RecordActivity;
import com.mobitech.speachtotext.textToSpeech.activities.SpeechListActivity;

public class MeditationDashBoardActivity extends AppCompatActivity {


    private CardView cardView1, cardView2, cardView3,cardView4,cardView5,cardView6,cardView7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_dashboard);


        cardView1 = findViewById(R.id.cardview1);
        cardView2 = findViewById(R.id.cardview2);
        cardView3 = findViewById(R.id.cardview3);
        cardView4 = findViewById(R.id.cardview4);
        cardView5=findViewById(R.id.cardview5);
        cardView6=findViewById(R.id.cardview6);
        cardView7=findViewById(R.id.cardview7);
        //Initialize and assign footer elements
        BottomNavigationView bottomNavigationView = findViewById(R.id.footer_nav);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.footer_meditate);

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
                        startActivity(new Intent(getApplicationContext(), RecordActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.footer_list:
                        startActivity(new Intent(getApplicationContext(), SpeechListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.footer_meditate:
                        return true;
                }
                return false;
            }
        });

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeditationDashBoardActivity.this, Cate_GuidedMindfulness.class);
                startActivity(intent);

                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);

            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeditationDashBoardActivity.this, Cate_Stressed.class);
                startActivity(intent);

                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);

            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeditationDashBoardActivity.this, Cate_Anxiety_Release.class);
                startActivity(intent);

                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);

            }
        });

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeditationDashBoardActivity.this, Cate_Relaxing_Music.class);
                startActivity(intent);
                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);

            }
        });

        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeditationDashBoardActivity.this, Cate_ten_min_meditation.class);
                startActivity(intent);

                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);

            }
        });

        cardView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeditationDashBoardActivity.this, Cate_Five_Minute_Exercises.class);
                startActivity(intent);

                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);
            }
        });
        cardView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MeditationDashBoardActivity.this, Cate_Nervousness.class);
                startActivity(intent);
                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);
            }
        });
    }

}