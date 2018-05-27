package com.algeriatour.endroit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algeriatour.R;

public class CentreIntereRecyclerAdapter extends RecyclerView.Adapter<CentreIntereRecyclerAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.centre_intere_commentaire, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // setup item
    }


    @Override
    public int getItemCount() {
        return 5;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View favoriteItemView;

        ViewHolder(View v) {
            super(v);
            favoriteItemView = v;
        }
    }
}