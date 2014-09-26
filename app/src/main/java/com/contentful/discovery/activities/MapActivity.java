package com.contentful.discovery.activities;

import android.content.Intent;
import android.os.Bundle;

import com.contentful.discovery.R;
import com.contentful.discovery.utils.IntentConsts;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Map Activity.
 * Displays a {@link com.google.android.gms.maps.MapFragment} with a single
 * {@link com.google.android.gms.maps.model.Marker} representing the {@code LatLng} coordinates
 * carried with the {@code Intent}.
 */
public class MapActivity extends CFFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        // Extract arguments from Intent
        Intent intent = getIntent();
        LatLng latLng = intent.getParcelableExtra(IntentConsts.EXTRA_LOCATION);
        String title = intent.getStringExtra(IntentConsts.EXTRA_TITLE);

        // Setup map (marker & auto-zoom)
        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragment_map))
                .getMap();

        map.addMarker(new MarkerOptions()
                .title(title)
                .position(latLng));

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,
                (float) getResources().getInteger(R.integer.map_default_zoom)));
    }
}
