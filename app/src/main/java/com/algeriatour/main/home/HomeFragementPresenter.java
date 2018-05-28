package com.algeriatour.main.home;

public class HomeFragementPresenter {
    private HomeFragmentConstraint.ViewContsraint home_view;
    private HomeFragementModel homeFragementModel;
    public HomeFragementPresenter(HomeFragmentConstraint.ViewContsraint home_view) {
        this.home_view = home_view;
        homeFragementModel = new HomeFragementModel();
    }

    public void loadDataToRecylerView() {
        /*
        *   ArrayList<Ville> villes;
        *   villes = model.getVilles();
        *   for(villes.size){
        *       home_view.addVilleToAdapter( ville.get(i));
        *   }
        *
        *
         */
    }
}
