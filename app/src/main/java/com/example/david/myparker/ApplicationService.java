package com.example.david.myparker;

/**
 * Created by David on 6/17/2018.
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;



public class ApplicationService extends Service implements LocationListener {
    protected LocationManager locationManager;
    Location location;
    private static final long min_distance_forupdate = 10;
    private static final long min_time_to_update = 2 * 60 * 1000;

    public ApplicationService(Context context) {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);


    }

    public Location getLocation(String provider) {
        if (locationManager.isProviderEnabled(provider)) {

            try {

                locationManager.requestLocationUpdates(provider, min_time_to_update, min_distance_forupdate, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(provider);
                    return location;

                }
            }catch (SecurityException r){

                Log.d("loc",r.getMessage());
            }
            return location;




        }

        return location;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
