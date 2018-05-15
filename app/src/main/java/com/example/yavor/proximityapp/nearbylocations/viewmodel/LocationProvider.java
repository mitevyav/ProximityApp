package com.example.yavor.proximityapp.nearbylocations.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.location.Location;

import com.example.yavor.proximityapp.nearbylocations.NearbyLocation;
import com.example.yavor.proximityapp.nearbylocations.restapi.QueryParams;

import java.util.List;

public interface LocationProvider {

    void stopLocationProvider();

    void startLocationProvider(Context context);

    Location getCurrentLocation();

    void init(Context context);

    void checkSettingsAndPermissions(final Activity activity);

    void updateQueryParams(QueryParams queryParams);

    MutableLiveData<Boolean> hasNewQuery();

    MutableLiveData<List<NearbyLocation>> getLocations();

}
