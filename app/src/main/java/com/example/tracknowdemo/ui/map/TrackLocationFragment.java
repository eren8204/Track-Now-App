package com.example.tracknowdemo.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tracknowdemo.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TrackLocationFragment extends Fragment {
    private static final String TAG = "DriverMapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float cameraZoom = 15f;
    private Boolean myLocationPermissionsGranted = false;
    private GoogleMap myMap;
    private FusedLocationProviderClient myFusedLocationProviderClient;
    String trackLocationId, trackLocationPassword;
    Double trackLocationCurrentLatitude, trackLocationCurrentLongitude;
    DatabaseReference databaseReference;
    GoogleMap myGoogleMap;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            myGoogleMap = googleMap;
            LatLng pinLocation = new LatLng(trackLocationCurrentLatitude, trackLocationCurrentLongitude);
            CameraUpdate pinLocationCamera = CameraUpdateFactory.newLatLngZoom(pinLocation, cameraZoom);
            googleMap.animateCamera(pinLocationCamera);
            googleMap.addMarker(new MarkerOptions().position(pinLocation).title("Tracking Id:" + trackLocationId));
            myMap = googleMap;
            if (myLocationPermissionsGranted) {
                getDeviceLocation();
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                myMap.setMyLocationEnabled(true);
            }
        }
    };
    Map<String, Marker> markers = new HashMap();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_track_location, container, false);
        trackLocationId = getArguments().getString("trackLocationId");
        trackLocationPassword = getArguments().getString("trackLocationPassword");
        trackLocationCurrentLatitude = getArguments().getDouble("trackLocationCurrentLatitude");
        trackLocationCurrentLongitude = getArguments().getDouble("trackLocationCurrentLongitude");
        databaseReference = FirebaseDatabase.getInstance().getReference("locations/" + trackLocationId);
        // Attach a listener to read the data at our posts reference
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MyLocation myLocation = dataSnapshot.getValue(MyLocation.class);
                LatLng pinLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                CameraUpdate pinLocationCamera = CameraUpdateFactory.newLatLngZoom(pinLocation, cameraZoom);
                myGoogleMap.animateCamera(pinLocationCamera);

//                markers.get(dataSnapshot.getKey()).remove();
                // you can also modify the marker instead of removing it and then add it again

                myGoogleMap.addMarker(new MarkerOptions().position(pinLocation).title("Tracking Id:" + trackLocationId));
//                    Log.d("TrackLocationWorking", "My Lat,longitude is " + myLocation.getLatitude() + " " + myLocation.getLongitude());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TrackLocationWorking", "The read failed: " + databaseError.getCode());
            }
        });
        getLocationPermission();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        myLocationPermissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            myLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    myLocationPermissionsGranted = true;
//                    initMap();
                    Log.d(TAG, "initMap:initializing map");
                }
            }
        }
    }

    private void moveCamera(LatLng latlng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat:" + latlng.latitude + ", lan: " + latlng.longitude);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                myLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation:getting the devices current location");
        myFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try {
            if (myLocationPermissionsGranted) {
                Task location = myFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), cameraZoom);
                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(getActivity(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException " + e.getMessage());
        }
    }
}