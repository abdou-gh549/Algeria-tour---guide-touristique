package com.algeriatour.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.*;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.algeriatour.ProfileActivity;
import com.algeriatour.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        // declar and add fragment to view Pager adapter
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new HomeFragment(), R.drawable.ic_home_black_24dp);
        viewPagerAdapter.addFragment(new FavoriteFragment(), R.drawable.ic_favorite_black_24dp);
        viewPagerAdapter.addFragment(new SearchFragment(), R.drawable.ic_search_black);

        navDrawerBtn.setOnClickListener(e -> drawer.openDrawer(Gravity.START));
        navigationView.setNavigationItemSelectedListener(this);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setUpTabLayout();

    }

    private void setUpTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setTint(ContextCompat.getColor(MainActivity.this, R.color
                        .appBarSelectedIcon));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setTint(ContextCompat.getColor(MainActivity.this, R.color
                        .appBarIcon));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tab.getIcon().setTint(ContextCompat.getColor(MainActivity.this, R.color
                        .appBarSelectedIcon));
            }
        });

        for (int i = 0; i < viewPagerAdapter.getCount(); i++) {
            tabLayout.getTabAt(i).setIcon(viewPagerAdapter.getFragmentImageId(i));
        }
        if (viewPagerAdapter.getCount() > 0) {
            tabLayout.getTabAt(0).select();
        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_menu_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_menu_maPosition:
                Toast.makeText(this, "ma position clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_menu_contactUs:
                Toast.makeText(this, "contact Us CLicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_menu_logout:
                Toast.makeText(this, "logout clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_menu_quit:
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
