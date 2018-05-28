package com.algeriatour.profile;

public class ProfileModel implements ProfileConstraint.ModelConstraint {
    @Override
    public boolean userNameExist(String userName) {
        return false;
    }

    @Override
    public boolean emailExist(String email) {
        return false;
    }

    @Override
    public boolean changePassword(int userId, String psw) {
        return false;
    }

    @Override
    public boolean changeEmail(int userId, String email) {
        return false;
    }

    @Override
    public boolean changePseudo(int userId, String pseudo) {
        return false;
    }


}
