package com.agharibi.googlegeofencelocator;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.location.GeofenceStatusCodes;

public class GeofenceErrorMessages {

    private GeofenceErrorMessages() {
        //Do not allow instantiation!
    }

    public static String getErrorString(Context context, int errorCode) {
        Resources resources = context.getResources();

        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "Geofence service is not available now";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Your app has registered too many geofences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Your have provided too many pendingIntents to the AddGeofence.";
            default:
                return "Unknown error: the geofence service is not available now";
        }
    }
}
