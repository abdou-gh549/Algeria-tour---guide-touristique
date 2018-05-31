package com.algeriatour.main.home;

import com.algeriatour.uml_class.Ville;

import java.util.ArrayList;

public class HomeFragmentConstraint {

    public interface ViewContsraint{
        void addVilleToAdapter(Ville villes);
        void upDateVille(Ville ville, int postion);
        void showProgressBar();
        void hideProgressBar();
        void showEmptyContentTextView(String text);
        void hideEmptyContentTextView();
    }

    public interface ModelConstraint{
        void loadVilles();
        void loadVillesImage(Ville ville, int position);
    }

    public interface PresenterConstraint{
        void onLoadVillesSuccess(ArrayList<Ville> villes);
        void onLoadVillesFailed(String msg);
        void onLoadImageSuccess(Ville ville, int position);
    }


}
