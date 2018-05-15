package com.example.yavor.proximityapp.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.widget.Toast;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.nearbylocations.NearbyLocation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public final class MapsUtils {

    private static final float DEFAULT_ZOOM_LEVEL = 14f;

    private MapsUtils() {

    }

    public static void launchMapForByNearbyLocation(Context context, NearbyLocation location) {
        String queryValue = context.getString(R.string.format_map_uri,
                                              location.getLatitude(),
                                              location.getLongitude(),
                                              location.getName());
        Uri locationUri = Uri.parse("geo:0,0")
                             .buildUpon()
                             .appendQueryParameter("q", queryValue)
                             .build();
        showMap(locationUri, context);

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

    private static void showMap(Uri geoLocation, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, R.string.no_maps, Toast.LENGTH_SHORT).show();
        }
    }
}
