package com.example.yavor.proximityapp.nearbylocations;

public class NearbyLocation {

    private float distance;

    private double latitude;

    private double longitude;

    private String name;

    public NearbyLocation(float distance, double latitude, double longitude, String name) {
        this.distance = distance;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    @Override
    public String toString() {
        return "NearbyLocation{" +
               "distance=" +
               distance +
               ", latitude=" +
               latitude +
               ", longitude=" +
               longitude +
               ", name='" +
               name +
               '\'' +
               '}';
    }

    public float getDistance() {

        return distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

}
