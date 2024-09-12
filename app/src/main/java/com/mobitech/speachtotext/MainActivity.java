package com.mobitech.speachtotext;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mobitech.speachtotext.textToSpeech.activities.AssignmentsActivity;
import com.mobitech.speachtotext.textToSpeech.activities.TextToSpeechActivity;
import com.mobitech.speachtotext.textToSpeech.constants.URLS;
import com.mobitech.speachtotext.textToSpeech.fragments.AudioList_Fragment;
import com.mobitech.speachtotext.textToSpeech.improvement_exercise.exercise_flipcard;
import com.mobitech.speachtotext.textToSpeech.model.RecordedFileModel;
import com.mobitech.speachtotext.textToSpeech.sharedPref.UserSettings;
import com.mobitech.speachtotext.textToSpeech.utils.DownloadingTask;
import com.mobitech.speachtotext.textToSpeech.utils.UtilityFunctions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mobitech.speachtotext.textToSpeech.constants.URLS.FILE_UPLOADER;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements RecognitionListener {



    private static final String TAG = MainActivity.class.getSimpleName();
    private ToggleButton toggleButton;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    TextView tv_file_name, tv_pitch, tv_pace, tv_word, tv_duration,tv_result, s, tv_filler_count;
    LinearLayout ll_main,mic;
    ImageView iv_back;
    Button btn_play;
    Button btn_suggestion;
    Button btn_assignments;
    String selectedFilePath, speechText;
    ImageView xd;
    TextView tm, pac;
    private ConstraintLayout playerSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;
    private File fileToPlay = null;
    int x = 0, filler_count=0, filWd=0;
    CountDownTimer yourCountDownTimer;
    TextView word;
    int filter = 0;
    TextView fil;
    float pace, pitch;
    //UI Elements
    private ImageButton playBtn;
    private TextView playerHeader;
    private TextView playerFilename;
    private SeekBar playerSeekbar;
    private Handler seekbarHandler;
    private Runnable updateSeekbar;
    private UserSettings userSettings;
    private RecordedFileModel recordedFileModel;
    Dialog dialog;

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_suggestion = findViewById(R.id.btn_suggestion);
        btn_assignments = findViewById(R.id.btn_assignments);


        InitUI();
        xd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request();
            }
        });

        btn_assignments.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AssignmentsActivity.class));
        });
        //To move back to audio_list_fragment

        final AudioList_Fragment audioList_fragment = new AudioList_Fragment();
        final FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_layout,audioList_fragment);
                fragmentTransaction.commit();

            }
        });


        //Initialize dialog for result info
        dialog = new Dialog(MainActivity.this);

        progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");
        recognizerIntent.putExtra("android.speech.extra.GET_AUDIO", true);


        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        s = (TextView) findViewById(R.id.s);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {

                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    // secret parameters that when added provide audio url in the result
                    intent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");
                    intent.putExtra("android.speech.extra.GET_AUDIO", true);

                    startActivityForResult(intent, 10);

                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);
                    //  speech.startListening(recognizerIntent);
                    s.setText("Stop");
                    yourCountDownTimer = new CountDownTimer(60000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            x++;
                            filter = 0;
                            // Log.d(TAG, "onTick: ");
                        }

                        public void onFinish() {
                        }

                    }.start();


                }
            }

        });
        if (recordedFileModel != null) {
//            fileToPlay = recordedFileModel.getFile();

            String RecordPath= getExternalFilesDir("/").getAbsolutePath();
            File file =new File(RecordPath+ "/" +recordedFileModel.getFile_name());
            if (file.exists()) {
                fileToPlay=file;
            }else{
                try {
                    new DownloadingTask(MainActivity.this, URLS.BASEURL+recordedFileModel.getFile_path(),
                            recordedFileModel.getFile_name(),RecordPath).execute().get();
                    File downloadedFile=DownloadingTask.outputFile;
                    fileToPlay=downloadedFile;
                    DownloadingTask.outputFile=null;
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

            }
            tv_file_name.setText(recordedFileModel.getFile_name());
            tv_pitch.setText(recordedFileModel.getPitch());
            pitch = Float.parseFloat(recordedFileModel.getPitch());
            if (pitch >= 90 && pitch <= 120){
                tv_pitch.setText(pitch+"   Excellent");
            }
            else if(pitch >120 && pitch <=140){
                tv_pitch.setText(pitch+"   Slightly high");
            }
            else if(pitch >=80 && pitch <90){
                tv_pitch.setText(pitch+"   Slightly low");
            }
            else if(pitch >140){
                tv_pitch.setText(pitch+"   Too high");
            }
            else if(pitch <80){
                tv_pitch.setText(pitch+"   Too low");
            }
            else{
                tv_pitch.setText(pitch+"   Poor");
            }


            //tv_pace.setText(recordedFileModel.getPace()+"/min");
            //Working for pace
            pace = Float.parseFloat(recordedFileModel.getPace());
            if (pace >=100 && pace<=120){
                tv_pace.setText(pace+"/min"+"   Excellent job");
            }
            else if(pace >120 && pace<=140){
                tv_pace.setText(pace+"/min"+"   Slightly fast");
            }
            else if(pace >=80 && pace<100){
                tv_pace.setText(pace+"/min"+"   Slightly slow");
            }
            else if(pace > 140){
                tv_pace.setText(pace+"/min"+"   Too fast");
            }
            else if (pace < 80)
            {
                tv_pace.setText(pace+"/min"+"    Too slow");
            }
            else {
                tv_pace.setText(pace+"/min"+"    Poor");
            }

            //Working for pitch

            tv_duration.setText(recordedFileModel.getDuration());
            tv_word.setText(recordedFileModel.getNoOFTotalWords());
            //tv_result.setText(recordedFileModel.getSpeechText());
            speechText = recordedFileModel.getSpeechText();
            speechText = highLightText(speechText, "basically");
            filler_count = countMatches(speechText,"basically");
            filWd += filler_count;

            speechText = highLightText(speechText, "clearly");
            filler_count = countMatches(speechText,"clearly");
            filWd += filler_count;
            speechText = highLightText(speechText, "seriously");
            filler_count = countMatches(speechText,"seriously");
            filWd += filler_count;
            speechText = highLightText(speechText, "literally");
            filler_count = countMatches(speechText,"literally");
            filWd += filler_count;
            speechText = highLightText(speechText, "you see");
            filler_count = countMatches(speechText,"you see");
            filWd += filler_count;
            speechText = highLightText(speechText, "okay");
            filler_count = countMatches(speechText,"okay");
            filWd += filler_count;
            speechText = highLightText(speechText, "I mean");
            filler_count = countMatches(speechText,"I mean");
            filWd += filler_count;
            speechText = highLightText(speechText, "you know");
            filler_count = countMatches(speechText,"you know");
            filWd += filler_count;
            speechText = highLightText(speechText, "i guess");
            filler_count = countMatches(speechText,"i guess");
            filWd += filler_count;
            speechText = highLightText(speechText, "i suppose");
            filler_count = countMatches(speechText,"i suppose");
            filWd += filler_count;

            String filCount = String.valueOf(filWd);
            //Toast.makeText(getApplicationContext(),filCount,Toast.LENGTH_SHORT).show();
            tv_filler_count.setText(filCount);

            tv_pace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPaceDialog(pace);
                }
            });

            tv_pitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPitchDialog(pitch);
                }
            });

        }



        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //We cant do anything here for this app
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    pauseAudio();
                } else {
                    if (fileToPlay != null&&mediaPlayer==null) {
                        playAudio(fileToPlay);
                    }else{
                        resumeAudio();
                    }
                }
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    pauseAudio();
                } else {
                    if (fileToPlay != null) {
                        playAudio(fileToPlay);
//                        resumeAudio();
                    }
                }
            }
        });


        playerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                pauseAudio();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mediaPlayer.seekTo(progress);
                resumeAudio();
            }
        });



        tv_file_name.setText(recordedFileModel.getFile_name());



    }

    //Result dialog function for pace
    private void openPaceDialog(float pace){
        dialog.setContentView(R.layout.result_info_dialog_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView pace_des = dialog.findViewById(R.id.result_des);
        TextView pace_info = dialog.findViewById(R.id.result_info);
        if (pace >=100 && pace<=120){

            pace_info.setText("Great!");
            pace_des.setText("Normal pace is around 100 words/min to 120 words/min \n You are speaking at a great pace \n Keep it up");
        }
        else if(pace >120 && pace<=140){

            pace_info.setText("Good!");
            pace_des.setText("Normal pace is around 100 words/min to 120 words/min \n You are speaking slightly fast \n Speak a little slower");
        }
        else if(pace >=80 && pace<100){

            pace_info.setText("Average!");
            pace_des.setText("Normal pace is around 100 words/min to 120 words/min \n You are speaking slightly slow \n Speak a little faster");
        }
        else if(pace > 140){

            pace_info.setText("Too fast!");
            pace_des.setText("Normal pace is around 100 words/min to 120 words/min \n You are speaking too fast \n Practice speaking slowly");
        }
        else if (pace < 80)
        {

            pace_info.setText("Too slow!");
            pace_des.setText("Normal pace is around 100 words/min to 120 words/min \n You are speaking too slow \n Practice speaking quickly");
        }
        else {

            pace_info.setText("Poor performance!");
            pace_des.setText("Normal pace is around 100 words/min to 120 words/min \n Your performance is poor \n Try improving");
        }

        Button dialog_btn_ok = dialog.findViewById(R.id.dialog_btn_ok);
        dialog_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //Result dialog function for Pitch
    private void openPitchDialog(float pace){
        dialog.setContentView(R.layout.result_info_dialog_box);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView pitch_des = dialog.findViewById(R.id.result_des);
        TextView pitch_info = dialog.findViewById(R.id.result_info);
        if (pitch >= 90 && pitch <= 120){
            pitch_info.setText("Great job");
            pitch_des.setText("Your pitch is almost perfect.\n Keep it up");
        }
        else if(pitch >120 && pitch <=140){
            pitch_info.setText("Nice");
            pitch_des.setText("Your pitch is slightly high.\n Try to lower a bit");
        }
        else if(pitch >=80 && pitch <90){
            pitch_info.setText("Good");
            pitch_des.setText("Your pitch is slightly low.\n Try to increase a bit");
        }
        else if(pitch >140){
            pitch_info.setText("Too high");
            pitch_des.setText("Your pitch is too high.\n Try improving");
        }
        else if(pitch <80){
            pitch_info.setText("Too low");
            pitch_des.setText("Your pitch is too low.\n Try improving");
        }
        else{
            pitch_info.setText("Poor");
            pitch_des.setText("Your pitch is out of range.\n Please try again");
        }

        Button dialog_btn_ok = dialog.findViewById(R.id.dialog_btn_ok);
        dialog_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private String highLightText(String speechText, String like) {
        String textToHighlight = like;
        String replaceWith = "<span style='background-color:yellow'>" + textToHighlight + "</span>";
        String modifiedText = speechText.replaceAll(textToHighlight, replaceWith);
        speechText = modifiedText;
        tv_result.setText(Html.fromHtml(modifiedText));
        return speechText;
    }

    /* Checks if a string is empty ("") or null. */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /* Counts how many times the substring appears in the larger string. */
    public static int countMatches(String text, String str)
    {
        if (isEmpty(text) || isEmpty(str)) {
            return 0;
        }

        Matcher matcher = Pattern.compile(str).matcher(text);

        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {


        tv_result.append(buffer.toString());
        Log.i("qwliejioqwueioqwue", "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(true);
        toggleButton.setChecked(false);
        s.setText("Tap on mic  and start \n speaking.");
        mic.setVisibility(View.GONE);
        ll_main.setVisibility(View.VISIBLE);
        yourCountDownTimer.cancel();
        tm.setText("" + x);
        pac.setText("" + ((x * 1000) / (Integer.parseInt(word.getText().toString()))) + "/mili");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this, TextToSpeechActivity.class));
        userSettings.clear();
        userSettings.clearAllPreferences();
        finish();
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
//        tv_result.setText(errorMessage);
        toggleButton.setChecked(false);
        s.setText("Tap on mic  and start \n speaking.");
        yourCountDownTimer.cancel();
        mic.setVisibility(View.GONE);
        ll_main.setVisibility(View.VISIBLE);
        tm.setText("" + x);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {

        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";


        String s = matches.get(0);
        String[] words = s.split("\\W+");
//        tv_result.setText(text);
//        tv_result.append("\n");
//        tv_result.setText(s);
        word.setText("" + words.length);
        setHighLightedText(tv_result, "like");
        setHighLightedText(tv_result, "Um");
        setHighLightedText(tv_result, "time");
    }

    public void setHighLightedText(TextView tv, String textToHighlight) {
        String tvt = tv.getText().toString();
        int ofe = tvt.indexOf(textToHighlight, 0);
        Spannable wordToSpan = new SpannableString(tv.getText());
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToHighlight, ofs);
            if (ofe == -1)
                break;
            else {
                // set color here
                filter++;
                fil.setText("" + filter);
                wordToSpan.setSpan(new BackgroundColorSpan(0xFFFFFF00), ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(wordToSpan, TextView.BufferType.SPANNABLE);
            }
        }
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isPlaying) {
            stopAudio();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        userSettings.clear();
//        userSettings.clearAllPreferences();
    }

    private void decodeAudio(String base64AudioData, File fileName, String path, MediaPlayer mp) {

        try {

            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(Base64.decode(base64AudioData.getBytes(), Base64.DEFAULT));
            fos.close();

            try {

                mp = new MediaPlayer();
                mp.setDataSource(path);
                mp.prepare();
                mp.start();

            } catch (Exception e) {

                //  DiagnosticHelper.writeException(e);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        progressBar.setVisibility(View.INVISIBLE);
//
//        toggleButton.setChecked(false);
        progressBar.setVisibility(View.INVISIBLE);

//        toggleButton.setChecked(false);
        s.setText("Tap on mic  andstart \n speaking.");
        mic.setVisibility(View.GONE);

        ll_main.setVisibility(View.VISIBLE);

        if (data != null) {

        }
        Bundle bundle = data.getExtras();
        ArrayList<String> matches = bundle.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
        // the recording url is in getData:
        Uri audioUri = data.getData();
        ContentResolver contentResolver = getContentResolver();

        encodeAudio(audioUri.toString());
        InputStream filestream = null;
        try {
            filestream = contentResolver.openInputStream(audioUri);

            //String s=filestream();

            Log.d("heched", "onActivityResult: "+s);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        String xo=   savefile(audioUri);
        selectedFilePath=audioUri.toString();
        Log.d("xossss", "onActivityResult: "+xo);








        Log.d("asdjkjasdlkjweiriowerio", "onActivityResult: "+audioUri.getPath());

        File f = new File(Environment.getExternalStorageDirectory() + "/somedir");
        if(f.isDirectory()) {
            //Write code for the folder exist condition

        }else{

            // create a File object for the parent directory
            File wallpaperDirectory = new File("/sdcard/Wallpaper/");
            // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs();
            // create a File object for the output file
            File outputFile = new File(wallpaperDirectory, audioUri.getPath());
            // now attach the OutputStream to the file object, instead of a String representation
            try {
                FileOutputStream fos = new FileOutputStream(outputFile);
                //File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+fil);
                //selectedFilePath=FilePath.getPath(this,Uri.fromFile(new File(outputFile.getPath())));
                Log.d("sdjklkjsdklfjskju", "onActivityResult: "+selectedFilePath);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        Log.d("selesad", "onActivityResult: "+selectedFilePath);
        // selectedFilePath = FilePath.getPath(this, audioUri);

        final MediaPlayer mp = MediaPlayer.create(this, audioUri);

        try {
            /*

            TextView pich = (TextView) findViewById(R.id.pich);

            int random = new Random().nextInt(61) + 20;
            if (random > 50) {
                pich.setText("Low");
            } else {
                pich.setText("Medium");
            }

            yourCountDownTimer.cancel();
            tm.setText("" + x);

            btn_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_play.setText("Stop");
                    mp.start();
                }
            });
            Log.d("rooooookkkkuuuusss", "onActivityResult: " + filestream);
            Log.d("rooooookkkkuuuusss", "onActivityResult: " + matches.get(0));

            String text = "";
            for (String result : matches)
                text += result + "\n";


            String s = matches.get(0);
            String[] words = s.split("\\W+");
//            tv_result.setText(text);
//            tv_result.append("\n");
//            tv_result.setText(s);
            word.setText("" + words.length);
            setHighLightedText(tv_result, "like");
            setHighLightedText(tv_result, "Um");
            setHighLightedText(tv_result, "time");

    */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void InitUI() {
        userSettings = new UserSettings(MainActivity.this);
        playerSheet = findViewById(R.id.player_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
        mic = (LinearLayout) findViewById(R.id.mic);
        playBtn = findViewById(R.id.player_play_btn);
        tv_result = findViewById(R.id.tv_result_speech);
        tv_duration = findViewById(R.id.tv_duration);
        playerHeader = findViewById(R.id.player_header_title);
        playerFilename = findViewById(R.id.player_filename);
        btn_play = findViewById(R.id.btn_play);
        playerSeekbar = findViewById(R.id.player_seekbar);
        tv_filler_count = findViewById(R.id.fil);
        recordedFileModel = userSettings.getRecordedFileObj();

        xd = (ImageView) findViewById(R.id.xd);
        iv_back = findViewById(R.id.iv_back);

        tv_word = findViewById(R.id.tv_word);

        tv_pitch = findViewById(R.id.tv_pitch);

        ll_main = findViewById(R.id.ll_main);
        tv_file_name = findViewById(R.id.tv_filename);
        tv_pace = findViewById(R.id.tv_pace);

        //  tm = (TextView) findViewById(R.id.tm);
        // pac = (TextView) findViewById(R.id.pac);

    }

    public void Play(View view) {
    }

    String savefile(Uri sourceuri) {
        String sourceFilename = sourceuri.getPath();
        String destinationFilename = android.os.Environment.getExternalStorageDirectory().getPath() + File.separatorChar + "abc.mp3";

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return destinationFilename;
    }


    private String encodeAudio(String selectedPath) {
        String x = null;
        byte[] audioBytes;
        try {

            // Just to check file size.. Its is correct i-e; Not Zero
            File audioFile = new File(selectedPath);
            long fileSize = audioFile.length();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(new File(selectedPath));
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
            audioBytes = baos.toByteArray();

            // Here goes the Base64 string
            x = Base64.encodeToString(audioBytes, Base64.DEFAULT);
            Log.d("Jcoubb", "encodeAudio: " + x);
        } catch (Exception e) {
            //   DiagnosticHelper.writeException(e);
        }
        return x;
    }


    void request() {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("time", "time");
        builder.addFormDataPart("words", "20");
        builder.addFormDataPart("pace", "12");
        builder.addFormDataPart("filter", "5");
        builder.addFormDataPart("pitch", "1");
        builder.addFormDataPart("speach", "sdfsdsdfsd");
        builder.addFormDataPart("fileToUpload", "glowws", RequestBody.create(MediaType.parse("multipart/form-data"), "" + selectedFilePath));


        MultipartBody requestBody = builder.build();

        ClientApi clientApix = Base.getClient().create(ClientApi.class);

        Call<String> callx = clientApix.uploadMultiFile(requestBody);

        callx.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                    // dialog.dismiss();
                    //finishAffinity();
                    // startActivity(new Intent(AddProduct.this, DAshboardMain.class));

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void pauseAudio() {
        mediaPlayer.pause();
        playBtn.setImageDrawable(getResources().getDrawable(R.drawable.player_play_btn, null));
        isPlaying = false;
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void resumeAudio() {
        if (mediaPlayer != null) {

            mediaPlayer.start();
            playBtn.setImageDrawable(getResources().getDrawable(R.drawable.player_pause_btn, null));
            isPlaying = true;

            updateRunnable();
            seekbarHandler.postDelayed(updateSeekbar, 0);
        }else{
            Toast.makeText(this, "Please select play button to proceed", Toast.LENGTH_SHORT).show();
        }

    }

    private void stopAudio() {
        //Stop The Audio
        playBtn.setImageDrawable(getResources().getDrawable(R.drawable.player_play_btn, null));
        playerHeader.setText("Stopped");
        isPlaying = false;
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void playAudio(File fileToPlay) {

        mediaPlayer = new MediaPlayer();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        playBtn.setImageDrawable(getResources().getDrawable(R.drawable.player_pause_btn, null));
        playerFilename.setText(fileToPlay.getName());
        playerHeader.setText("Playing");
        //Play the audio
        isPlaying = true;
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio();
                playerHeader.setText("Finished");
            }
        });

        playerSeekbar.setMax(mediaPlayer.getDuration());

        seekbarHandler = new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);

    }

    private void updateRunnable() {
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                playerSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this, 500);
            }
        };
    }
    public void goto_flipcard(View v) {
        TextView btn_suggestion = (TextView) findViewById(R.id.btn_suggestion);
        Intent intent = new Intent(getApplicationContext(), exercise_flipcard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}