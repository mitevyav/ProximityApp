package com.example.yavor.proximityapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.nearbylocations.restapi.QueryParams;

public class QueryParamsUtils {

    public static String getDistance(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.pref_key_distance),
                                           context.getString(R.string.pref_distance_default_value));

    }

    public static String getType(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.pref_key_type),
                                           context.getString(R.string.pref_type_default_value));

    }

    public static QueryParams createQueryParamsFromLocation(Context context, Location location) {
        return new QueryParams(location,
                               QueryParamsUtils.getDistance(context),
                               QueryParamsUtils.getType(context),
                               context.getString(R.string.api_key));
    }

}
