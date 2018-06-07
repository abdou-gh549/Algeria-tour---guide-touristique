package com.algeriatour.map.activity;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.algeriatour.R;
import com.algeriatour.uml_class.PlaceInfo;

public class MapPointViewDetaille {
    private View pointViewDetaille;
    private TextView pointName;
    private TextView pointType;
    private TextView pointAdress;
    private RatingBar pointRattingBar;
    private ImageView pointImage;
    private PlaceInfo pointInteret;
    OnMapPointViewDetailleAction action;

    public MapPointViewDetaille(@NonNull View pointViewDetaille, @NonNull
            OnMapPointViewDetailleAction action) {
        this.action = action;
        this.pointViewDetaille = pointViewDetaille;
        pointAdress = pointViewDetaille.findViewById(R.id.map_point_detaille_adress);
        pointName = pointViewDetaille.findViewById(R.id.map_point_detaille_name);
        pointType = pointViewDetaille.findViewById(R.id.map_point_detaille_type);
        pointRattingBar = pointViewDetaille.findViewById(R.id.map_point_detaille_rattingBar);
        pointImage = pointViewDetaille.findViewById(R.id.map_point_detaille_image);
        setUpClickListiner();
    }

    private void setUpClickListiner() {
        pointViewDetaille.findViewById(R.id.map_point_detaille_navigateButton).setOnClickListener
                (v->{
                    action.onNavigationClick(pointInteret);
                });

        pointViewDetaille.findViewById(R.id.map_point_detaille_more).setOnClickListener
                (v->{
                    action.onMoreInformationClick(pointInteret);
                });
    }

    public void setData(PlaceInfo p) {
        pointInteret = p;
        pointName.setText(pointInteret.getName());
        pointType.setText(pointInteret.getType());
        pointAdress.setText(pointInteret.getCompletAdress());
        pointRattingBar.setRating(pointInteret.getRate());
    }

    public void setPointImage(Bitmap image) {
        pointImage.setImageBitmap(image);
    }

    public void hideMapPointViewDetaille() {
        pointViewDetaille.setVisibility(View.GONE);
    }


    public void showMapPointViewDetaille() {
        pointViewDetaille.setVisibility(View.VISIBLE);
    }

    public int getHeight() {

        return pointViewDetaille.getHeight();
    }


    interface OnMapPointViewDetailleAction{
        void onNavigationClick(PlaceInfo placeInfo);
        void onMoreInformationClick(PlaceInfo placeInfo);
    }
}
