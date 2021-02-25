package com.androidfood.mvvm.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.androidfood.mvvm.constants.AppConstants_Java;

import java.util.List;

public class LocationHelper implements LocationListener {

    private static LocationManager locationManager;
    private static List<String> provider;


    public void LocationInitialize(Context context) {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        provider = locationManager.getProviders(true);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        Location bestLocation = null;
        for (String provider : provider) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;

                double lat =  (bestLocation.getLatitude());
                double lng =  (bestLocation.getLongitude());


                lat = Double.parseDouble((String.format("%.3f",lat)));
                lng = Double.parseDouble((String.format("%.3f",lng)));


                Log.d("LATLNG" , String.valueOf(lat +" "+lng));

                AppConstants_Java.lat = lat;
                AppConstants_Java.lng = lng;

                return;

            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
//        double lat =  (location.getLatitude());
//        double lng =  (location.getLongitude());
//
//
//        lat = Double.parseDouble((String.format("%.3f",lat)));
//        lng = Double.parseDouble((String.format("%.3f",lng)));
//
//
//        Log.d("LATLNG" , String.valueOf(lat +" "+lng));
//
//        PrefsHelper.putDouble(PrefConstants.LAT , lat);
//        PrefsHelper.putDouble(PrefConstants.LNG , lng);
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
