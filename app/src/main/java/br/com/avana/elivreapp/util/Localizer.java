package br.com.avana.elivreapp.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import static br.com.avana.elivreapp.fragment.MapFragment.MY_LOCATION_ENABLE;

public class Localizer extends LocationCallback implements Serializable {

    private GoogleMap map;
    private View view;
    private Activity activity;
    private FusedLocationProviderClient providerClient;
    public Location currentLocation;

    public Localizer(Activity activity, GoogleMap map, View view) {

        this.activity = activity;
        this.map = map;
        this.view = view;

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setSmallestDisplacement(50);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this.activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        providerClient = LocationServices.getFusedLocationProviderClient(this.activity);
        providerClient.requestLocationUpdates(locationRequest, this, Looper.myLooper());
        if (map != null){
            map.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        currentLocation = (Location) locationResult.getLastLocation();
        if (map != null){
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 15));

            if (view != null && view.findViewById(Integer.parseInt("1")) != null) {
                View locationButton = ((View) view.findViewById(Integer.parseInt("1"))
                        .getParent()).findViewById(Integer.parseInt("2"));

                if (locationButton != null) {
                    locationButton.setVisibility(View.GONE);
                }
            }
        }
    }


}
