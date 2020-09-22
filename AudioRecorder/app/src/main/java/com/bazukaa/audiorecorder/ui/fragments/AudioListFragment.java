package com.bazukaa.audiorecorder.ui.fragments;

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

import com.bazukaa.audiorecorder.R;
import com.bazukaa.audiorecorder.adapters.AudioListAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AudioListFragment extends Fragment {

    @BindView(R.id.player_sheet)
    ConstraintLayout playerSheet;
    @BindView(R.id.audio_list_view)
    RecyclerView rvAudioList;

    private File[] allFiles;

    private BottomSheetBehavior bottomSheetBehavior;

    private AudioListAdapter adapter;

    public AudioListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        adapter = new AudioListAdapter(allFiles);

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
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }
}
