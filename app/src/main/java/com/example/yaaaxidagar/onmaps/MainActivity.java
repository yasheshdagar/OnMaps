package com.example.yaaaxidagar.onmaps;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    static double latitude, longitude;
    GoogleMap googleMap;
    Marker marker;
    Geocoder geocoder;
    List<Address> addresses;
    MarkerOptions markerOptions;
    LatLng latLng;
    static String locality="Not Available";
    boolean mapReady=false;
    boolean centered=true;

    Button normalView,centre,hybridView;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.panorama:
                Intent street=new Intent(MainActivity.this,StreetPanorama.class);
                startActivity(street);
                break;

            case R.id.mylocation:
                Intent locationdisplay=new Intent(MainActivity.this, LocationsDisplay.class);
                startActivity(locationdisplay);
                break;

            case R.id.share:
                Intent share=new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT,"http://maps.google.com/maps?saddr=" +latitude+","+longitude);
                startActivity(share);
                break;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        Log.i("None","I am onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

        if(networkInfo==null || !networkInfo.isConnected()){
            Toast.makeText(MainActivity.this,"Turn on your internet",Toast.LENGTH_LONG).show();
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        normalView=(Button)findViewById(R.id.map);
        centre=(Button)findViewById(R.id.centre);
        hybridView=(Button)findViewById(R.id.hybrid);


        normalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mapReady) googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        centre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 10000, null);
            }
        });

        hybridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mapReady) googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        geocoder=new Geocoder(this);
        addresses=new ArrayList<>();

        MapFragment fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        fragment.getMapAsync(this);//tells the fragment to manage the map functionality

    }

    @Override
    public void onConnected(Bundle bundle) {

        Log.i("None","I am onConnected");

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,this);


    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu,menu);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

        if (marker != null) {
            marker.remove();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        latLng=new LatLng(latitude,longitude);

        try {
            addresses=geocoder.getFromLocation(latitude,longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(geocoder.isPresent()){
            locality=generateAddress();
        }

        if(centered){ //this code is to move the camera to locate the lat lng specified
            CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(14).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            centered=false;
        }

        markerOptions = new MarkerOptions().position(latLng).title(locality);
        marker = googleMap.addMarker(markerOptions);

    }

    private String generateAddress() {
        StringBuilder sb=new StringBuilder();
        Address s=addresses.get(0);
        sb.append(s.getAddressLine(0)+"\n");
        Log.i("MainActivity",s.getAddressLine(0));

        return sb.toString();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady=true;
        this.googleMap = googleMap;
    }

}