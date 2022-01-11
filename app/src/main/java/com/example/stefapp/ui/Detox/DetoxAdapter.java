package com.example.stefapp.ui.Detox;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DetoxAdapter extends RecyclerView.Adapter<DetoxAdapter.DetoxAdapterViewHolder > {


    @NonNull
    @Override
    public DetoxAdapter.DetoxAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DetoxAdapter.DetoxAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DetoxAdapterViewHolder extends RecyclerView.ViewHolder{
        public DetoxAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
