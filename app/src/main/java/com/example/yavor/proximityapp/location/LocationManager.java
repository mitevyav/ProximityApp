package com.example.yavor.proximityapp.location;

public interface LocationManager {


    void startLocationUpdates();

    void stopLocationUpdates();

    void getLastKnownLocation();
}
