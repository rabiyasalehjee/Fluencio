package com.mobitech.speachtotext;

import com.google.gson.annotations.SerializedName;

public class FSignup {

    @SerializedName("name")
    public String name;

    @SerializedName("email")
    public String email;

    @SerializedName("phone")
    public String phone;

    @SerializedName("password")
    public String password;

    public FSignup(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

}
