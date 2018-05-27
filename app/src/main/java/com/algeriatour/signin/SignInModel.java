package com.algeriatour.signin;

public class SignInModel implements SignInConstraint.ModelConstraint {
    @Override
    public boolean emailAlreadyExist(String Email) {
        // TODO check if email exist
        return false;
    }

    @Override
    public boolean pseudoAlreadyExist(String Email) {
        // TODO check if pseudo exist
        return false;
    }

    @Override
    public boolean signIn(String pseudo, String email, String password) {
        //TODO SignIn
        return true;
    }
}
