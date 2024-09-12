package com.mobitech.speachtotext.textToSpeech.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;

import com.mobitech.speachtotext.R;
import com.wang.avi.AVLoadingIndicatorView;

public final class LoadingDialog extends Dialog {

    private static final String TAG = LoadingDialog.class.getSimpleName();

    private AVLoadingIndicatorView loadingIndicatorView;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingIndicatorView = new AVLoadingIndicatorView(getContext());
        loadingIndicatorView.setIndicator("BallPulseSyncIndicator");
        loadingIndicatorView.setIndicatorColor(R.color.white);
        setContentView(loadingIndicatorView);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(100,100);
        this.setCancelable(false);
        this.setOnCancelListener(null);

    }

    public LoadingDialog(Context context) {
        super(context);
    }

}
