package com.example.yavor.proximityapp.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.TimeUnit;

@SuppressLint("MissingPermission")
public class LocationManagerImpl implements LocationManager {

    private static final String TAG = "LocationManagerImpl";

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 505;

    public static final int REQUEST_CHECK_SETTINGS = 506;

    private static final long FASTEST_UPDATE_INTERVAL_SECONDS = 5;

    private static final long UPDATE_INTERVAL_SECONDS = 10;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                Log.d(TAG, "onLocationResult - " + location.toString());
            }
        }
    };

    private FusedLocationProviderClient fusedLocationClient;

    private boolean isUpdatesStarted = false;

    private Location lastLocation;

    private LocationRequest locationRequest;

    public LocationManagerImpl(Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        createLocationRequest();
    }

    @Override
    public void start(Activity activity) {
        Log.d(TAG, "start()");
        checkSettings(activity);
    }

    @Override
    public void stop() {
        Log.d(TAG, "stop(): isUpdatesStarted - " + isUpdatesStarted);
        if (!isUpdatesStarted) {
            return;
        }
        isUpdatesStarted = false;

        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public Location getLastKnownLocation() {
        return lastLocation;
    }

    private void requestLocationUpdates(Activity activity) {
        Log.d(TAG, "requestLocationUpdates(): isUpdatesStarted - " + isUpdatesStarted);

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                                              new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                              PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        if (isUpdatesStarted) {
            return;
        }

        isUpdatesStarted = true;

        if (lastLocation == null) {
            initLastLocation();
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void checkSettings(final Activity activity) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.d(TAG, "LocationSettingsResponse onSuccess");
                requestLocationUpdates(activity);
            }
        });

        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "LocationSettingsResponse onFailure");
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {

                    }
                }
            }
        });
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(TimeUnit.SECONDS.toMillis(UPDATE_INTERVAL_SECONDS));
        locationRequest.setFastestInterval(TimeUnit.SECONDS.toMillis(FASTEST_UPDATE_INTERVAL_SECONDS));
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void initLastLocation() {
        fusedLocationClient.getLastLocation()
                           .addOnSuccessListener(new OnSuccessListener<Location>() {
                               @Override
                               public void onSuccess(Location location) {
                                   if (location != null) {
                                       Log.d(TAG, "location - " + location.toString());
                                       LocationManagerImpl.this.lastLocation = location;
                                   }
                               }
                           });
    }
}
