package com.mobitech.speachtotext;

import com.google.gson.annotations.SerializedName;

public class Flogin {


    @SerializedName("email")
    public String email;
    @SerializedName("password")
    public String password;

    public Flogin(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
