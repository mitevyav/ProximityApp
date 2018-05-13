package com.example.yavor.proximityapp.nearbylocations.restapi;

import android.location.Location;

public class QueryParams {

    private static final String DELIMITER = ",";

    private String key;

    /**
     * The latitude/longitude around which to retrieve place information. This must be specified
     * as latitude,longitude.
     */
    private String location;

    private String radius;

    private String type;

    public QueryParams(String location, String radius, String type, String key) {
        this.location = location;
        this.radius = radius;
        this.type = type;
        this.key = key;
    }

    public String getKey() {

        return key;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location.getLatitude() + DELIMITER + location.getLongitude();
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRadius() {
        return radius;
    }

    public String getType() {
        return type;
    }
}
