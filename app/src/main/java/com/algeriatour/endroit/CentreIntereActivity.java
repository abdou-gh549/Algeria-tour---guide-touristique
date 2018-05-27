package com.algeriatour.endroit;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ScrollView;

import com.algeriatour.R;
import com.algeriatour.main.favorite.FavoriteRecycleViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CentreIntereActivity extends AppCompatActivity {
    @BindView(R.id.centreIntere_comment_recycleView)
    RecyclerView commentRecyclerView;

    @BindView(R.id.centreIntere_scrollView) NestedScrollView scrollView;

    @BindView(R.id.centreIntere_toolbar) Toolbar toolbar;

    private LinearLayoutManager mLayoutManager;
    private CentreIntereRecyclerAdapter mrecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.centre_intere_activity);
        ButterKnife.bind(this);

        commentRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mrecyclerViewAdapter = new CentreIntereRecyclerAdapter();

        commentRecyclerView.setLayoutManager(mLayoutManager);
        commentRecyclerView.setAdapter(mrecyclerViewAdapter);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Mémorial du Martyr");



    }

}
