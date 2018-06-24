package br.com.avana.elivreapp.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

import br.com.avana.elivreapp.FormActivity;
import br.com.avana.elivreapp.R;
import br.com.avana.elivreapp.adapter.EvaluationAdapter;
import br.com.avana.elivreapp.model.Avaliacao;
import br.com.avana.elivreapp.model.PostModel;
import br.com.avana.elivreapp.pref.Preferences;
import br.com.avana.elivreapp.util.DateConvert;
import br.com.avana.elivreapp.util.Evaluations;
import br.com.avana.elivreapp.util.Localizer;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback, ChildEventListener {

    public final static int MY_LOCATION_ENABLE = 1;

    public final static int GO_FORM = 3;
    private static final int ZOOM_MAP = 20;

    private GoogleMap googleMap;
    private DatabaseReference mRef;

    private LatLng position;
    private EvaluationAdapter adapter;
    private Avaliacao item;
    private View locationButton;
    private Localizer localizer;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        googleMap = map;

        if (Preferences.isMapThemeDark(Objects.requireNonNull(getActivity()))){
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_style_dark));
        } else {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_style_default));
        }

        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}
                    , MY_LOCATION_ENABLE);
        } else {
            openLocalizer();
            googleMap.setMyLocationEnabled(true);
        }

        googleMap.getUiSettings().setMapToolbarEnabled(false);

        if (this.getView() != null && this.getView().findViewById(Integer.parseInt("1")) != null) {
            locationButton = ((View) this.getView().findViewById(Integer.parseInt("1"))
                    .getParent()).findViewById(Integer.parseInt("2"));

            if (locationButton != null) {
                locationButton.setVisibility(View.GONE);
            }
        }

        FloatingActionButton fabMyLocation = getActivity().findViewById(R.id.map_my_location);
        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationButton != null) {
                    locationButton.callOnClick();
                }
            }
        });

        FloatingActionButton fabAdd = getActivity().findViewById(R.id.map_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationButton != null){
                    locationButton.callOnClick();
                }
                openDialog();
            }
        });

        if (Preferences.isTargetFirstTimeSeen(getActivity())) {
            openTapTargetSearch();
            Preferences.setTargetFirstTimeSeen(getActivity());
        }

        String dateLimit = DateConvert.addHourInterval(getActivity(), null,
                Preferences.getTimeOcurrenceInterval(Objects.requireNonNull(getActivity())));

        mRef = FirebaseDatabase.getInstance()
                .getReference(getString(R.string.database_posts_name))
                .orderByChild(getString(R.string.database_posts_data_string))
                .startAt(dateLimit)
                .getRef();

        mRef.addChildEventListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Preferences.isMapThemeDark(Objects.requireNonNull(getActivity()))) {
            if (googleMap != null){
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_style_dark));
            }
        } else {
            if (googleMap != null){
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_style_default));
            }
        }
    }

    public void moveMapCamera(LatLng latLng){
        if (latLng == null){ return; }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_MAP));
    }

    private MarkerOptions getMarkerOptionsByPost(PostModel post) {

        MarkerOptions options = new MarkerOptions();

        options.position(post.getPosition());
        options.title(post.getTitle());
        options.snippet(post.getSnippet());

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker_free);

        if (post.getAvaliacao().getAvaliacao() == Avaliacao.ANGRY_FACE) {
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker_angry);
        }

        if (post.getAvaliacao().getAvaliacao() == Avaliacao.NEUTRAL_FACE) {
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker_neutral);
        }

        if (post.getAvaliacao().getAvaliacao() == Avaliacao.HAPPY_FACE) {
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker_happy);
        }

        options.icon(bitmapDescriptor);

        return options;
    }

    private void openTapTargetSearch() {

        //ImageView searchIcon = (ImageView) ((LinearLayout) autoCompleteFragment.getView()).getChildAt(0);

        new MaterialTapTargetPrompt.Builder(getActivity())
                //.setTarget(searchIcon.getId())
                .setTarget(R.id.action_search)
                .setPrimaryText(R.string.tap_search_title)
                .setSecondaryText(R.string.tap_search_subtitle)
                .setBackgroundColour(getResources().getColor(R.color.tap_background_1))
                .setAutoDismiss(true)
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                            openTapTargetLocal();
                        }
                        if (state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                            openTapTargetLocal();
                        }
                    }
                }).show();
    }

    private void openTapTargetLocal() {

        new MaterialTapTargetPrompt.Builder(getActivity())
                .setTarget(R.id.map_my_location)
                .setPrimaryText(R.string.tap_local_title)
                .setSecondaryText(R.string.tap_local_subtitle)
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

    private void openTapTargetNew() {

        new MaterialTapTargetPrompt.Builder(getActivity())
                .setTarget(R.id.map_add)
                .setPrimaryText(R.string.tap_new_title)
                .setSecondaryText(R.string.tap_new_subtitle)
                .setBackgroundColour(getResources().getColor(R.color.tap_background_3))
                .setAutoDismiss(true)
                .show();
    }

    public void openDialog(){

        List<Avaliacao> avaliacoes = Evaluations.getEvaluationlist(Objects.requireNonNull(getActivity()));
        adapter = new EvaluationAdapter(avaliacoes, getActivity());

        AlertDialog.Builder options = new AlertDialog.Builder(getActivity());

        TextView title = new TextView(getActivity());
        title.setText(getString(R.string.dialog_evaluation_title));
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

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        PostModel post = dataSnapshot.getValue(PostModel.class);
        MarkerOptions options = getMarkerOptionsByPost(post);
        googleMap.addMarker(options);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {}

    public void openLocalizer() {
        localizer = new Localizer(getActivity(), googleMap, getView());
    }

}
