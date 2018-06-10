package com.ynov.tbu.schoolexplorer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.Status;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ynov.tbu.schoolexplorer.R;
import com.ynov.tbu.schoolexplorer.controller.ApiRequest;
import com.ynov.tbu.schoolexplorer.controller.SchoolController;
import com.ynov.tbu.schoolexplorer.fragment.ActionBarFragment;
import com.ynov.tbu.schoolexplorer.fragment.ShowSchoolFragment;
import com.ynov.tbu.schoolexplorer.model.School;
import com.ynov.tbu.schoolexplorer.response.SchoolResponse;
import com.ynov.tbu.schoolexplorer.response.SchoolsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActionBarFragment.OnFragmentInteractionListener, GoogleMap.OnMarkerClickListener, ShowSchoolFragment.OnFragmentInteractionListener {

    private GoogleMap mMap;
    private ShowSchoolFragment schoolFragment;

    private Integer focusSchoolId = -1;
    private Marker currentMarkerSelected = null;

    public static final int SIZE_MARKER = 100;
    public static final int SIZE_MARKER_SELECTED = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        focusSchoolId = this.getIntent().getIntExtra("focusSchoolId", -1);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, ActionBarFragment.newInstance("Carte", Color.GREEN, R.drawable.list_icon))
                .commit()
        ;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15.0f));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("MAPS_ERROR", "An error occurred: " + status);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        // On vérifie qu'on peut accèder à la localisation
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

        mMap.setOnMarkerClickListener(this);

        SchoolController schoolController = ApiRequest.getController(SchoolController.class);
        schoolController.getSchools(ApiRequest.getInstance().getToken(), ApiRequest.getSchoolsStatus()).enqueue(new Callback<SchoolsResponse>() {
            @Override
            public void onResponse(Call<SchoolsResponse> call, Response<SchoolsResponse> response) {
                SchoolsResponse schoolsResponse = response.body();
                if (schoolsResponse != null) {
                    LatLng shcoolPos = null;
                    for (School school: schoolsResponse.getSchools()) {
                        // Add a marker in Sydney and move the camera
                        shcoolPos = new LatLng(school.getLatitude(),  school.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(shcoolPos).title(school.getName()).icon(getMarkerIcon(school.getNumberStudent(), SIZE_MARKER))).setTag(school);

                        // Si on a demandé le focus sur cette école on bouge la caméra dessus
                        if (school.getId() == focusSchoolId) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(shcoolPos, 15.0f));
                        }
                    }

                    // Si on a demandé aucun focus, on focus la dernière école
                    if (focusSchoolId == -1) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(shcoolPos, 15.0f));
                    }
                } else {
                    Log.e("WS_ERROR", "Erreur lors de la lecture de la réponse du serveur", null);
                }
            }

            @Override
            public void onFailure(Call<SchoolsResponse> call, Throwable t) {
                Log.e("WS_ERROR", "Erreur lors de la lecture de la réponse du serveur", t);
            }
        });
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        School school = (School)marker.getTag();
        schoolFragment = ShowSchoolFragment.newInstance(school);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.school_show_container, schoolFragment)
                .commit()
        ;

        // ON remet la taille normal à l'ancien si il existe
        resetCurrentMarker();

        // On recréé un marker plus gros pour celui qu'on sélectionne
        if (school != null) {
            marker.remove();
            LatLng shcoolPos = new LatLng(school.getLatitude(), school.getLongitude());
            currentMarkerSelected = mMap.addMarker(new MarkerOptions().position(shcoolPos).title(school.getName()).icon(getMarkerIcon(school.getNumberStudent(), SIZE_MARKER_SELECTED)));
            currentMarkerSelected.setTag(school);
        }

        return true;
    }

    private BitmapDescriptor getMarkerIcon(Integer nbStudents, int size) {
        Bitmap b = null;
        if (nbStudents < 50) {
            b =((BitmapDrawable)getResources().getDrawable(R.drawable.marker_red_icon)).getBitmap();
        } else if (nbStudents < 200) {
            b =((BitmapDrawable)getResources().getDrawable(R.drawable.marker_orange_icon)).getBitmap();
        } else {
            b =((BitmapDrawable)getResources().getDrawable(R.drawable.marker_green_icon)).getBitmap();
        }
        return BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(b, size, size, false));
    }

    private void resetCurrentMarker() {
        if (currentMarkerSelected != null) {
            School schoolOld = (School)currentMarkerSelected.getTag();
            currentMarkerSelected.remove();
            currentMarkerSelected = null;
            // Add a marker in Sydney and move the camera
            if (schoolOld != null) {
                LatLng shcoolPos = new LatLng(schoolOld.getLatitude(),  schoolOld.getLongitude());
                mMap.addMarker(new MarkerOptions().position(shcoolPos).title(schoolOld.getName()).icon(getMarkerIcon(schoolOld.getNumberStudent(), SIZE_MARKER))).setTag(schoolOld);
            }
        }
    }

    @Override
    public void onMenuShow() {
        Intent myIntent = new Intent(this, ListSchoolsActivity.class);
        this.startActivity(myIntent);
    }

    @Override
    public void onCloseView() {
        resetCurrentMarker();
        getFragmentManager()
                .beginTransaction()
                .remove(schoolFragment)
                .commit()
        ;
    }
}
