package com.mobitech.speachtotext.textToSpeech.utils;

import android.app.Application;


import java.util.Locale;

public class SpeechToText extends Application {

    private static final String TAG = SpeechToText.class.getSimpleName();
    private static SpeechToText mInstance;

    private Locale mSystemLocale;


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        mSystemLocale = Locale.getDefault();




    }

    public static synchronized SpeechToText getInstance() {
        return mInstance;
    }



}
