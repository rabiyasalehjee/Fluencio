package com.mobitech.speachtotext.textToSpeech.model;

public class UserModel {

    String UserName,UserId;

    public UserModel() {
    }

    public UserModel(String userName, String userId) {
        UserName = userName;
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
