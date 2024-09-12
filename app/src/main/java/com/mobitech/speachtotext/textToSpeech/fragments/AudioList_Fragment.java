package com.mobitech.speachtotext.textToSpeech.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobitech.speachtotext.MainActivity;
import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.adapter.AudioListAdapter;
import com.mobitech.speachtotext.textToSpeech.constants.URLS;
import com.mobitech.speachtotext.textToSpeech.listeners.ServerRequestListener;
import com.mobitech.speachtotext.textToSpeech.model.RecordedFileModel;
import com.mobitech.speachtotext.textToSpeech.serverRequestHelper.MyServerRequest;
import com.mobitech.speachtotext.textToSpeech.sharedPref.UserSettings;
import com.mobitech.speachtotext.textToSpeech.utils.CustomMarkView;
import com.mobitech.speachtotext.textToSpeech.utils.UtilityFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AudioList_Fragment extends Fragment /*implements AudioListAdapter.onItemListClick*/ {


    private final MediaPlayer mediaPlayer = null;
    BarDataSet Bardataset;
    BarDataSet Bardataset2;
    BarData PITCHBARDATA,PACEBARDATA;
    private RelativeLayout rel_tuning,rel_tuning2,rel_tuning3;
    private RecyclerView audioList,audioTopicList,audioInterviewList;
    private FloatingActionButton fab_showgraph;
    private final ArrayList<RecordedFileModel> allFiles = new ArrayList<>();
    private final ArrayList<RecordedFileModel> all_list_Files = new ArrayList<>();
    private final ArrayList<RecordedFileModel> topic_Files = new ArrayList<>();
    private final ArrayList<RecordedFileModel> interview_Files = new ArrayList<>();
    private AudioListAdapter audioListAdapter;
    //UI Elements
    private Handler seekbarHandler;
    private Runnable updateSeekbar;


    private UserSettings userSettings;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_list_, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rel_tuning = view.findViewById(R.id.rel_tuning);
        rel_tuning2 = view.findViewById(R.id.rel_tuning2);
        rel_tuning3 = view.findViewById(R.id.rel_tuning3);
        audioList = view.findViewById(R.id.rv_audio_list_view);
        audioTopicList = view.findViewById(R.id.rv_audio_topic_list_view);
        audioInterviewList = view.findViewById(R.id.rv_audio_interview_list_view);
        fab_showgraph = view.findViewById(R.id.fab_showgraph);

        userSettings = new UserSettings(requireActivity());
//        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
//        File directory = new File(path);
//        allFiles = directory.listFiles();

        //TODO uncomment when api is called
        GetFIleListApi();

//        DummyFileList();

//        assert allFiles != null;

        fab_showgraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowGraph(allFiles.size());
//                ShowGraph(3);
            }
        });



    }
/*    private void  DummyFileList(){
        interview_Files.add(new RecordedFileModel(
                "1",
                "2",
                "1",
                "4",
                "100",
                "32.200",
                "Hello how are you?",
                "Exam",
                "Hello interview",

                "/asset/app-release.apk",
                "01:00",
                "Sat, 26 Jun 2021 22:38:30 GMT"

        ));
        allFiles.add(new RecordedFileModel(
                "1",
                "2",
                "1",
                "4",
                "100",
                "32.200",
                "Hello how are you?",
                "Exam",
                "Hello interview",

                "/asset/app-release.apk",
                "01:00",
                "Sat, 26 Jun 2021 22:38:30 GMT"

        ));
        interview_Files.add(new RecordedFileModel(
                "1",
                "2",
                "1",
                "4",
                "100",
                "32.200",
                "Hello how are you?",
                "Exam",
                "Hello interview",

                "/asset/app-release.apk",
                "01:00",
                "Sat, 26 Jun 2021 22:38:30 GMT"

        ));
        allFiles.add(new RecordedFileModel(
                "1",
                "2",
                "1",
                "4",
                "100",
                "32.200",
                "Hello how are you?",
                "Exam",
                "Hello interview",

                "/asset/app-release.apk",
                "01:00",
                "Sat, 26 Jun 2021 22:38:30 GMT"

        ));
        interview_Files.add(new RecordedFileModel(
                "1",
                "2",
                "1",
                "4",
                "100",
                "32.200",
                "Hello how are you?",
                "Exam",
                "Hello interview",

                "/asset/app-release.apk",
                "01:00",
                "Sat, 26 Jun 2021 22:38:30 GMT"

        ));
        allFiles.add(new RecordedFileModel(
                "1",
                "2",
                "1",
                "4",
                "100",
                "32.200",
                "Hello how are you?",
                "Exam",
                "Hello interview",

                "/asset/app-release.apk",
                "01:00",
                "Sat, 26 Jun 2021 22:38:30 GMT"

        ));
        topic_Files.add(new RecordedFileModel(
                "2",
                "5",
                "1",
                "4",
                "100",
                "42.200",
                "Hello how are you?",
                "Exam",
                "Hello topic",

                "/asset/app-release.apk",
                "01:00",
                "Sat, 26 Jun 2021 22:38:30 GMT"

        ));
        allFiles.add(new RecordedFileModel(
                "2",
                "5",
                "1",
                "4",
                "100",
                "42.200",
                "Hello how are you?",
                "Exam",
                "Hello topic",

                "/asset/app-release.apk",
                "01:00",
                "Sat, 26 Jun 2021 22:38:30 GMT"

        ));
        topic_Files.add(new RecordedFileModel(
                "2",
                "5",
                "1",
                "4",
                "100",
                "42.200",
                "Hello how are you?",
                "Exam",
                "Hello topic",

                "/asset/app-release.apk",
                "01:00",
                "Sat, 26 Jun 2021 22:38:30 GMT"

        ));
        allFiles.add(new RecordedFileModel(
                "2",
                "5",
                "1",
                "4",
                "100",
                "42.200",
                "Hello how are you?",
                "Exam",
                "Hello topic",

                "/asset/app-release.apk",
                "01:00",
                "Sat, 26 Jun 2021 22:38:30 GMT"

        ));
        all_list_Files.add(new RecordedFileModel(
                "3",
                "44",
                "1",
                "4",
                "100",
                "42.200",
                "Hello how a22re you?",
                "Exam",
                "Hello all list",

                "/asset/app-release.apk",
                "01:00",
                "Sat, 26 Jun 2021 22:38:30 GMT"

        ));
        allFiles.add(new RecordedFileModel(
                "3",
                "44",
                "1",
                "4",
                "100",
                "42.200",
                "Hello how a22re you?",
                "Exam",
                "Hello all list",

                "/asset/app-release.apk",
                "01:00",
                "Sat, 26 Jun 2021 22:38:30 GMT"

        ));
        all_list_Files.add(new RecordedFileModel(
                "3",
                "44",
                "1",
                "4",
                "100",
                "42.200",
                "Hello how a22re you?",
                "Exam",
                "Hello all list",

                "/asset/app-release.apk",
                "01:00",
                "Sat, 26 Jun 2021 22:38:30 GMT"

        ));
        allFiles.add(new RecordedFileModel(
                "3",
                "44",
                "1",
                "4",
                "100",
                "42.200",
                "Hello how a22re you?",
                "Exam",
                "Hello all list",

                "/asset/app-release.apk",
                "01:00",
                "Sat, 26 Jun 2021 22:38:30 GMT"

        ));
        audioListAdapter = new AudioListAdapter(all_list_Files,  getActivity());
        audioListAdapter = new AudioListAdapter(topic_Files,  getActivity());
        audioListAdapter = new AudioListAdapter(interview_Files,  getActivity());

        if (topic_Files.size() == 0) {
            audioTopicList.setVisibility(View.GONE);
            rel_tuning.setVisibility(View.GONE);
        }
        if (interview_Files.size() == 0) {
            audioInterviewList.setVisibility(View.GONE);
            rel_tuning2.setVisibility(View.GONE);
        }
        if (all_list_Files.size() == 0) {
            audioList.setVisibility(View.GONE);
            rel_tuning3.setVisibility(View.GONE);
        }


        //list recylerview
        audioListAdapter = new AudioListAdapter(all_list_Files,  getActivity());
        audioList.setHasFixedSize(true);
        audioList.setLayoutManager(new LinearLayoutManager(getContext()));
        audioList.setAdapter(audioListAdapter);


        //interview recylerview
        audioListAdapter = new AudioListAdapter(interview_Files,  getActivity());
        audioInterviewList.setHasFixedSize(true);
        audioInterviewList.setLayoutManager(new LinearLayoutManager(getContext()));
        audioInterviewList.setAdapter(audioListAdapter);


        //topic recylerview
        audioListAdapter = new AudioListAdapter(topic_Files,  getActivity());
        audioTopicList.setHasFixedSize(true);
        audioTopicList.setLayoutManager(new LinearLayoutManager(getContext()));
        audioTopicList.setAdapter(audioListAdapter);


        fab_showgraph.setVisibility(View.VISIBLE);
        if (allFiles.size() != 0) {
            fab_showgraph.setVisibility(View.VISIBLE);
        } else {
            fab_showgraph.setVisibility(View.GONE);
        }
    }*/

    private void GetFIleListApi() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("id", rlogin.getId()); // Storing string
//        editor.putString("name", rlogin.getName()); // Storing string
//        editor.putString("phone", rlogin.getPhone()); // Storing string
//        editor.commit();
        String ListForSpeeches="";

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        String allSpeechURL = URLS.LIST_FOR_SPEECHES + pref.getString("id","2");

        Log.i("cheking URL", "GetFIleListApi: " + URLS.GET_FILE_LIST+pref.getString("id","2"));
        if (userSettings != null) {

            if (userSettings.getString(allSpeechURL) != null) {
                if (!userSettings.getString(allSpeechURL).equals("")){
                    ListForSpeeches=userSettings.getString(allSpeechURL);
                }
            }
        }else{
            ListForSpeeches=allSpeechURL ;
        }
        MyServerRequest myServerRequest = new MyServerRequest(requireActivity(), allSpeechURL,
                new ServerRequestListener() {
                    @Override
                    public void onPreResponse() {
                        UtilityFunctions.showProgressDialog(requireActivity(), true);
                    }

                    @Override
                    public void onPostResponse(JSONObject jsonResponse, int requestCode) {

                        UtilityFunctions.hideProgressDialog(true);

                        try {
                            JSONObject response= jsonResponse.getJSONObject("respone");

                                //      "NoOFTotalWords": "2",
                                //      "UserID": "1",
                                //      "date": "Sat, 26 Jun 2021 22:38:30 GMT",
                                //      "duration": "1",
                                //      "fileRoute": "/asset/app-release.apk",
                                //      "filter": "4",
                                //      "pace": "100",
                                //      "pitch": "32.200",
                                //      "speechText":
                              //  JSONObject jsonObject = response.getJSONObject(i);
                                JSONArray allSpeechesArray=response.getJSONArray("allSpeeches");
                                JSONArray interviewSpeechesArray=response.getJSONArray("interviewSpeeches");
                                JSONArray topicsSpeechesArray=response.getJSONArray("topicsSpeeches");
                                for (int j = 0; j < allSpeechesArray.length(); j++) {
                                    JSONObject allSpeechesObject=allSpeechesArray.getJSONObject(j);
                                    all_list_Files.add(new RecordedFileModel(
                                            allSpeechesObject.getString("ID"),
                                            allSpeechesObject.getString("NoOFTotalWords"),
                                            allSpeechesObject.getString("UserID"),
                                            allSpeechesObject.getString("filter"),
                                            allSpeechesObject.getString("pace"),
                                            allSpeechesObject.getString("pitch"),
                                            allSpeechesObject.getString("speechText"),
                                            allSpeechesObject.getString("topic"),
                                            allSpeechesObject.getString("fileName"),
                                            allSpeechesObject.getString("fileRoute"),
                                            allSpeechesObject.getString("duration"),
                                            allSpeechesObject.getString("date"),
                                            "all",
                                            allSpeechesObject.getBoolean("showToTeacher")

                                    ));
                                    allFiles.add(new RecordedFileModel(
                                            allSpeechesObject.getString("ID"),
                                            allSpeechesObject.getString("NoOFTotalWords"),
                                            allSpeechesObject.getString("UserID"),
                                            allSpeechesObject.getString("filter"),
                                            allSpeechesObject.getString("pace"),
                                            allSpeechesObject.getString("pitch"),
                                            allSpeechesObject.getString("speechText"),
                                            allSpeechesObject.getString("topic"),
                                            allSpeechesObject.getString("fileName"),
                                            allSpeechesObject.getString("fileRoute"),
                                            allSpeechesObject.getString("duration"),
                                            allSpeechesObject.getString("date"),
                                            "all",
                                            allSpeechesObject.getBoolean("showToTeacher")

                                    ));

                                }
                                for (int j = 0; j < interviewSpeechesArray.length(); j++) {
                                    JSONObject interviewSpeechesObject=interviewSpeechesArray.getJSONObject(j);
                                    interview_Files.add(new RecordedFileModel(
                                            interviewSpeechesObject.getString("ID"),
                                            interviewSpeechesObject.getString("NoOFTotalWords"),
                                            interviewSpeechesObject.getString("UserID"),
                                            interviewSpeechesObject.getString("filter"),
                                            interviewSpeechesObject.getString("pace"),
                                            interviewSpeechesObject.getString("pitch"),
                                            interviewSpeechesObject.getString("speechText"),
                                            interviewSpeechesObject.getString("topic"),
                                            interviewSpeechesObject.getString("fileName"),
                                            interviewSpeechesObject.getString("fileRoute"),
                                            interviewSpeechesObject.getString("duration"),
                                            interviewSpeechesObject.getString("date"),
                                            "interview",
                                            interviewSpeechesObject.getBoolean("showToTeacher")
                                    ));
                                    allFiles.add(new RecordedFileModel(
                                            interviewSpeechesObject.getString("ID"),
                                            interviewSpeechesObject.getString("NoOFTotalWords"),
                                            interviewSpeechesObject.getString("UserID"),
                                            interviewSpeechesObject.getString("filter"),
                                            interviewSpeechesObject.getString("pace"),
                                            interviewSpeechesObject.getString("pitch"),
                                            interviewSpeechesObject.getString("speechText"),
                                            interviewSpeechesObject.getString("topic"),
                                            interviewSpeechesObject.getString("fileName"),
                                            interviewSpeechesObject.getString("fileRoute"),
                                            interviewSpeechesObject.getString("duration"),
                                            interviewSpeechesObject.getString("date"),
                                            "all",
                                            interviewSpeechesObject.getBoolean("showToTeacher")
                                    ));

                                }
                                for (int j = 0; j < topicsSpeechesArray.length(); j++) {
                                    JSONObject topicsSpeechesObject=topicsSpeechesArray.getJSONObject(j);
                                    topic_Files.add(new RecordedFileModel(
                                            topicsSpeechesObject.getString("ID"),
                                            topicsSpeechesObject.getString("NoOFTotalWords"),
                                            topicsSpeechesObject.getString("UserID"),
                                            topicsSpeechesObject.getString("filter"),
                                            topicsSpeechesObject.getString("pace"),
                                            topicsSpeechesObject.getString("pitch"),
                                            topicsSpeechesObject.getString("speechText"),
                                            topicsSpeechesObject.getString("topic"),
                                            topicsSpeechesObject.getString("fileName"),
                                            topicsSpeechesObject.getString("fileRoute"),
                                            topicsSpeechesObject.getString("duration"),
                                            topicsSpeechesObject.getString("date"),
                                            "topic",
                                            topicsSpeechesObject.getBoolean("showToTeacher")
                                    ));
                                    allFiles.add(new RecordedFileModel(
                                            topicsSpeechesObject.getString("ID"),
                                            topicsSpeechesObject.getString("NoOFTotalWords"),
                                            topicsSpeechesObject.getString("UserID"),
                                            topicsSpeechesObject.getString("filter"),
                                            topicsSpeechesObject.getString("pace"),
                                            topicsSpeechesObject.getString("pitch"),
                                            topicsSpeechesObject.getString("speechText"),
                                            topicsSpeechesObject.getString("topic"),
                                            topicsSpeechesObject.getString("fileName"),
                                            topicsSpeechesObject.getString("fileRoute"),
                                            topicsSpeechesObject.getString("duration"),
                                            topicsSpeechesObject.getString("date"),
                                            "all",
                                            topicsSpeechesObject.getBoolean("showToTeacher")
                                    ));

                                }


//                            for (int i = 0; i < response.length(); i++) {
//                                // "ID": 1,
//                                //      "NoOFTotalWords": "2",
//                                //      "UserID": "1",
//                                //      "date": "Sat, 26 Jun 2021 22:38:30 GMT",
//                                //      "duration": "1",
//                                //      "fileRoute": "/asset/app-release.apk",
//                                //      "filter": "4",
//                                //      "pace": "100",
//                                //      "pitch": "32.200",
//                                //      "speechText":
//                                JSONObject jsonObject = response.getJSONObject(i);
//                                JSONArray allSpeechesArray=jsonObject.getJSONArray("allSpeeches");
//                                JSONArray interviewSpeechesArray=jsonObject.getJSONArray("interviewSpeeches");
//                                JSONArray topicsSpeechesArray=jsonObject.getJSONArray("topicsSpeeches");
//                                for (int j = 0; j < allSpeechesArray.length(); j++) {
//                                    JSONObject allSpeechesObject=allSpeechesArray.getJSONObject(j);
//                                    all_list_Files.add(new RecordedFileModel(
//                                            allSpeechesObject.getString("ID"),
//                                            allSpeechesObject.getString("NoOFTotalWords"),
//                                            allSpeechesObject.getString("UserID"),
//                                            allSpeechesObject.getString("filter"),
//                                            allSpeechesObject.getString("pace"),
//                                            allSpeechesObject.getString("pitch"),
//                                            allSpeechesObject.getString("speechText"),
//                                            allSpeechesObject.getString("topic"),
//                                            allSpeechesObject.getString("fileName"),
//                                            allSpeechesObject.getString("fileRoute"),
//                                            allSpeechesObject.getString("duration"),
//                                            allSpeechesObject.getString("date")
//
//                                    ));
//                                    allFiles.add(new RecordedFileModel(
//                                            allSpeechesObject.getString("ID"),
//                                            allSpeechesObject.getString("NoOFTotalWords"),
//                                            allSpeechesObject.getString("UserID"),
//                                            allSpeechesObject.getString("filter"),
//                                            allSpeechesObject.getString("pace"),
//                                            allSpeechesObject.getString("pitch"),
//                                            allSpeechesObject.getString("speechText"),
//                                            allSpeechesObject.getString("topic"),
//                                            allSpeechesObject.getString("fileName"),
//                                            allSpeechesObject.getString("fileRoute"),
//                                            allSpeechesObject.getString("duration"),
//                                            allSpeechesObject.getString("date")
//
//                                    ));
//
//                                }
//                                for (int j = 0; j < interviewSpeechesArray.length(); j++) {
//                                    JSONObject interviewSpeechesObject=interviewSpeechesArray.getJSONObject(j);
//                                    interview_Files.add(new RecordedFileModel(
//                                            interviewSpeechesObject.getString("ID"),
//                                            interviewSpeechesObject.getString("NoOFTotalWords"),
//                                            interviewSpeechesObject.getString("UserID"),
//                                            interviewSpeechesObject.getString("filter"),
//                                            interviewSpeechesObject.getString("pace"),
//                                            interviewSpeechesObject.getString("pitch"),
//                                            interviewSpeechesObject.getString("speechText"),
//                                            interviewSpeechesObject.getString("topic"),
//                                            interviewSpeechesObject.getString("fileName"),
//                                            interviewSpeechesObject.getString("fileRoute"),
//                                            interviewSpeechesObject.getString("duration"),
//                                            interviewSpeechesObject.getString("date")
//
//                                    ));
//                                    allFiles.add(new RecordedFileModel(
//                                            interviewSpeechesObject.getString("ID"),
//                                            interviewSpeechesObject.getString("NoOFTotalWords"),
//                                            interviewSpeechesObject.getString("UserID"),
//                                            interviewSpeechesObject.getString("filter"),
//                                            interviewSpeechesObject.getString("pace"),
//                                            interviewSpeechesObject.getString("pitch"),
//                                            interviewSpeechesObject.getString("speechText"),
//                                            interviewSpeechesObject.getString("topic"),
//                                            interviewSpeechesObject.getString("fileName"),
//                                            interviewSpeechesObject.getString("fileRoute"),
//                                            interviewSpeechesObject.getString("duration"),
//                                            interviewSpeechesObject.getString("date")
//
//                                    ));
//
//                                }
//                                for (int j = 0; j < topicsSpeechesArray.length(); j++) {
//                                    JSONObject topicsSpeechesObject=topicsSpeechesArray.getJSONObject(j);
//                                    topic_Files.add(new RecordedFileModel(
//                                            topicsSpeechesObject.getString("ID"),
//                                            topicsSpeechesObject.getString("NoOFTotalWords"),
//                                            topicsSpeechesObject.getString("UserID"),
//                                            topicsSpeechesObject.getString("filter"),
//                                            topicsSpeechesObject.getString("pace"),
//                                            topicsSpeechesObject.getString("pitch"),
//                                            topicsSpeechesObject.getString("speechText"),
//                                            topicsSpeechesObject.getString("topic"),
//                                            topicsSpeechesObject.getString("fileName"),
//                                            topicsSpeechesObject.getString("fileRoute"),
//                                            topicsSpeechesObject.getString("duration"),
//                                            topicsSpeechesObject.getString("date")
//
//                                    ));
//                                    allFiles.add(new RecordedFileModel(
//                                            topicsSpeechesObject.getString("ID"),
//                                            topicsSpeechesObject.getString("NoOFTotalWords"),
//                                            topicsSpeechesObject.getString("UserID"),
//                                            topicsSpeechesObject.getString("filter"),
//                                            topicsSpeechesObject.getString("pace"),
//                                            topicsSpeechesObject.getString("pitch"),
//                                            topicsSpeechesObject.getString("speechText"),
//                                            topicsSpeechesObject.getString("topic"),
//                                            topicsSpeechesObject.getString("fileName"),
//                                            topicsSpeechesObject.getString("fileRoute"),
//                                            topicsSpeechesObject.getString("duration"),
//                                            topicsSpeechesObject.getString("date")
//
//                                    ));
//
//                                }
////                                allFiles.add(new RecordedFileModel(
////                                        jsonObject.getString("ID"),
////                                        jsonObject.getString("NoOFTotalWords"),
////                                        jsonObject.getString("UserID"),
////                                        jsonObject.getString("filter"),
////                                        jsonObject.getString("pace"),
////                                        jsonObject.getString("pitch"),
////                                        jsonObject.getString("speechText"),
////                                        jsonObject.getString("topic"),
////                                        jsonObject.getString("fileName"),
////
////                                        jsonObject.getString("fileRoute"),
////                                        jsonObject.getString("duration"),
////                                        jsonObject.getString("date")
////
////                                ));
//                            }
                            if (topic_Files.size() == 0) {
                                audioTopicList.setVisibility(View.GONE);
                                rel_tuning.setVisibility(View.GONE);
                            }
                            if (interview_Files.size() == 0) {
                                audioInterviewList.setVisibility(View.GONE);
                                rel_tuning2.setVisibility(View.GONE);
                            }
                            if (all_list_Files.size() == 0) {
                                audioList.setVisibility(View.GONE);
                                rel_tuning3.setVisibility(View.GONE);
                            }


                            //list recylerview
                            audioListAdapter = new AudioListAdapter(all_list_Files,  getActivity());
                            audioList.setHasFixedSize(true);
                            audioList.setLayoutManager(new LinearLayoutManager(getContext()));
                            audioList.setAdapter(audioListAdapter);


                            //interview recylerview
                            audioListAdapter = new AudioListAdapter(interview_Files,  getActivity());
                            audioInterviewList.setHasFixedSize(true);
                            audioInterviewList.setLayoutManager(new LinearLayoutManager(getContext()));
                            audioInterviewList.setAdapter(audioListAdapter);


                            //topic recylerview
                            audioListAdapter = new AudioListAdapter(topic_Files,  getActivity());
                            audioTopicList.setHasFixedSize(true);
                            audioTopicList.setLayoutManager(new LinearLayoutManager(getContext()));
                            audioTopicList.setAdapter(audioListAdapter);



                            fab_showgraph.setVisibility(View.VISIBLE);
                            if (allFiles.size() != 0) {
                                fab_showgraph.setVisibility(View.VISIBLE);
                            } else {
                                fab_showgraph.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            Log.d("TAG", "onPostResponse: "+e.getMessage());
                            e.printStackTrace();
                        }

//                UtilityFunctions.hideProgressDialog(true);
//                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Login.this, TextToSpeechActivity.class));
//                finish();
//                try {
//                    if (jsonResponse.getBoolean(AppConstants.HAS_RESPONSE)){
//                        UtilityFunctions.hideProgressDialog(true);
//                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(Login.this,DashboardActivity.class));
//                        finish();
//                    }else{
//                        UtilityFunctions.hideProgressDialog(true);
//                        Toast.makeText(Login.this, " "+jsonResponse.getString(AppConstants.MESSAGE), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    Log.i(TAG, "onPostResponse: "+jsonResponse);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                    }
                });
        myServerRequest.sendGetRequest();
    }

//    @Override
//    public void onClickListener(RecordedFileModel model) {
//        requireActivity().startActivity(new Intent(requireActivity(), MainActivity.class));
//
//        userSettings.createRecordedFileObj(model);
//        requireActivity().finish();
//    }


    @Override
    public void onStop() {
        super.onStop();
    }

    private void ShowGraph(int Count) {
        LayoutInflater factory = LayoutInflater.from(getActivity());

        View customDialogue = factory.inflate(R.layout.custom_dialog_layout_graph_view,
                null);
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(customDialogue);
        BarChart barChart = customDialogue.findViewById(R.id.line_magic);
        BarChart barChart2 = customDialogue.findViewById(R.id.line_magic_2);
        ArrayList<BarEntry> PitchBARENTRY = new ArrayList<>();
        ArrayList<BarEntry> PaceBARENTRY = new ArrayList<>();
        ArrayList<String> BarEntryLabels = new ArrayList<>();
        float groupSpace = 0.1f;
        float barSpace = 0f;
        float barWidth = 0.3f;
        Description desc = new Description();
        desc.setText("");
        for (int i = 0; i < Count; i++) {

            PitchBARENTRY.add(new BarEntry(i, Float.parseFloat(String.valueOf(allFiles.get(i).getPitch()))));
            PaceBARENTRY.add(new BarEntry(i, Float.parseFloat(String.valueOf(allFiles.get(i).getPace()))));
//            PitchBARENTRY.add(new BarEntry(i, Float.parseFloat(String.valueOf(7))));
//            PaceBARENTRY.add(new BarEntry(i, Float.parseFloat(String.valueOf(5))));
            BarEntryLabels.add("Entry " + i);
        }
        barChart.setDrawGridBackground(false);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setMaxHighlightDistance(200F);
        barChart.setViewPortOffsets(0F, 0F, 0F, 32F);
        barChart.setDescription(desc);
        barChart.setDrawBarShadow(false);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(true);
        barChart.setDrawGridBackground(true);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(false);

        barChart2.setDrawGridBackground(false);
        barChart2.setTouchEnabled(true);
        barChart2.setDragEnabled(true);
        barChart2.setScaleEnabled(true);
        barChart2.setMaxHighlightDistance(200F);
        barChart2.setViewPortOffsets(0F, 0F, 0F, 32F);
        barChart2.setDescription(desc);
        barChart2.setDrawBarShadow(false);
        barChart2.setMaxVisibleValueCount(50);
        barChart2.setPinchZoom(true);
        barChart2.setDrawGridBackground(true);
        barChart2.getDescription().setEnabled(false);
        barChart2.setDrawValueAboveBar(false);

        XAxis x1 = barChart.getXAxis();
        x1.setGranularity(1f);
        x1.setPosition(XAxis.XAxisPosition.TOP);

        XAxis x2 = barChart2.getXAxis();
        x2.setGranularity(1f);
        x2.setPosition(XAxis.XAxisPosition.TOP);

        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setEnabled(false);

        YAxis rightYAxis2 = barChart2.getAxisRight();
        rightYAxis2.setEnabled(false);

        Bardataset = new BarDataSet(PitchBARENTRY, "Pitch Entries");
        Bardataset.setDrawValues(false);
        Bardataset.setValueTextColor(Color.WHITE);
        Bardataset.setColors(ColorTemplate.MATERIAL_COLORS);
        Bardataset2 = new BarDataSet(PaceBARENTRY, "Pace Entries");
        Bardataset2.setDrawValues(false);
        Bardataset2.setValueTextColor(Color.WHITE);
        Bardataset2.setColors(ColorTemplate.COLORFUL_COLORS);
        ArrayList<IBarDataSet> Pitchdatasets = new ArrayList<>();
        Pitchdatasets.add(Bardataset);
        ArrayList<IBarDataSet> Pacedatasets = new ArrayList<>();
        Pacedatasets.add(Bardataset2);
        PITCHBARDATA = new BarData(Pitchdatasets);
        PACEBARDATA = new BarData(Pacedatasets);
        barChart.setData(PITCHBARDATA);
        barChart.getBarData().setBarWidth(barWidth);
//        barChart.groupBars(0.1f, groupSpace, barSpace);
        barChart.setDrawGridBackground(false);
        barChart.setClickable(true);
        barChart.setMarker(new CustomMarkView(getActivity(), R.layout.marker_layout));

        barChart2.setData(PACEBARDATA);
        barChart2.getBarData().setBarWidth(barWidth);
//        barChart2.groupBars(0.1f, groupSpace, barSpace);
        barChart2.setDrawGridBackground(false);
        barChart2.setClickable(true);
        barChart2.setMarker(new CustomMarkView(getActivity(), R.layout.marker_layout));

        Legend legend = barChart.getLegend();
        legend.setYOffset(0f);
        legend.setYEntrySpace(50f);

        Legend legend2 = barChart2.getLegend();
        legend2.setYOffset(0f);
        legend2.setYEntrySpace(50f);

        barChart.setScaleEnabled(true);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.animateY(2000);
        barChart.invalidate();

        barChart2.setScaleEnabled(true);
        barChart2.getAxisLeft().setDrawGridLines(false);
        barChart2.getXAxis().setDrawGridLines(false);
        barChart2.animateY(2000);
        barChart2.invalidate();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        dialog.getWindow().setDimAmount(0.90f);
        dialog.getWindow().getDecorView().setBackground(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }
}