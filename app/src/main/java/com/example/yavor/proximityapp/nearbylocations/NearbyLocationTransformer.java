package com.example.yavor.proximityapp.nearbylocations;

import android.location.Location;

import com.example.yavor.proximityapp.nearbylocations.json.NearbyLocationJson;

import java.util.LinkedList;
import java.util.List;

public class NearbyLocationTransformer {

    private Location currentLocation;

    private List<NearbyLocationJson> nearbyLocationJson;

    private List<NearbyLocation> nearbyLocations = new LinkedList<>();

    public NearbyLocationTransformer(Location currentLocation,
                                     List<NearbyLocationJson> nearbyLocationJson) {
        this.currentLocation = currentLocation;
        this.nearbyLocationJson = nearbyLocationJson;
        transform();
    }

    public List<NearbyLocation> getNearbyLocations() {
        return nearbyLocations;
    }

    private void transform() {
        if (nearbyLocationJson == null) {
            return;
        }
        for (NearbyLocationJson locationJson : nearbyLocationJson) {
            int distance = getDistance(locationJson);
            NearbyLocation location = new NearbyLocation(distance,
                                                         locationJson.getLatitude(),
                                                         locationJson.getLongitude(),
                                                         locationJson.getName());
            nearbyLocations.add(location);
        }
    }

    private Location getLocationObject(NearbyLocationJson nearbyLocationJson) {
        Location location = new Location("Dummy location");
        location.setLatitude(nearbyLocationJson.getLatitude());
        location.setLongitude(nearbyLocationJson.getLongitude());
        return location;
    }

    private int getDistance(NearbyLocationJson nearbyLocationJson) {
        Location location = getLocationObject(nearbyLocationJson);
        return (int) location.distanceTo(currentLocation);
    }

}
