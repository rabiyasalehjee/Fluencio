package com.mobitech.speachtotext.textToSpeech.constants;

public class URLS {
    public static String BASEURL = "http://172.20.10.4:5000";
    public static String LOGIN = BASEURL+"/login?";
    public static String UPDATED_SHOW_TO_TEACHER = BASEURL+"/updateshowToTeacherByID?fileID=";
    public static String REGISTERATION = BASEURL+"/signup?";
    public static String LIST_FOR_SPEECHES = BASEURL+"/getSpeeches?userID=";
    public static String FILE_UPLOADER = BASEURL+"/uploadFile";
    public static String GET_FILE_LIST = BASEURL+"/getFileDetailsByUserID?userID=";
    public static String GET_All_TOPIC_SPEECHES = BASEURL+"/getAllTopicSpeeches?userID=";
    public static String GET_ALL_INTERVIEW_SPEECHES = BASEURL+"/getAllInterviewSpeeches?userID=";
    public static String GET_INTERVIEW_LIST = BASEURL+"/allinterviewquestions";
    public static String GET_ALL_TOPICS_LIST = BASEURL+"/getAllTopics";
    public static String UPLOAD_INTERVIEW_QUESTION = BASEURL+"/uploadanswer";
    public static String UPLOAD_TOPIC = BASEURL+"/uploadtopic";
    public static String GET_USER_DATA = BASEURL+"/getUserByID?userID=";
    public static String DELETE_FILE = BASEURL+"/deleteFileByTypeAndID?fileID=";
    public static String GET_ALL_ASSIGNMENTS = BASEURL+"/getAllAssignments";
    public static String UPDATE_ASSIGNMENTS = BASEURL+"/updateAssignmentBySpeechID";
    public static String UPDATE_USER = BASEURL+"/updatePassword?";


}
