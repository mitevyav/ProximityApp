package com.example.yavor.proximityapp.ui;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.location.Location;

import com.example.yavor.proximityapp.devicelocation.DeviceLocationChangedListener;
import com.example.yavor.proximityapp.devicelocation.DeviceLocationManager;
import com.example.yavor.proximityapp.devicelocation.DeviceLocationManagerImpl;
import com.example.yavor.proximityapp.nearbylocations.NearbyLocation;
import com.example.yavor.proximityapp.nearbylocations.NearbyLocationTransformer;
import com.example.yavor.proximityapp.nearbylocations.json.NearbyLocationJson;
import com.example.yavor.proximityapp.nearbylocations.restapi.NearbyLocationsRestManager;
import com.example.yavor.proximityapp.nearbylocations.restapi.QueryParams;
import com.example.yavor.proximityapp.utils.QueryParamsUtils;

import java.util.Collections;
import java.util.List;

public class NearbyLocationsViewModel extends ViewModel implements DeviceLocationChangedListener,
                                                                   LocationProvider,
                                                                   NearbyLocationsRestManager.NearbyLocationRestResultListener {

    private Location currentLocation;

    private DeviceLocationManager deviceLocationManager;

    private MutableLiveData<List<NearbyLocation>> locationsLiveData;

    private MutableLiveData<Boolean> newQueryLiveData;

    private QueryParams queryParams;

    private NearbyLocationsRestManager restManager;

    @Override
    public void stopLocationProvider() {
        deviceLocationManager.stop();
    }

    @Override
    public void startLocationProvider(Activity activity) {
        deviceLocationManager.start(activity);
    }

    @Override
    public Location getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public void init(Context context) {
        deviceLocationManager = new DeviceLocationManagerImpl(context, this);
        restManager = new NearbyLocationsRestManager(this);
        queryParams = QueryParamsUtils.createQueryParamsFromLocation(context, currentLocation);
    }

    @Override
    public void updateQueryParams(QueryParams queryParams) {
        this.queryParams = queryParams;
        if (locationsLiveData != null) {
            locationsLiveData.setValue(Collections.<NearbyLocation>emptyList());
        }
        restManager.makeRequest(queryParams);
        newQueryLiveData.setValue(true);
    }

    @Override
    public MutableLiveData<Boolean> hasNewQuery() {
        if (newQueryLiveData == null) {
            newQueryLiveData = new MutableLiveData<>();
        }
        return newQueryLiveData;
    }

    @Override
    public MutableLiveData<List<NearbyLocation>> getLocations() {
        if (locationsLiveData == null) {
            locationsLiveData = new MutableLiveData<>();
        }
        return locationsLiveData;
    }

    @Override
    public void locationChanged(Location location) {
        Location previousLocation = this.currentLocation;
        this.currentLocation = location;
        queryParams.setLocation(location);

        if (previousLocation == null) {
            newQueryLiveData.setValue(true);
        }

        if (currentLocation != null) {
            restManager.makeRequest(queryParams);
        }

    }

    @Override
    public void onResult(List<NearbyLocationJson> result) {
        List<NearbyLocation> locations = new NearbyLocationTransformer(currentLocation,
                                                                       result).getNearbyLocations();
        if (locationsLiveData != null) {
            locationsLiveData.setValue(locations);
        }

    }
}
