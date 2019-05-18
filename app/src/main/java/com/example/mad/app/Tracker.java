package com.example.mad.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by MAD on 19/03/2018.
 */

public class Tracker implements LocationListener{

    Context context;

    public Tracker(Context c) {
        context = c;
    }

    public Location localisation(){
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

         if(isGPSEnable){
             locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,6000,10,this);
             Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
             return location;
         } else {
             Toast.makeText(context, "Activer votre GPS", Toast.LENGTH_LONG).show();
         }

        return null;
    }
    
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
