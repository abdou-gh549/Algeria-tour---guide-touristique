package com.algeriatour.villes;

public class VillePresenter {


    private VilleConstraint.ViewConstraint ville_view;
    private VilleModel villeModel;

    public VillePresenter(VilleConstraint.ViewConstraint ville_view) {
        this.ville_view = ville_view;
        villeModel = new VilleModel();
    }

    public void setUpData() {
        /*
        *   Ville ville = villeModel.getVilleInformation(villeId)
        *   ville_view.setVilleDescreption(ville.getDesception)
        *   ville_view.setVilleName( ville.getName)
        *   ville_view.setVilleImage( ville_view.getImage)
        * */

        /*
        *   ArrayList<CentreInteret> centreInterets = villeModel.getVilleCentreInteret();
        *   for( centre: centreInterets){
        *       ville_view.addCentreInteret(centre);
        *   }
         */

    }
}
