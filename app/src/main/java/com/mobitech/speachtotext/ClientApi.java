package com.mobitech.speachtotext;


import com.mobitech.speachtotext.textToSpeech.Rlogin;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ClientApi {

    @POST("api/User/signup")
    Call<String> signup(@Body FSignup login);


    @POST("api/User/signin")
    Call<Rlogin> signin(@Body Flogin login);

    @POST("api/Catagories/upload")
    Call<String> uploadMultiFile(@Body RequestBody file);



    @POST("api/user/addspeach")
    Call<String> addspeach(@Body Fspeach login);




}
