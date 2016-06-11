package com.meetUpfunc;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.mysampleapp.R;

public class getMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    static Context myContext;
    private TextView locationText;
    private Button addLocation;
    private GoogleMap mMap;
    LocationManager myLocationManager;
    GPSClass myGPS;
    private double currentLatitude;
    private double currentLongtitude;
    private String centerLatitude;
    private String centerLongtitude;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS=14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.getmap);
        Intent intent = getIntent();
        centerLatitude=intent.getStringExtra("place_lati");
        centerLongtitude= intent.getStringExtra("place_longi");

        mapFragment.getMapAsync(this);
        myContext=getApplicationContext();
        myGPS= new GPSClass();

        if (ContextCompat.checkSelfPermission(getMapsActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getMapsActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(getMapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }

        myLocationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, myGPS); //start chcecking


    }

    public static Context getContext(){return myContext;}
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ContextCompat.checkSelfPermission(getMapsActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getMapsActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(getMapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }

        LatLng center = new LatLng(Double.parseDouble(centerLatitude), Double.parseDouble(centerLongtitude));
        CameraPosition cp = new CameraPosition.Builder().target((center)).zoom(15).build();
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15));
    }

    class GPSClass implements LocationListener {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
//        Log.i("Message: ","Location changed, " + location.getAccuracy() + " , " + location.getLatitude()+ "," + location.getLongitude());
            currentLatitude=location.getLatitude();
            currentLongtitude = location.getLongitude();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}

    }
    @Override
    public void onBackPressed()
    {
        Intent backtoComment = new Intent();
        setResult(RESULT_OK, backtoComment);
        finish();
    }

}
