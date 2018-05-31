package com.algeriatour.villes;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.algeriatour.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VilleActivity extends AppCompatActivity implements VilleConstraint.ViewConstraint {


    @BindView(R.id.ville_toolbar)
    Toolbar toolbar;

    @BindView(R.id.ville_centreInteret_recycleView)
    RecyclerView villeRecyclerView;

    @BindView(R.id.ville_descreption)
    TextView ville_descreption;

    @BindView(R.id.ville_image)
    ImageView ville_image;

    @BindView(R.id.ville_scrollView)
    NestedScrollView scrollView;

    private LinearLayoutManager mLayoutManager;
    private VilleRecyclerViewAdapter mrecyclerViewAdapter;
    private VillePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ville_activity);
        ButterKnife.bind(this);

        setUpActionToolBar();
        setUpAdapter();

        presenter = new VillePresenter(this);
        presenter.setUpData();
    }

    private void setUpActionToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpAdapter() {
        villeRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mrecyclerViewAdapter = new VilleRecyclerViewAdapter();
        villeRecyclerView.setLayoutManager(mLayoutManager);
        villeRecyclerView.setAdapter(mrecyclerViewAdapter);

        scrollView.smoothScrollTo(0, 0);

    }

    @Override
    public void setVilleImage(Bitmap image) {
        ville_image.setImageBitmap(image);
    }

    @Override
    public void setVilleName(String name) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(name);
        }
    }

    @Override
    public void setVilleDescreption(String descreption) {
        ville_descreption.setText(descreption);
    }

    @Override
    public void addCentreInteret(/* de type centreInteret*/) {

    }
}
