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

    public QueryParams(Location location, String radius, String type, String key) {
        setLocation(location);
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
        if (location == null) {
            return;
        }
        this.location = location.getLatitude() + DELIMITER + location.getLongitude();
    }

    public String getRadius() {
        return radius;
    }

    public String getType() {
        return type;
    }
}
