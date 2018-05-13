package com.example.yavor.proximityapp.nearbylocations;

import android.util.Log;

import com.example.yavor.proximityapp.nearbylocations.json.NearbyLocationJson;
import com.example.yavor.proximityapp.nearbylocations.json.NearbyLocationsResultJson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class NearbyLocationsRestManager implements Callback<NearbyLocationsResultJson> {

    private static final String TAG = "NearbyLocationsRestManager";

    @Override
    public void onResponse(Call<NearbyLocationsResultJson> call, Response<NearbyLocationsResultJson> response) {
        Log.d(TAG, "onResponse");
        if (response.isSuccessful()) {
            NearbyLocationsResultJson nearbyLocationsResultJson = response.body();
            for (NearbyLocationJson nearbyLocationJson : nearbyLocationsResultJson.getPlacesJson()) {
                Log.d(TAG, "nearbyLocationJson - " + nearbyLocationJson.toString());
            }
        } else {
            Log.d(TAG, "response.errorBody - " + response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<NearbyLocationsResultJson> call, Throwable t) {
        Log.d(TAG, "onFailure");
        t.printStackTrace();
    }

    public void makeRequest(QueryParams queryParams) {
        Retrofit retrofit =
                new Retrofit.Builder().addConverterFactory(JacksonConverterFactory.create())
                                      .baseUrl("https://maps.googleapis.com/")
                                      .build();

        NearbyLocationsRestApi service = retrofit.create(NearbyLocationsRestApi.class);

        Call<NearbyLocationsResultJson> call = service.getPlacesInfo(queryParams.getLocation(),
                                                                     queryParams.getRadius(),
                                                                     queryParams.getType(),
                                                                     queryParams.getKey());

        call.enqueue(this);
    }

}
