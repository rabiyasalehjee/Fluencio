package com.mobitech.speachtotext.textToSpeech.model;

import java.io.File;

public class RecordedFileModel {


    // "ID": 1,
    //            "NoOFTotalWords": "2",
    //            "UserID": "1",
    //            "date": "Sat, 26 Jun 2021 19:37:18 GMT",
    //            "duration": "1",
    //            "fileRoute": "asset/app-release.apk",
    //            "filter": "4",
    //            "pace": "3",
    //            "pitch": "5",
    //            "speechText": "6",
    //            "topic": "44"
    String id,NoOFTotalWords,UserID,filter,pace,pitch,speechText,topic,
            file_name,file_path,duration,date,type;
    File file;

    boolean showToTeacher;

    public RecordedFileModel() {
    }


    public RecordedFileModel(String file_name, String file_path, String duration) {
        this.file_name = file_name;
        this.file_path = file_path;
        this.duration = duration;
    }

    public RecordedFileModel(String file_name, File file, String duration) {
        this.file_name = file_name;
        this.file = file;
        this.duration = duration;
    }


    public RecordedFileModel(String id, String noOFTotalWords, String userID, String filter,
                             String pace, String pitch, String speechText, String topic, String file_name,
                             String file_path, String duration, String date) {
        this.id = id;
        NoOFTotalWords = noOFTotalWords;
        UserID = userID;
        this.filter = filter;
        this.pace = pace;
        this.pitch = pitch;
        this.speechText = speechText;
        this.topic = topic;
        this.file_name = file_name;
        this.file_path = file_path;
        this.duration = duration;
        this.date = date;
//        this.file = file;
    }
    public RecordedFileModel(String id, String noOFTotalWords, String userID, String filter,
                             String pace, String pitch, String speechText, String topic, String file_name,
                             String file_path, String duration, String date,String type) {
        this.id = id;
        NoOFTotalWords = noOFTotalWords;
        UserID = userID;
        this.filter = filter;
        this.pace = pace;
        this.pitch = pitch;
        this.speechText = speechText;
        this.topic = topic;
        this.file_name = file_name;
        this.file_path = file_path;
        this.duration = duration;
        this.date = date;
        this.type = type;
//        this.file = file;
    }

     public RecordedFileModel(String id, String noOFTotalWords, String userID, String filter,
                             String pace, String pitch, String speechText, String topic, String file_name,
                             String file_path, String duration, String date,String type,boolean showToTeacher ) {
        this.id = id;
        NoOFTotalWords = noOFTotalWords;
        UserID = userID;
        this.filter = filter;
        this.pace = pace;
        this.pitch = pitch;
        this.speechText = speechText;
        this.topic = topic;
        this.file_name = file_name;
        this.file_path = file_path;
        this.duration = duration;
        this.date = date;
        this.type = type;
        this.showToTeacher = showToTeacher;
//        this.file = file;
    }



    public boolean isShowToTeacher() {
        return showToTeacher;
    }

    public void setShowToTeacher(boolean showToTeacher) {
        this.showToTeacher = showToTeacher;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoOFTotalWords() {
        return NoOFTotalWords;
    }

    public void setNoOFTotalWords(String noOFTotalWords) {
        NoOFTotalWords = noOFTotalWords;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getPace() {
        return pace;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public String getSpeechText() {
        return speechText;
    }

    public void setSpeechText(String speechText) {
        this.speechText = speechText;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
