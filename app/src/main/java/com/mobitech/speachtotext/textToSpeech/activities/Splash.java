package com.mobitech.speachtotext.textToSpeech.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import com.mobitech.speachtotext.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.mobitech.speachtotext.textToSpeech.NavBar.NavDrawerActivity;
import com.mobitech.speachtotext.textToSpeech.Walkthroughs.OnBoarding;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIMER = 4000;
    //variable
    ImageView backgroundImage;
    Animation sideAnim, bottomAnim;
    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

     //   backgroundImage = findViewById(R.id.splash);
        //Animation
        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);
        //set animation on elements


/*this whole comment will be remove when the application is ready .. becuase this is the code where we will see if the user is visiting first time ..
        //Calling New Activity after SPLASH_SCREEN seconds 1s = 1000
        new Handler().postDelayed(new Runnable() {
            @Override
             public void run() {
               //  if the user is new / visiting first time show them onboarding screen otherwise take is to the login screen
               onBoardingScreen = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime",true);

                 if(isFirstTime) {
                     SharedPreferences.Editor editor = onBoardingScreen.edit();
                     editor.putBoolean("firstTime", false);
                     editor.commit(); // commit all the changes

                      Intent intent = new Intent(getApplicationContext(), OnBoarding.class);
                      startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                         finish();
                         }
                 // if its not the first visit of the user
                else {
                     Intent intent = new Intent(getApplicationContext(), letscreateprofile.class);
                     startActivity(intent);
                     overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                     finish();
                 }
                          }
                          },SPLASH_TIMER);*/
        SharedPreferences pref = getSharedPreferences("MyPref", 0);
        boolean isLoggedIn=pref.getBoolean("IsLoggedIn",false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (!isLoggedIn) {
                    // intent = new Intent(getApplicationContext(), OnBoarding.class); // for jtech maybe

                    // comment down rest the code of IF block, if you want Onboarding screen everytime you open an APP
                    onBoardingScreen = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
                    boolean isFirstTime = onBoardingScreen.getBoolean("firstTime",true);

                    if(isFirstTime) {
                        SharedPreferences.Editor editor = onBoardingScreen.edit();
                        editor.putBoolean("firstTime", false);
                        editor.commit(); // commit all the changes

                        intent = new Intent(getApplicationContext(), OnBoarding.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }
                    else {
                        intent = new Intent(getApplicationContext(), letscreateprofile.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                    }

                } // comment till here

                else{
                    intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        }, SPLASH_TIMER);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite wave = new ThreeBounce();
        progressBar.setIndeterminateDrawable(wave);

    }
}


