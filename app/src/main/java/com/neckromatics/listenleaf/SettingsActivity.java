package com.neckromatics.listenleaf;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.preference.PreferenceFragmentCompat;

//import androidx.preference.SwitchPreferenceCompat;

public class SettingsActivity extends AppCompatActivity {

    SwitchCompat switchMode;
    boolean nightMode;


    //private  SwitchPreferenceCompat preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //switchMode = findViewById(R.id.switchmode)

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);


        }



    }
}