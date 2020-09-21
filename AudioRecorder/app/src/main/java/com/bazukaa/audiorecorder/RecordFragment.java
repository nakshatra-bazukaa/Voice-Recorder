package com.bazukaa.audiorecorder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordFragment extends Fragment {

    private NavController navController;

    private boolean isRecording = false;

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
        if(isRecording){
            // Stop rec
            ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.record_btn_paused, null));
            isRecording = false;
        }else{
            // Start rec
            ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.record_btn_start, null));
            isRecording = true;
        }

    }
}
