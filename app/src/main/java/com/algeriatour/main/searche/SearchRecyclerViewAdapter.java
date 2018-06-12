package com.algeriatour.main.searche;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.algeriatour.R;
import com.algeriatour.point.PointIntereActivity;
import com.algeriatour.uml_class.PlaceInfo;
import com.algeriatour.uml_class.SearchResult;
import com.algeriatour.utils.StaticValue;
import com.algeriatour.villes.VilleActivity;

import java.util.ArrayList;

public class SearchRecyclerViewAdapter  extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {

    ArrayList<SearchResult> searchResults = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(
                R.layout.search_recylere_view_item, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(searchResults.get(position));
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public void addSearchResult( SearchResult sr){
        searchResults.add(sr);
        notifyDataSetChanged();
    }
    public void setImage(Bitmap image, int postion){
        if(postion < getItemCount() && postion >= 0){
            searchResults.get(postion).setImage(image);
            notifyItemChanged(postion);
        }
    }

    public void clearData() {
        searchResults.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private SearchResult data;
        private View view;
        public ViewHolder(View v) {
            super(v);
            this.view = v;
            v.setOnClickListener( view ->{
                PlaceInfo placeInfo = data.getPlaceInfo();
                Intent intent;
                if(data.getType().toLowerCase().equals("ville")){
                    intent = new Intent(view.getContext(), VilleActivity.class);
                    intent.putExtra(StaticValue.VILLE_WITHOUT_IMAGE_TAG, placeInfo);
                }else{
                    intent = new Intent(view.getContext(), PointIntereActivity.class);
                    intent.putExtra(StaticValue.POINT_TAG, placeInfo);
                }
                view.getContext().startActivity(intent);

            });
        }

        public void setData(SearchResult data) {
            this.data = data;
            Bitmap image;
            if (data.getImage() != null) {
                image = data.getImage();
            } else {
                BitmapDrawable bitmapdraw = (BitmapDrawable) view.getResources()
                        .getDrawable(R.drawable.wait_image_2);
                image = bitmapdraw.getBitmap();
            }

            ((ImageView) view.findViewById(R.id.search_result_item_image)).setImageBitmap(image);
            ((TextView) view.findViewById(R.id.search_result_item_name)).setText(data.getName());
            ((TextView) view.findViewById(R.id.search_result_item_adress)).setText(data.getWilaya());
            ((TextView) view.findViewById(R.id.search_result_item_type)).setText(data.getType());

        }
    }
}
