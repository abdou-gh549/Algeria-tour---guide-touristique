package com.algeriatour.point;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.algeriatour.R;
import com.algeriatour.uml_class.Commentaire;

import java.util.ArrayList;

public class PointIntereRecyclerAdapter extends RecyclerView.Adapter<PointIntereRecyclerAdapter.ViewHolder> {

    ArrayList<Commentaire> commentaires;

    public PointIntereRecyclerAdapter() {
        commentaires = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.centre_intere_commentaire, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setComment(commentaires.get(position));
    }


    @Override
    public int getItemCount() {
        return commentaires.size();
    }

    public void addComment(Commentaire commentaire) {
        commentaires.add(commentaire);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View commentView;
        private Commentaire comment;

        ViewHolder(View v) {
            super(v);
            commentView = v;
        }

        public void setComment(Commentaire comment) {
            this.comment = comment;

            ((TextView) commentView.findViewById(R.id.centre_interet_comment_username)).setText
                    (comment.getUserName());

            ((TextView) commentView.findViewById(R.id.centre_intere_comment_date)).setText
                    (comment.getDate());

            ((TextView) commentView.findViewById(R.id.centre_intere_comment_body)).setText
                    (comment.getComment());

            ((RatingBar) commentView.findViewById(R.id.centre_intere_comment_rattingBar))
                    .setRating(comment.getRatting());

        }
    }
}