package com.example.yavor.proximityapp.preferences;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.example.yavor.proximityapp.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        // Init and update the pref summary
        String prefKey = getString(R.string.pref_key_type);
        final Preference pref = getPreferenceManager().findPreference(prefKey);
        pref.setSummary(pref.getSharedPreferences().getString(prefKey, ""));
        pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                pref.setSummary(newValue.toString());
                return true;
            }
        });

        prefKey = getString(R.string.pref_key_distance);
        final Preference prefType = getPreferenceManager().findPreference(prefKey);
        prefType.setSummary(getString(R.string.distance_pref_summary,
                                      prefType.getSharedPreferences().getString(prefKey, "")));
        prefType.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                prefType.setSummary(getString(R.string.distance_pref_summary, newValue.toString()));
                return true;
            }
        });
    }
}