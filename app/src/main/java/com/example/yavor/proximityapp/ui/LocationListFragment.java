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

import java.util.List;

public class LocationListFragment extends Fragment {

    private LocationsAdapter adapter;

    private RecyclerView recyclerView;

    private NearbyLocationsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_location_list, container, false);

        viewModel = ViewModelProviders.of(getActivity()).get(NearbyLocationsViewModel.class);

        adapter = new LocationsAdapter(viewModel.getLocations().getValue());
        recyclerView = rootView.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        viewModel.getLocations().observe(this, new Observer<List<NearbyLocation>>() {
            @Override
            public void onChanged(@Nullable List<NearbyLocation> nearbyLocations) {
                adapter.swap(nearbyLocations);
            }
        });
        return rootView;
    }

}
