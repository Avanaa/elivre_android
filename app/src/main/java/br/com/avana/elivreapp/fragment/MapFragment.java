package br.com.avana.elivreapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import br.com.avana.elivreapp.FormActivity;
import br.com.avana.elivreapp.ListActivity;
import br.com.avana.elivreapp.R;
import br.com.avana.elivreapp.adapter.EvaluationAdapter;
import br.com.avana.elivreapp.model.Avaliacao;
import br.com.avana.elivreapp.model.PostModel;
import br.com.avana.elivreapp.util.Localizer;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    public final static int MY_LOCATION_ENABLE = 1;
    public final static int OPEN_LOCALIZER = 2;

    public final static int GO_FORM = 3;
    public final static int GO_LIST = 4;

    private GoogleMap googleMap;
    private DatabaseReference mRef;
    private PlaceAutocompleteFragment autoCompleteFragment;
    //private ClusterManager<PostModel> clusterManager;

    private LatLng position;
    private EvaluationAdapter adapter;
    private List<Avaliacao> avaliacoes;
    private Avaliacao item;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
        mRef = FirebaseDatabase.getInstance().getReference("Posts");
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        this.googleMap = map;

        //clusterManager = new ClusterManager<PostModel>(getActivity(), this.googleMap);
        //this.googleMap.setOnCameraIdleListener(clusterManager);
        //this.googleMap.setOnMarkerClickListener(clusterManager);

        // Set position My Location Button
        if (this.getView() != null && this.getView().findViewById(Integer.parseInt("1")) != null){
            View locationButton = ((View) this.getView().findViewById(Integer.parseInt("1"))
                    .getParent()).findViewById(Integer.parseInt("2"));

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
        }

        // Set MyLocationButton true and open Localizer
        if (ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_LOCATION_ENABLE);
        } else {
            new Localizer(getActivity(), googleMap);
            this.googleMap.setMyLocationEnabled(true);
        }

        // Click MyLocation button
        this.googleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom( new LatLng(location.getLatitude(), location.getLongitude()), 15));
            }
        });

        // Long click Map
        this.googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                position = latLng;
                openDialog();
            }
        });

        // Click Map
        this.googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                position = latLng;
                openDialog();
            }
        });

        googleMap.getUiSettings().setMapToolbarEnabled(false);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                PostModel post = dataSnapshot.getValue(PostModel.class);
                MarkerOptions options = new MarkerOptions();

                options.position(post.getPosition());
                options.title(post.getTitle());
                options.snippet(post.getSnippet());

                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker_free);

                if (post.getAvaliacao().getAvaliacao() == Avaliacao.ANGRY_FACE){
                    bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker_angry);
                }

                if (post.getAvaliacao().getAvaliacao() == Avaliacao.NEUTRAL_FACE){
                    bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker_neutral);
                }

                if (post.getAvaliacao().getAvaliacao() == Avaliacao.HAPPY_FACE){
                    bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker_happy);
                }

                options.icon(bitmapDescriptor);

                googleMap.addMarker(options);

                //clusterManager.addItem(data.getValue(PostModel.class));
                //clusterManager.cluster();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        autoCompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autoCompleteFragment.setFilter(new AutocompleteFilter.Builder()
                .setCountry("BR")
                .build());

        autoCompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 18));
            }

            @Override
            public void onError(Status status) {}
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            // Set myLocationButton true
            case MY_LOCATION_ENABLE:

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    new Localizer(getActivity(), googleMap);
                    this.googleMap.setMyLocationEnabled(true);
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

    public void openDialog(){

        avaliacoes = new ArrayList<Avaliacao>();
        avaliacoes.add(new Avaliacao(Avaliacao.ANGRY_FACE, Avaliacao.ANGRY_FACE_DESC));
        avaliacoes.add(new Avaliacao(Avaliacao.NEUTRAL_FACE, Avaliacao.NEUTRAL_FACE_DESC));
        avaliacoes.add(new Avaliacao(Avaliacao.HAPPY_FACE, Avaliacao.HAPPY_FACE_DESC));
        avaliacoes.add(new Avaliacao(Avaliacao.FREE, Avaliacao.FREE_DESC));

        adapter = new EvaluationAdapter(avaliacoes, getActivity());

        AlertDialog.Builder options = new AlertDialog.Builder(getActivity());

        options.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                item = adapter.getItem(which);

                Intent intentGoForm = new Intent(getActivity(), FormActivity.class);

                intentGoForm.putExtra("avaliacao", item);
                intentGoForm.putExtra("position", position);

                startActivityForResult(intentGoForm, GO_FORM);
            }
        });
        options.create().show();
    }
}
