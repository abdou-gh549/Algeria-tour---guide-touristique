package com.algeriatour.main;

import com.algeriatour.R;
import com.algeriatour.utils.StaticValue;
import com.algeriatour.utils.User;

public class MainPresenter {
    private MainConstraint.IViewConstraint m_mainView;

    MainPresenter(MainConstraint.IViewConstraint mainView) {
        m_mainView = mainView;
    }

    boolean navigationItemSelected(int itemId) {
        switch (itemId) {
            case R.id.nav_menu_profile:
                m_mainView.startProfileActivity();
                break;

            case R.id.nav_menu_contactUs:
                m_mainView.startContactUs();
                break;
            case R.id.nav_menu_logout:
                // change static var
                User.disconnect();
                // logout
                m_mainView.disconnect();
                break;
            case R.id.nav_menu_quit:
                m_mainView.exitApp();
                break;
            case R.id.nav_menu_creatAccount:
                m_mainView.openLoginActivity();
                break;
        }

        m_mainView.closeDrawer();

        return true;
    }

    void onBackPressed(boolean drawerIsOpen){
        if(drawerIsOpen){
            m_mainView.closeDrawer();
        }else{
            m_mainView.defaultBackPress();
        }
    }

}
