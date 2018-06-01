package com.algeriatour.utils;

import android.app.Application;

import com.algeriatour.uml_class.Membre;

/**
 * class to set important static var as user type
 */
public class User extends Application {
    private static int userType = StaticValue.VISITOR;

    private static User singleton;
    private static Membre membre;

    public static void connect(Membre m) {
        if (m != null) {
            userType = StaticValue.MEMBER;
            User.membre = m;
        }
    }

    public static void disconnect() {
        membre = null;
        userType = StaticValue.VISITOR;
    }

    public static void getMembre(Membre membre) {
        User.membre = membre;
    }

    public static Membre getMembre() {
        return membre;
    }


    static public int getUserType() {
        return userType;
    }


    public static User getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }


}
