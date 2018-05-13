package com.example.yavor.proximityapp.nearbylocations.restapi;

import com.example.yavor.proximityapp.nearbylocations.json.NearbyLocationsResultJson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NearbyLocationsRestApi {

    @GET("maps/api/place/nearbysearch/json")
    Call<NearbyLocationsResultJson> getPlacesInfo(@Query("location") String location,
                                                  @Query("radius") String radius,
                                                  @Query("type") String type,
                                                  @Query("key") String key);

}
