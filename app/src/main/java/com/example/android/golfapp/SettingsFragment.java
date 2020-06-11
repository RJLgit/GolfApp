package com.example.android.golfapp;

import android.os.Bundle;

import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    String[] myNames;
    public SettingsFragment(String[] names) {
        myNames = names;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_golf);

        MultiSelectListPreference multiSelectListPreference = findPreference("player_filter_preference");
        multiSelectListPreference.setEntries(myNames);
        multiSelectListPreference.setEntryValues(myNames);
        multiSelectListPreference.setDefaultValue(myNames);

    }
}
