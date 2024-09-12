package com.mobitech.speachtotext.textToSpeech.adapter;

import static com.mobitech.speachtotext.textToSpeech.utils.UtilityFunctions.openCustomDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobitech.speachtotext.MainActivity;
import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.constants.URLS;
import com.mobitech.speachtotext.textToSpeech.listeners.ServerRequestListener;
import com.mobitech.speachtotext.textToSpeech.model.AssignmentModel;
import com.mobitech.speachtotext.textToSpeech.model.RecordedFileModel;
import com.mobitech.speachtotext.textToSpeech.serverRequestHelper.MyServerRequest;
import com.mobitech.speachtotext.textToSpeech.sharedPref.UserSettings;
import com.mobitech.speachtotext.textToSpeech.utils.TimeAgo;
import com.mobitech.speachtotext.textToSpeech.utils.UtilityFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AudioViewHolder> {

    private ArrayList<AssignmentModel> assignmentModelsList;
    private TimeAgo timeAgo;
    private Activity activity;
    private UserSettings userSettings;
//    private onItemListClick onItemListClick;

    public AssignmentAdapter(ArrayList<AssignmentModel> assignmentModelsList/*, onItemListClick onItemListClick*/, Activity activity){
        this.assignmentModelsList = assignmentModelsList;

//        this.onItemListClick = onItemListClick;
        this.activity=activity;
        userSettings=new UserSettings(activity);
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_assignments_recyclerview, parent, false);
        timeAgo = new TimeAgo();
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        holder.list_name.setText(assignmentModelsList.get(position).getName());
        holder.list_desc.setText(assignmentModelsList.get(position).getSubject());
        holder.btn_assign.setOnClickListener(view -> {
            openPitchDialog(assignmentModelsList.get(position));
        });
    }

    private void SubmitData(AssignmentModel model,Dialog dialog) {


        String allSpeechURL = URLS.UPDATE_ASSIGNMENTS +"?fileID="+userSettings.getRecordedFileObj().getId()+"&assignmentID="+model.getID();

        Log.i("cheking URL", "GetFIleListApi: " +allSpeechURL);

        MyServerRequest myServerRequest = new MyServerRequest(activity, allSpeechURL,
                new ServerRequestListener() {
                    @Override
                    public void onPreResponse() {
                        UtilityFunctions.showProgressDialog(activity, true);
                    }

                    @Override
                    public void onPostResponse(JSONObject jsonResponse, int requestCode) {

                        UtilityFunctions.hideProgressDialog(true);

                        dialog.dismiss();
                        try {
                            boolean response = jsonResponse.getBoolean("respone");

                            if (response){
                                Toast.makeText(activity, "Assignment Submitted Successfully", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(activity, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            Log.d("TAG", "onPostResponse: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
        myServerRequest.sendGetRequest();
    }
    public void removeAt(int position) {
        assignmentModelsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, assignmentModelsList.size());
    }




    @Override
    public int getItemCount() {
        return assignmentModelsList.size();
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        private TextView list_name;
        private TextView list_desc;
        private Button btn_assign;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.list_name);
            list_desc = itemView.findViewById(R.id.list_desc);
            btn_assign = itemView.findViewById(R.id.btn_assign);


        }

    }
    private void openPitchDialog(AssignmentModel model){

        Dialog dialog= new Dialog(activity);
        dialog.setContentView(R.layout.layout_add_code_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText edt_email = dialog.findViewById(R.id.edt_email);
        Button btn_submit = dialog.findViewById(R.id.btn_submit);


        btn_submit.setOnClickListener(view -> {
            if (edt_email.getText().toString().isEmpty()) {
                Toast.makeText(activity, "Please Enter Code To Submit", Toast.LENGTH_SHORT).show();
            }else if(!edt_email.getText().toString().equals(model.getCode())){
                Toast.makeText(activity, "Code is Invalid", Toast.LENGTH_SHORT).show();
                edt_email.setText("");
            } else{
                SubmitData(model,dialog);
            }
        });


        dialog.show();
    }
}