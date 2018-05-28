package com.algeriatour.main.home;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.algeriatour.R;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class FilterDialog extends Dialog {
    @BindView(R.id.home_filter_listView)
    ListView filterList;
    boolean savedStateList[] = new boolean[48];

    ListFilterAdapter listFilterAdapter;

    public FilterDialog(@NonNull Context context) {
        super(context);
        // setUpAdapter
        setContentView(R.layout.home_dialog_wilaya_filter);
        ButterKnife.bind(this);

        setCancelable(false);
        ArrayList<Wilaya> wilayas = getWilayas();
        listFilterAdapter = new ListFilterAdapter(getContext(), wilayas);
        filterList.setAdapter(listFilterAdapter);
    }

    private ArrayList<Wilaya> getWilayas() {

        ArrayList<String> wilayasName = new ArrayList<>(
                Arrays.asList(getContext().getResources().getStringArray(R.array.algeria_wilayas)));
        ArrayList<Wilaya> wilaya = new ArrayList<>();

        for (String w : wilayasName) {
            wilaya.add(new Wilaya(w));
        }
        return wilaya;
    }


    @OnItemClick(R.id.home_filter_listView)
    public void onItemClick(AdapterView<?> adapterView, View view, int postion, long id) {
        if (listFilterAdapter.isSelected(postion)) {
            listFilterAdapter.unSelect(postion);
            Log.d("tixx", "onItemClick: " + postion + "status checked");
        } else {
            listFilterAdapter.select(postion);
            Log.d("tixx", "onItemClick: " + postion + "status unchecked");
        }

        listFilterAdapter.notifyDataSetChanged();

    }

    public void saveState() {
        for (int i = 0; i < 48; i++) {
            savedStateList[i] = listFilterAdapter.isSelected(i);
        }
    }

    @OnClick(R.id.home_dialoge_filter_cancel_btn)
    void onCancelButtonClick() {
        for (int i = 0; i < 48; i++) {
            if (savedStateList[i]) {
                listFilterAdapter.select(i);
            } else {
                listFilterAdapter.unSelect(i);
            }
        }
        listFilterAdapter.notifyDataSetChanged();
        hide();
    }


    @OnClick(R.id.home_dialoge_filter_done_btn)
    void onDoneButtonClick() {
        hide();
    }

}
