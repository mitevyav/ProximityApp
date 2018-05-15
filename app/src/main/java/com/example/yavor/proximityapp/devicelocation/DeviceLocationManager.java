package com.example.yavor.proximityapp.devicelocation;

import android.app.Activity;
import android.content.Context;

public interface DeviceLocationManager {

    void start(Context context);

    void stop();

    void checkSettingsAndPermissions(final Activity activity);
}
