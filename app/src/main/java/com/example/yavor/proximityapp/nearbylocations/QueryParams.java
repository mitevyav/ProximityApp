package com.example.yavor.proximityapp.nearbylocations;

public class QueryParams {

    private String key;

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

    public String getRadius() {
        return radius;
    }

    public String getType() {
        return type;
    }
}
