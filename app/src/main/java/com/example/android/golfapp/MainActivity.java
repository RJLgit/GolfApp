package com.example.android.golfapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.android.golfapp.Data.GolfDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, EnterFragment.EnterFragmentListener {
    private static final String TAG = "MainActivity";
    //UI elements
    FrameLayout container;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    //Database variables
    private GolfDatabase myGolfDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.fragment_container);
        bottomNavigationView = findViewById(R.id.bott_nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_app_icon);

        toolbar.setSubtitle(getString(R.string.toolbar_enter_subtitle));

        myGolfDatabase = GolfDatabase.getInstance(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new EnterFragment()).commit();

    }

    @Override
    public void onRecordSent(String name, String course, int par, int score, Date date) {
        Log.d(TAG, "onRecordSent: " + "name: " + name + ". Course: " + course + ". Par: " + par
        + ". Score: " + score + ". Date: " + date);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment myFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_enter:
                myFragment = new EnterFragment();
                toolbar.setSubtitle(getString(R.string.toolbar_enter_subtitle));
                break;
            case R.id.nav_list:
                myFragment = new ListFragment();
                toolbar.setSubtitle(getString(R.string.toolbar_list_subtitle));
                break;
            case R.id.nav_stats:
                myFragment = new StatsFragment();
                toolbar.setSubtitle(getString(R.string.toolbar_stats_subtitle));
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, myFragment).commit();
        return true;
    }
}
