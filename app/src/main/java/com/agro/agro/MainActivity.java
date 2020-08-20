package com.agro.agro;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.agro.agro.api.ApiServiceGenerator;
import com.agro.agro.entity.DateEntity;
import com.agro.agro.viewmodels.DatabaseViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String username = sharedPreferences.getString("adafruit_io_username", "");
        String key = sharedPreferences.getString("adafruit_io_key", "");

        assert username != null;
        assert key != null;
        if (username.isEmpty() || key.isEmpty()) {
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getApplicationContext());
            materialAlertDialogBuilder.setTitle("Adafruit").setMessage("Set Adafruit IO Username and Key in Settings.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else {
            ApiServiceGenerator.setup(username, key);
        }

        DatabaseViewModel viewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);

        viewModel.check().observe(this, new Observer<List<DateEntity>>() {
            @Override
            public void onChanged(List<DateEntity> dateEntities) {
                Timber.e("Da : %s", dateEntities.toString());
            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
