package com.agharibi.etsygoogleplus.google;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.agharibi.etsygoogleplus.api.Etsy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

public class GoogleServicesHelper implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CODE_RESOLUTION = -100;
    private static final int REQUEST_CODE_AVAILABLITY = -101;
    private static final String CLIENT_ID = "772703114165-vp7laqrsb4e1jkvka5gmboifhnbc3oec.apps.googleusercontent.com";

    private GoogleServicesListener mGoogleServicesListener;
    private Activity mActivity;
    private GoogleApiClient mGoogleApiClient;

    public GoogleServicesHelper(Activity activity, GoogleServicesListener listener) {
        mActivity = activity;
        mGoogleServicesListener = listener;
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API,
                        Plus.PlusOptions.builder()
                            .setServerClientId(CLIENT_ID)
                            .build())
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mGoogleServicesListener.onConnected();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleServicesListener.onDisconnected();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(mActivity, REQUEST_CODE_RESOLUTION);
            }
            catch (IntentSender.SendIntentException e) {
                // Try to connect again.
                connect();
            }
        }
        else {
            mGoogleServicesListener.onDisconnected();
        }
    }

    public void handleActvityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_RESOLUTION || requestCode == REQUEST_CODE_AVAILABLITY) {
            if(resultCode == Activity.RESULT_OK) {
                connect();
            }
            else {
                mGoogleServicesListener.onDisconnected();
            }
        }
    }
    public void connect() {
        if(isGooglePlayServicesAvailable()) {
            mGoogleApiClient.connect();
        }
        else {
            mGoogleServicesListener.onDisconnected();
        }
    }

    public void disconnect() {
        if(isGooglePlayServicesAvailable()) {
            mGoogleApiClient.connect();
        }
        else {
            mGoogleServicesListener.onDisconnected();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int availability = api.isGooglePlayServicesAvailable(mActivity);
        switch (availability) {
            case ConnectionResult.SUCCESS:
                return true;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
            case ConnectionResult.SERVICE_INVALID:
            case ConnectionResult.SERVICE_DISABLED:
                api.getErrorDialog(mActivity, availability, REQUEST_CODE_AVAILABLITY).show();
                return false;
            default:
                return false;
        }
    }

    public interface GoogleServicesListener {
        public void onConnected();
        public void onDisconnected();
    }
}
