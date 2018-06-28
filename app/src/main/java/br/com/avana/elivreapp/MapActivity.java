package br.com.avana.elivreapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.Objects;

import br.com.avana.elivreapp.fragment.MapFragment;
import br.com.avana.elivreapp.util.Localizer;

public class MapActivity extends AppCompatActivity {

    private static final int GO_SEARCH = 2;
    public static final int GO_SETTINGS = 3;

    private MapFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.map_toolbar);
        setSupportActionBar(toolbar);

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}
                    , MapFragment.MY_LOCATION_ENABLE);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        fragment = new MapFragment();

        transaction.replace(R.id.map, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                try {
                    Intent goSearch = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
                    startActivityForResult(goSearch, GO_SEARCH);
                    
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.action_settings:
                Intent goSettings = new Intent(this, SettingsActivity.class);
                startActivityForResult(goSettings, GO_SETTINGS);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GO_SEARCH){
            if (resultCode == Activity.RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(this, data);
                fragment.moveMapCamera(place.getLatLng());
            }
        }

        if  (requestCode == GO_SETTINGS){
            if (resultCode == LoginActivity.SIGN_OUT){
                Intent signOut = new Intent(this, LoginActivity.class);
                signOut.putExtra("SIGN_OUT", true);
                startActivity(signOut);
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case MapFragment.MY_LOCATION_ENABLE:
                if ((ActivityCompat.checkSelfPermission(Objects.requireNonNull(this),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        && (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){

                    fragment.openLocalizer();
                    fragment.checkIsFirstTimeSeen();
                }
                break;
        }
    }
}
