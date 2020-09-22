package com.bazukaa.audiorecorder.ui.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bazukaa.audiorecorder.R;
import com.bazukaa.audiorecorder.adapters.AudioListAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AudioListFragment extends Fragment implements AudioListAdapter.onAudioItemListClick {

    @BindView(R.id.player_sheet)
    ConstraintLayout playerSheet;
    @BindView(R.id.audio_list_view)
    RecyclerView rvAudioList;
    @BindView(R.id.player_play_btn)
    ImageButton btnPlay;
    @BindView(R.id.player_header_title)
    TextView playerTitle;
    @BindView(R.id.player_filename)
    TextView playerFilename;

    private File[] allFiles;
    private File fileToPlay;

    private BottomSheetBehavior bottomSheetBehavior;

    private AudioListAdapter adapter;

    private MediaPlayer mediaPlayer = null;

    private boolean isPlaying = false;

    public AudioListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_audio_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File dir = new File(path);
        allFiles = dir.listFiles();

        adapter = new AudioListAdapter(allFiles, this);

        rvAudioList.setHasFixedSize(true);
        rvAudioList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAudioList.setAdapter(adapter);

        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) { }
        });
    }

    @Override
    public void onClickListener(File file, int position) {
        if(isPlaying){
            stopAudioRecord();
        }else{
            fileToPlay = file;
            playAudioRecord(fileToPlay);
        }
    }

    private void stopAudioRecord() {
        btnPlay.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_play, null));
        playerTitle.setText("Stopped");
        isPlaying = false;
    }

    private void playAudioRecord(File fileToPlay) {
        mediaPlayer = new MediaPlayer();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        btnPlay.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_pause, null));
        playerFilename.setText(fileToPlay.getName());
        playerTitle.setText("Playing");
        isPlaying = true;

        mediaPlayer.setOnCompletionListener(mp -> {
            stopAudioRecord();
            playerTitle.setText("Finished");
        });
    }
}
