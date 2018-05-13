package com.example.yavor.proximityapp.location;

import android.app.Activity;
import android.location.Location;

public interface LocationManager {

    void start(Activity activity);

    void stop();

    Location getLastKnownLocation();
}
