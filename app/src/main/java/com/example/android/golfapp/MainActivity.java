package com.example.android.golfapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.FrameLayout;

import com.example.android.golfapp.Data.GolfDatabase;
import com.example.android.golfapp.Data.GolfRecord;
import com.example.android.golfapp.Data.GolfViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    String[] myGolfNames;


    private Menu myMenu;
    boolean menuVisible;

    int previousFragment = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(GolfViewModel.class);
        viewModel.getNames().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                Set<String> removeDuplicatesSet = new HashSet<>(strings);
                String[] arr = removeDuplicatesSet.toArray(new String[removeDuplicatesSet.size()]);
                setMyGolfNames(arr);
            }
        });




        container = findViewById(R.id.fragment_container);
        bottomNavigationView = findViewById(R.id.bott_nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_app_icon);


        myGolfDatabase = GolfDatabase.getInstance(this);


        //Will need to replace this with data obtained from the database
        //((EnterFragment) myEnterFragment).setNames(new String[]{"Bob", "Tony", "Jeff"});
        if (savedInstanceState == null) {
            toolbar.setSubtitle(getString(R.string.toolbar_enter_subtitle));
            hideMenus();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.fragment_container, new EnterFragment()).commit();

        } else {
            toolbar.setSubtitle(savedInstanceState.getString("toolbarSubtitle"));
            menuVisible = savedInstanceState.getBoolean("showMenu");
           /* if (savedInstanceState.getBoolean("showMenu")) {
                Log.d(TAG, "onCreate: " + savedInstanceState.getBoolean("showMenu"));
                showMenus();
            } else {
                hideMenus();
            }*/
        }
    }

    public void setMyGolfNames(String[] myGolfNames) {
        this.myGolfNames = myGolfNames;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("toolbarSubtitle", toolbar.getSubtitle().toString());
        outState.putBoolean("showMenu", menuVisible);
        super.onSaveInstanceState(outState);
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (menuItem.getItemId()) {
            case R.id.nav_enter:
                toolbar.setSubtitle(getString(R.string.toolbar_enter_subtitle));
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.fragment_container, new EnterFragment()).commit();
                //hideMenus();
                previousFragment = 0;
                break;
            case R.id.nav_list:
                toolbar.setSubtitle(getString(R.string.toolbar_list_subtitle));
                if (previousFragment == 0) {
                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left);
                }
                transaction.replace(R.id.fragment_container, new ListFragment()).commit();
                //showMenus();
                previousFragment = 1;
                break;
            case R.id.nav_stats:
                toolbar.setSubtitle(getString(R.string.toolbar_stats_subtitle));
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_right);
                transaction.replace(R.id.fragment_container, new StatsFragment()).commit();
                //hideMenus();
                previousFragment = 2;
                break;
            default:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new EnterFragment()).commit();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        myMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        myMenu.setGroupVisible(R.id.settingsItemsToHide, false);
        if (menuVisible) {
            showMenus();
        } else {
            hideMenus();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sortAndFilterSettings) {
            //Pass name data here
            if (myGolfNames != null) {
                Intent intent = new Intent(this, SettingsActivity.class);

                intent.putExtra("Names", myGolfNames);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void hideMenus() {
        Log.d(TAG, "hideMenus: " + myMenu);
        if (myMenu != null) {
            myMenu.setGroupVisible(R.id.settingsItemsToHide, false);
            menuVisible = false;
        }
    }
    public void showMenus() {
        Log.d(TAG, "showMenus: " + myMenu);
        if (myMenu != null) {
            myMenu.setGroupVisible(R.id.settingsItemsToHide, true);
            menuVisible = true;
        }
    }

/*    @Override
    public void onBackPressed() {

        if (myEnterFragment != null && !myEnterFragment.isVisible()) {
            //onNavigationItemSelected(bottomNavigationView.getMenu().getItem(0));
            bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(0).getItemId());
        } else {
            super.onBackPressed();
        }


    }*/
}
