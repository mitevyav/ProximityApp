package com.example.yavor.proximityapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.nearbylocations.NearbyLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap googleMap;

    private NearbyLocationsViewModel viewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(NearbyLocationsViewModel.class);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        updateMap();
        viewModel.getLocations().observe(this, new Observer<List<NearbyLocation>>() {
            @Override
            public void onChanged(@Nullable List<NearbyLocation> nearbyLocations) {
                updateMap();
            }
        });
    }

    private void updateMap() {
        addPins(viewModel.getLocations().getValue());
        moveCameraCurrentLocation(viewModel.getDeviceLocationManager().getLastLocation());
    }

    private void addPins(List<NearbyLocation> locations) {
        if (locations == null || googleMap == null) {
            return;
        }
        for (NearbyLocation location : locations) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLng).title(getPinLabel(location)));
        }

    }

    private String getPinLabel(NearbyLocation location) {
        return getString(R.string.ping_label, location.getName(), location.getDistance());
    }

    private void moveCameraCurrentLocation(Location location) {
        if (location == null) {
            return;
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

}