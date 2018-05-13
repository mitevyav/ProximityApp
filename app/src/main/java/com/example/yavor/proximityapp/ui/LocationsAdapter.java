package com.example.yavor.proximityapp.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.nearbylocations.NearbyLocation;

import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;

    private static final int TYPE_ITEM = 1;

    private List<NearbyLocation> nearbyLocations;

    public LocationsAdapter(List<NearbyLocation> nearbyLocations) {
        this.nearbyLocations = nearbyLocations;
    }

    public void swap(List<NearbyLocation> nearbyLocations) {
        this.nearbyLocations = nearbyLocations;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item,
                                                                             parent,
                                                                             false);
            //inflate your layout and pass it to view holder
            return new LocationViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_header,
                                                                     parent,
                                                                     false);

            return new HeaderViewHolder(itemView);
        }

        throw new RuntimeException("No matching type " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LocationViewHolder) {
            LocationViewHolder locationViewHolder = (LocationViewHolder) holder;
            NearbyLocation location = getItem(position);
            locationViewHolder.name.setText(location.getName());
            locationViewHolder.distance.setText(locationViewHolder.distance.getContext()
                                                                           .getString(R.string.distance_item_string,
                                                                                      location.getDistance()));
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        if (nearbyLocations == null) {
            return 1;
        }
        return nearbyLocations.size() + 1;
    }

    private NearbyLocation getItem(int position) {
        return nearbyLocations.get(position - 1);
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {

        public TextView name, distance;

        public LocationViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            distance = view.findViewById(R.id.distance);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}