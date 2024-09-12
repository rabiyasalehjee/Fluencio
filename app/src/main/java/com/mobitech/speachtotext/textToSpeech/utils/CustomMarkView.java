package com.mobitech.speachtotext.textToSpeech.utils;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.mobitech.speachtotext.R;

public class CustomMarkView extends MarkerView {


    private TextView tvContent;
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public CustomMarkView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);
    }

    @Override
    public MPPointF getOffset() {
//        return super.getOffset();
        //  var offset: MPPointF? = null
        //
        //            offset = MPPointF((-(width / 2)).toFloat(), (-height - 10).toFloat())
        //
        //            return offset
        MPPointF offset=null;
        offset = new MPPointF(Float.parseFloat(String.valueOf((-(getWidth() / 2)))),
                Float.parseFloat(String.valueOf((-getHeight() - 10))));
        return offset;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
//        super.refreshContent(e, highlight);
        tvContent.setText(" "+e.getY());
        Log.d("TAG", "refreshContent: "+e.getY());
//        tvContent.text = "  " + e.y + " (kWh)"
    }
}
