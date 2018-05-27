package com.algeriatour.main.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.algeriatour.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.home_recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.home_spinner_filter)
    Spinner spinner_filter;

    private RecyclerView.LayoutManager mLayoutManager;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        HomeRecycleViewAdapter mAdapter = new  HomeRecycleViewAdapter();
        recyclerView.setAdapter(mAdapter);




        ArrayList<StateVO> listVOs = new ArrayList<>();

        listVOs.add(new StateVO("wilaya "));
        listVOs.add(new StateVO("alger"));
        listVOs.add(new StateVO("blida"));
        listVOs.add(new StateVO("Tizi Ouezou", true));
        listVOs.add(new StateVO("Setif"));
        listVOs.add(new StateVO("Oran"));
        listVOs.add(new StateVO("Tlemcen"));

        SpinnerAdapter myAdapter = new SpinnerAdapter(getActivity(), 0, listVOs);
        spinner_filter.setAdapter(myAdapter);





        return view;
    }

}
