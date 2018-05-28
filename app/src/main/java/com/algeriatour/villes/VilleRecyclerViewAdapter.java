package com.algeriatour.villes;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algeriatour.R;
import com.algeriatour.endroit.CentreIntereActivity;

public class VilleRecyclerViewAdapter extends RecyclerView.Adapter<VilleRecyclerViewAdapter.ViewHolder>{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ville_centre_intere_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // setup item
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        ViewHolder(View v) {
            super(v);

            v.setOnClickListener(view ->{
                Intent intent = new Intent(view.getContext(), CentreIntereActivity.class);
                view.getContext().startActivity(intent);
            });
        }
    }
}
