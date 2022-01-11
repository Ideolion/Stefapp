package com.example.stefapp.ui.Detox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stefapp.R;

import java.util.List;

public class DetoxAdapter extends RecyclerView.Adapter<DetoxAdapter.DetoxAdapterViewHolder > {
    private int selectedPosition;
    Context context;
    List<GetAudio> arrayListAudio;
    private RecyclerItemClickListener listener;

    public DetoxAdapter(Context context, List<GetAudio> arrayListAudio, RecyclerItemClickListener listener) {
        this.context = context;
        this.arrayListAudio = arrayListAudio;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DetoxAdapter.DetoxAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.audio_item,parent,false);
        return new DetoxAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetoxAdapter.DetoxAdapterViewHolder holder, int position) {
        GetAudio getAudio = arrayListAudio.get(position);
       if(getAudio != null){
           if (selectedPosition == position){
               holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.menu_color4));

           }else {
               holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.menu_color2));

           }
       }






        holder.textViewAudioTitle.setText(getAudio.getName());
       holder.bind(getAudio,listener);


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DetoxAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewAudioTitle;



        public DetoxAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAudioTitle = itemView.findViewById(R.id.textViewAudioTitle);

        }

        public void bind(GetAudio getAudio, RecyclerItemClickListener listener) {



        }
    }

    public interface RecyclerItemClickListener {


    }
}
