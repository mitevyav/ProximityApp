package com.example.yavor.proximityapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.nearbylocations.NearbyLocation;

public final class MapsUtils {

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
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }
}
