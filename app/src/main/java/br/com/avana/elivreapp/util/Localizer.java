package br.com.avana.elivreapp.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class Localizer extends LocationCallback {

    private GoogleMap map;
    private Activity activity;
    private FusedLocationProviderClient providerClient;

    public Localizer(Activity activity, GoogleMap map) {

        this.activity = activity;
        this.map = map;

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setSmallestDisplacement(50);
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        providerClient = LocationServices.getFusedLocationProviderClient(this.activity);
        providerClient.requestLocationUpdates(locationRequest, this, Looper.myLooper());
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        Location currentLocation = (Location) locationResult.getLastLocation();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15));
    }
}
