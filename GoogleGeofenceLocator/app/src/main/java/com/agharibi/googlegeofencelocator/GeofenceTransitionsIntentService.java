package com.agharibi.googlegeofencelocator;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {

    protected static final String TAG = GeofenceTransitionsIntentService.class.getSimpleName();
    protected GoogleApiClient mGoogleApiClient;

    public GeofenceTransitionsIntentService() {
        //Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if(geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this, geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        //Get goefence transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER  ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            //Get the list of geofences which were triggered.
            List<Geofence> triggeredGeofences = geofencingEvent.getTriggeringGeofences();

            //Get the transition detail
            String geofenceTransitionDetails = getGoefenceTransitionDetails(
                    this,
                    geofenceTransition,
                    triggeredGeofences
            );
            sendNotification(geofenceTransitionDetails);
            Log.i(TAG, geofenceTransitionDetails);
        }
        else {
            Log.e(TAG, "Invalid transition type: " + geofenceTransition);
        }
    }

    private String getGoefenceTransitionDetails(Context context,
                                                int geofenceTransition,
                                                List<Geofence> triggeredGeofences) {
        String geofenceTransitons = getTransitonString(geofenceTransition);
        ArrayList ids = new ArrayList();
        for(Geofence geofence : triggeredGeofences) {
            ids.add(geofence.getRequestId());
        }
        String currentIds = TextUtils.join(", ", ids);

        return geofenceTransitons + ": " + currentIds;
    }

    private String getTransitonString(int geofenceTransition) {
        return null;
    }

    private void sendNotification(String notificationDetails) {
        //Create an explicit intent which starts the main activity
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        //Build a stack task
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //Add main activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);
        //Push the intent onto the stack
        stackBuilder.addNextIntent(notificationIntent);
        //Get the pendingIntent which contains the entire stack.
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Get a notification builder that is compatible with platform version >= 4
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        //Define the notification settings.
        notification
                .setSmallIcon(R.drawable.ic_launcher)
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText("Click notification to return to app")
                .setContentIntent(notificationPendingIntent);

        notification.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification.build());
    }
    @Override
    public void onCreate() {
        super.onCreate();

    }

}
