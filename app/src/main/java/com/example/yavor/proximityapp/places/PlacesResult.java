package com.example.yavor.proximityapp.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlacesResult {

    @JsonProperty("results")
    List<Place> places;

    public List<Place> getResults() {
        return places;
    }
}
