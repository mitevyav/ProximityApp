package com.example.yavor.proximityapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

            LocationViewHolder viewHolder = new LocationViewHolder(itemView);
            itemView.setOnClickListener(viewHolder);
            return viewHolder;
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

    private void startMapIntent(Context context, NearbyLocation location) {
        String uri = context.getString(R.string.format_map_uri,
                                       location.getLatitude(),
                                       location.getLongitude(),
                                       location.getLatitude(),
                                       location.getLongitude(),
                                       location.getName());
        Uri gmmIntentUri = Uri.parse(uri);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(mapIntent);
        }
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView name, distance;

        public LocationViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            distance = view.findViewById(R.id.distance);
        }

        @Override
        public void onClick(View v) {
            startMapIntent(v.getContext(), getItem(getAdapterPosition()));
        }

    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}