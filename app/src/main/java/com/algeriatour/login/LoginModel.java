package com.algeriatour.login;

import com.algeriatour.utils.StaticValue;

public class LoginModel implements LoginConstraint.modelConstraint {

    @Override
    public boolean userExist(String email, String psw) {


        return StaticValue.isUser = StaticValue.userExist;
    }
}
