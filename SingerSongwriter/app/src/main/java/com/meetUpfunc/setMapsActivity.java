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

public class setMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    static Context myContext;
    private TextView locationText;
    private Button addLocation;
    private GoogleMap mMap;
    LocationManager myLocationManager;
    GPSClass myGPS;
    private double currentLatitude;
    private double currentLongtitude;
    private double centerLatitude;
    private double centerLongtitude;
    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS=14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        myContext=getApplicationContext();
        myGPS= new GPSClass();
        locationText = (TextView) findViewById(R.id.location);
        addLocation = (Button) findViewById(R.id.addLocation);

        if (ContextCompat.checkSelfPermission(setMapsActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(setMapsActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(setMapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }

        myLocationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, myGPS); //start chcecking
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                addMaplog("" + currentLatitude, "" + currentLongtitude);

            }
        });


    }

    public static Context getContext(){return myContext;}
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ContextCompat.checkSelfPermission(setMapsActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(setMapsActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(setMapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }

        LatLng sydney = new LatLng(37.5595, 127.0887);
        CameraPosition cp = new CameraPosition.Builder().target((sydney)).zoom(15).build();
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
    }

    public void addMaplog(String latitude, String longtitude) {

//        showAlert(latitude, longtitude);

        LatLng myPosition = mMap.getCameraPosition().target;
//        Toast.makeText(getApplicationContext(),""+myPosition.latitude+" : "+myPosition.longitude,Toast.LENGTH_LONG).show();
        Intent intent = getIntent();
        intent.putExtra("place_lati",""+myPosition.latitude);
        intent.putExtra("place_longti",""+myPosition.longitude);
        setResult(2, intent);
        finish();

    }

    public void showAlert(String latitude, String longtitude )
    {

        final String curLati = latitude;
        final String curLonti = longtitude;
        AlertDialog.Builder alert = new AlertDialog.Builder(setMapsActivity.this);
        alert.setTitle("Log");
        alert.setMessage("위치 제목을 입력하세요.");
        // Set an EditText view to get user input
        final EditText input = new EditText(setMapsActivity.this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String logName = input.getText().toString();
                if (logName.equals("")) {
                    Toast.makeText(getApplicationContext(), "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else
                {
                }
            }
        });
        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
        alert.show();
    }

    class GPSClass implements LocationListener {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
//        Log.i("Message: ","Location changed, " + location.getAccuracy() + " , " + location.getLatitude()+ "," + location.getLongitude());
            currentLatitude=location.getLatitude();
            currentLongtitude = location.getLongitude();
            locationText.setText(" " + currentLatitude + " : " + currentLongtitude);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}
        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}

    }
}