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
import android.support.v7.app.AppCompatActivity;
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
import br.com.avana.elivreapp.quickstart.TapTarget;
import br.com.avana.elivreapp.util.DateConvert;
import br.com.avana.elivreapp.util.Evaluations;
import br.com.avana.elivreapp.util.Localizer;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

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
    private Query mQuery;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        googleMap = map;

        defineThemeMap();

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
            checkIsFirstTimeSeen();
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

        initializeFirebaseDatabase();
        defineThemeMap();
    }

    public void checkIsFirstTimeSeen() {
        if (Preferences.isFirstTimeSeenMapScreen(getActivity())) {
            openTapTargets();
            Preferences.setMapScreenViewed(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        initializeFirebaseDatabase();
        defineThemeMap();
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

    private void openTapTargets(){

        TapTarget targetNew = new TapTarget((AppCompatActivity) getActivity(),
                R.id.map_add,
                R.string.tap_new_title,
                R.string.tap_new_subtitle,
                R.color.tap_background_3,
                null);

        TapTarget targetLocal = new TapTarget((AppCompatActivity) getActivity(),
                R.id.map_my_location,
                R.string.tap_local_title,
                R.string.tap_local_subtitle,
                R.color.tap_background_2,
                targetNew);

        TapTarget targetSearch = new TapTarget((AppCompatActivity) getActivity(),
                R.id.action_search,
                R.string.tap_search_title,
                R.string.tap_search_subtitle,
                R.color.tap_background_1,
                targetLocal);

        targetSearch.getBuilder().show();
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

                startActivity(intentGoForm);
            }
        });
        options.create().show();
    }

    public void openLocalizer() {
        localizer = new Localizer(getActivity(), googleMap, getView());
    }

    private void defineThemeMap() {
        if (googleMap == null){ return; }
        if (Preferences.isMapThemeDark(Objects.requireNonNull(getActivity()))){
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_style_dark));
        } else {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_style_default));
        }
    }

    private void initializeFirebaseDatabase() {
        if (googleMap == null){ return; }
        googleMap.clear();
        mRef = FirebaseDatabase.getInstance()
                .getReference(getString(R.string.database_posts_name))
                .getRef();

        mQuery = mRef.orderByChild(getString(R.string.database_posts_data_string))
                .startAt(DateConvert.addHourInterval(getActivity(), null,
                        Preferences.getTimeOcurrenceInterval(Objects.requireNonNull(getActivity()))));

        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshotValues) {
                for (DataSnapshot dataSnapshot : dataSnapshotValues.getChildren()){
                    PostModel post = dataSnapshot.getValue(PostModel.class);
                    MarkerOptions options = getMarkerOptionsByPost(post);
                    googleMap.addMarker(options);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
