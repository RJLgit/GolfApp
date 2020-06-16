package com.example.android.golfapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.preference.MultiSelectListPreference;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String[] theNames = getIntent().getStringArrayExtra("Names");

        SettingsFragment settingsFragment = new SettingsFragment(theNames);

        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_app_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setSubtitle("Change which data to see");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_settings_container, settingsFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //Navigates up to MainActivity using this method
            NavUtils.navigateUpFromSameTask(this);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);

        }
        return super.onOptionsItemSelected(item);
    }
}
