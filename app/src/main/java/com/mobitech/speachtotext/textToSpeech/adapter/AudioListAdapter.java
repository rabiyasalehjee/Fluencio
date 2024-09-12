package com.mobitech.speachtotext.textToSpeech.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mobitech.speachtotext.Login;
import com.mobitech.speachtotext.MainActivity;
import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.Signup;
import com.mobitech.speachtotext.textToSpeech.constants.URLS;
import com.mobitech.speachtotext.textToSpeech.listeners.CustomInfoDialogListener;
import com.mobitech.speachtotext.textToSpeech.listeners.ServerRequestListener;
import com.mobitech.speachtotext.textToSpeech.model.RecordedFileModel;
import com.mobitech.speachtotext.textToSpeech.serverRequestHelper.MyServerRequest;
import com.mobitech.speachtotext.textToSpeech.sharedPref.UserSettings;
import com.mobitech.speachtotext.textToSpeech.utils.TimeAgo;
import com.mobitech.speachtotext.textToSpeech.utils.UtilityFunctions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static com.mobitech.speachtotext.textToSpeech.constants.Constants.SINGLE_AUDIO_ITEM;
import static com.mobitech.speachtotext.textToSpeech.utils.UtilityFunctions.openCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {

    private ArrayList<RecordedFileModel> allFiles;
    private TimeAgo timeAgo;
    private Activity activity;
    private UserSettings userSettings;
//    private onItemListClick onItemListClick;

    public AudioListAdapter(ArrayList<RecordedFileModel> allFiles/*, onItemListClick onItemListClick*/, Activity activity){
        this.allFiles = allFiles;

//        this.onItemListClick = onItemListClick;
        this.activity=activity;
        userSettings=new UserSettings(activity);
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        timeAgo = new TimeAgo();
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {


        holder.aSwitch.setChecked(allFiles.get(position).isShowToTeacher());

        holder.aSwitch.setOnClickListener(view -> {
            holder.aSwitch.setChecked(!allFiles.get(position).isShowToTeacher());
            allFiles.get(position).setShowToTeacher(!allFiles.get(position).isShowToTeacher());

            SignUpRequest(allFiles.get(position).getId());
            notifyDataSetChanged();

        });
        holder.list_title.setText(allFiles.get(position).getFile_name());
        holder.list_date.setText(allFiles.get(position).getDate());

        holder.cl_main.setOnClickListener(v -> {
            activity.startActivity(new Intent(activity, MainActivity.class));

            userSettings.createRecordedFileObj(allFiles.get(position));
            activity.finish();
        });
        holder.delete.setOnClickListener(view -> openCustomDialog(activity,
                "Alert", "Are you sure you want to delete " + allFiles.get(position).getFile_name(), true,
                () -> DeleteData(allFiles.get(position).getId(),allFiles.get(position).getType(),allFiles.get(position).getFile_name(),position)));

    }

    private void DeleteData(String id,String type,String file_name,int position) {


        String allSpeechURL = URLS.DELETE_FILE + id+"&type="+type;

        Log.i("cheking URL", "GetFIleListApi: " + URLS.DELETE_FILE + id+"&type="+type);

        MyServerRequest myServerRequest = new MyServerRequest(activity, allSpeechURL,
                new ServerRequestListener() {
                    @Override
                    public void onPreResponse() {
                        UtilityFunctions.showProgressDialog(activity, true);
                    }

                    @Override
                    public void onPostResponse(JSONObject jsonResponse, int requestCode) {

                        UtilityFunctions.hideProgressDialog(true);

                        try {
                            boolean response = jsonResponse.getBoolean("respone");

                            if (response){
                                removeAt(position);
                                Toast.makeText(activity, file_name+" has been deleted", Toast.LENGTH_SHORT).show();
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
        allFiles.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, allFiles.size());
    }

    void SignUpRequest(String id){
         MyServerRequest myServerRequest = new MyServerRequest(activity, URLS.UPDATED_SHOW_TO_TEACHER+id,
                new ServerRequestListener() {
                    @Override
                    public void onPreResponse() {
                        UtilityFunctions.showProgressDialog(activity, true);
                    }

                    @Override
                    public void onPostResponse(JSONObject jsonResponse, int requestCode) {
                        UtilityFunctions.hideProgressDialog(true);
                        try {

                            if (jsonResponse.getBoolean("respone")) {
                                Log.d("TAG", "onPostResponse: successful");
                            }

                        } catch (JSONException e) {
                            UtilityFunctions.hideProgressDialog(true);
                            e.printStackTrace();
                        }



                    }
                });
        myServerRequest.sendGetRequest();
    }


    @Override
    public int getItemCount() {
        return allFiles.size();
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        private ImageView list_image/*,list_upload,list_graph_view*/,delete;
        private TextView list_title;
        private TextView list_date;
        private Switch aSwitch;
        private LinearLayout cl_main;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            list_image = itemView.findViewById(R.id.list_image_view);
            aSwitch = itemView.findViewById(R.id.simpleSwitch);
            delete = itemView.findViewById(R.id.delete_image_view);
//            list_graph_view = itemView.findViewById(R.id.list_view_graph_image_view);
//            list_upload = itemView.findViewById(R.id.list_item_upload_image_view);
            list_title = itemView.findViewById(R.id.list_title);
            list_date = itemView.findViewById(R.id.list_date);
            cl_main = itemView.findViewById(R.id.cl_main);
//            itemView.setOnClickListener(this);

        }

//        @Override
//        public void onClick(View v) {
//            onItemListClick.onClickListener(allFiles.get(getAdapterPosition()));
//
//        }
    }
//    public interface onItemListClick {
//        void onClickListener(RecordedFileModel model);
//    }
}
