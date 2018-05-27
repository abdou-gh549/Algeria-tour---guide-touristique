package com.algeriatour.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.algeriatour.profile.ProfileActivity;
import com.algeriatour.R;
import com.algeriatour.main.favorite.FavoriteFragment;
import com.algeriatour.main.home.HomeFragment;
import com.algeriatour.main.searche.SearchFragment;
import com.algeriatour.utils.StaticValue;

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
    private MainPresenter mainPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        mainPresenter = new MainPresenter(this);

        // declar and add fragment to view Pager adapter
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new HomeFragment(), R.drawable.ic_home_black_24dp);
        viewPagerAdapter.addFragment(new FavoriteFragment(), R.drawable.ic_favorite);
        viewPagerAdapter.addFragment(new SearchFragment(), R.drawable.ic_search_black);

        setUpDrawer();


        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setUpTabLayout();

        /*************************/

    }

    private void setUpDrawer() {
        navDrawerBtn.setOnClickListener(e -> drawer.openDrawer(Gravity.START));
        navigationView.setNavigationItemSelectedListener(this);

        if(StaticValue.isUser){
            navigationView.inflateMenu(R.menu.main_drawer_user);

        }else{
            navigationView.inflateMenu(R.menu.main_drawer_visitor);
            LinearLayout headerView = (LinearLayout) navigationView.getHeaderView(0);
            headerView  .removeAllViews();

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
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


    @Override
    public void startContactUs() {
        Toast.makeText(this, "contact Us CLicked", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void exitApp() {
        finish();
    }


}
