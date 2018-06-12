package com.algeriatour.main.favorite;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import dmax.dialog.SpotsDialog;
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

    private LinearLayoutManager mLayoutManager;
    private FavoriteRecycleViewAdapter recyclerViewAdapter;
    private FavoritePresenter presenter;
    private SpotsDialog progressDialog;
    private Dialog editFavorite;

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
        progressDialog = new SpotsDialog(getActivity());
        progressDialog.setCancelable(false);

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

    @Override
    public void onResume() {
        Log.d("tixx", "onResume: favrotie");
        if (User.getUserType() == StaticValue.MEMBER)
             presenter.loadFavoriteList();
        super.onResume();
    }

    private void setUpSwipToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (User.getUserType() == StaticValue.VISITOR) {
              swipeRefreshLayout.setRefreshing(false);
            }else{
                presenter.loadFavoriteList();
            }
        });
    }

    private void setUpAdapter() {
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewAdapter = new FavoriteRecycleViewAdapter(this);
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
        recyclerViewAdapter.addFavortie(favorite);
    }

    @Override
    public void setFavoriteImage(Bitmap image, long position) {
        recyclerViewAdapter.setFavoriteImage(image, position);
    }

    @Override
    public void deleteFavoriteClick(long favoriteId) {
        presenter.deleteFavorite(favoriteId);
    }

    @Override
    public void showProgressRefresh() {
        informationTextView.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.dismiss();
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

    @Override
    public void removeFavorite(long favoriteID) {
        recyclerViewAdapter.removeFavorite(favoriteID);
    }

    @Override
    public void editFavoriteClick(Favorite favorite) {
        editFavorite = new Dialog(getActivity());
        editFavorite.setContentView(R.layout.favorite_edit_item);
        editFavorite.setCancelable(false);
        EditText noteEditText = editFavorite.findViewById(R.id.favorite_edit_item_note);
        Button saveButton = editFavorite.findViewById(R.id.favorite_edit_item_save);
        Button cancelButton = editFavorite.findViewById(R.id.favorite_edit_item_cancel);
        noteEditText.setText(favorite.getNote());
        cancelButton.setOnClickListener(view -> editFavorite.dismiss());
        saveButton.setOnClickListener(view -> {
            String newNote = noteEditText.getText().toString();
            if (favorite.getNote().equals(newNote)) {
                showInformationToast(getString(R.string.favorite_edit_nothing_to_change_message));
            } else {
                showProgressDialog();
                presenter.updateNoteOfFavorite(favorite.getFavoriteId(), newNote);
            }
        });
        editFavorite.show();
    }

    @Override
    public void hideEditDialog() {
        editFavorite.dismiss();
    }

    @Override
    public void showInformationToast(String s) {
        Toasty.info(getActivity(), s, Toast.LENGTH_LONG, true).show();
    }
}
