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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yavor.proximityapp.R;
import com.example.yavor.proximityapp.nearbylocations.viewmodel.LocationProvider;
import com.example.yavor.proximityapp.nearbylocations.viewmodel.NearbyLocationsViewModel;
import com.example.yavor.proximityapp.utils.QueryParamsUtils;

public class NewQueryParamsDialogFragment extends DialogFragment
        implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    public static final String TAG = "NewQueryParamsDialogFragment";

    private TextView label;

    private LocationProvider locationProvider;

    private SeekBar seekBar;

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

        int progress = Integer.parseInt(QueryParamsUtils.getDistance(getActivity()));
        label = view.findViewById(R.id.label_seekbar);
        label.setText(getString(R.string.slider_label, progress));
        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(progress);

        view.findViewById(R.id.searchButton).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        CharSequence spinnerItem = (CharSequence) spinner.getSelectedItem();
        saveQueryParams(String.valueOf(seekBar.getProgress()), spinnerItem.toString());
        locationProvider.updateQueryParams(QueryParamsUtils.createQueryParamsFromLocation(
                getActivity(),
                locationProvider.getCurrentLocation()));
        dismiss();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        label.setText(getString(R.string.slider_label, progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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

    private void saveQueryParams(String distance, String type) {
        QueryParamsUtils.saveTypeAndDistance(getActivity(), distance, type);
    }

}
