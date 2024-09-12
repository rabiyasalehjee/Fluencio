package com.mobitech.speachtotext.textToSpeech.MeditationFeature.GUaudioPlayers;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.CATEGORIES.Cate_GuidedMindfulness;
import com.skyfishjy.library.RippleBackground;

import java.util.concurrent.TimeUnit;

public class GUFiveMinBrAudioPlayer extends AppCompatActivity {
    TextView playerPosition, playerDuration;
    SeekBar seekBar;
    ImageView btRew, btPlay, btPause, btFf;
    MediaPlayer mediaPlayer;
    Handler handler;
    CardView cardView1;
    RippleBackground rippleBackground;

    {
        handler = new Handler(Looper.getMainLooper());
    }

    Runnable runnable;
    Button back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gu_fivemin_br_audio_player);


        playerPosition = findViewById(R.id.player_position);
        playerDuration = findViewById(R.id.player_duration);
        seekBar = findViewById(R.id.seek_bar);
        btRew = findViewById(R.id.bt_rew);
        btFf = findViewById(R.id.bt_ff);
        btPause = findViewById(R.id.bt_pause);
        btPlay = findViewById(R.id.bt_play);
        back_btn=findViewById(R.id.back_btn);
        rippleBackground=findViewById(R.id.content);

        cardView1=findViewById(R.id.cardview1);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GUFiveMinBrAudioPlayer.this, Cate_GuidedMindfulness.class);
                startActivity(intent);

                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);
                mediaPlayer.stop();
            }
        });



        mediaPlayer = MediaPlayer.create(this, R.raw.fiveminbreathing);

        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                handler.postDelayed(this, 500);
            }
        };

        int duration = mediaPlayer.getDuration();

        String sDuration = convertFormat(duration);

        playerDuration.setText(sDuration);
        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPlay.setVisibility(View.GONE);

                btPause.setVisibility(View.VISIBLE);

                mediaPlayer.start();

                seekBar.setMax(mediaPlayer.getDuration());

                handler.postDelayed(runnable, 0);

                rippleBackground.startRippleAnimation();
                cardView1.setVisibility(View.VISIBLE);
            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btPause.setVisibility(View.GONE);

                btPlay.setVisibility(View.VISIBLE);

                mediaPlayer.pause();

                handler.removeCallbacks(runnable);

                rippleBackground.stopRippleAnimation();
                //cardView1.setVisibility(View.GONE);

            }
        });

        btFf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                if (mediaPlayer.isPlaying() && duration != currentPosition) {
                    //isplaying and duation is not equal yo current position
                    //fast forwrd for 5 sec
                    currentPosition = currentPosition + 5000;

                    playerPosition.setText((convertFormat(currentPosition)));
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });

        btRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = mediaPlayer.getCurrentPosition();

                if (mediaPlayer.isPlaying() && currentPosition > 5000) {
                    currentPosition = currentPosition - 5000;
                    playerPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
    }
    @Override
    protected void onStop(){
        super.onStop();
        mediaPlayer.stop();


    }
    @Override
    protected void onPause(){
        super.onPause();
        mediaPlayer.stop();

    }
    private String convertFormat(int duration) {
        return String.format("%02d:%02d"
                , TimeUnit.MILLISECONDS.toMinutes(duration)
                , TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

    }
}