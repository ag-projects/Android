package com.agharibi.googlebasicmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mGoogleMap;
    private boolean mapReady = false;

    private MarkerOptions renton, kirkland, everett, lynnwood, montlake, kent, showare;
    private static final CameraPosition SEATTLE  = CameraPosition.builder()
            .target(new LatLng(47.6204, -122.2491))
            .zoom(10)
            .bearing(0)
            .tilt(45)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        renton = new MarkerOptions()
                .position(new LatLng(47.489805,-122.120502))
                .title("Renton")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cast_dark));

        kirkland = new MarkerOptions()
                .position(new LatLng(47.7301986, -122.1768858))
                .title("Kirkland");

        everett = new MarkerOptions()
                .position(new LatLng(47.978748, -122.202001))
                .title("Everett");

        lynnwood = new MarkerOptions()
                .position(new LatLng(47.819533, -122.32288))
                .title("Lynnwood");

        montlake = new MarkerOptions()
                .position(new LatLng(47.7973733, -122.3281771))
                .title("Montlake");

        kent = new MarkerOptions()
                .position(new LatLng(47.385938, -122.258212))
                .title("Kent");

        showare = new MarkerOptions()
                .position(new LatLng(47.38702, -122.23986))
                .title("Showare");

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        mGoogleMap = googleMap;

        mGoogleMap.addMarker(renton);
        mGoogleMap.addMarker(kirkland);
        mGoogleMap.addMarker(everett);
        mGoogleMap.addMarker(lynnwood);
        mGoogleMap.addMarker(montlake);
        mGoogleMap.addMarker(kent);
        mGoogleMap.addMarker(showare);

        flyTo(SEATTLE);
    }

    private void flyTo(CameraPosition target) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }
}
