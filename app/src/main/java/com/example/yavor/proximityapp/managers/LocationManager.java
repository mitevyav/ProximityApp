package com.example.yavor.proximityapp.managers;

public interface LocationManager {


    void startLocationUpdates();

    void stopLocationUpdates();

    void getLastKnownLocation();
}
