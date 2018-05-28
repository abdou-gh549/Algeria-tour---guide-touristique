package com.algeriatour.main.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.algeriatour.R;

import java.util.ArrayList;

public class ListFilterAdapter extends ArrayAdapter<Wilaya> {

    public ListFilterAdapter(Context context, ArrayList<Wilaya> wilaya) {
        super(context, 0, wilaya);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout
                    .home_list_item_filtre, null);
        }
        TextView txtv = convertView.findViewById(R.id.home_item_filter_textView);
        CheckBox checkBox = convertView.findViewById(R.id.home_filter_checkBox);
        txtv.setText(getItem(position).getTitle());
        checkBox.setChecked(getItem(position).isSelected());
        return convertView;
    }

    public boolean isSelected(int position) {
        try {
            return getItem(position).isSelected();
        } catch (Exception exp) {
            Log.d("tixx", "isSelected: " + "position not found " + position);
            return false;
        }
    }

    public void select(int position) {
        try {
            getItem(position).setSelected(true);
        } catch (Exception exp) {
            Log.d("tixx", "select: " + "position not found " + position);
        }
    }

    public void unSelect(int position) {
        try {
            getItem(position).setSelected(false);

        } catch (Exception exp) {
            Log.d("tixx", "unSelect: " + "position not found " + position);
        }
    }
}