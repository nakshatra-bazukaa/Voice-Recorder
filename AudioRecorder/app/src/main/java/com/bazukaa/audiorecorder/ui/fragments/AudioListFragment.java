package com.bazukaa.audiorecorder.ui.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazukaa.audiorecorder.R;
import com.bazukaa.audiorecorder.adapters.AudioListAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
    @BindView(R.id.player_seekbar)
    SeekBar playerSeekbar;

    private File[] allFiles;
    private File fileToPlay;

    private BottomSheetBehavior bottomSheetBehavior;

    private AudioListAdapter adapter;

    private MediaPlayer mediaPlayer = null;

    private boolean isPlaying = false;

    private Handler seekbarHandler;
    private Runnable updateSeekbar;

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
                if(fileToPlay != null){
                    int progress = seekBar.getProgress();
                    mediaPlayer.seekTo(progress);
                }
                resumeAudio();
            }
        });
    }

    @Override
    public void onClickListener(File file, int position) {
        fileToPlay = file;
        if(isPlaying){
            stopAudioRecord();
            playAudioRecord(fileToPlay);
        }else{
            playAudioRecord(fileToPlay);
        }
    }
    @OnClick(R.id.player_play_btn)
    public void playBtnClicked(){
        if(isPlaying){
            pauseAudio();
        }else{
            if(fileToPlay != null)
                resumeAudio();
        }
    }
    private void stopAudioRecord() {
        btnPlay.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_play, null));
        playerTitle.setText("Stopped");
        isPlaying = false;
        mediaPlayer.stop();
        seekbarHandler.removeCallbacks(updateSeekbar);
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

    private void pauseAudio(){
        mediaPlayer.pause();
        btnPlay.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_play, null));
        isPlaying = false;
        seekbarHandler.removeCallbacks(updateSeekbar);
    }
    private void resumeAudio(){
        mediaPlayer.start();
        btnPlay.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_pause, null));
        isPlaying = true;
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isPlaying)
            stopAudioRecord();
    }
}
