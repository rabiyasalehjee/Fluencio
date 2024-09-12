package com.mobitech.speachtotext;

import com.google.gson.annotations.SerializedName;

public class Fid {

    @SerializedName("id")
    public String id;

    public Fid(String id) {
        this.id = id;
    }
}
