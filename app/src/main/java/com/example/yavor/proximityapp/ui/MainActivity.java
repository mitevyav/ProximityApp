package com.example.yavor.proximityapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.location.LocationManager;
import com.example.yavor.proximityapp.location.LocationManagerImpl;
import com.example.yavor.proximityapp.places.PlacesManager;
import com.example.yavor.proximityapp.places.QueryParams;

import static com.example.yavor.proximityapp.location.LocationManagerImpl.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.yavor.proximityapp.location.LocationManagerImpl.REQUEST_CHECK_SETTINGS;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter fragmentPagerAdapter;

    private LocationManager locationManager;

    private ViewPager viewPager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,
              "onActivityResult - requestCode - " + requestCode + ", resultCode - " + resultCode);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        locationManager.start(this);
                        break;
                    case Activity.RESULT_CANCELED:
                        locationManager.stop();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        locationManager.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        locationManager.start(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.start(this);
                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new PlacesManager().makeRequest(new QueryParams("-33.8670522,151.1957362",
                                                        "1500",
                                                        "restaurant",
                                                        "AIzaSyCV_JQdRwDBkBXTx7sIiRLYfC6Q1KoYoWs"));

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),
                                                        getApplicationContext());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(fragmentPagerAdapter);

        locationManager = new LocationManagerImpl(getApplicationContext());
    }
}
