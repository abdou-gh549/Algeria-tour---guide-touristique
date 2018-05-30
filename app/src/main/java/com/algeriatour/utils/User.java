package com.algeriatour.utils;

import android.app.Application;
/**
 *  class to set important static var as user type
 * */
public class User extends Application{
    private static int userType = StaticValue.VISITOR;
    
    private static User singleton;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    static public int getUserType(){
        return userType;
    }

    static public void setUserType(int type){
        userType = type;
    }


    public static User getInstance() {
        return singleton;
    }


}
