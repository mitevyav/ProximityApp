package com.example.yavor.proximityapp.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.nearbylocations.NearbyLocation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public final class MapsUtils {

    private static final float DEFAULT_ZOOM_LEVEL = 14f;

    private static final String GOOGLE_MAPS_PCKG_NAME = "com.google.android.apps.maps";

    private MapsUtils() {

    }

    public static void launchMapForByNearbyLocation(Context context, NearbyLocation location) {
        String uri = context.getString(R.string.format_map_uri,
                                       location.getLatitude(),
                                       location.getLongitude(),
                                       location.getLatitude(),
                                       location.getLongitude(),
                                       location.getName());
        Uri gmmIntentUri = Uri.parse(uri);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage(GOOGLE_MAPS_PCKG_NAME);
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }

    public static float getZoomLevel(Location location, GoogleMap googleMap, Context context) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        double queryRadius = Double.valueOf(QueryParamsUtils.getDistance(context));
        Circle circle = googleMap.addCircle(new CircleOptions().center(latLng).radius(queryRadius));
        circle.setVisible(false);

        float zoomLevel = DEFAULT_ZOOM_LEVEL;
        if (circle != null) {
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }
}
