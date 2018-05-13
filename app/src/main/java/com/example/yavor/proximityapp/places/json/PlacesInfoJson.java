package com.example.yavor.proximityapp.places.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlacesInfoJson {

    @JsonProperty("results")
    List<PlaceJson> placesJson;

    public List<PlaceJson> getPlacesJson() {
        return placesJson;
    }
}
