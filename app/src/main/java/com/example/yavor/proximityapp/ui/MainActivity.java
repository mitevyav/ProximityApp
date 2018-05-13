package com.example.yavor.proximityapp.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.devicelocation.DeviceLocationManagerImpl;
import com.example.yavor.proximityapp.nearbylocations.restapi.NearbyLocationsRestManager;
import com.example.yavor.proximityapp.nearbylocations.restapi.QueryParams;

import static com.example.yavor.proximityapp.devicelocation.DeviceLocationManagerImpl.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.yavor.proximityapp.devicelocation.DeviceLocationManagerImpl.REQUEST_CHECK_SETTINGS;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter fragmentPagerAdapter;

    private NearbyLocationsViewModel viewModel;

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
                        viewModel.getDeviceLocationManager().start(this);
                        break;
                    case Activity.RESULT_CANCELED:
                        viewModel.getDeviceLocationManager().stop();
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
        viewModel.getDeviceLocationManager().stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        viewModel.getDeviceLocationManager().start(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.getDeviceLocationManager().start(this);
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
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(NearbyLocationsViewModel.class);
        QueryParams queryParams = new QueryParams("-33.8670522,151.1957362",
                                                  "1000",
                                                  "restaurant",
                                                  "AIzaSyCV_JQdRwDBkBXTx7sIiRLYfC6Q1KoYoWs");

        viewModel.setDeviceLocationManager(new DeviceLocationManagerImpl(getApplication(),
                                                                         viewModel));
        viewModel.setQueryParams(queryParams);
        viewModel.setRestManager(new NearbyLocationsRestManager(viewModel));
    }
}
