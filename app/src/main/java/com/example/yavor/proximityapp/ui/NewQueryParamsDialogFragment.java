package com.example.yavor.proximityapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.nearbylocations.viewmodel.LocationProvider;
import com.example.yavor.proximityapp.nearbylocations.viewmodel.NearbyLocationsViewModel;
import com.example.yavor.proximityapp.utils.QueryParamsUtils;

public class NewQueryParamsDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "NewQueryParamsDialogFragment";

    private static final int MAX_DISTANCE_IN_METERS = 50000;

    private static final int MIN_DISTANCE_IN_METERS = 10;

    private EditText editText;

    private LocationProvider locationProvider;

    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.query_fragment_dialog, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationProvider = ViewModelProviders.of(getActivity()).get(NearbyLocationsViewModel.class);
        spinner = view.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                                                                             R.array.types_array,
                                                                             android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(getSelectedItem(adapter));

        editText = view.findViewById(R.id.distance);
        editText.setText(QueryParamsUtils.getDistance(getActivity()));

        view.findViewById(R.id.searchButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        CharSequence spinnerItem = (CharSequence) spinner.getSelectedItem();
        String distance = editText.getEditableText().toString();
        if (!isValidDistance(distance)) {
            showInvalidValuesToast();
            return;
        }

        saveQueryParams(distance, spinnerItem.toString());
        locationProvider.updateQueryParams(QueryParamsUtils.createQueryParamsFromLocation(
                getActivity(),
                locationProvider.getCurrentLocation()));
        dismiss();
    }

    private int getSelectedItem(ArrayAdapter<CharSequence> adapter) {
        String type = QueryParamsUtils.getType(getActivity());
        String currentItem;
        for (int position = 0; position < adapter.getCount(); position++) {
            currentItem = adapter.getItem(position).toString();
            if (type.equals(currentItem)) {
                return position;
            }
        }
        return 0;
    }

    private void showInvalidValuesToast() {
        Toast.makeText(getActivity(), R.string.invalid_distance_toast, Toast.LENGTH_LONG).show();
    }

    private void saveQueryParams(String distance, String type) {
        QueryParamsUtils.saveTypeAndDistance(getActivity(), distance, type);
    }

    private boolean isValidDistance(String distance) {
        int intDistance = Integer.parseInt(distance);

        if (intDistance > MAX_DISTANCE_IN_METERS || intDistance < MIN_DISTANCE_IN_METERS) {
            return false;
        }
        return true;
    }
}
