package com.algeriatour.main.home;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.algeriatour.R;
import com.algeriatour.uml_class.Ville;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeFragmentConstraint.ViewContsraint {

    @BindView(R.id.home_recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.home_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.home_empty_town_textview)
    TextView emptyTownTextView;

    @BindView(R.id.home_container_layout)
    View containerLayout;

    HomeRecycleViewAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;
    private HomeFragementPresenter presenter;
    private FilterDialog filterDialog;

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
        presenter = new HomeFragementPresenter(this);
        mAdapter = new HomeRecycleViewAdapter();
        presenter.loadVilleToRecylerView();
        setUpRecyclerView();
        initFilterDialog();

        return view;
    }

    private void initFilterDialog() {
        filterDialog = new FilterDialog(getContext());
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.dialog_filter_btn)
    void onOpenDialogFilterClicked() {
        filterDialog.show();
        filterDialog.saveState();
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
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
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
