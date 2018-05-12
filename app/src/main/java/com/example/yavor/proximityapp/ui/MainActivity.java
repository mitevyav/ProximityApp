package com.example.yavor.proximityapp.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.location.LocationManager;
import com.example.yavor.proximityapp.location.LocationManagerImpl;
import com.example.yavor.proximityapp.places.PlacesManager;
import com.example.yavor.proximityapp.places.QueryParams;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 505;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new PlacesManager().makeRequest(new QueryParams("-33.8670522,151.1957362",
                                                        "1500",
                                                        "restaurant",
                                                        "AIzaSyCV_JQdRwDBkBXTx7sIiRLYfC6Q1KoYoWs"));
        locationManager = new LocationManagerImpl(this, getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        checkPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.startLocationUpdates();
                } else {

                }
                return;
            }
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                                              new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                              PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
}
