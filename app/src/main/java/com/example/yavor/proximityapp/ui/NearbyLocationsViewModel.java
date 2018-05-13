package com.example.yavor.proximityapp.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Location;

import com.example.yavor.proximityapp.devicelocation.DeviceLocationChangedListener;
import com.example.yavor.proximityapp.devicelocation.DeviceLocationManager;
import com.example.yavor.proximityapp.nearbylocations.NearbyLocation;
import com.example.yavor.proximityapp.nearbylocations.NearbyLocationTransformer;
import com.example.yavor.proximityapp.nearbylocations.json.NearbyLocationJson;
import com.example.yavor.proximityapp.nearbylocations.restapi.NearbyLocationsRestManager;
import com.example.yavor.proximityapp.nearbylocations.restapi.QueryParams;

import java.util.Collections;
import java.util.List;

public class NearbyLocationsViewModel extends ViewModel implements DeviceLocationChangedListener,
                                                                   NearbyLocationsRestManager.NearbyLocationRestResultListener {

    private Location currentLocation;

    private DeviceLocationManager deviceLocationManager;

    private MutableLiveData<List<NearbyLocation>> locationsLiveData;

    private QueryParams queryParams;

    private NearbyLocationsRestManager restManager;

    public void setRestManager(NearbyLocationsRestManager restManager) {
        this.restManager = restManager;
    }

    public void setQueryParams(QueryParams queryParams) {
        this.queryParams = queryParams;
        if (locationsLiveData != null) {
            locationsLiveData.setValue(Collections.<NearbyLocation>emptyList());
        }
    }

    public MutableLiveData<List<NearbyLocation>> getLocations() {
        if (locationsLiveData == null) {
            locationsLiveData = new MutableLiveData<>();
        }
        return locationsLiveData;
    }

    @Override
    public void locationChanged(Location location) {
        this.currentLocation = location;
        if (queryParams == null || currentLocation == null) {
            locationsLiveData.setValue(Collections.<NearbyLocation>emptyList());
            return;
        }
        queryParams.setLocation(currentLocation);
        restManager.makeRequest(queryParams);
    }

    public DeviceLocationManager getDeviceLocationManager() {
        return deviceLocationManager;
    }

    public void setDeviceLocationManager(DeviceLocationManager deviceLocationManager) {
        this.deviceLocationManager = deviceLocationManager;
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
