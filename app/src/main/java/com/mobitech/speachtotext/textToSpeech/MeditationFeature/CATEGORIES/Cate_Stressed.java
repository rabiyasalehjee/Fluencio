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
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.CategoryStressed.ReframeStress;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.CategoryStressed.RelieveStress;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.MeditationDashBoardActivity;


public class Cate_Stressed extends AppCompatActivity {

    private CardView Bcategory1,Bcategory2;
    private Button back_btn;
    TextView reqmarquee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate__stressed);

        Bcategory1=findViewById(R.id.Bcategory1);
        Bcategory2=findViewById(R.id.Bcategory2);


        reqmarquee=findViewById(R.id.reqmaruee);

        reqmarquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        reqmarquee.setSelected(true);
        reqmarquee.setHorizontallyScrolling(true);


        back_btn=findViewById(R.id.back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cate_Stressed.this, MeditationDashBoardActivity.class));
                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);

            }
        });



        Bcategory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Cate_Stressed.this, ReframeStress.class);
                startActivity(intent);
                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);

            }
        });

        Bcategory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Cate_Stressed.this, RelieveStress.class);
                startActivity(intent);
                overridePendingTransition(R.anim.meditation_fadein, R.anim.meditation_fadeout);

            }
        });

    }

}