package com.mobitech.speachtotext.textToSpeech.MeditationFeature.CATEGORIES;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.MeditationDashBoardActivity;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.Player_Anxiety_Release.sub_cat__one_audioplayer;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.Player_Anxiety_Release.sub_cat_two_audioplayer;


public class Cate_Anxiety_Release extends AppCompatActivity {
    private CardView Scategory1,Scategory2;
    private Button back_btn;
    TextView reqmarquee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate__anxiety__release);

        Scategory1=findViewById(R.id.Scategory1);
        Scategory2=findViewById(R.id.Scategory2);

        reqmarquee=findViewById(R.id.reqmaruee);

        reqmarquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        reqmarquee.setSelected(true);
        reqmarquee.setHorizontallyScrolling(true);

        back_btn=findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cate_Anxiety_Release.this, MeditationDashBoardActivity.class));
                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);
            }
        });





        Scategory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Cate_Anxiety_Release.this, sub_cat__one_audioplayer.class);
                startActivity(intent);

                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);

            }
        });

        Scategory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Cate_Anxiety_Release.this, sub_cat_two_audioplayer.class);
                startActivity(intent);

                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);

            }
        });



    }
}