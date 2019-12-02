package com.appzone.freshcrops.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;

public class ServiceUpdateLocation extends Service implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initGoogleApiClient();
    }

    private void initGoogleApiClient() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();
        googleApiClient.connect();
    }



    @Override
    public void onLocationChanged(Location location) {
        EventBus.getDefault().post(location);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        StartLocationUpdate();
    }

    private void StartLocationUpdate() {
        initLocationRequest();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };

        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
    }

    private void initLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000*5);
        locationRequest.setFastestInterval(1000*5);

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient!=null)
        {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleApiClient!=null)
        {
            googleApiClient.disconnect();

        }
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
    }
}
