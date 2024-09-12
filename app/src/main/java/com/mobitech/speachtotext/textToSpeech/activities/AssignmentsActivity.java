package com.mobitech.speachtotext.textToSpeech.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.adapter.AssignmentAdapter;
import com.mobitech.speachtotext.textToSpeech.adapter.AudioListAdapter;
import com.mobitech.speachtotext.textToSpeech.constants.URLS;
import com.mobitech.speachtotext.textToSpeech.listeners.ServerRequestListener;
import com.mobitech.speachtotext.textToSpeech.model.AssignmentModel;
import com.mobitech.speachtotext.textToSpeech.model.RecordedFileModel;
import com.mobitech.speachtotext.textToSpeech.serverRequestHelper.MyServerRequest;
import com.mobitech.speachtotext.textToSpeech.utils.UtilityFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AssignmentsActivity extends AppCompatActivity {

    private RecyclerView rv_assignments;
    private AssignmentAdapter assignmentAdapter;
    private ArrayList<AssignmentModel> assignmentModelsList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        GetAssignmentListApi();
    }



    private void GetAssignmentListApi() {
        rv_assignments=findViewById(R.id.rv_assignment);

        Log.i("cheking URL", "GetFIleListApi: " + URLS.GET_ALL_ASSIGNMENTS );
        
        MyServerRequest myServerRequest = new MyServerRequest(AssignmentsActivity.this, URLS.GET_ALL_ASSIGNMENTS ,
                new ServerRequestListener() {
                    @Override
                    public void onPreResponse() {
                        UtilityFunctions.showProgressDialog(AssignmentsActivity.this, true);
                    }

                    @Override
                    public void onPostResponse(JSONObject jsonResponse, int requestCode) {

                        UtilityFunctions.hideProgressDialog(true);

                        try {
                            JSONArray response= jsonResponse.getJSONArray("respone");


                            for (int j = 0; j < response.length(); j++) {
                                JSONObject allSpeechesObject=response.getJSONObject(j);
                                assignmentModelsList.add(new AssignmentModel(
                                        allSpeechesObject.getString("Code"),
                                        allSpeechesObject.getString("ID"),
                                        allSpeechesObject.getString("Name"),
                                        allSpeechesObject.getString("Subject"),
                                        allSpeechesObject.getString("TeacherID")));

                            }
                            //list recylerview
                            assignmentAdapter = new AssignmentAdapter(assignmentModelsList,  AssignmentsActivity.this);
                            rv_assignments.setHasFixedSize(true);
                            rv_assignments.setLayoutManager(new LinearLayoutManager(AssignmentsActivity.this));
                            rv_assignments.setAdapter(assignmentAdapter);
                        } catch (JSONException e) {
                            Log.d("TAG", "onPostResponse: "+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
        myServerRequest.sendGetRequest();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}