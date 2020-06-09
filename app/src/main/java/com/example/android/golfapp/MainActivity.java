package com.example.android.golfapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.android.golfapp.Data.GolfDatabase;
import com.example.android.golfapp.Data.GolfRecord;
import com.example.android.golfapp.Data.GolfViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, EnterFragment.EnterFragmentListener {
    private static final String TAG = "MainActivity";
    //UI elements
    FrameLayout container;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    //Database variables
    private GolfDatabase myGolfDatabase;
    GolfViewModel viewModel;

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

        final EnterFragment myEnterFragment = new EnterFragment();

        viewModel = new ViewModelProvider(this).get(GolfViewModel.class);
        viewModel.getNames().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> golfNames) {
                Log.d(TAG, "onChanged: " + "set names adapter");
                Set<String> removeDuplicatesSet = new HashSet<>(golfNames);
                String[] arr = removeDuplicatesSet.toArray(new String[removeDuplicatesSet.size()]);
                myEnterFragment.setNames(arr);
            }
        });
        viewModel.getCourses().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> golfCourses) {
                Log.d(TAG, "onChanged: " + "set courses adapter");
                Set<String> removeDuplicatesSet = new HashSet<>(golfCourses);
                String[] arr = removeDuplicatesSet.toArray(new String[removeDuplicatesSet.size()]);
                myEnterFragment.setCourses(arr);
            }
        });
        //Will need to replace this with data obtained from the database
        //((EnterFragment) myEnterFragment).setNames(new String[]{"Bob", "Tony", "Jeff"});

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, myEnterFragment).commit();

    }
    //Inserts the record in the database
    @Override
    public void onRecordSent(String name, String course, int par, int score, Date date) {
        Log.d(TAG, "onRecordSent: " + "name: " + name + ". Course: " + course + ". Par: " + par
        + ". Score: " + score + ". Date: " + date);
        final GolfRecord golfRecord = new GolfRecord(name, course, par, score, date);
        GolfViewModel viewModel = new ViewModelProvider(this).get(GolfViewModel.class);
        viewModel.insertRecord(golfRecord);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment myFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_enter:
                final EnterFragment enterFragment = new EnterFragment();
                viewModel.getNames().observe(this, new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> golfNames) {
                        Log.d(TAG, "onChanged: " + "set names adapter");
                        Set<String> removeDuplicatesSet = new HashSet<>(golfNames);
                        String[] arr = removeDuplicatesSet.toArray(new String[removeDuplicatesSet.size()]);
                        enterFragment.setNames(arr);
                    }
                });
                viewModel.getCourses().observe(this, new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> golfCourses) {
                        Log.d(TAG, "onChanged: " + "set courses adapter");
                        Set<String> removeDuplicatesSet = new HashSet<>(golfCourses);
                        String[] arr = removeDuplicatesSet.toArray(new String[removeDuplicatesSet.size()]);
                        enterFragment.setCourses(arr);
                    }
                });
                toolbar.setSubtitle(getString(R.string.toolbar_enter_subtitle));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, enterFragment).commit();
                break;
            case R.id.nav_list:
                ListFragment listFragment = new ListFragment(myGolfDatabase);
                toolbar.setSubtitle(getString(R.string.toolbar_list_subtitle));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, listFragment).commit();
                break;
            case R.id.nav_stats:
                StatsFragment statsFragment = new StatsFragment();
                toolbar.setSubtitle(getString(R.string.toolbar_stats_subtitle));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, statsFragment).commit();
                break;
            default:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, myFragment).commit();
                break;
        }
        return true;
    }
}
