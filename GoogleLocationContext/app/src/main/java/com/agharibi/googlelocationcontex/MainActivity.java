package com.agharibi.googlelocationcontex;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status>{

    protected final String TAG = MainActivity.class.getSimpleName();
    protected TextView mStatusText;
    protected Button requestUpdatesButton;
    protected Button removeUpdatesButton;
    protected GoogleApiClient mGoogleApiClient;
    protected ActivityDetectionBroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusText = (TextView) findViewById(R.id.detectedActivities);
        requestUpdatesButton = (Button) findViewById(R.id.request_activity_update_button);
        removeUpdatesButton = (Button) findViewById(R.id.remove_activity_update_button);
        mBroadcastReceiver = new ActivityDetectionBroadcastReceiver();
        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(Constants.BROADCAST_ACTION));
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public String getActivityString(int detectedActivityType) {
        Resources resources = this.getResources();
        switch (detectedActivityType) {
            case DetectedActivity.IN_VEHICLE:
                return resources.getString((R.string.in_vehicle));
            case DetectedActivity.ON_BICYCLE:
                return resources.getString((R.string.on_bicycle));
            case DetectedActivity.ON_FOOT:
                return resources.getString((R.string.on_foot));
            case DetectedActivity.RUNNING:
                return resources.getString((R.string.running));
            case DetectedActivity.STILL:
                return resources.getString((R.string.still));
            case DetectedActivity.TILTING:
                return resources.getString((R.string.tilting));
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            case DetectedActivity.UNKNOWN:
                return resources.getString((R.string.unknown));
            default:
                return resources.getString((R.string.unidentifiable_activity));

        }
    }

    public void requestActivityUpdatesButtonHandler(View view) {
        if(!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_LONG).show();
            return;
        }
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
                mGoogleApiClient,
                Constants.DETECTED_INTERNAL_IN_MILLISECONDS,
                getActivityDetectionPendingIntent()
        ).setResultCallback(this);
        requestUpdatesButton.setEnabled(false);
        removeUpdatesButton.setEnabled(true);
    }



    public void removeActivityUpdatesHandler(View view) {
        if(!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_LONG).show();
            return;
        }
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(
                mGoogleApiClient,
                getActivityDetectionPendingIntent()
        ).setResultCallback(this);
        requestUpdatesButton.setEnabled(true);
        removeUpdatesButton.setEnabled(false);
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onResult(Status status) {
        if(status.isSuccess()) {
            Log.e(TAG, "Successfully added activity detection");
        }
        else {
            Log.e(TAG, "Error adding or removing activity detection");
        }
    }

    public class ActivityDetectionBroadcastReceiver extends BroadcastReceiver {

        protected final String TAG = ActivityDetectionBroadcastReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<DetectedActivity> updatedActivities = intent.getParcelableArrayListExtra(Constants.ACTIVITY_EXTRA);

            String strStatus = "";
            for(DetectedActivity activity : updatedActivities) {
                strStatus += getActivityString(activity.getType()) + activity.getConfidence() + "%\n";
            }
            mStatusText.setText(strStatus);
        }

    }
}
