package com.algeriatour.main.favorite;

import android.content.Intent;
import android.content.QuickViewConstants;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.algeriatour.R;
import com.algeriatour.main.home.HomeRecycleViewAdapter;
import com.algeriatour.point.PointIntereActivity;
import com.algeriatour.uml_class.Favorite;
import com.algeriatour.utils.StaticValue;

import java.util.ArrayList;

public class FavoriteRecycleViewAdapter extends RecyclerView.Adapter<FavoriteRecycleViewAdapter.ViewHolder> {
    ArrayList<Favorite> favorites;
    FavoriteConstraint.ViewConstraint favoriteView;
    public FavoriteRecycleViewAdapter(FavoriteConstraint.ViewConstraint favoriteView) {
        this.favorites = new ArrayList<>();
        this.favoriteView = favoriteView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.favorite_recyclerview_item, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setFavorite(favorites.get(position));
    }
    @Override
    public int getItemCount() {
        return favorites.size();
    }

    void addFavortie(Favorite fav){
        for (int i = 0; i < favorites.size(); i++) {
            if(favorites.get(i).getPointInteret().getId() == fav.getPointInteret().getId()){
                // point already exist so replace it
                Log.d("tixx", "addFavortie: im in replace fav id = " + fav.getPointInteret().getId());
                favorites.set(i, fav);
                notifyItemChanged(i);
                return;
            }
        }
        favorites.add(fav);
        notifyDataSetChanged();
    }
    void setFavoriteImage(Bitmap favoriteImage, long pointId){
        for (int i = 0; i < favorites.size(); i++) {
            if( favorites.get(i).getPointInteret().getId() == pointId){
                favorites.get(i).getPointInteret().setImage(favoriteImage);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void removeFavorite(long favoriteID) {
        for (int i = 0; i < favorites.size(); i++) {
            if(favorites.get(i).getFavoriteId() == favoriteID){
                favorites.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View favoriteItemView;
         private Favorite favorite;

         ViewHolder(View v) {
            super(v);
            favoriteItemView = v;
             v.findViewById(R.id.favorite_recyclerItem_delete).setOnClickListener(view ->
                     favoriteView.deleteFavoriteClick(favorite.getFavoriteId()));
             v.findViewById(R.id.favorite_recyclerItem_edit).setOnClickListener(view ->
                     favoriteView.editFavoriteClick(favorite));

             v.setOnClickListener(view -> {
                 Intent intent = new Intent(v.getContext(), PointIntereActivity.class);
                 intent.putExtra(StaticValue.POINT_TAG, favorite.getPointInteret().getPlaceInfo());
                 v.getContext().startActivity(intent);
             });
        }

         public void setFavorite(Favorite favorite) {
             this.favorite = favorite;
             ((TextView)favoriteItemView.findViewById(R.id.favorite_recyclerItem_titre)).setText
                     (favorite.getPointInteret().getName());
             ((TextView)favoriteItemView.findViewById(R.id.favorite_recyclerItem_adress)).setText
                     (favorite.getPointInteret().getCompletAdress());
             ((TextView)favoriteItemView.findViewById(R.id.favorite_recyclerItem_type)).setText
                     (favorite.getPointInteret().getType());
             ((TextView)favoriteItemView.findViewById(R.id.favorite_recyclerItem_rattingTextView)).setText
                     (favorite.getPointInteret().getRate()+"");
             ((RatingBar)favoriteItemView.findViewById(R.id.favorite_recyclerItem_rattingBar))
                     .setRating(favorite.getPointInteret().getRate());
             ((TextView)favoriteItemView.findViewById(R.id.favorite_recyclerItem_note)).setText(favorite.getNote());
             ((TextView)favoriteItemView.findViewById(R.id.favorite_recyclerItem_dateAjout))
                     .setText(favorite.getDatAjout());

             if(favorite.getPointInteret().getImage() != null){
                 ((ImageView)favoriteItemView.findViewById(R.id.favorite_recyclerItem_endroitImage))
                         .setImageBitmap(favorite.getPointInteret().getImage());
             }
         }
     }
}
