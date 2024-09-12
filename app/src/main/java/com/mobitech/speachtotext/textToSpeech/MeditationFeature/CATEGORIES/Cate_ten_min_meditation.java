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


public class Cate_ten_min_meditation extends AppCompatActivity {
    ImageView btPlay, btPause, btPlay2, btPause2, btPlay3, btPause3, btPlay4, btPause4, btPlay5, btPause5, btPlay6, btPause6;
    MediaPlayer mediaPlayer, mediaPlayer2, mediaPlayer3, mediaPlayer4, mediaPlayer5, mediaPlayer6;
    Button back_btn;
    TextView reqmarquee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate__ten_min_meditation);

        reqmarquee=findViewById(R.id.reqmaruee);

        reqmarquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        reqmarquee.setSelected(true);
        reqmarquee.setHorizontallyScrolling(true);


        btPause = findViewById(R.id.bt_pause);
        btPlay = findViewById(R.id.bt_play);
        back_btn = findViewById(R.id.back_btn);

        btPause2 = findViewById(R.id.bt_pause2);
        btPlay2 = findViewById(R.id.bt_play2);

        btPause3 = findViewById(R.id.bt_pause3);
        btPlay3 = findViewById(R.id.bt_play3);

        btPause4 = findViewById(R.id.bt_pause4);
        btPlay4 = findViewById(R.id.bt_play4);

        btPause5 = findViewById(R.id.bt_pause5);
        btPlay5 = findViewById(R.id.bt_play5);

        btPause6 = findViewById(R.id.bt_pause6);
        btPlay6 = findViewById(R.id.bt_play6);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cate_ten_min_meditation.this, MeditationDashBoardActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);
                mediaPlayer.stop();
                mediaPlayer2.stop();
                mediaPlayer3.stop();
                mediaPlayer4.stop();
                mediaPlayer5.stop();
                mediaPlayer6.stop();
            }
        });


        mediaPlayer = MediaPlayer.create(this, R.raw.tenminmeditationforbeginners);
        mediaPlayer2 = MediaPlayer.create(this, R.raw.tenminutemeditationtostartyourday);
        mediaPlayer3 = MediaPlayer.create(this, R.raw.tenminutemeditationforanxiety);
        mediaPlayer4 = MediaPlayer.create(this, R.raw.tenminutemeditationforstress);
        mediaPlayer5 = MediaPlayer.create(this, R.raw.tenminutedailymeditation);
        mediaPlayer6 = MediaPlayer.create(this, R.raw.tenminutemeditationfordepression);


//player1
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


        //player4


        btPlay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPlay4.setVisibility(View.GONE);

                btPause4.setVisibility(View.VISIBLE);

                mediaPlayer4.start();


            }
        });

        btPause4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPause4.setVisibility(View.GONE);

                btPlay4.setVisibility(View.VISIBLE);

                mediaPlayer4.pause();


            }
        });

        mediaPlayer3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btPause4.setVisibility(View.GONE);
                btPlay4.setVisibility(View.VISIBLE);
                mediaPlayer4.seekTo(0);
            }
        });


        //player5

        btPlay5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPlay5.setVisibility(View.GONE);

                btPause5.setVisibility(View.VISIBLE);

                mediaPlayer5.start();


            }
        });

        btPause5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPause5.setVisibility(View.GONE);

                btPlay5.setVisibility(View.VISIBLE);

                mediaPlayer5.pause();

            }
        });

        mediaPlayer5.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btPause5.setVisibility(View.GONE);
                btPlay5.setVisibility(View.VISIBLE);
                mediaPlayer5.seekTo(0);
            }
        });


//player6


        btPlay6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPlay6.setVisibility(View.GONE);

                btPause6.setVisibility(View.VISIBLE);

                mediaPlayer6.start();


            }
        });

        btPause6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPause6.setVisibility(View.GONE);

                btPlay6.setVisibility(View.VISIBLE);

                mediaPlayer6.pause();


            }
        });

        mediaPlayer6.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btPause6.setVisibility(View.GONE);
                btPlay6.setVisibility(View.VISIBLE);
                mediaPlayer6.seekTo(0);
            }
        });


    }

    @Override
    protected void onStop(){
        super.onStop();
        mediaPlayer.stop();
        mediaPlayer2.stop();
        mediaPlayer3.stop();
        mediaPlayer4.stop();
        mediaPlayer5.stop();
        mediaPlayer6.stop();


    }
    @Override
    protected void onPause(){
        super.onPause();
        mediaPlayer.stop();
        mediaPlayer2.stop();
        mediaPlayer3.stop();
        mediaPlayer4.stop();
        mediaPlayer5.stop();
        mediaPlayer6.stop();

    }
}