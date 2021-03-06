package com.example.yavor.proximityapp.ui;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.nearbylocations.viewmodel.LocationProvider;
import com.example.yavor.proximityapp.nearbylocations.viewmodel.NearbyLocationsViewModel;

import static com.example.yavor.proximityapp.devicelocation.DeviceLocationManagerImpl.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.yavor.proximityapp.devicelocation.DeviceLocationManagerImpl.REQUEST_CHECK_SETTINGS;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter fragmentPagerAdapter;

    private LocationProvider locationProvider;

    private ViewPager viewPager;

    @Override
    public void onClick(View v) {
        showNewQueryDialog();
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
                        locationProvider.startLocationProvider(getApplicationContext());
                        break;
                    case Activity.RESULT_CANCELED:
                        locationProvider.stopLocationProvider();
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
        locationProvider.stopLocationProvider();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        locationProvider.startLocationProvider(getApplicationContext());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationProvider.startLocationProvider(getApplicationContext());
                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewModel();

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),
                                                        getApplicationContext());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(fragmentPagerAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        locationProvider.checkSettingsAndPermissions(this);
    }

    private void initViewModel() {
        locationProvider = ViewModelProviders.of(this).get(NearbyLocationsViewModel.class);
        locationProvider.init(getApplicationContext());
        locationProvider.hasNewQuery().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                locationProvider.checkSettingsAndPermissions(MainActivity.this);
            }
        });
    }

    private void showNewQueryDialog() {
        DialogFragment newFragment = new NewQueryParamsDialogFragment();
        newFragment.show(getSupportFragmentManager(), NewQueryParamsDialogFragment.TAG);
    }

}
