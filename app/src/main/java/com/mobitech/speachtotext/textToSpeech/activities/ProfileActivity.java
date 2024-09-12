package com.mobitech.speachtotext.textToSpeech.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.MeditationDashBoardActivity;
import com.mobitech.speachtotext.textToSpeech.NavBar.NavDrawerActivity;
import com.mobitech.speachtotext.textToSpeech.fragments.AudioList_Fragment;
import com.mobitech.speachtotext.textToSpeech.fragments.ProfileFragment;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fcv_profile, new ProfileFragment());
        tx.commit();

        //Initialize and assign footer elements
        BottomNavigationView bottomNavigationView = findViewById(R.id.footer_nav);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.footer_profile);
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
                    case R.id.footer_meditate:
                        startActivity(new Intent(getApplicationContext(), MeditationDashBoardActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.footer_list:
                        startActivity(new Intent(getApplicationContext(), SpeechListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.footer_profile:
                        return true;
                }
                return false;
            }
        });
    }
}