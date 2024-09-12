package com.mobitech.speachtotext.textToSpeech.improvement_exercise;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobitech.speachtotext.Login;
import com.mobitech.speachtotext.MainActivity;
import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.NavBar.NavDrawerActivity;
import com.mobitech.speachtotext.textToSpeech.activities.InterviewRecord;
import com.mobitech.speachtotext.textToSpeech.activities.RecordActivity;
import com.mobitech.speachtotext.textToSpeech.activities.TextToSpeechActivity;
import com.mobitech.speachtotext.textToSpeech.adapter.AudioListAdapter;
import com.mobitech.speachtotext.textToSpeech.constants.URLS;
import com.mobitech.speachtotext.textToSpeech.listeners.ServerRequestListener;
import com.mobitech.speachtotext.textToSpeech.model.RecordedFileModel;
import com.mobitech.speachtotext.textToSpeech.model.TopicModel;
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
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.mobitech.speachtotext.textToSpeech.constants.URLS.UPLOAD_INTERVIEW_QUESTION;
import static com.mobitech.speachtotext.textToSpeech.constants.URLS.UPLOAD_TOPIC;

public class exercise_flipcard extends AppCompatActivity {
    Button goback;
    ImageView iv_back,iv_front;

    LinearLayout ll_main; private Button btn_upload_to_server;
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
    ArrayList<TopicModel> questionModelRandomList;
    private WavAudioRecorder mRecorder;

    ArrayList<TopicModel>allFiles=new ArrayList<>();
    UserSettings userSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_flipcard);
        userSettings = new UserSettings(exercise_flipcard.this);
        goback = findViewById(R.id.back_btn);
        iv_back = findViewById(R.id.iv_back);
        iv_front = findViewById(R.id.iv_front);

        btn_upload_to_server = findViewById(R.id.btn_upload_to_server);
        recordBtn = findViewById(R.id.record_btn);
        timer = findViewById(R.id.record_timer);
//        filenameText = findViewById(R.id.record_filename);
        textview1 = findViewById(R.id.record_filename);
        progressBar = findViewById(R.id.progressBar1);

        progressBar.setVisibility(View.INVISIBLE);
        GetFIleListApi();
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(exercise_flipcard.this);
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
                Toast.makeText(exercise_flipcard.this, "Please start recording for uploading", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void goback (View v){

        TextView goback = (TextView) findViewById(R.id.back_btn);
        Intent intent = new Intent(getApplicationContext(), NavDrawerActivity.class);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

//
    private void GetFIleListApi() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("id", rlogin.getId()); // Storing string
//        editor.putString("name", rlogin.getName()); // Storing string
//        editor.putString("phone", rlogin.getPhone()); // Storing string
//        editor.commit();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        Log.i("cheking URL", "GetFIleListApi: " + URLS.GET_ALL_TOPICS_LIST);
        MyServerRequest myServerRequest = new MyServerRequest(exercise_flipcard.this, URLS.GET_ALL_TOPICS_LIST,
                new ServerRequestListener() {
                    @Override
                    public void onPreResponse() {
                        UtilityFunctions.showProgressDialog(exercise_flipcard.this, true);
                    }

                    @Override
                    public void onPostResponse(JSONObject jsonResponse, int requestCode) {

                        UtilityFunctions.hideProgressDialog(true);

                        try {
                            JSONArray response= jsonResponse.getJSONArray("respone");
                            for (int i = 0; i < response.length(); i++) {
                                //"ID": 2,
                                //      "topicHintfileRoute": "/getTopicFiles?filename=topicHint_2_img.PNG",
                                //      "topicfileRoute"
                                JSONObject jsonObject = response.getJSONObject(i);
                                allFiles.add(new TopicModel(
                                        jsonObject.getString("ID"),
                                        jsonObject.getString("topicHintfileRoute"),
                                        jsonObject.getString("topicfileRoute")
                                ));
                            }
                            Random random = new Random();

                            questionModelRandomList= new ArrayList<>();
                            int j=new Random().nextInt(allFiles.size()-1);
                            questionModelRandomList.add(allFiles.get(j));
//                            RequestOptions options = new RequestOptions()
//                                    .centerCrop()
//                                    .placeholder(R.mipmap.ic_launcher_round)
//                                    .error(R.mipmap.ic_launcher_round);



                            Glide.with(exercise_flipcard.this).load(URLS.BASEURL+"/"+questionModelRandomList.get(0).getTopic_img_url()).into(iv_front);
                            Glide.with(exercise_flipcard.this).load(URLS.BASEURL+"/"+questionModelRandomList.get(0).getTopichint_img_url()).into(iv_back);
                        } catch (JSONException e) {
                            Log.d("TAG", "onPostResponse: "+e.getMessage());
                            e.printStackTrace();
                        }


                    }
                });
        myServerRequest.sendGetRequest();
    }

    private void UploadingFileToServer(String FilePath2) {
//        File fileToPlay = new File(FilePath);
        File fileToPlay2 = new File(FilePath2);
        UtilityFunctions.showProgressDialog(exercise_flipcard.this, true);

        OkHttpClient client = new OkHttpClient();

        try {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
//            String q_ids=Questions_ids.toString().;
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                    .addFormDataPart("file", fileToPlay.getName(),
//                            RequestBody.create(MediaType.parse(".3gp"), fileToPlay))
                    .addFormDataPart("file", fileToPlay2.getName(),
                            RequestBody.create(MediaType.parse(".wav"), fileToPlay2))
                    .addFormDataPart("topicID", questionModelRandomList.get(0).getTopic_id())
                    .addFormDataPart("userID", pref.getString("id","2"))
                    .addFormDataPart("filter", "")
                    .addFormDataPart("topic", "")
                    .build();


            Request request = new Request.Builder()
                    .url(UPLOAD_TOPIC)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {

                @Override
                public void onFailure(final okhttp3.Call call, final IOException e) {
                    // Handle the error

                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            UtilityFunctions.hideProgressDialog(true);
//                                            stopRecordingUpdated();
                                            userSettings.set(URLS.LIST_FOR_SPEECHES,URLS.GET_All_TOPIC_SPEECHES);
                                            onBackPressed();
                                        }
                                    }, 5000);
//                                    Toast.makeText(exercise_flipcard.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                userSettings.set(URLS.LIST_FOR_SPEECHES,URLS.GET_All_TOPIC_SPEECHES);
                                onBackPressed();
                            }
                        });

                    }
                }
            });
        } catch (Exception ex) {
            // Handle the error
            UtilityFunctions.hideProgressDialog(true);

//            Log.e("TAG", "uploadFile: " + ex.getMessage());
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


//        long t = (userSettings.getInt("time") * 60L);

//        long result = TimeUnit.SECONDS.toMillis(t);
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
        if (ActivityCompat.checkSelfPermission(exercise_flipcard.this, recordPermission) == PackageManager.PERMISSION_GRANTED) {
            //Permission Granted
            return true;
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(exercise_flipcard.this, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }
}