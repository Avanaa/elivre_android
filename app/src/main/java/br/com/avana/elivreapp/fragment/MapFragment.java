package br.com.avana.elivreapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import br.com.avana.elivreapp.FormActivity;
import br.com.avana.elivreapp.R;
import br.com.avana.elivreapp.adapter.EvaluationAdapter;
import br.com.avana.elivreapp.model.Avaliacao;
import br.com.avana.elivreapp.model.PostModel;
import br.com.avana.elivreapp.pref.Preferences;
import br.com.avana.elivreapp.util.Evaluations;
import br.com.avana.elivreapp.util.Localizer;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    public final static int MY_LOCATION_ENABLE = 1;

    public final static int GO_FORM = 3;
    public final static int GO_LIST = 4;

    private GoogleMap googleMap;
    private DatabaseReference mRef;
    private PlaceAutocompleteFragment autoCompleteFragment;
    //private ClusterManager<PostModel> clusterManager;

    private LatLng position;
    private EvaluationAdapter adapter;
    private Avaliacao item;
    private View locationButton;
    private Localizer localizer;

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
            localizer = new Localizer(getActivity(), googleMap);
            this.googleMap.setMyLocationEnabled(true);

            if (this.getView() != null && this.getView().findViewById(Integer.parseInt("1")) != null){
                locationButton = ((View) this.getView().findViewById(Integer.parseInt("1"))
                        .getParent()).findViewById(Integer.parseInt("2"));

                if (locationButton != null){
                    locationButton.setVisibility(View.GONE);
                }
            }

            FloatingActionButton myLocationAdd = getActivity().findViewById(R.id.map_my_location);
            myLocationAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (locationButton != null){
                        locationButton.callOnClick();
                    }
                }
            });

            FloatingActionButton fabAdd = getActivity().findViewById(R.id.map_add);
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog();
                }
            });
        }

        // Click MyLocation button
        this.googleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom( new LatLng(location.getLatitude(), location.getLongitude()), 15));
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

        if (Preferences.isTargetFirstTimeSeen(getActivity())){
            openTapTargetSearch();
            Preferences.setTargetFirstTimeSeen(getActivity());
        }
    }

    private void openTapTargetSearch(){

        ImageView searchIcon = (ImageView)((LinearLayout)autoCompleteFragment.getView()).getChildAt(0);
        new MaterialTapTargetPrompt.Builder(getActivity())
                .setTarget(searchIcon.getId())
                .setPrimaryText("Procurar por endereço")
                .setSecondaryText("Faça uma busca pelo endereço para visualizar as ocorrências do local")
                .setBackgroundColour(getResources().getColor(R.color.tap_background_1))
                .setAutoDismiss(true)
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED){
                            openTapTargetLocal();
                        }
                        if (state == MaterialTapTargetPrompt.STATE_DISMISSED){
                            openTapTargetLocal();
                        }
                    }
                }).show();
    }

    private void openTapTargetLocal(){

        new MaterialTapTargetPrompt.Builder(getActivity())
                .setTarget(R.id.map_my_location)
                .setPrimaryText("Seu Local")
                .setSecondaryText("Clique aqui e será direcionado para seu local atual")
                .setBackgroundColour(getResources().getColor(R.color.tap_background_2))
                .setAutoDismiss(true)
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                            openTapTargetNew();
                        }
                        if (state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                            openTapTargetNew();
                        }
                    }
                }).show();
    }

    private void openTapTargetNew(){

        new MaterialTapTargetPrompt.Builder(getActivity())
                .setTarget(R.id.map_add)
                .setPrimaryText("Criar ocorrência")
                .setSecondaryText("Clique aqui para criar uma nova ocorrência")
                .setBackgroundColour(getResources().getColor(R.color.tap_background_3))
                .setAutoDismiss(true)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case MY_LOCATION_ENABLE:

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    localizer = new Localizer(getActivity(), googleMap);
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

        List<Avaliacao> avaliacoes = Evaluations.getEvaluationlist();
        adapter = new EvaluationAdapter(avaliacoes, getActivity());

        AlertDialog.Builder options = new AlertDialog.Builder(getActivity());

        TextView title = new TextView(getActivity());
        title.setText("Como foi o atendimento?");
        title.setGravity(Gravity.CENTER);
        title.setTextSize(20);
        title.setPadding(10, 10, 10, 10);

        options.setCustomTitle(title);

        options.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                item = adapter.getItem(which);
                position = new LatLng(localizer.currentLocation.getLatitude(), localizer.currentLocation.getLongitude());

                Intent intentGoForm = new Intent(getActivity(), FormActivity.class);

                intentGoForm.putExtra("avaliacao", item);
                intentGoForm.putExtra("position", position);

                startActivityForResult(intentGoForm, GO_FORM);
            }
        });
        options.create().show();
    }
}
