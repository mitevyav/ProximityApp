package com.example.yavor.proximityapp.places;

import android.util.Log;

import com.example.yavor.proximityapp.places.json.PlaceJson;
import com.example.yavor.proximityapp.places.json.PlacesInfoJson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class PlacesRestManager implements Callback<PlacesInfoJson> {

    private static final String TAG = "PlacesRestManager";

    @Override
    public void onResponse(Call<PlacesInfoJson> call, Response<PlacesInfoJson> response) {
        Log.d(TAG, "onResponse");
        if (response.isSuccessful()) {
            PlacesInfoJson placesInfoJson = response.body();
            for (PlaceJson placeJson : placesInfoJson.getPlacesJson()) {
                Log.d(TAG, "placeJson - " + placeJson.toString());
            }
        } else {
            Log.d(TAG, "response.errorBody - " + response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<PlacesInfoJson> call, Throwable t) {
        Log.d(TAG, "onFailure");
        t.printStackTrace();
    }

    public void makeRequest(QueryParams queryParams) {
        Retrofit retrofit =
                new Retrofit.Builder().addConverterFactory(JacksonConverterFactory.create())
                                      .baseUrl("https://maps.googleapis.com/")
                                      .build();

        PlacesRestApi service = retrofit.create(PlacesRestApi.class);

        Call<PlacesInfoJson> call = service.getPlacesInfo(queryParams.getLocation(),
                                                          queryParams.getRadius(),
                                                          queryParams.getType(),
                                                          queryParams.getKey());

        call.enqueue(this);
    }

}
