package com.example.yavor.proximityapp.nearbylocations.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NearbyLocationJson {

    private double latitude;

    private double longitude;

    private String name;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "NearbyLocationJson{" +
               "latitude=" +
               latitude +
               ", longitude=" +
               longitude +
               ", name='" +
               name +
               '\'' +
               '}';
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("geometry")
    private void unpackNested(Map<String, Object> geometry) {
        Map<String, Double> location = (Map<String, Double>) geometry.get("location");
        this.longitude = location.get("lng");
        this.latitude = location.get("lat");
    }
}
