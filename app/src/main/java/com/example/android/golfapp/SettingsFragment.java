package com.example.android.golfapp;

import android.os.Bundle;

import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    //This variable holds all the distinct player names held in the database
    String[] myNames;
    public SettingsFragment(String[] names) {
        myNames = names;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_golf);
        //Sets the data to the preference to select the players. This has to be done dynamically as it responds to the database changing
        MultiSelectListPreference multiSelectListPreference = findPreference(getString(R.string.preference_player_filter_key));
        multiSelectListPreference.setEntries(myNames);
        multiSelectListPreference.setEntryValues(myNames);
        multiSelectListPreference.setDefaultValue(myNames);

    }
}
