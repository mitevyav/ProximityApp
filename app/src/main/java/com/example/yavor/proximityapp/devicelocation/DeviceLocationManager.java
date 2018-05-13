package com.example.yavor.proximityapp.devicelocation;

import android.app.Activity;
import android.location.Location;

public interface DeviceLocationManager {

    Location getLastLocation();

    void start(Activity activity);

    void stop();
}
