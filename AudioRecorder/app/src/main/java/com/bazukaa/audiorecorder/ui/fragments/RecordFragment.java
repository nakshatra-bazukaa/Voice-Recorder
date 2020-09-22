package com.bazukaa.audiorecorder.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bazukaa.audiorecorder.R;
import com.bazukaa.audiorecorder.util.Constants;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordFragment extends Fragment {

    @BindView(R.id.record_timer)
    Chronometer recordTimer;

    private NavController navController;
    private MediaRecorder mediaRecorder;

    private boolean isNotRecording = true;
    private String recordFile;

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    @OnClick(R.id.record_list_btn)
    public void recordsListBtnClicked(View v){
        navController.navigate(R.id.action_recordFragment_to_audioListFragment);
    }
    @OnClick(R.id.record_btn)
    public void recordBtnClicked(View v){
        if(isNotRecording && checkAudioPermission()){
            // Start rec
            startRecording();
            ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.record_btn_stop, null));
            isNotRecording = false;
        }else{
            // Stop rec
            stopRecording();
            ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.record_btn_start, null));
            isNotRecording = true;
        }

    }
    private void startRecording() {
        recordTimer.setBase(SystemClock.elapsedRealtime());
        recordTimer.start();

        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_mm_ss");
        Date now = new Date();

        recordFile = "Recording_" + formatter.format(now) + ".3gp";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        mediaRecorder.start();
    }
    private void stopRecording() {
        recordTimer.stop();

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }
    private boolean checkAudioPermission() {
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, Constants.RECORD_AUDIO_PERMISSION);
            return false;
        }
    }
}
