package com.example.yavor.proximityapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.devicelocation.DeviceLocationChangedListener;
import com.example.yavor.proximityapp.devicelocation.DeviceLocationManager;
import com.example.yavor.proximityapp.devicelocation.DeviceLocationManagerImpl;
import com.example.yavor.proximityapp.nearbylocations.NearbyLocationsRestManager;
import com.example.yavor.proximityapp.nearbylocations.QueryParams;

import static com.example.yavor.proximityapp.devicelocation.DeviceLocationManagerImpl.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.yavor.proximityapp.devicelocation.DeviceLocationManagerImpl.REQUEST_CHECK_SETTINGS;

public class MainActivity extends AppCompatActivity implements DeviceLocationChangedListener {

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter fragmentPagerAdapter;

    private DeviceLocationManager locationManager;

    private ViewPager viewPager;

    @Override
    public void locationChanged(Location location) {
        Log.d(TAG, "locationChanged - " + location.toString());
    }

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
        new NearbyLocationsRestManager().makeRequest(new QueryParams("-33.8670522,151.1957362",
                                                                     "1500",
                                                                     "restaurant",
                                                                     "AIzaSyCV_JQdRwDBkBXTx7sIiRLYfC6Q1KoYoWs"));

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),
                                                        getApplicationContext());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(fragmentPagerAdapter);

        locationManager = new DeviceLocationManagerImpl(getApplicationContext(), this);
    }
}
