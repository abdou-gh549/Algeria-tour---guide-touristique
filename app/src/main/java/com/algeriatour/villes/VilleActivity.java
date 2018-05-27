package com.algeriatour.villes;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.algeriatour.R;
import com.algeriatour.endroit.CentreIntereRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VilleActivity extends AppCompatActivity {


    @BindView(R.id.ville_toolbar)
    Toolbar toolbar;


    @BindView(R.id.ville_centreInteret_recycleView)
    RecyclerView villeRecyclerView;

    @BindView(R.id.ville_scrollView)
    NestedScrollView scrollView;


    private LinearLayoutManager mLayoutManager;
    private VilleRecyclerViewAdapter mrecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ville_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Jmila");

        villeRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mrecyclerViewAdapter = new VilleRecyclerViewAdapter();
        villeRecyclerView.setLayoutManager(mLayoutManager);
        villeRecyclerView.setAdapter(mrecyclerViewAdapter);

        scrollView.smoothScrollTo(0, 0);

    }
}
