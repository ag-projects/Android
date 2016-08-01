package com.agharibi.googlegeofencelocator;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

public final class Constants {

    public static final float GEOFENCE_RADIUS_IN_METERS = 1;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 12 * 60 * 60* 1000;

    private Constants() {

    }

    public static final HashMap<String , LatLng> LANDMARKS = new HashMap<>();
    static {
        //San Diego State University
        LANDMARKS.put("SDSU_CAMPUS", new LatLng(32.7757886,-117.075428));

        //San Diego Airport
        LANDMARKS.put("San Diego Airport", new LatLng(32.7747748,-117.0738537));

        //San Diego Gaslamp Quarter
        LANDMARKS.put("San Diego Gaslamp Quarter", new LatLng(32.7111921,-117.1646042));

    }

}
