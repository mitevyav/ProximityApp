package com.example.yavor.proximityapp.nearbylocations.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NearbyLocationsResultJson {

    @JsonProperty("results")
    List<NearbyLocationJson> placesJson;

    public List<NearbyLocationJson> getPlacesJson() {
        return placesJson;
    }
}
