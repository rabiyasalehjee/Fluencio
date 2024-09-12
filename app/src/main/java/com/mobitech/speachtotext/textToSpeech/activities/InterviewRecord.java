package com.mobitech.speachtotext.textToSpeech.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.adapter.AudioListAdapter;
import com.mobitech.speachtotext.textToSpeech.constants.URLS;
import com.mobitech.speachtotext.textToSpeech.fragments.AudioList_Fragment;
import com.mobitech.speachtotext.textToSpeech.listeners.ServerRequestListener;
import com.mobitech.speachtotext.textToSpeech.model.QuestionModel;
import com.mobitech.speachtotext.textToSpeech.model.RecordedFileModel;
import com.mobitech.speachtotext.textToSpeech.serverRequestHelper.MyServerRequest;
import com.mobitech.speachtotext.textToSpeech.sharedPref.UserSettings;
import com.mobitech.speachtotext.textToSpeech.utils.UtilityFunctions;
import com.mobitech.speachtotext.textToSpeech.utils.WavAudioRecorder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.mobitech.speachtotext.textToSpeech.constants.URLS.FILE_UPLOADER;
import static com.mobitech.speachtotext.textToSpeech.constants.URLS.UPLOAD_INTERVIEW_QUESTION;

public class InterviewRecord extends AppCompatActivity {


    TextView questTxt, nxtBtn, backBtn;
    int questLength, count=0;
    String[] questions;

    ImageView iv_instructions;
    String level="";
    ArrayList<QuestionModel> questionModelList=new ArrayList<>();
    ArrayList<QuestionModel> questionModelRandomList=new ArrayList<>();

    LinearLayout ll_main,ll_sub,ll_frag; private Button btn_upload_to_server;
    private ImageButton recordBtn;
    private TextView textview1;
    private String recordPath;
    private boolean isRecording = false;
    private final String recordPermission = Manifest.permission.RECORD_AUDIO;
    private final int PERMISSION_CODE = 21;
    private MediaRecorder mediaRecorder;
    public static String recordFile2="";
    private Chronometer timer;
    int recCount = 0;
    private ProgressBar progressBar;

    private WavAudioRecorder mRecorder;

    UserSettings userSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview_record);

        ll_sub  = findViewById(R.id.ll_sub_view);
        ll_frag  = findViewById(R.id.ll_fragment_view);

        userSettings =UserSettings.getInstance(InterviewRecord.this);
        level=userSettings.getString("level");
        questTxt = (TextView)findViewById(R.id.questionTxt);
        nxtBtn = (TextView)findViewById(R.id.nxtBtn);
        ll_main = findViewById(R.id.ll_main);
        iv_instructions = findViewById(R.id.iv_instructions);
        backBtn = (TextView)findViewById(R.id.backBtn);


        btn_upload_to_server = findViewById(R.id.btn_upload_to_server);
        recordBtn = findViewById(R.id.record_btn);
        timer = findViewById(R.id.record_timer);
//        filenameText = findViewById(R.id.record_filename);
        textview1 = findViewById(R.id.record_filename);
        progressBar = findViewById(R.id.progressBar1);

        progressBar.setVisibility(View.INVISIBLE);

        if (level.equals("") || level == null) {


        }else{
//            DummyList();
            GetQuestionList();
//            questLength = questions.length;
//        Toast.makeText(InterviewRecord.this,questLength,Toast.LENGTH_SHORT).show();

            recordBtn.setOnClickListener(v -> {
                if (isRecording) {
                    //Stop Recording
                    stopRecording();

                    // Change button image and set Recording state to false
                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                    textview1.setText("Tap to Restart Recording..");
//                    textview.setText(" ");
                    isRecording = false;
                } else {
                    //Check permission to record audio
                    if (checkPermissions()) {
                        //Start Recording
                        startRecording();

                        // Change button image and set Recording state to false
                        recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording, null));
                        textview1.setText("Keep speaking.. \n you're doing great..");
//                        textview.setText("");
                        isRecording = true;
                    }
                }
            });
            btn_upload_to_server.setOnClickListener(v -> {
                if (isRecording) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(InterviewRecord.this);
                    alertDialog.setPositiveButton("NO", (dialog, which) -> {
                        isRecording = false;

                        recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                        textview1.setText("");

                        stopRecording();

                        UploadingFileToServer(recordPath + "/" + recordFile2);

                    });
                    alertDialog.setNegativeButton("YES", null);
                    alertDialog.setTitle("Confirmation");
                    alertDialog.setMessage("Do you want to record more?");
                    alertDialog.create().show();
                } else {
                    Toast.makeText(InterviewRecord.this, "Please start recording for uploading", Toast.LENGTH_SHORT).show();
                }
            });
        }
//        questions = new String[]{"Briefly introduce yourself.", "What makes you unique?","If you could change one thing about your personality, what would it be?",
//                "Tell us about a stressful scenario in the past and how you handled it.","Describe a situation where you have used technical skills in your work."};


        /*if(count==questLength){
            nxtBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(InterviewRecord.this, "No More Questions", Toast.LENGTH_SHORT).show();
                }
            });
        }*/
    }

    private void DummyList(){
//        questions = new String[]{"Briefly introduce yourself.",
//                "What makes you unique?",
//                "If you could change one thing about your personality, what would it be?",
//                "Tell us about a stressful scenario in the past and how you handled it.",
//                "Describe a situation where you have used technical skills in your work."};

        if (questionModelRandomList.size()>0){
            questionModelRandomList.clear();
        }
        questionModelRandomList.add(new QuestionModel("Share your strengths related to the job. " +
                "You can also state that you are a quick learner and highly motivated individual.",
                "Briefly introduce yourself."));
        questionModelRandomList.add(new QuestionModel("Focus more on talking about your achievements and learning and keep it short.",
                "Tell us about yourself."));
        questionModelRandomList.add(new QuestionModel("Share your strengths related to the job. ",
                "What makes you unique?."));
        questionModelRandomList.add(new QuestionModel(
                "You can also state that you are a quick learner and highly motivated individual.",
                "If you could change one thing about your personality, what would it be?"));
        questionModelRandomList.add(new QuestionModel("Try stating some positive attributes of the company. ",
                "Why do you want to work in our company?"));
        questionModelRandomList.add(new QuestionModel("Be sure to let them know that you have made mistakes, you have learned from them to become better.",
                "Do you have any regrets?"));


    }

    private void GetQuestionList() {
        MyServerRequest myServerRequest = new MyServerRequest(InterviewRecord.this, URLS.GET_INTERVIEW_LIST,
                new ServerRequestListener() {
                    @Override
                    public void onPreResponse() {
                        UtilityFunctions.showProgressDialog(InterviewRecord.this, true);
                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onPostResponse(JSONObject jsonResponse, int requestCode) {

                        UtilityFunctions.hideProgressDialog(true);

                        try {
                            JSONArray response= jsonResponse.getJSONArray("respone");
                            if (questionModelList.size() > 0) {
                                questionModelList.clear();
                            }
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = response.getJSONObject(i);
                                QuestionModel questionModel = new QuestionModel();

                                if(level.equals("basic")){

                                if(jsonObject.getString("Category").equals("Basic")){
                                    questionModel.setCategory(jsonObject.getString("Category"));
                                    questionModel.setInstruction(jsonObject.getString("instruction"));

                                        questionModel.setQuestion_id(String.valueOf(jsonObject.getInt("quest_id")));
                                    questionModel.setQuestion(jsonObject.getString("question"));
                                    questionModel.setSuggested_answer(jsonObject.getString("suggested_answer"));

                                    questionModelList.add(questionModel);

                                }

                                } else if (level.equals("advance")) {
                                    if(jsonObject.getString("Category").equals("Advanced")){
                                        questionModel.setCategory(jsonObject.getString("Category"));
                                        questionModel.setInstruction(jsonObject.getString("instruction"));

                                        questionModel.setQuestion_id(String.valueOf(jsonObject.getInt("quest_id")));
                                        questionModel.setQuestion(jsonObject.getString("question"));
                                        questionModel.setSuggested_answer(jsonObject.getString("suggested_answer"));

                                        questionModelList.add(questionModel);

                                    }


                                }else if (level.equals("personal")) {


                                    if (!jsonObject.getString("Category").equals("Advanced") ||
                                            !jsonObject.getString("Category").equals("Basic")) {
                                        questionModel.setCategory(jsonObject.getString("Category"));
                                        questionModel.setInstruction(jsonObject.getString("instruction"));
                                        questionModel.setQuestion_id(String.valueOf(jsonObject.getInt("quest_id")));
                                        questionModel.setQuestion(jsonObject.getString("question"));
                                        questionModel.setSuggested_answer(jsonObject.getString("suggested_answer"));
                                        questionModelList.add(questionModel);

                                    }

                                }



                            }
                            if (questionModelList.size() != 0) {
                                if (questionModelRandomList.size() > 0) {
                                    questionModelRandomList.clear();
                                }
                                Random random = new Random();
                                ArrayList<Integer> arrayList = new ArrayList<Integer>();

                                while (arrayList.size() < 5) { // how many numbers u need - it will 6
                                    int a = random.nextInt(questionModelList.size()-1); // this will give numbers between 1 and 50.

                                    if (!arrayList.contains(a)) {
                                        arrayList.add(a);
                                    }
                                }
                                for (int i = 0; i < arrayList.size(); i++) {
//                                    int jr= ThreadLocalRandom.current().ints(0, 100).distinct().limit(5);
                                    int j=new Random().nextInt(questionModelList.size()-1);
                                    questionModelRandomList.add(questionModelList.get(arrayList.get(i)));
                                }
                                if (questionModelRandomList.size() != 0) {
                                    questTxt.setText(questionModelRandomList.get(count).getQuestion());

                                    for(int i=0; i<questionModelRandomList.size(); i++){
                                        backBtn.setOnClickListener(v -> {
                                            count--;
                                            questTxt.setText(questionModelRandomList.get(count).getQuestion());

                                            if (count == 0) {
                                                backBtn.setVisibility(View.GONE);

                                            }
//                                            else if (count == -1) {
//                                                count = 0;
//                                            } else{
//                                                questTxt.setText(questionModelRandomList.get(count).getQuestion());
//                                            }

                                        });

                                        iv_instructions.setOnClickListener(v -> {
                                            new AlertDialog.Builder(InterviewRecord.this)
                                                    .setTitle("Instructions")
                                                    .setMessage(questionModelRandomList.get(count).getInstruction())
                                                    .setCancelable(true)
                                                    .setPositiveButton("Cancel", (dialog,which) -> {
                                                        dialog.dismiss();
                                                    }).show();
                                        });


                                        nxtBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                count++;

                                                if(count<questionModelRandomList.size()){
                                                    questTxt.setText(questionModelRandomList.get(count).getQuestion());
                                                    backBtn.setVisibility(View.VISIBLE);
                                                }
                                                else{
                                                    Toast.makeText(InterviewRecord.this, "No More Questions", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });


                                    }
                                }
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

    private void UploadingFileToServer(String FilePath2) {
//        File fileToPlay = new File(FilePath);
        File fileToPlay2 = new File(FilePath2);
        UtilityFunctions.showProgressDialog(InterviewRecord.this, true);

        OkHttpClient client = new OkHttpClient();
        StringBuilder Questions_ids= new StringBuilder();
        try {
            for (int i = 0; i < questionModelRandomList.size(); i++) {

                if (i == questionModelRandomList.size() - 1) {
                    Questions_ids.append(questionModelRandomList.get(i).getQuestion_id());
                }else{
                    Questions_ids.append(questionModelRandomList.get(i).getQuestion_id()).append(",");
                }
            }
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
//            String q_ids=Questions_ids.toString().;
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                    .addFormDataPart("file", fileToPlay.getName(),
//                            RequestBody.create(MediaType.parse(".3gp"), fileToPlay))
                    .addFormDataPart("file", fileToPlay2.getName(),
                            RequestBody.create(MediaType.parse(".wav"), fileToPlay2))
                    .addFormDataPart("questionIDs", Questions_ids.toString())
                    .addFormDataPart("userID", pref.getString("id","2"))
                    .addFormDataPart("filter", "")
                    .addFormDataPart("topic", "")
                    .build();


            Request request = new Request.Builder()
                    .url(UPLOAD_INTERVIEW_QUESTION)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {

                @Override
                public void onFailure(final okhttp3.Call call, final IOException e) {
                    // Handle the error
                    UtilityFunctions.hideProgressDialog(true);

                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(InterviewRecord.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );


                    Log.e("TAG", "onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(final okhttp3.Call call, okhttp3.Response response) throws IOException {


                    recordFile2="";
                    UtilityFunctions.hideProgressDialog(true);
                    if (response.code() == 200) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                ll_sub.setVisibility(View.GONE);
                                ll_frag.setVisibility(View.VISIBLE);


                                userSettings.set(URLS.LIST_FOR_SPEECHES,URLS.GET_ALL_INTERVIEW_SPEECHES);
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.interview_nav_view, new AudioList_Fragment())
                                        .commit();
//                                navigationView.setVisibility(View.VISIBLE);
//                                navController.navigate(R.id.action_record_Fragment_to_audioList_Fragment);
//                                onBackPressed();
                            }
                        });

                    }
                }
            });
        } catch (Exception ex) {
            // Handle the error
            UtilityFunctions.hideProgressDialog(true);

            Log.e("TAG", "uploadFile: " + ex.getMessage());
        }
    }
    @Override
    public void onStop() {



        super.onStop();
        /*if (isRecording) {
            stopRecording();
        }*/
        if (null != mRecorder) {
            mRecorder.release();
        }
    }

    private void stopRecording() {
        //Stop Timer, very obvious
        timer.stop();

        //Change text on page to file saved
        // filenameText.setText("Recording Stopped, File Saved : " + recordFile);

        //Stop media recorder and set it to null for further use to record new audio
//        mediaRecorder.stop();
//        mediaRecorder.release();
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ll_frag.setVisibility(View.GONE);
        ll_sub.setVisibility(View.VISIBLE);
    }

    private void startRecording() {
        //Start timer from 0
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        //Get app external directory path
        recordPath = getExternalFilesDir("/").getAbsolutePath();

        //Get current date and time
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        // Date now = new Date();
//        private static final String mRcordFilePath = Environment.getExternalStorageDirectory() + "/testwave.wav";
        //initialize filename variable with date and time at the end to ensure the new file wont overwrite previous file
//        recordFile = "Recording_" + formatter.format(now) + ".3gp";
        //   recordFile2 = "Recording_" + formatter.format(now) + ".wav";
        recCount = recCount+1;
        String recNo = Integer.toString(recCount);
        recordFile2 = "Recording_" + (new Date().getTime()) + ".wav";


        long t = (userSettings.getInt("time") * 60L);

        long result = TimeUnit.SECONDS.toMillis(t);
//        userSettings.getInt("time");

        //   filenameText.setText("Recording, File Name : " + recordFile);
        mRecorder = WavAudioRecorder.getInstanse();
        mRecorder.setOutputFile(recordPath + "/" + recordFile2);
        mRecorder.prepare();

        mRecorder.start();

//        mRecorder.set
        //Setup Media Recorder for recording
//        mediaRecorder = new MediaRecorder();
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
//        mediaRecorder.setOutputFile(recordPath + "/" + recordFile2);
////        mediaRecorder.setOutputFile(recordPath + "/" + recordFile2);
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//
//        try {
//            mediaRecorder.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //Start Recording
//        mediaRecorder.start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mRecorder) {
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.INVISIBLE);
            mRecorder.release();
        }
    }

    private boolean checkPermissions() {
        //Check permission
        if (ActivityCompat.checkSelfPermission(InterviewRecord.this, recordPermission) == PackageManager.PERMISSION_GRANTED) {
            //Permission Granted
            return true;
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(InterviewRecord.this, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }
}