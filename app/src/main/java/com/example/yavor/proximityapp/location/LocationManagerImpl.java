package com.example.yavor.proximityapp.location;

import android.Manifest;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationManagerImpl implements LocationManager {

    private static final String TAG = "LocationManagerImpl";

    private Context context;

    private FusedLocationProviderClient fusedLocationClient;

    private boolean isStarted = false;

    public LocationManagerImpl(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public LocationManagerImpl(LifecycleOwner owner, Context context) {
        this(context);
        this.context = context;
        owner.getLifecycle().addObserver(new LifeCycleObserver());
    }

    @Override
    public void startLocationUpdates() {
        if (isStarted) {
            return;
        }
        isStarted = true;

        initLastLocation();
    }

    @Override
    public void stopLocationUpdates() {
        if (!isStarted) {
            return;
        }
        isStarted = false;
    }

    @Override
    public void getLastKnownLocation() {

    }

    private void initLastLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            isStarted = false;
            return;
        }
        fusedLocationClient.getLastLocation()
                           .addOnSuccessListener(new OnSuccessListener<Location>() {
                               @Override
                               public void onSuccess(Location location) {
                                   if (location != null) {
                                       Log.d(TAG, "location - " + location.toString());
                                   }
                               }
                           });
    }

    private class LifeCycleObserver implements LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void connectListener() {
            Log.d(TAG, "LifeCycleObserver - ON_RESUME");
            startLocationUpdates();
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void disconnectListener() {
            Log.d(TAG, "LifeCycleObserver - ON_PAUSE");
            stopLocationUpdates();
        }
    }
}
