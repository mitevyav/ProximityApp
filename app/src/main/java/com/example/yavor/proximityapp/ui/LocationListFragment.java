package com.example.yavor.proximityapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.nearbylocations.NearbyLocation;
import com.example.yavor.proximityapp.nearbylocations.viewmodel.LocationProvider;
import com.example.yavor.proximityapp.nearbylocations.viewmodel.NearbyLocationsViewModel;

import java.util.List;

public class LocationListFragment extends Fragment {

    private LocationsAdapter adapter;

    private LocationProvider locationProvider;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location_list, container, false);

        locationProvider = ViewModelProviders.of(getActivity()).get(NearbyLocationsViewModel.class);

        adapter = new LocationsAdapter(locationProvider.getLocations().getValue());
        recyclerView = rootView.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        locationProvider.getLocations().observe(this, new Observer<List<NearbyLocation>>() {
            @Override
            public void onChanged(@Nullable List<NearbyLocation> nearbyLocations) {
                adapter.swap(nearbyLocations);
            }
        });
        return rootView;
    }

}
