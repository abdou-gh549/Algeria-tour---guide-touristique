package com.algeriatour.villes;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.algeriatour.R;
import com.algeriatour.uml_class.PlaceInfo;
import com.algeriatour.uml_class.PointInteret;
import com.algeriatour.uml_class.Ville;
import com.algeriatour.utils.Networking;
import com.algeriatour.utils.StaticValue;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class VilleActivity extends AppCompatActivity implements VilleConstraint.ViewConstraint {


    @BindView(R.id.ville_toolbar)
    Toolbar toolbar;

    @BindView(R.id.ville_centreInteret_recycleView)
    RecyclerView villeRecyclerView;

    @BindView(R.id.ville_descreption)
    TextView ville_descreption;
    @BindView(R.id.ville_diaplayInfoTextView)
    TextView displayInfoTextView;

    @BindView(R.id.ville_image)
    ImageView ville_image;

    @BindView(R.id.ville_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.ville_scrollView)
    NestedScrollView scrollView;

    private LinearLayoutManager mLayoutManager;
    private VilleRecyclerViewAdapter mrecyclerViewAdapter;
    private VillePresenter presenter;

    private Ville ville;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ville_activity);
        ButterKnife.bind(this);
        Networking.initAndroidNetworking(this);
        ville = new Ville();
        presenter = new VillePresenter(this);
        setUpActionToolBar();
        setUpAdapter();
        setUpVilleData();

        presenter.loadPointIntere(ville.getId());
    }

    private void setUpVilleData() {
        // get data
        PlaceInfo placeInfo;
        placeInfo = (PlaceInfo) getIntent().getSerializableExtra(StaticValue
                .VILLE_WITHOUT_IMAGE_TAG);

        ville.setPlaceInfo(placeInfo);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(ville.getName());
        ville_descreption.setText(ville.getDescreption());

        // load ville image
        presenter.loadVilleImage(ville);
    }

    private void setUpActionToolBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

    }

    private void setUpAdapter() {
        villeRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mrecyclerViewAdapter = new VilleRecyclerViewAdapter();
        villeRecyclerView.setLayoutManager(mLayoutManager);
        villeRecyclerView.setAdapter(mrecyclerViewAdapter);

        scrollView.smoothScrollTo(0, 0);

    }


    @Override
    public void showProgressBar() {
        displayInfoTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setVilleImage(Bitmap villeImage) {
        ville.setImage(villeImage);
        ville_image.setImageBitmap(villeImage);
    }

    @Override
    public void addPointInteret(PointInteret pointInteret) {
        pointInteret.setWilaya(ville.getWilaya());
        mrecyclerViewAdapter.addPointInteret(pointInteret);

    }

    @Override
    public void setPointInteretImage(PointInteret pointInteret, int position) {
        mrecyclerViewAdapter.setPointInteretImage(pointInteret.getImage(), position);
    }

    @Override
    public void showToastError(String msg) {
        Toasty.error(this, msg, Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void showTextInDispalyInfor(String msg) {
        displayInfoTextView.setVisibility(View.VISIBLE);
        hideProgressBar();
        displayInfoTextView.setText(msg);
    }
}
