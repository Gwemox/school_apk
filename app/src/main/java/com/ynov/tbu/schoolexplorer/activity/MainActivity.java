package com.ynov.tbu.schoolexplorer.activity;

import com.ynov.tbu.schoolexplorer.R;
import com.ynov.tbu.schoolexplorer.utils.GPSTracker;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnListSchools = findViewById(R.id.btnListSchools);
        btnListSchools.setOnClickListener(genericOnClickListener);
        ImageButton btnMaps = findViewById(R.id.btnMaps);
        btnMaps.setOnClickListener(genericOnClickListener);
        ImageButton btnNewSchool = findViewById(R.id.btnNewSchool);
        btnNewSchool.setOnClickListener(genericOnClickListener);
        ImageButton btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(genericOnClickListener);

        GPSTracker.checkLocationPermission(MainActivity.this);
    }

    private void ListSchools() {
        Intent myIntent = new Intent(this.getBaseContext(), ListSchoolsActivity.class);
        this.startActivity(myIntent);
    }

    private void NewSchool() {
        Intent myIntent = new Intent(this.getBaseContext(), SchoolShowActivity.class);
        this.startActivity(myIntent);
    }

    private void Maps() {
        Intent myIntent = new Intent(this.getBaseContext(), MapsActivity.class);
        this.startActivity(myIntent);
    }

    private void Settings() {
        Intent myIntent = new Intent(this.getBaseContext(), SettingActivity.class);
        this.startActivity(myIntent);
    }

    private final View.OnClickListener genericOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(final View view) {
            switch (view.getId()){
                case R.id.btnListSchools:
                    ListSchools();
                    break;
                case R.id.btnMaps:
                    Maps();
                    break;
                case R.id.btnNewSchool:
                    NewSchool();
                    break;
                case R.id.btnSettings:
                    Settings();
                    break;
            }
        }
    };
}