package com.mobitech.speachtotext.textToSpeech.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.chibde.visualizer.BarVisualizer;
import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.TimedMode.Timer_Countdown;
import com.mobitech.speachtotext.textToSpeech.activities.TextToSpeechActivity;
import com.mobitech.speachtotext.textToSpeech.constants.URLS;
import com.mobitech.speachtotext.textToSpeech.sharedPref.UserSettings;
import com.mobitech.speachtotext.textToSpeech.utils.UtilityFunctions;
import com.mobitech.speachtotext.textToSpeech.utils.WavAudioRecorder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.mobitech.speachtotext.textToSpeech.constants.URLS.FILE_UPLOADER;

public class Record_Fragment extends Fragment implements View.OnClickListener {

    private static final String TAG = Record_Fragment.class.getSimpleName();
    TextView textview1;
    TextView textview;
    private NavController navController;
    private ImageButton listBtn;
    private RelativeLayout rl_timer;
    private Button btn_upload_to_server;
    private ImageButton recordBtn;
    private TextView filenameText;
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

    private long timeCountInMilliSeconds = 1 * 60000;

    public enum TimerStatus {
        STARTED,
        STOPPED
    }
    private Timer_Countdown.TimerStatus timerStatus = Timer_Countdown.TimerStatus.STOPPED;
    private ProgressBar progressBarCircle;
    private TextView textViewTime;
    private CountDownTimer countDownTimer;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record_, container, false);
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Intitialize Variables
        navController = Navigation.findNavController(view);
        /*SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("id","1" ); // Storing string
        editor.commit();*/
        listBtn = view.findViewById(R.id.record_list_btn);
        btn_upload_to_server = view.findViewById(R.id.btn_upload_to_server);
        recordBtn = view.findViewById(R.id.record_btn);
        timer = view.findViewById(R.id.record_timer);
        filenameText = view.findViewById(R.id.record_filename);
        textview1 = view.findViewById(R.id.record_filename);
        rl_timer = view.findViewById(R.id.rl_timer_record);
        textview = view.findViewById(R.id.speechtext);
        progressBar = view.findViewById(R.id.progressBar1);
        progressBarCircle =view.findViewById(R.id.progressBarCircle);
        textViewTime = view.findViewById(R.id.textViewTime);

        progressBar.setVisibility(View.INVISIBLE);


        userSettings= UserSettings.getInstance(getActivity());
        /* Setting up on click listener
           - Class must implement 'View.OnClickListener' and override 'onClick' method
         */
        listBtn.setOnClickListener(this);
        btn_upload_to_server.setOnClickListener(this);
        recordBtn.setOnClickListener(this);
        if (userSettings.getBoolean("IsFreeStyle")) {
            rl_timer.setVisibility(View.INVISIBLE);
        }else{
            rl_timer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_upload_to_server:
                if (isRecording) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setPositiveButton("NO", (dialog, which) -> {
                        isRecording = false;

                        recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                        textview1.setText("");
                        textview.setText(" ");
                        stopRecording();
                        UploadingFileToServer(recordPath + "/" + recordFile2);
                    });
                    alertDialog.setNegativeButton("YES", null);
                    alertDialog.setTitle("Confirmation");
                    alertDialog.setMessage("Do you want to record more?");
                    alertDialog.create().show();
                } else {
                    Toast.makeText(getActivity(), "Please start recording for uploading", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.record_list_btn:
                /*
                Navigation Controller
                Part of Android Jetpack, used for navigation between both fragments
                 */

                /*if (isRecording) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isRecording = false;

                            recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                            textview1.setText("Tap to Resume Recording..");
                            textview.setText(" ");
                            stopRecording();
                            UploadingFileToServer(recordPath + "/" + recordFile2);
                        }
                    });
                    alertDialog.setNegativeButton("CANCEL", null);
                    alertDialog.setTitle("Audio Still recording");
                    alertDialog.setMessage("Are you sure, you want to stop the recording?");
                    alertDialog.create().show();
                } else {
                }*/
                if (isRecording){

                    isRecording = false;

                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                    textview1.setText("Tap to Restart Recording..");
                    textview.setText(" ");
                    stopRecording();
                }

                userSettings.set(URLS.LIST_FOR_SPEECHES,URLS.GET_FILE_LIST);
                navController.navigate(R.id.action_record_Fragment_to_audioList_Fragment);
                break;

            case R.id.record_btn:
               /* if (WavAudioRecorder.State.INITIALIZING == mRecorder.getState()) {

                    startRecording();
                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording, null));
                    textview1.setText("Keep speaking.. \n you're doing better");
                    textview.setText("");
                    isRecording = true;
                }else if (WavAudioRecorder.State.ERROR == mRecorder.getState()) {
                    mRecorder.release();
                    mRecorder = WavAudioRecorder.getInstanse();
                    mRecorder.setOutputFile(recordFile2);
                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                    textview1.setText("Tap to Resume Recording..");
                    textview.setText(" ");
                    timer.stop();

                    isRecording = false;
                }else{
                    mRecorder.stop();
                    mRecorder.reset();
                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                    textview1.setText("Tap to Resume Recording..");
                    textview.setText(" ");
                    timer.stop();

                    isRecording = false;
                }*/

                ///new Timer().schedule(new TimerTask() {
                //
                //                @Override
                //                public void run() {
                //                    runOnUiThread(new Runnable() {
                //                        @Override
                //                        public void run() {
                //                            mediaRecorder.stop();
                //                            mediaRecorder.reset();
                //                            mediaRecorder.release();
                //                        }
                //                    });
                //
                //                }
                //
                //            }, 15000);
                if (isRecording) {
                    //Stop Recording
                    stopRecording();

                    // Change button image and set Recording state to false
                    recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
                    textview1.setText("Tap to Restart Recording..");
                    textview.setText(" ");
                    isRecording = false;
                } else {
                    //Check permission to record audio
                    if (checkPermissions()) {
                        //Start Recording
                        startRecording();

                        // Change button image and set Recording state to false
                        recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_recording, null));
                        textview1.setText("Keep speaking.. \n you're doing great..");
                        textview.setText("");
                        isRecording = true;
                    }
                }
                break;
        }


    }

    private void UploadingFileToServer(String FilePath2) {
//        File fileToPlay = new File(FilePath);
        File fileToPlay2 = new File(FilePath2);
        UtilityFunctions.showProgressDialog(getActivity(), true);
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        OkHttpClient client = new OkHttpClient();
        try {
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                    .addFormDataPart("file", fileToPlay.getName(),
//                            RequestBody.create(MediaType.parse(".3gp"), fileToPlay))
                    .addFormDataPart("file", fileToPlay2.getName(),
                            RequestBody.create(MediaType.parse(".wav"), fileToPlay2))
                    .addFormDataPart("userID", pref.getString("id","2"))
                    .addFormDataPart("filter", "")
                    .addFormDataPart("topic", "")
                    .build();


            Request request = new Request.Builder()
                    .url(FILE_UPLOADER)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {

                @Override
                public void onFailure(final okhttp3.Call call, final IOException e) {
                    // Handle the error
                    UtilityFunctions.hideProgressDialog(true);

                    requireActivity().runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );


                    Log.e(TAG, "onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(final okhttp3.Call call, okhttp3.Response response) throws IOException {


                    recordFile2="";
                    UtilityFunctions.hideProgressDialog(true);
                    if (response.code() == 200) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                        try {
//                                            Log.i(TAG, "run: "+data.getString(AppConstants.RESPONSE));
//                                            Toast.makeText(RegisteredActivity.this, data.getString(AppConstants.RESPONSE), Toast.LENGTH_SHORT).show();
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }

                                navController.navigate(R.id.action_record_Fragment_to_audioList_Fragment);
                            }
                        });
//                        try {
//
////                            JSONObject data = new JSONObject(response.body().string());
////                            if (data.getBoolean(AppConstants.HAS_RESPONSE)){
////                                Log.e(TAG, "onResponse: " + response.toString());
////
////                            }else{
////                                runOnUiThread(new Runnable() {
////                                    @Override
////                                    public void run() {
////                                        try {
////                                            Log.i(TAG, "onResponse: "+data.getString(AppConstants.MESSAGE));
////                                            Toast.makeText(RegisteredActivity.this, ""+data.getString(AppConstants.MESSAGE), Toast.LENGTH_SHORT).show();
////                                        } catch (JSONException e) {
////                                            e.printStackTrace();
////                                        }
////
////                                    }
////                                });
////                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
////                            Toast.makeText(RegisteredActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
                    }
                }
            });
        } catch (Exception ex) {
            // Handle the error
            UtilityFunctions.hideProgressDialog(true);

            Log.e(TAG, "uploadFile: " + ex.getMessage());
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
        if (rl_timer.getVisibility() == View.VISIBLE) {
            if (timerStatus != Timer_Countdown.TimerStatus.STOPPED)
            {// hiding the reset icon
//                imageViewReset.setVisibility(View.GONE);
                // changing stop icon to start icon
//                imageViewStartStop.setImageResource(R.drawable.timer_start);
                // making edit text editable
//                editTextMinute.setEnabled(true);
                // changing the timer status to stopped
                timerStatus = Timer_Countdown.TimerStatus.STOPPED;
                stopCountDownTimer();
            }
        }
        mRecorder = null;
    }

    private void stopRecordingUpdated() {
        isRecording = false;

        recordBtn.setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stopped, null));
        textview1.setText("Tap to Restart Recording..");
        textview.setText(" ");
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
        if (rl_timer.getVisibility() == View.VISIBLE) {
            if (timerStatus != Timer_Countdown.TimerStatus.STOPPED)
            {// hiding the reset icon
//                imageViewReset.setVisibility(View.GONE);
                // changing stop icon to start icon
//                imageViewStartStop.setImageResource(R.drawable.timer_start);
                // making edit text editable
//                editTextMinute.setEnabled(true);
                // changing the timer status to stopped
                timerStatus = Timer_Countdown.TimerStatus.STOPPED;
                stopCountDownTimer();
            }
        }
        Toast.makeText(getActivity(), "Uploading to server", Toast.LENGTH_SHORT).show();
        UploadingFileToServer(recordPath + "/" + recordFile2);
    }
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }
    private void startRecording() {
        //Start timer from 0
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        //Get app external directory path
        recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();

        //Get current date and time
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA);
        // Date now = new Date();
//        private static final String mRcordFilePath = Environment.getExternalStorageDirectory() + "/testwave.wav";
        //initialize filename variable with date and time at the end to ensure the new file wont overwrite previous file
//        recordFile = "Recording_" + formatter.format(now) + ".3gp";
        //   recordFile2 = "Recording_" + formatter.format(now) + ".wav";


//        recCount = recCount+1;
//        String recNo = Integer.toString(recCount);
//        recordFile2 = "Recording_" + recNo + ".wav";

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        String newStr= currentDateandTime.replace(":","_");
        recordFile2 = "Recording_" + (new Date().getTime()) + ".wav";

        Log.d(TAG, "startRecording: "+recordFile2);
        long t = (userSettings.getInt("time") * 60L);

        long result = TimeUnit.SECONDS.toMillis(t);
//        userSettings.getInt("time");

        //   filenameText.setText("Recording, File Name : " + recordFile);
        mRecorder = WavAudioRecorder.getInstanse();
        mRecorder.setOutputFile(recordPath + "/" + recordFile2);
        mRecorder.prepare();

        mRecorder.start();
        boolean isfreestyle=userSettings.getBoolean("IsFreeStyle");
        if ( !isfreestyle) {

            if (timerStatus == Timer_Countdown.TimerStatus.STOPPED) {


                // call to initialize the timer values
                setTimerValues(userSettings.getInt("time"));
                // call to initialize the progress bar values
                setProgressBarValues();
                // showing the reset icon

                // changing play icon to stop icon

                // making edit text not editable

                // changing the timer status to started
                timerStatus = Timer_Countdown.TimerStatus.STARTED;
                // call to start the count down timer
                startCountDownTimer();

            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopRecordingUpdated();
                }
            }, result);
        }
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

    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {

                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                // call to initialize the progress bar values
                setProgressBarValues();
                // hiding the reset icon
//                imageViewReset.setVisibility(View.GONE);
//                // changing stop icon to start icon
//                imageViewStartStop.setImageResource(R.drawable.timer_start);
//                // making edit text editable
//                editTextMinute.setEnabled(true);
                // changing the timer status to stopped
                timerStatus = Timer_Countdown.TimerStatus.STOPPED;
            }

        }.start();
        countDownTimer.start();
    }
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;


    }
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }
    private void setTimerValues(int time) {

//        if (!editTextMinute.getText().toString().isEmpty()) {
//            // fetching value from edit text and type cast to integer
//            time = Integer.parseInt(editTextMinute.getText().toString().trim());
//        } else {
//            // toast message to fill edit text
//            Toast.makeText(getApplicationContext(), getString(R.string.message_minutes), Toast.LENGTH_LONG).show();
//        }
        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = time * 60 * 1000;
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
        if (ActivityCompat.checkSelfPermission(getContext(), recordPermission) == PackageManager.PERMISSION_GRANTED) {
            //Permission Granted
            return true;
        } else {
            //Permission not granted, ask for permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }
}