package com.algeriatour.resetpassword;

import com.algeriatour.R;
import com.algeriatour.utils.AlgeriaTourUtils;

public class ResetPasswordPresenter {
    ResetPasswordConstraint.ViewConstraint resetPsw_view;

    public ResetPasswordPresenter(ResetPasswordConstraint.ViewConstraint resetPsw_view) {
        this.resetPsw_view = resetPsw_view;
    }

    public void sendPasswordClicked() {
        String email = resetPsw_view.getEmail();
        String pseudo = resetPsw_view.getPseudo();
        if( ! checkInput(email, pseudo))
            return;
        /*
        * model.getUser(pseudo, name)
        * if( return == null )
        *   show error user doesn't exist
        * else
        *   send password to email
        */

        // suppose que user always exist
        resetPsw_view.sendPassword("abdellah.abdou21@gmail.com", "this is password");
    }

    private boolean checkInput(String email, String pseudo){
        boolean inputValid = true;
        if (email.isEmpty()) {
            resetPsw_view.showEmailError(resetPsw_view.getStringFromRessource(R.string.empty_email_error));
            inputValid = false;
        } else if (!AlgeriaTourUtils.isValidEmail(email)) {
            resetPsw_view.showEmailError(resetPsw_view.getStringFromRessource(R.string.invalid_email_format_error));
            inputValid = false;
        }

        if (pseudo.isEmpty()) {
            resetPsw_view.showPseudoError(resetPsw_view.getStringFromRessource(R.string.empty_email_error));
            inputValid = false;
        } else if (pseudo.contains(" ")) {
            resetPsw_view.showPseudoError(resetPsw_view.getStringFromRessource(R.string.space_pseudo_error));
            inputValid = false;
        }

        return inputValid;
    }
}
