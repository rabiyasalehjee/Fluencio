package com.mobitech.speachtotext;

import com.google.gson.annotations.SerializedName;

public class Fspeach {

    @SerializedName("file")
   
    public String file;
    @SerializedName("time")
   
    public String time;
    @SerializedName("pace")
   
    public String pace;
    @SerializedName("filter")
   
    public String filter;
    @SerializedName("pitch")
   
    public String pitch;
    @SerializedName("speach")
   
    public String speach;
    @SerializedName("topic")
   
    public String topic;

    public Fspeach(String file, String time, String pace, String filter, String pitch, String speach, String topic) {
        this.file = file;
        this.time = time;
        this.pace = pace;
        this.filter = filter;
        this.pitch = pitch;
        this.speach = speach;
        this.topic = topic;
    }
}
