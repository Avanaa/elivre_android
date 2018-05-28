package br.com.avana.elivreapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import br.com.avana.elivreapp.FormActivity;
import br.com.avana.elivreapp.ListActivity;
import br.com.avana.elivreapp.util.Localizer;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    public final static int MY_LOCATION_ENABLE = 1;
    public final static int OPEN_LOCALIZER = 2;

    public final static int GO_FORM = 3;
    public final static int GO_LIST = 4;


    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.googleMap = googleMap;

        // Set MyLocationButton true
        if (ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_LOCATION_ENABLE);
        } else {
            this.googleMap.setMyLocationEnabled(true);
        }

        // Click MyLocation button
        this.googleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), 15));
            }
        });

        // Click Map
        this.googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Intent intentGoForm = new Intent(getContext(), FormActivity.class);
                intentGoForm.putExtra("position", latLng);
                startActivityForResult(intentGoForm, GO_FORM);
            }
        });

        // Long click Map
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intentGoList = new Intent(getContext(), ListActivity.class);
                intentGoList.putExtra("position", latLng);
                startActivityForResult(intentGoList, GO_LIST);
            }
        });

        // Open localize provider
        if (ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, OPEN_LOCALIZER);
        } else {
            new Localizer(getActivity(), googleMap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            // Set myLocationButton true
            case MY_LOCATION_ENABLE:

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    this.googleMap.setMyLocationEnabled(true);
                }
                break;

            // Open localize provider
            case OPEN_LOCALIZER:

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    new Localizer(getActivity(), googleMap);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK){

            switch (requestCode){
                case GO_FORM:
                    //Insert new post on map
                    break;

                case GO_LIST:
                    // Do Nothing
                    break;
            }
        }

        if (resultCode == Activity.RESULT_CANCELED){
            //Do nothing
        }
    }
}
