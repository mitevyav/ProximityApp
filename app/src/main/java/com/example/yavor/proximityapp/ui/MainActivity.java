package com.example.yavor.proximityapp.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.devicelocation.DeviceLocationManagerImpl;
import com.example.yavor.proximityapp.nearbylocations.restapi.NearbyLocationsRestManager;
import com.example.yavor.proximityapp.nearbylocations.restapi.QueryParams;
import com.example.yavor.proximityapp.preferences.SettingsActivity;
import com.example.yavor.proximityapp.utils.PreferenceUtils;

import static com.example.yavor.proximityapp.devicelocation.DeviceLocationManagerImpl.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.yavor.proximityapp.devicelocation.DeviceLocationManagerImpl.REQUEST_CHECK_SETTINGS;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String UPDATE_QUERY_PARAMS_KEY = "UPDATE_QUERY_PARAMS_KEY";

    private FragmentPagerAdapter fragmentPagerAdapter;

    private boolean isUpdateQueryParams = false;

    private NearbyLocationsViewModel viewModel;

    private ViewPager viewPager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences:
                launchPreferences();
                return false;
        }

        return (super.onOptionsItemSelected(item));
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
        if (isUpdateQueryParams) {
            isUpdateQueryParams = false;
            setQueryParams();
        }
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

        if (savedInstanceState != null) {
            isUpdateQueryParams = savedInstanceState.getBoolean(UPDATE_QUERY_PARAMS_KEY);
        }

        initViewModel();

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),
                                                        getApplicationContext());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(fragmentPagerAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(UPDATE_QUERY_PARAMS_KEY, isUpdateQueryParams);
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this).get(NearbyLocationsViewModel.class);

        viewModel.setDeviceLocationManager(new DeviceLocationManagerImpl(getApplication(),
                                                                         viewModel));
        viewModel.setRestManager(new NearbyLocationsRestManager(viewModel));
        setQueryParams();
    }

    private void setQueryParams() {
        QueryParams queryParams = new QueryParams("",
                                                  PreferenceUtils.getDistance(this),
                                                  PreferenceUtils.getType(this),
                                                  getString(R.string.api_key));
        viewModel.setQueryParams(queryParams);
    }

    private void launchPreferences() {
        isUpdateQueryParams = true;
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
