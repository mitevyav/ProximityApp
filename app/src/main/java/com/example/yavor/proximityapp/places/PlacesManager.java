package com.example.yavor.proximityapp.places;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class PlacesManager implements Callback<PlacesInfo> {

    private static final String TAG = "PlacesManager";

    @Override
    public void onResponse(Call<PlacesInfo> call, Response<PlacesInfo> response) {
        Log.d(TAG, "onResponse");
        if (response.isSuccessful()) {
            PlacesInfo placesInfo = response.body();
            for (Place place : placesInfo.getPlaces()) {
                Log.d(TAG, "place - " + place.toString());
            }
        } else {
            Log.d(TAG, "response.errorBody - " + response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<PlacesInfo> call, Throwable t) {
        Log.d(TAG, "onFailure");
        t.printStackTrace();
    }

    public void makeRequest(QueryParams queryParams) {
        Retrofit retrofit =
                new Retrofit.Builder().addConverterFactory(JacksonConverterFactory.create())
                                      .baseUrl("https://maps.googleapis.com/")
                                      .build();

        PlacesService service = retrofit.create(PlacesService.class);

        Call<PlacesInfo> call = service.getPlacesInfo(queryParams.getLocation(),
                                                      queryParams.getRadius(),
                                                      queryParams.getType(),
                                                      queryParams.getKey());

        call.enqueue(this);
    }

}
