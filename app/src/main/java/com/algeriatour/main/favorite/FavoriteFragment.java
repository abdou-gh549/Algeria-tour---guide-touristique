package com.algeriatour.main.favorite;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.algeriatour.R;
import com.algeriatour.login.LoginActivity;
import com.algeriatour.utils.StaticValue;
import com.algeriatour.utils.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    @BindView(R.id.favorite_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.favorite_visitorLayout)
    LinearLayout visitorLayout;

    LinearLayoutManager mLayoutManager;
    FavoriteRecycleViewAdapter recyclerViewAdapter;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);

        setUpAdapter();
        // user test
        // TODO mazl makamlthach
        if(User.getUserType() == StaticValue.MEMBER){
            recyclerView.setVisibility(View.VISIBLE);
            visitorLayout.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.GONE);
            visitorLayout.setVisibility(View.VISIBLE);
        }
        return view;

    }

    private void setUpAdapter() {
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewAdapter = new FavoriteRecycleViewAdapter();
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @OnClick(R.id.favorite_visitorConnectTxtV)
    public void onConnectTextViewClicked(){
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

}
