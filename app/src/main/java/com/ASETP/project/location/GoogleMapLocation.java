package com.ASETP.project.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.ASETP.project.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

/**
 * @author MirageLee
 * @date 2020/10/20
 */
public class GoogleMapLocation {
    private GoogleMap googleMap;

    private final Context context;

    private Location lastKnownLocation;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final PlacesClient placesClient;

    private static final int DEFAULT_ZOOM = 15;

    OnLocationSuccessListener onLocationSuccessListener;

    public GoogleMapLocation(Context context, OnLocationSuccessListener onLocationSuccessListener) {
        this.context = context;
        this.onLocationSuccessListener = onLocationSuccessListener;
        Places.initialize(this.context, this.context.getString(R.string.google_api));
        placesClient = Places.createClient(this.context);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.context);
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public void updateLocationUi() {
        if (googleMap == null) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            googleMap.setMyLocationEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    public void fetchLastLocation(Activity activity) {
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnCompleteListener(activity, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    // Set the map's camera position to the current location of the device.
                    lastKnownLocation = task.getResult();
                    if (lastKnownLocation != null) {
                        Log.e(this.getClass().getSimpleName(), lastKnownLocation.toString());
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(lastKnownLocation.getLatitude(),
                                        lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    }
                } else {
                    Log.d(this.getClass().getSimpleName(), "Current location is null. Using defaults.");
                    Log.e(this.getClass().getSimpleName(), "Exception: %s", task.getException());
                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                }
            }
        });
    }

    public void getCurrentLocation() {
        if (googleMap == null) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.LAT_LNG);
        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();
                        onLocationSuccessListener.onPlace(likelyPlaces.getPlaceLikelihoods().get(0));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                likelyPlaces.getPlaceLikelihoods().get(0).getPlace().getLatLng(), DEFAULT_ZOOM));
                    } else {
                        Log.e(this.getClass().getSimpleName(), "Exception: %s", task.getException());
                    }
                }
            });
        }


    }

    public interface OnLocationSuccessListener {
        /**
         * when search place success
         * @param placeLikelihood the position that likely to be
         */
        void onPlace(PlaceLikelihood placeLikelihood);
    }
}