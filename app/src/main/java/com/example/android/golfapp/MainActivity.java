package com.example.android.golfapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
    GolfViewModel viewModel;
    String[] myGolfNames;

    //Menu variables
    private Menu myMenu;
    boolean menuVisible;

    //Helper variable to handle transition animations
    int previousFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //View model observes the player names in the database, and when it is updated it updates the myGolfNames variable
        //This is needed because the options menu is set in the activity. And when the settings menu item is clicked it needs
        //to send this information in an intent to the settings activity to populate one of the settings.
        viewModel = new ViewModelProvider(this).get(GolfViewModel.class);
        viewModel.getNames().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                Set<String> removeDuplicatesSet = new HashSet<>(strings);
                String[] arr = removeDuplicatesSet.toArray(new String[removeDuplicatesSet.size()]);
                setMyGolfNames(arr);
            }
        });
        //Assigns the UI elements to variables
        container = findViewById(R.id.fragment_container);
        bottomNavigationView = findViewById(R.id.bott_nav_bar);
        toolbar = findViewById(R.id.toolbar);
        //This activity implements the OnNavigationItemSelectedListener interface, so it listens for clicks on the bottom nav bar
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        //The toolbar is set as the action bar with the logo set too
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_app_icon);

        //If there is no savedinstance state then want to load the EnterFragment into the container. This is the behaviour for when the activity launches
        if (savedInstanceState == null) {
            toolbar.setSubtitle(getString(R.string.toolbar_enter_subtitle));
            //Hides the menu as do not want to allow sort/filter when the Enterfragment is in the container
            hideMenus();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //Sets the animation for the loading of the fragment
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.fragment_container, new EnterFragment()).commit();
        } else {
            //This is the behaviour when the app is rotated or oncreate it called for some other reason and there is a savedinstancestate.
            //Gets the toolbar subtitle and a boolean of whether to show the menu from the savedinstancestate object.
            toolbar.setSubtitle(savedInstanceState.getString(getString(R.string.saved_instance_toolbar_key)));
            menuVisible = savedInstanceState.getBoolean(getString(R.string.saved_instance_menu_key));
        }
    }

    //This method records the toolbar subtitle and whether the menu is visibile in the bundle so it can be used to restore the
    //activity to the correct state when the activity is restarted via rotation or another reason
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(getString(R.string.saved_instance_toolbar_key), toolbar.getSubtitle().toString());
        outState.putBoolean(getString(R.string.saved_instance_menu_key), menuVisible);
        super.onSaveInstanceState(outState);
    }

    //This method saves the menu object to a variable in the Activity so it can be accessed to hide or show it later.
    //The menu's visibility is set to false and is only changed back to be true if the menuVisible variable is true.
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

    //If the menu item clicked is the sort/filter one then it opens the SettingsActivity, passing the names of people along with it.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sortAndFilterSettings) {
            if (myGolfNames != null) {
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra(getString(R.string.settings_names_extra), myGolfNames);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //These two methods are helped methods which either show or hide the menus. When the menus are shown then menuVisible is set to true.
    //This boolean can then be saved to the SavedInstanceState object and used to show the menu if it needs showing when the activity oncreate is triggered.
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


    public void setMyGolfNames(String[] myGolfNames) {
        this.myGolfNames = myGolfNames;
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
    public void onBackPressed() {

        if (findViewById(R.id.firstDividerView) == null) {
            //onNavigationItemSelected(bottomNavigationView.getMenu().getItem(0));
            bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(0).getItemId());
        } else {
            super.onBackPressed();
        }
    }
}
