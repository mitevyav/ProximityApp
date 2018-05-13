package com.example.yavor.proximityapp.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.nearbylocations.NearbyLocation;

import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> {

    private List<NearbyLocation> nearbyLocations;

    public LocationsAdapter(List<NearbyLocation> nearbyLocations) {
        this.nearbyLocations = nearbyLocations;
    }

    public void swap(List<NearbyLocation> nearbyLocations) {
        this.nearbyLocations = nearbyLocations;
        notifyDataSetChanged();
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item,
                                                                         parent,
                                                                         false);

        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        NearbyLocation location = nearbyLocations.get(position);
        holder.name.setText(location.getName());
        holder.distance.setText(String.valueOf(location.getDistance()));

    }

    @Override
    public int getItemCount() {
        if (nearbyLocations == null) {
            return 0;
        }
        return nearbyLocations.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {

        public TextView name, distance;

        public LocationViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            distance = view.findViewById(R.id.distance);
        }
    }
}