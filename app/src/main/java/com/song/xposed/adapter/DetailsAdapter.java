package com.song.xposed.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by chensongsong on 2020/10/13.
 */
public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.DetailsHolder> {


    @NonNull
    @Override
    public DetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class DetailsHolder extends RecyclerView.ViewHolder{

        public DetailsHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
