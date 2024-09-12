package com.mobitech.speachtotext.textToSpeech.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.FileProvider;

import com.mobitech.speachtotext.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadingTask extends AsyncTask<File, File, File> {

    private Context context;

    private String downloadUrl = "", downloadFileName = "",RecordPath="";
    private ProgressDialog progressDialog;
//    File apkStorage = null;
    public static File outputFile = null;

//    String ext;

    public DownloadingTask(Context context, String downloadUrl,String downloadFileName,String RecordPath) {
//        this.ext = ext;
        this.context = context;

        this.downloadUrl = downloadUrl;
        this.RecordPath=RecordPath;
        this.downloadFileName = downloadFileName;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Downloading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(File result) {
        super.onPostExecute(result);
        try {

            if (outputFile != null) {
                progressDialog.dismiss();

                outputFile=result;
//                return outputFile;
//                    Toast.makeText(context, "Document Downloaded Successfully", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                    }
                }, 3000);

                Log.e("TAG", "Download Failed");
//                return null;

            }
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            //Change button text if exception occurs

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 3000);
            Log.e("TAG", "Download Failed with Exception - " + e.getLocalizedMessage());
//            return null;
        }



    }

    @Override
    protected File doInBackground(File... files) {
        try {
            URL url = new URL(downloadUrl);//Create Download URl
            HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
            c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
            c.connect();//connect the URL Connection

            //If Connection response is not OK then show Logs
            if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("TAG", "Server returned HTTP " + c.getResponseCode()
                        + " " + c.getResponseMessage());

            }
//            apkStorage = new File((Activity)context.getExternalFilesDir("/").getAbsolutePath()+"/"+downloadFileName);


            //Get File if SD card is present

            //If File is not present create directory


            outputFile = new File(RecordPath+ "/" +downloadFileName);//Create Output file in Main File

            outputFile.createNewFile();
            //Create New File if not present
//            if (!outputFile.exists()) {
//                Log.e(TAG, "File Created");
//            }

            FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

            InputStream is = c.getInputStream();//Get InputStream for connection

            byte[] buffer = new byte[1024];//Set buffer type
            int len1 = 0;//init length
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);//Write new file
            }

            //Close all connection after doing task
            fos.close();
            is.close();
            return outputFile;

        } catch (Exception e) {

            //Read exception if something went wrong
            e.printStackTrace();
            outputFile = null;
            Log.e("TAG", "Download Error Exception " + e.getMessage());
            return null;
        }


    }
}