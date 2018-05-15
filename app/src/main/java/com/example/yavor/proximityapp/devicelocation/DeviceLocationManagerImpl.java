package com.example.yavor.proximityapp.devicelocation;

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
public class DeviceLocationManagerImpl implements DeviceLocationManager {

    private static final String TAG = "DeviceLocationManager";

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 505;

    public static final int REQUEST_CHECK_SETTINGS = 506;

    private static final long FASTEST_UPDATE_INTERVAL_SECONDS = 5;

    private static final long UPDATE_INTERVAL_SECONDS = 10;

    LocationCallback locationCallback = new LocationCallbackImpl();

    private FusedLocationProviderClient fusedLocationClient;

    private boolean invalidLocationSettings = true;

    private boolean isUpdatesStarted = false;

    private Location lastLocation;

    private DeviceLocationChangedListener listener;

    private LocationRequest locationRequest;

    public DeviceLocationManagerImpl(Context context, DeviceLocationChangedListener listener) {
        this.listener = listener;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        createLocationRequest();
    }

    @Override
    public void start(Context context) {
        Log.d(TAG, "start()");
        requestLocationUpdates(context);
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
    public void checkSettingsAndPermissions(final Activity activity) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.d(TAG, "LocationSettingsResponse onSuccess");
                invalidLocationSettings = false;
                requestPermissions(activity);
            }
        });

        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "LocationSettingsResponse onFailure");
                if (e instanceof ResolvableApiException) {
                    try {
                        invalidLocationSettings = true;
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {

                    }
                }
            }
        });
    }

    private void requestLocationUpdates(Context context) {
        Log.d(TAG, "requestLocationUpdates(): isUpdatesStarted - " + isUpdatesStarted);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED || invalidLocationSettings) {
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

    private void requestPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                                              new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                              PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
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
                                       updateLastLocation(location);
                                   }
                               }
                           });
    }

    private void updateLastLocation(Location location) {
        lastLocation = location;
        listener.locationChanged(location);
    }

    private class LocationCallbackImpl extends LocationCallback {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            Location local = locationResult.getLastLocation();
            if (local != null) {
                updateLastLocation(local);
            }
        }
    }
}
