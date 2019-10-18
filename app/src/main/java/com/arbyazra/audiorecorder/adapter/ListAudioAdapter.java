package com.arbyazra.audiorecorder.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arbyazra.audiorecorder.R;
import com.arbyazra.audiorecorder.model.AudioData;

import java.util.ArrayList;

public class ListAudioAdapter extends RecyclerView.Adapter<ListAudioAdapter.ViewHolder> {
    private ArrayList<AudioData> mAudioData;
    private Context context;
    private MediaPlayer mp;

    public ListAudioAdapter(ArrayList<AudioData> audioData, Context context){
        this.mAudioData = audioData;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView,onCreated;
        public ImageButton btn_play,btn_stop;
        public ViewHolder(View v) {
            super(v);
            textView = v.findViewById(R.id.tv_outputName);
            onCreated = v.findViewById(R.id.tv_onCreated);
            btn_play = v.findViewById(R.id.btn_Play);
            btn_stop = v.findViewById(R.id.btn_Stop);

        }
    }



    @NonNull
    @Override
    public ListAudioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListAudioAdapter.ViewHolder holder, final int position) {

        holder.textView.setText(mAudioData.get(position).getName());
        holder.onCreated.setText(mAudioData.get(position).getOncreated());
        holder.btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp = new MediaPlayer();
                try {
                    mp.setDataSource(mAudioData.get(position).getOutputname());
                    mp.prepare();

                    Toast.makeText(context, "Playing Audio", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mp.start();
            }
        });
        holder.btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btn_stop.isEnabled()){
                    if(mp!=null){
                        mp.stop();
                        mp.release();
                        mp =null;
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mAudioData.size();
    }


}
