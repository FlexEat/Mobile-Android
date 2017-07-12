package com.uw.fydp.flexeat.flexeat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.net.MalformedURLException;
import java.net.URL;

public class CheckInActivity extends AppCompatActivity {


    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        placeListener();
    }

    public void placeListener(){
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            LatLng locationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            int radius = 500;
                            String type = "restaurant";
                            String key = getResources().getString(R.string.google_api_key);

                            Log.d("Location", location.toString());
                            URL url = null;
                            try {
                                url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                                        + Double.toString(locationLatLng.latitude) + "," + Double.toString(locationLatLng.longitude)
                                        + "&radius=" + Integer.toString(radius) + "&type=" + type + "&key=" + key);
                                //HTTP REQUEST
                            }catch (MalformedURLException e){

                            }

                        }
                    }
                });
    }
}
