package com.algeriatour.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algeriatour.R;
import com.algeriatour.SplashActivity;
import com.algeriatour.contect_nous.ContacterNousActivity;
import com.algeriatour.login.LoginActivity;
import com.algeriatour.main.favorite.FavoriteFragment;
import com.algeriatour.main.home.HomeFragment;
import com.algeriatour.main.searche.SearchFragment;
import com.algeriatour.profile.ProfileActivity;
import com.algeriatour.utils.StaticValue;
import com.algeriatour.utils.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainConstraint.IViewConstraint {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.appBar_navDrawerImageView)
    ImageButton navDrawerBtn;
    @BindView(R.id.main_viewPager)
    ViewPager viewPager;

    @BindView(R.id.main_tabLayout)
    TabLayout tabLayout;

    private ViewPagerAdapter viewPagerAdapter;
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        mainPresenter = new MainPresenter(this);
        Log.d("lifecycle", "Main OnCreat");

        // get user type
        // if the email is empty so there is no success authentification  -> user is visitor
        if (User.getUserType() == StaticValue.MEMBER) {
            saveLoginTosharedPreference(User.getMembre().getPseudo(), User.getMembre().getPassword());
        }
        // declar and add fragment to view Pager adapter
        setUpViewPager();
        setUpDrawer();

    }


    private void setUpViewPager() {
        Log.d("main", "setUpViewPager: init view pager");
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new HomeFragment(), R.drawable.ic_home_black_24dp);
        viewPagerAdapter.addFragment(new FavoriteFragment(), R.drawable.ic_favorite);
        viewPagerAdapter.addFragment(new SearchFragment(), R.drawable.ic_search_black);

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        setUpTabLayout();
    }

    private void setUpDrawer() {
        navDrawerBtn.setOnClickListener(e -> drawer.openDrawer(Gravity.START));
        navigationView.setNavigationItemSelectedListener(this);
        if (User.getUserType() == StaticValue.MEMBER) {
            makeItMembreDrawer();
            setDrawerEmail(User.getMembre().getEmail());
            setDrawerPseudo(User.getMembre().getPseudo());
        } else {
            makeItVisitorDrawer();
        }
    }

    private void setUpTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTabColor(tab, R.color.appBarSelectedIcon);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTabColor(tab, R.color.appBarIcon);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                setTabColor(tab, R.color.appBarSelectedIcon);
            }
        });

        for (int i = 0; i < viewPagerAdapter.getCount(); i++) {
            tabLayout.getTabAt(i).setIcon(viewPagerAdapter.getFragmentImageId(i));
            tabLayout.getTabAt(i).getIcon().setTint(ContextCompat.getColor(this, R.color
                    .appBarIcon));

        }

        if (viewPagerAdapter.getCount() > 0) {
            tabLayout.getTabAt(0).select();
        }


    }

    private void setTabColor(TabLayout.Tab tab, int colorId) {
        Drawable icon = tab.getIcon();
        if (icon != null) {
            icon.setTint(ContextCompat.getColor(MainActivity.this, colorId));
        }
    }

    @Override
    public void onBackPressed() {
        mainPresenter.onBackPressed(drawer.isDrawerOpen(GravityCompat.START));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return mainPresenter.navigationItemSelected(item.getItemId());
    }

    @Override
    public void defaultBackPress() {
        super.onBackPressed();
    }

    @Override
    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void startProfileActivity() {
        try {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } catch (Exception exp) {
            Toasty.error(this, "sothing happend cant open profile", Toast.LENGTH_LONG, true).show();
        }

    }

    @Override
    public void startContactUs() {
        Intent intent = new Intent(this, ContacterNousActivity.class);
        startActivity(intent);
    }

    @Override
    public void exitApp() {
        finish();
    }

    @Override
    public void makeItVisitorDrawer() {
        navigationView.inflateMenu(R.menu.main_drawer_visitor);
        LinearLayout headerView = (LinearLayout) navigationView.getHeaderView(0);
        headerView.removeAllViews();
    }

    @Override
    public void makeItMembreDrawer() {
        navigationView.inflateMenu(R.menu.main_drawer_user);
    }

    @Override
    public void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void setDrawerEmail(String email) {
        LinearLayout headerView = (LinearLayout) navigationView.getHeaderView(0);
        TextView emailTextView = headerView.findViewById(R.id.nav_header_emailTextView);
        emailTextView.setText(email);
    }

    @Override
    public void setDrawerPseudo(String pseudo) {
        LinearLayout headerView = (LinearLayout) navigationView.getHeaderView(0);
        TextView pseudoTextView = headerView.findViewById(R.id.nav_header_pseudoTextView);
        pseudoTextView.setText(pseudo);
    }

    @Override
    public void disconnect() {
        removeLoginFromSharedPreference();
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void saveLoginTosharedPreference(String pseudo, String psw) {
        Log.d("tixx", "saveLoginTosharedPreference: pseudo = " + pseudo + " psw = " + psw);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences
                (StaticValue.LOGIN_SHARED_PEFERENCE,
                        MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StaticValue.PSEUDO_TAGE, pseudo);
        editor.putString(StaticValue.PASSWORD_TAGE, psw);
        editor.apply();
    }

    private void removeLoginFromSharedPreference() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(StaticValue
                .LOGIN_SHARED_PEFERENCE, MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        Log.d("tixx", "removeLoginFromSharedPreference: called");
    }
}
