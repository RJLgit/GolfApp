package com.example.android.golfapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    //UI elements
    FrameLayout container;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;

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
        
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new EnterFragment()).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment myFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_enter:
                myFragment = new EnterFragment();
                break;
            case R.id.nav_list:
                myFragment = new ListFragment();
                break;
            case R.id.nav_stats:
                myFragment = new StatsFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, myFragment).commit();
        return true;
    }
}
