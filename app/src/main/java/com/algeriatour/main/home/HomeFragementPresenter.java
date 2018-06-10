package com.algeriatour.main.home;

import com.algeriatour.R;
import com.algeriatour.uml_class.Ville;
import com.algeriatour.utils.AlgeriaTourUtils;

import java.util.ArrayList;

public class HomeFragementPresenter implements HomeFragmentConstraint.PresenterConstraint{
    private HomeFragmentConstraint.ViewContsraint home_view;
    private HomeFragementModel homeFragementModel;
    public HomeFragementPresenter(HomeFragmentConstraint.ViewContsraint home_view) {
        this.home_view = home_view;
        homeFragementModel = new HomeFragementModel(this);
    }

    public void loadVilleToRecylerView() {
        home_view.hideEmptyContentTextView();
        home_view.showProgressBar();
        homeFragementModel.loadVilles();
    }

    @Override
    public void onLoadVillesSuccess(ArrayList<Ville> villes) {
        if(villes.isEmpty()){
            home_view.showEmptyContentTextView(AlgeriaTourUtils.getString(R.string.home_empty_ville));
        }
        else{
            for (int i = 0; i < villes.size(); i++) {
                home_view.addVilleToAdapter(villes.get(i));
            }
        }
        home_view.hideProgressBar();
    }

    @Override
    public void onLoadVillesFailed(String msg) {
        home_view.hideProgressBar();
        home_view.showEmptyContentTextView(msg);
    }

    @Override
    public void onLoadImageSuccess(Ville ville, int position) {
        home_view.upDateVille(ville, position);
    }
}
