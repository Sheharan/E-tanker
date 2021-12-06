package com.example.etanker_supplier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etanker_supplier.Utils.Common;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Location extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    DocumentReference documentReference;
    String UserId = "";

    public static final int DEFAULT_TIME_INTERVAl = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSION_FINE_LOCATION = 14;


    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_upadtes, tv_sensor, tv_address;
    Switch sw_locationsupdates, sw_gps;

    boolean updateson = false;

    //related to the fusedlocation api
    LocationRequest locationRequest;

    LocationCallback locationCallback;

    //Google Api services for locating location
    private FusedLocationProviderClient mfusedLocationProviderClient;

    Button updateLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        UserId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        Log.d("My Activity", UserId);
        //documentReference = fstore.collection(Common.Consumers).document(UserId);


        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_speed = findViewById(R.id.tv_speed);
        tv_upadtes = findViewById(R.id.tv_updates);
        sw_gps = findViewById(R.id.sw_gps);
        sw_locationsupdates = findViewById(R.id.sw_locationsupdates);
        tv_address = findViewById(R.id.tv_address);
        updateLocationButton = findViewById(R.id.button);


        //set all the properties of the GPS
        locationRequest = new LocationRequest();

        //event that is called whenever the default update check condition is met
        locationCallback = new LocationCallback();


        //how often defaut location check occur
        locationRequest.setInterval(100 * DEFAULT_TIME_INTERVAl);

        //how often does default location check occurs when it is set to high accuracy mode
        locationRequest.setFastestInterval(100 * FAST_UPDATE_INTERVAL);

        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //event that is called whenever the default update check condition is met
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                updateUiDetails(locationResult.getLastLocation());
            }
        };


        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_gps.isChecked()) {
                    //high accuracy -gps
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("Using GPS sensors");
                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("Using Towers + WIFI");
                }
            }
        });

        sw_locationsupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_locationsupdates.isChecked()) {
                    //turn on location tracking
                    startLocationUpdate();

                } else {
                    //turn of the location tracking
                    stopLocationUpdate();
                }
            }
        });

        updateGPS();

        updateLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Location.this);
                alertDialog.setTitle("Do you want to update your Location?")
                        .setMessage("This address will be shown to the customer , Please be sure before updating")
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (ActivityCompat.checkSelfPermission(Location.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    requestRuntimePermission();
                                    return;
                                }
                                mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
                                    @Override
                                    public void onSuccess(final android.location.Location location) {
                                        if (location != null) {
                                            fstore.collection(Common.Suppliers).document(UserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if(documentSnapshot.exists()){
                                                        documentReference=fstore.collection(Common.Suppliers).document(UserId);
                                                        Log.d("My Activity", "LocationCheckpoint1");
                                                        String latitude = String.valueOf(location.getLatitude());
                                                        String longitude = String.valueOf(location.getLongitude());

                                                        Map<String, Object> supplierInfo = new HashMap<>();
                                                        supplierInfo.put("latitude", latitude);
                                                        supplierInfo.put("longitude", longitude);

                                                        documentReference.set(supplierInfo, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(Location.this, "Location Updated Successfully", Toast.LENGTH_SHORT).show();
                                                                Intent home = new Intent(getApplicationContext(), SupplierHome.class);
                                                                startActivity(home);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(Location.this, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }

                                                }
                                            });
                                        } else
                                            Toast.makeText(Location.this, "Please check your location(GPS) setting first ", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Location.this, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                }
                break;
        }
    }


    private void updateGPS() {
        //get Permission from the user to track GPS
        //get the current location from the fused client
        //update the information in the UI
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Location.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //user provided the permission
            mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<android.location.Location>() {
                @Override
                public void onSuccess(android.location.Location location) {
                    if (location != null) {
                        //put the values of the location to the UI components
                        Log.d("My Activity", "First phase");
                        String value = String.valueOf(location.getLatitude());
                        Log.d("My Activity", value);
                        updateUiDetails(location);
                    } else {
                        Toast.makeText(Location.this, "error! please check your location setting", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            requestRuntimePermission();

        }
    }

    private void requestRuntimePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
        }
    }

    private void updateUiDetails(android.location.Location location) {
        //set the value in the Ui here
        tv_lat.setText(String.valueOf(location.getLatitude()));
        //tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));

        if (location.hasAltitude()) {
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        } else {
            tv_altitude.setText("Not available");
        }

        if (location.hasSpeed()) {
            tv_speed.setText(String.valueOf(location.getSpeed()));
        } else {
            tv_speed.setText("Not available");
        }

        Geocoder geocoder = new Geocoder(Location.this);

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            tv_address.setText(addresses.get(0).getAddressLine(0));
        } catch (Exception e) {
            tv_address.setText("Unable to get street address");
        }


    }

    private void startLocationUpdate() {
        tv_upadtes.setText("Location is being traced");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //give permission
            requestRuntimePermission();
            return;
        }
        mfusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        updateGPS();

    }

    private void stopLocationUpdate(){

        tv_upadtes.setText("Location is not being traced");
        tv_lat.setText("Tracking disabled");
        tv_lon.setText("Tracking disabled");
        tv_altitude.setText("Tracking disabled");
        tv_speed.setText("Tracking disabled");
        tv_accuracy.setText("Tracking disabled");

    }

}
