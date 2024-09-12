package com.mobitech.speachtotext.textToSpeech.Walkthroughs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.mobitech.speachtotext.R;


public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public SliderAdapter(Context context) {
        this.context = context;
    }

    int images[] ={
            R.drawable.onboard_presentation_1,
            R.drawable.onboard_interview_1,
            R.drawable.onboard_lecture_1,
            R. drawable.onboard_yoga_1
    };



    String headings[] ={
           "Improve your presentation skill",
            "Improve your interview skill",
            "Improve your teaching skill",
            "Get over the fear"
    };

    int description [] ={
            R.string.first_slide_desc,
            R.string.second_slide_desc,
            R.string.third_slide_desc,
            R.string.fourth_slide_desc
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view  == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slides_layout, container, false);

        ImageView imageview = view.findViewById(R.id.slider_image_1);
        TextView slider_heading = view.findViewById(R.id.slider_heading_1);
        TextView slider_desc = view.findViewById(R.id.slider_desc_1);

        imageview.setImageResource(images[position]);
        slider_heading.setText(headings[position]);
        slider_desc.setText(description[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

       container.removeView((ConstraintLayout) object);
    }


}





