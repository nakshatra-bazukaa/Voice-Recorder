package com.bazukaa.audiorecorder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bazukaa.audiorecorder.R;

import java.io.File;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {

    File[] allFiles;

    public AudioListAdapter(File[] allFiles) {
        this.allFiles = allFiles;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AudioViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_list_item, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        holder.setAudioListData(allFiles[position]);
    }
    @Override
    public int getItemCount() {
        return allFiles.length;
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder{

        private ImageView listImage;
        private TextView listTitle, listDate;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            listImage = itemView.findViewById(R.id.list_image_view);
            listTitle = itemView.findViewById(R.id.list_title);
            listDate = itemView.findViewById(R.id.list_date);
        }

        void setAudioListData(File audioRecord){
            listTitle.setText(audioRecord.getName());
            listDate.setText(audioRecord.lastModified() + "");
        }
    }
}
