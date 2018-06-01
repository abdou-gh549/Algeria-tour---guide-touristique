package com.algeriatour.main.favorite;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algeriatour.R;
import com.algeriatour.login.LoginActivity;
import com.algeriatour.uml_class.Favorite;
import com.algeriatour.utils.StaticValue;
import com.algeriatour.utils.User;
import com.androidnetworking.AndroidNetworking;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements FavoriteConstraint.ViewConstraint {

    @BindView(R.id.favorite_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.favorite_visitorLayout)
    LinearLayout visitorLayout;

    @BindView(R.id.favorite_information_textView)
    TextView informationTextView;

    @BindView(R.id.favorite_swipToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    LinearLayoutManager mLayoutManager;
    FavoriteRecycleViewAdapter recyclerViewAdapter;
    FavoritePresenter presenter;
    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);
        presenter = new FavoritePresenter(this);

        setUpSwipToRefresh();
        // user test
        if (User.getUserType() == StaticValue.MEMBER) {
            recyclerView.setVisibility(View.VISIBLE);
            visitorLayout.setVisibility(View.GONE);
            setUpAdapter();
        } else {
            recyclerView.setVisibility(View.GONE);
            visitorLayout.setVisibility(View.VISIBLE);
        }
        return view;

    }

    private void setUpSwipToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if(User.getUserType() == StaticValue.MEMBER){
                AndroidNetworking.cancelAll();
                presenter.loadFavoriteList();
            }
            else{
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setUpAdapter() {
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewAdapter = new FavoriteRecycleViewAdapter();
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @OnClick(R.id.favorite_visitorConnectTxtV)
    public void onConnectTextViewClicked() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void addFavorite(Favorite favorite) {

    }

    @Override
    public void setFavoriteImage(Bitmap image) {
        
    }

    @Override
    public void showProgressBar() {
        informationTextView.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showInformationText(String msg) {
        informationTextView.setVisibility(View.VISIBLE);
        informationTextView.setText(msg);
    }

    @Override
    public void hideInformationText() {
        informationTextView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorToast(String msg) {
        Toasty.error(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSucessToast(String msg) {
        Toasty.success(getContext(), msg, Toast.LENGTH_LONG).show();

    }
}
