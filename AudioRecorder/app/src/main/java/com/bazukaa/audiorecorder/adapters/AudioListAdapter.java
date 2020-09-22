package com.bazukaa.audiorecorder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bazukaa.audiorecorder.R;
import com.bazukaa.audiorecorder.util.LongToTime;

import java.io.File;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.AudioViewHolder> {

    File[] allFiles;

    private LongToTime longToTime;

    private onAudioItemListClick onAudioItemListClick;

    public AudioListAdapter(File[] allFiles, onAudioItemListClick onAudioItemListClick) {
        this.allFiles = allFiles;
        this.onAudioItemListClick = onAudioItemListClick;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        longToTime = new LongToTime();
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

    public class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView listImage;
        private TextView listTitle, listDate;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            listImage = itemView.findViewById(R.id.list_image_view);
            listTitle = itemView.findViewById(R.id.list_title);
            listDate = itemView.findViewById(R.id.list_date);

            itemView.setOnClickListener(this);
        }

        void setAudioListData(File audioRecord){
            listTitle.setText(audioRecord.getName());
            listDate.setText(longToTime.convertFromLongToTime(audioRecord.lastModified()));
        }

        @Override
        public void onClick(View v) {
            onAudioItemListClick.onClickListener(allFiles[getAdapterPosition()], getAdapterPosition());
        }
    }

    public interface onAudioItemListClick{
        void onClickListener(File file, int position);
    }
}
