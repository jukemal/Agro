package com.agro.agro.ui.settings;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.agro.agro.api.ApiServiceGenerator;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        Context context = getPreferenceManager().getContext();
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

        EditTextPreference ipAddressPreference = new EditTextPreference(context);
        ipAddressPreference.setKey("video_ip_address");
        ipAddressPreference.setTitle("Video IP Address");
        ipAddressPreference.setSummary("IP Address for Video Feed.");
        ipAddressPreference.setDefaultValue("192.168.1.10");

        ipAddressPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String pattern = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";

                if (!newValue.toString().matches(pattern)) {
                    Toast.makeText(getContext(), "Should be a valid IP address.", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    return true;
                }
            }
        });

        screen.addPreference(ipAddressPreference);

        EditTextPreference usernamePreference = new EditTextPreference(context);
        usernamePreference.setKey("adafruit_io_username");
        usernamePreference.setTitle("Adafruit IO Username");
        usernamePreference.setSummary("Username for Adafruit IO.");

        usernamePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ApiServiceGenerator.setUsername(newValue.toString());
                return true;
            }
        });

        screen.addPreference(usernamePreference);

        EditTextPreference keyPreference = new EditTextPreference(context);
        keyPreference.setKey("adafruit_io_key");
        keyPreference.setTitle("Adafruit IO Key");
        keyPreference.setSummary("Key for Adafruit IO.");

        keyPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ApiServiceGenerator.setKey(newValue.toString());
                return true;
            }
        });

        screen.addPreference(keyPreference);

        setPreferenceScreen(screen);
    }
}
