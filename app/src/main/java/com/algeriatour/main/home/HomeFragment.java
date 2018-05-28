package com.algeriatour.main.home;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algeriatour.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomeFragmentConstraint.ViewContsraint {

    @BindView(R.id.home_recyclerView)
    RecyclerView recyclerView;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        presenter = new HomeFragementPresenter(this);

        setUpRecyclerView();
        initFilterDialog();

        presenter.loadDataToRecylerView();


        return view;
    }

    private void initFilterDialog() {
        filterDialog = new FilterDialog(getContext());
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HomeRecycleViewAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.dialog_filter_btn)
    void onOpenDialogFilterClicked(){
        filterDialog.show();
        filterDialog.saveState();
    }

    @Override
    public void addVilleToAdapter( /* class ville */) {
        // Todo finish funciton
        mAdapter.addVille(/* class type ville */);
    }
}
