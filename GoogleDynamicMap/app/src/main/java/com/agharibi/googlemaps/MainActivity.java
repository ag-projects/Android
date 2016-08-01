package com.agharibi.googlemaps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final String TAG = MainActivity.class.getSimpleName();
    protected GoogleMap mGoogleMap;
    protected boolean mapReady = false;

    private Button btnMap;
    private Button btnSatellite;
    private Button btnHybrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMap = (Button) findViewById(R.id.btn_Map);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapReady) {
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });

        btnSatellite = (Button) findViewById(R.id.btnSatellite);
        btnSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapReady) {
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
            }
        });

        btnHybrid = (Button) findViewById(R.id.btnHybrid);
        btnHybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapReady) {
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        mGoogleMap = googleMap;
        LatLng sanDiego = new LatLng(32.7747748,-117.073859);
        CameraPosition position = CameraPosition.builder().target(sanDiego).zoom(14).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }
}
