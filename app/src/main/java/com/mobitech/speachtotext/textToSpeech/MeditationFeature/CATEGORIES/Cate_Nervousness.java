package com.mobitech.speachtotext.textToSpeech.MeditationFeature.CATEGORIES;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.MeditationDashBoardActivity;
import com.scwang.wave.MultiWaveHeader;

public class Cate_Nervousness extends AppCompatActivity {
    private Button back_btn;
    private TextUtils.TruncateAt noneEllipsize;
    TextView textview1,textview2,textview3,reqmarquee;

    ImageView btPlay, btPause, btPlay2, btPause2, btPlay3, btPause3;
    MediaPlayer mediaPlayer, mediaPlayer2, mediaPlayer3;

    MultiWaveHeader waveHeader,waveFooter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nervousness);

        reqmarquee=findViewById(R.id.reqmaruee);

        reqmarquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        reqmarquee.setSelected(true);
        reqmarquee.setHorizontallyScrolling(true);

        textview1=findViewById(R.id.textview1);

        textview2=findViewById(R.id.textview2);

        textview3=findViewById(R.id.textview4);

        waveHeader=findViewById(R.id.wave_header);
        waveFooter=findViewById(R.id.wave_footer);

        waveHeader.setVelocity(1);
        waveHeader.setProgress(1);
        waveHeader.isRunning();
        waveHeader.setGradientAngle(45);
        waveHeader.setWaveHeight(40);

        waveFooter.setVelocity(1);
        waveFooter.setProgress(1);
        waveFooter.isRunning();
        waveFooter.setGradientAngle(45);
        waveFooter.setWaveHeight(40);


        back_btn=findViewById(R.id.back_btn);




        btPause = findViewById(R.id.bt_pause);
        btPlay = findViewById(R.id.bt_play);


        btPause2 = findViewById(R.id.bt_pause2);
        btPlay2 = findViewById(R.id.bt_play2);

        btPause3 = findViewById(R.id.bt_pause3);
        btPlay3 = findViewById(R.id.bt_play3);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cate_Nervousness.this, MeditationDashBoardActivity.class));
                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);
                mediaPlayer.stop();
                mediaPlayer2.stop();
                mediaPlayer3.stop();

            }
        });



        mediaPlayer = MediaPlayer.create(this, R.raw.calmyourmind);
        mediaPlayer2 = MediaPlayer.create(this, R.raw.calmyournerves);
        mediaPlayer3 = MediaPlayer.create(this, R.raw.detachmentfromoverthinking);


        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPlay.setVisibility(View.GONE);

                btPause.setVisibility(View.VISIBLE);

                mediaPlayer.start();

//                mediaPlayer2.stop();
//                mediaPlayer3.stop();
//                mediaPlayer4.stop();
//                mediaPlayer5.stop();
//                mediaPlayer6.stop();





            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPause.setVisibility(View.GONE);

                btPlay.setVisibility(View.VISIBLE);

                mediaPlayer.pause();




            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btPause.setVisibility(View.GONE);
                btPlay.setVisibility(View.VISIBLE);
                mediaPlayer.seekTo(0);
            }
        });


//player2

        btPlay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPlay2.setVisibility(View.GONE);

                btPause2.setVisibility(View.VISIBLE);

                mediaPlayer2.start();




            }
        });

        btPause2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPause2.setVisibility(View.GONE);

                btPlay2.setVisibility(View.VISIBLE);

                mediaPlayer2.pause();


            }
        });

        mediaPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btPause2.setVisibility(View.GONE);
                btPlay2.setVisibility(View.VISIBLE);
                mediaPlayer2.seekTo(0);
            }
        });


//player3


        btPlay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPlay3.setVisibility(View.GONE);

                btPause3.setVisibility(View.VISIBLE);

                mediaPlayer3.start();


            }
        });

        btPause3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPause3.setVisibility(View.GONE);

                btPlay3.setVisibility(View.VISIBLE);

                mediaPlayer3.pause();


            }
        });

        mediaPlayer3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btPause3.setVisibility(View.GONE);
                btPlay3.setVisibility(View.VISIBLE);
                mediaPlayer3.seekTo(0);
            }
        });
    }
    @Override
    protected void onStop(){
        super.onStop();
        mediaPlayer.stop();
        mediaPlayer2.stop();
        mediaPlayer3.stop();


    }
    @Override
    protected void onPause(){
        super.onPause();
        mediaPlayer.stop();
        mediaPlayer2.stop();
        mediaPlayer3.stop();

    }
}