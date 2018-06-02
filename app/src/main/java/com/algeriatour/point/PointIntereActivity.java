package com.algeriatour.point;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.algeriatour.R;
import com.algeriatour.uml_class.Commentaire;
import com.algeriatour.uml_class.PlaceInfo;
import com.algeriatour.uml_class.PointInteret;
import com.algeriatour.utils.StaticValue;
import com.algeriatour.utils.User;
import com.androidnetworking.AndroidNetworking;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class PointIntereActivity extends AppCompatActivity implements PointIneteretConstraint.ViewConstraint {
    @BindView(R.id.centreIntere_comment_recycleView)
    RecyclerView commentRecyclerView;

    @BindView(R.id.centreIntere_scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.centreIntere_toolbar)
    Toolbar toolbar;

    @BindView(R.id.centre_intere_image)
    ImageView villeImageView;
    @BindView(R.id.centre_intere_descreption_descreption_txtv)
    TextView pointInteretDescreption;
    @BindView(R.id.centre_intere_descreption_rattingBar)
    RatingBar pointInteretRattingBar;
    @BindView(R.id.centre_intere_descreption_rattingValue)
    TextView pointInteretRattingValue;
    @BindView(R.id.centre_intere_descreption_type)
    TextView pointInteretType;
    @BindView(R.id.centre_intere_descreption_ville)
    TextView pointInteretVille;
    @BindView(R.id.centre_intere_swipToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.centreIntere_comment_text_info)
    TextView commentTextInfo;

    SpotsDialog progressDialog;


    private LinearLayoutManager mLayoutManager;
    private PointIntereRecyclerAdapter mrecyclerViewAdapter;
    private PointInteret pointInteret;
    private PointInteretPresneter presneter;
    private Dialog addFavoriteDialog;


    @OnClick(R.id.centre_intere_favorite_fab)
    void onAddToFavoriteClick() {
        if(User.getUserType() == StaticValue.VISITOR){
            showToastInformation("you need to be member for use add to favorite");
        }else{
            presneter.checkIfFavoriteExist(pointInteret.getId());
        }
    }

    @OnClick(R.id.centre_intere_comment_fab)
    void onAddCommentClick() {
        if(User.getUserType() == StaticValue.VISITOR){
            showToastInformation("you need to be member for adding a comment");
        }else{
            showToastInformation("add comment clicked");

        }
    }

    @OnClick(R.id.centre_intere_map_fab)
    void onOpenInMapClick() {
        showToastInformation("open in map clicked");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.centre_intere_activity);
        ButterKnife.bind(this);
        pointInteret = new PointInteret();
        presneter = new PointInteretPresneter(this);
        progressDialog = new SpotsDialog(this);
        progressDialog.setCancelable(false);
        setUpToolBar();
        setUpCommentAdapter();
        setUpSwipToRefresh();
        loadPointInteretInformation();

        presneter.loadCommentaire(pointInteret.getId());
        initAddToFavoriteDialog();

    }

    private void initAddToFavoriteDialog() {
        addFavoriteDialog = new Dialog(this);
        addFavoriteDialog.setContentView(R.layout.centre_intere_add_favorite);
        addFavoriteDialog.setCancelable(false);
        EditText noteEditText = addFavoriteDialog.findViewById(R.id.centre_intere_addFavorite_note);
        Button addButton = addFavoriteDialog.findViewById(R.id.centre_intere_addFavorite_add);
        Button cancelButton = addFavoriteDialog.findViewById(R.id.centre_intere_addFavorite_cancel);
        cancelButton.setOnClickListener(view -> addFavoriteDialog.dismiss());
        addButton.setOnClickListener(view -> {
            presneter.addToFavorite(pointInteret.getId(), noteEditText.getText().toString());
        });
    }

    private void setUpSwipToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            AndroidNetworking.cancelAll();
            presneter.loadCommentaire(pointInteret.getId());
            presneter.loadPointIneteretImage(pointInteret.getId());
        });
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

    }

    private void loadPointInteretInformation() {
        PlaceInfo placeInfo = (PlaceInfo) getIntent().getSerializableExtra(StaticValue.POINT_TAG);
        pointInteret.setPlaceInfo(placeInfo);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(pointInteret.getName());
        }
        pointInteretDescreption.setText(pointInteret.getDescreption());
        pointInteretType.setText(pointInteret.getType());
        pointInteretVille.setText(pointInteret.getCompletAdress());
        pointInteretRattingValue.setText(Float.toString(pointInteret.getRate()));
        pointInteretRattingBar.setRating(pointInteret.getRate());

        presneter.loadPointIneteretImage(pointInteret.getId());


    }

    private void setUpCommentAdapter() {
        mLayoutManager = new LinearLayoutManager(this);
        mrecyclerViewAdapter = new PointIntereRecyclerAdapter();
        commentRecyclerView.setHasFixedSize(true);
        commentRecyclerView.setLayoutManager(mLayoutManager);
        commentRecyclerView.setAdapter(mrecyclerViewAdapter);
    }

    @Override
    public void showProgressBar() {
        commentTextInfo.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void showAddFavoriteDialog() {
        addFavoriteDialog.show();
    }

    @Override
    public void hideAddFavoriteDialog() {
        addFavoriteDialog.dismiss();
    }

    @Override
    public void showToastInformation(String msg) {
        Toasty.info(this, msg, Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void showToastError(String msg) {
        Toasty.error(this, msg, Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void showTextInDispalyInfor(String msg) {
        commentTextInfo.setVisibility(View.VISIBLE);
        commentTextInfo.setText(msg);
    }

    @Override
    public void showNotificationTaost(String msg) {
        Toasty.info(this, msg, Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void showToastSuccess(String msg) {
        Toasty.success(this, msg, Toast.LENGTH_LONG, true).show();

    }

    @Override
    public void setPointInteretImage(Bitmap villeImage) {
        villeImageView.setImageBitmap(villeImage);
        pointInteret.setImage(villeImage);
    }


    @Override
    public void addComment(Commentaire commentaire) {
        mrecyclerViewAdapter.addComment(commentaire);
    }


}
