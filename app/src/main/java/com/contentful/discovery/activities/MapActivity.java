package com.contentful.discovery.activities;

import android.content.Intent;
import android.os.Bundle;
import com.contentful.discovery.R;
import com.contentful.discovery.utils.IntentConsts;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends CFFragmentActivity implements OnMapReadyCallback {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_map);

    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.fragment_map);

    mapFragment.getMapAsync(this);
  }

  @Override public void onMapReady(GoogleMap map) {
    Intent intent = getIntent();
    LatLng latLng = intent.getParcelableExtra(IntentConsts.EXTRA_LOCATION);
    String title = intent.getStringExtra(IntentConsts.EXTRA_TITLE);

    map.addMarker(new MarkerOptions().title(title).position(latLng));

    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,
        (float) getResources().getInteger(R.integer.map_default_zoom)));
  }
}
