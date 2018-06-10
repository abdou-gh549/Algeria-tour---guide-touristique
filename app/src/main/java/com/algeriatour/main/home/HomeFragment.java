package com.algeriatour.main.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.algeriatour.R;
import com.algeriatour.uml_class.Ville;
import com.algeriatour.utils.Networking;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeFragmentConstraint.ViewContsraint {

    @BindView(R.id.home_recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.home_empty_town_textview)
    TextView emptyTownTextView;

    @BindView(R.id.home_container_layout)
    View containerLayout;

    @BindView(R.id.home_swipeToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    HomeRecycleViewAdapter mAdapter;


    private RecyclerView.LayoutManager mLayoutManager;
    private HomeFragementPresenter presenter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("fragment", "onCreatView: ");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        Networking.initAndroidNetworking(getContext());
        presenter = new HomeFragementPresenter(this);
        mAdapter = new HomeRecycleViewAdapter();
        setUpSwipToRefresh();
        setUpRecyclerView();
        presenter.loadVilleToRecylerView();

        return view;
    }

    private void setUpSwipToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.loadVilleToRecylerView();
        });
    }


    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public void addVilleToAdapter(Ville ville) {
        mAdapter.addVille(ville);
    }

    @Override
    public void upDateVille(Ville ville, int position) {
        mAdapter.updateVille(ville, position);
    }

    @Override
    public void showProgressBar() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showEmptyContentTextView(String text) {
        emptyTownTextView.setText(text);
        emptyTownTextView.setVisibility(View.VISIBLE);
        containerLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyContentTextView() {
        emptyTownTextView.setVisibility(View.GONE);
        containerLayout.setVisibility(View.VISIBLE);
    }
}
