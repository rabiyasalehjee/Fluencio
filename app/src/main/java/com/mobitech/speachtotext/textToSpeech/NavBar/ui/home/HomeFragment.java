package com.mobitech.speachtotext.textToSpeech.NavBar.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobitech.speachtotext.R;
import com.mobitech.speachtotext.textToSpeech.MeditationFeature.MeditationDashBoardActivity;
import com.mobitech.speachtotext.textToSpeech.TimedMode.Timer_Countdown;
import com.mobitech.speachtotext.textToSpeech.activities.InterviewCategory;
import com.mobitech.speachtotext.textToSpeech.activities.ProfileActivity;
import com.mobitech.speachtotext.textToSpeech.activities.RecordActivity;
import com.mobitech.speachtotext.textToSpeech.activities.SpeechListActivity;
import com.mobitech.speachtotext.textToSpeech.activities.TextToSpeechActivity;

public class HomeFragment extends Fragment {

    //private HomeViewModel homeViewModel;
    Button btn1_c1;
    TextView usernameonhome;
    CardView presentation, interviewCard, meditateCard;
    RelativeLayout presentationLayout, interviewLayout, meditateLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       /* homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        usernameonhome = root.findViewById(R.id.usernameonhome);
        presentation = root.findViewById(R.id.presentation);
        presentationLayout = root.findViewById(R.id.presentation_layout);
        interviewCard = root.findViewById(R.id.interviewCard);
        interviewLayout = root.findViewById(R.id.interiewLayout);
        meditateCard = root.findViewById(R.id.meditateCard);
        meditateLayout = root.findViewById(R.id.meditateLayout);

        presentationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RecordActivity.class));
            }
        });
        presentation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RecordActivity.class));
            }
        });
        interviewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InterviewCategory.class));
            }
        });
        interviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), InterviewCategory.class));
            }
        });
        meditateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MeditationDashBoardActivity.class));
            }
        });
        meditateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MeditationDashBoardActivity.class));
            }
        });

        /*        btn1_c1 = root.findViewById(R.id.btn1_c1);
        btn1_c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RecordActivity.class));

            }
        });

         */

        //Initialize and assign footer elements
        BottomNavigationView bottomNavigationView = root.findViewById(R.id.footer_nav);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.footer_home);

        //Perform Item selected Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.footer_home:
                        return true;
                    case R.id.footer_record:
                        startActivity(new Intent(getActivity(), RecordActivity.class));
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.footer_meditate:
                        startActivity(new Intent(getActivity(), MeditationDashBoardActivity.class));
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.footer_list:
                        startActivity(new Intent(getActivity(), SpeechListActivity.class));
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.footer_profile:
                        startActivity(new Intent(getActivity(), ProfileActivity.class));
//                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        return root;
    }


}