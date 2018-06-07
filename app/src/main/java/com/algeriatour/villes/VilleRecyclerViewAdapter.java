package com.algeriatour.villes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.algeriatour.R;
import com.algeriatour.point.PointIntereActivity;
import com.algeriatour.uml_class.PlaceInfo;
import com.algeriatour.uml_class.PointInteret;
import com.algeriatour.utils.StaticValue;

import java.util.ArrayList;

public class VilleRecyclerViewAdapter extends RecyclerView.Adapter<VilleRecyclerViewAdapter.ViewHolder> {

    ArrayList<PointInteret> pointInterets;
    public VilleRecyclerViewAdapter(){
        pointInterets = new ArrayList<>();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ville_centre_intere_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setPointInteret(pointInterets.get(position));
    }

    @Override
    public int getItemCount() {
        return pointInterets.size();
    }

    public void addPointInteret(PointInteret pointInteret) {
        for (int i = 0; i < pointInterets.size(); i++) {
            if(pointInterets.get(i).getId() == pointInteret.getId()){
                // point already exist so replace it
                pointInterets.set(i, pointInteret);
                notifyItemChanged(i);
                return;
            }
        }
        pointInterets.add(pointInteret);
        notifyDataSetChanged();
    }

    public void setPointInteretImage(Bitmap pointInteretImage, int position){
        pointInterets.get(position).setImage(pointInteretImage);
        notifyItemChanged(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        PointInteret pointInteret;
        View view;

        ViewHolder(View v) {
            super(v);
            view = v;
            pointInteret = new PointInteret();
            v.setOnClickListener(view -> {
                PlaceInfo placeInfo = pointInteret.getPlaceInfo();
                Intent intent = new Intent(view.getContext(), PointIntereActivity.class);
                intent.putExtra(StaticValue.POINT_TAG, placeInfo);
                view.getContext().startActivity(intent);
            });
        }

        void setPointInteret(PointInteret pointInteret) {
            this.pointInteret = pointInteret;
            if(pointInteret.getImage() != null){
                 ((ImageView) view.findViewById(R.id.ville_centreInteretItem_photo)).setImageBitmap(pointInteret.getImage());
            }
            ((TextView) view.findViewById(R.id.ville_centreInteretItem_nome)).setText(pointInteret.getName());
            ((TextView) view.findViewById(R.id.ville_centreInteretItem_ville)).setText(pointInteret.getWilaya());
            ((TextView) view.findViewById(R.id.ville_centreInteretItem_type)).setText(pointInteret.getType());
        }
    }
}
