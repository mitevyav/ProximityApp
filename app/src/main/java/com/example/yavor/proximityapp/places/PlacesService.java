package com.example.yavor.proximityapp.places;

import com.example.yavor.proximityapp.places.json.PlacesInfoJson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesService {

    @GET("maps/api/place/nearbysearch/json")
    Call<PlacesInfoJson> getPlacesInfo(@Query("location") String location,
                                       @Query("radius") String radius,
                                       @Query("type") String type,
                                       @Query("key") String key);

}
