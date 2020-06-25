package com.agro.agro.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        Context context = getPreferenceManager().getContext();
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

        EditTextPreference editTextPreference=new EditTextPreference(context);
        editTextPreference.setKey("video_ip_address");
        editTextPreference.setTitle("Video IP Address");
        editTextPreference.setSummary("IP Address for Video Feed.");
        editTextPreference.setDefaultValue("192.168.1.10");

        editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String pattern="^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";

                if (!newValue.toString().matches(pattern)){
                    Toast.makeText(getContext(),"Should be a valid IP address.",Toast.LENGTH_SHORT).show();
                    return false;
                }else {
                    return true;
                }
            }
        });

        screen.addPreference(editTextPreference);

        setPreferenceScreen(screen);
    }
}
