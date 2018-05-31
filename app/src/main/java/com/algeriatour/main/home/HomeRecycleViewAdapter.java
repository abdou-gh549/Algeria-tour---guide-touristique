package com.algeriatour.main.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.algeriatour.R;
import com.algeriatour.endroit.CentreIntereActivity;
import com.algeriatour.uml_class.Ville;
import com.algeriatour.villes.VilleActivity;

import java.util.ArrayList;

public class HomeRecycleViewAdapter extends RecyclerView.Adapter<HomeRecycleViewAdapter.ViewHolder> {

    private ArrayList<Ville> villes;

    public HomeRecycleViewAdapter() {
        this.villes = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.home_ville_cardview, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setVilleName( villes.get(position).getName());
        holder.setVilleWilaya( villes.get(position).getWilaya());
        holder.setVilleRate( villes.get(position).getRate());
        if(villes.get(position).getImage() != null)
            holder.setVilleImage( villes.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return villes.size();
    }

    public void addVille(Ville ville) {
        villes.add(ville);
        notifyDataSetChanged();
    }
    public void updateVille(Ville ville, int position){
        villes.set(position, ville);
        notifyItemChanged(position);
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
        public void setVilleImage(Bitmap bitmap){
            ((ImageView)cardView.findViewById(R.id.home_cardView_Image)).setImageBitmap(bitmap);
        }
        public void setVilleName(String name){
            ((TextView)cardView.findViewById(R.id.home_cardView_name)).setText(name);

        }
        public void setVilleWilaya(String wilaya){
            ((TextView)cardView.findViewById(R.id.home_cardView_wilaya)).setText(wilaya);

        }
        public void setVilleRate(float rate){
            ((RatingBar)cardView.findViewById(R.id.home_cardView_ratting_bar)).setRating(rate);
        }
    }
}


