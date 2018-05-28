package com.algeriatour.main.home;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.algeriatour.R;
import com.algeriatour.endroit.CentreIntereActivity;
import com.algeriatour.villes.VilleActivity;

public class HomeRecycleViewAdapter extends RecyclerView.Adapter<HomeRecycleViewAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.home_ville_cardview, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public void addVille( /* class de type ville*/) {
        // add vill to array
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
            cardView.setOnClickListener(view -> {
                CardView cardView = (CardView) view;
                //Todo on recycler view CLick Listiner
                Intent intent = new Intent(view.getContext(), VilleActivity.class);
                view.getContext().startActivity(intent);
            });
        }
    }
}


