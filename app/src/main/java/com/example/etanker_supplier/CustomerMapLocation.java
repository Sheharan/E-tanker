package com.example.etanker_supplier;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CustomerMapLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String lat="";
    String lon="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_map_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        lat=getIntent().getStringExtra("latitude");
        lon=getIntent().getStringExtra("longitude");

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

        Double latitude=Double.parseDouble(lat);
        Double longitude=Double.parseDouble(lon);

        // Add a marker in customer Location and move the camera
        LatLng customerLocation = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(customerLocation).title("Destination"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(customerLocation));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(customerLocation,17));
//
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(customerLocation)     // Sets the center of the map to location user
//                .zoom(17)                   // Sets the zoom
//                .bearing(90)                // Sets the orientation of the camera to east
//                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
//                .build();
//
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//

    }
}
