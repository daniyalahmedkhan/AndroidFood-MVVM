package com.androidfood.mvvm.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.google.android.gms.maps.model.LatLng;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;

public class JavaHelper {


    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("KeyHASH", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHASH", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("KeyHASH", "printHashKey()", e);
        }
    }


    public static String getAddress(Context context, Double lat, Double lng) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            return address;
        } catch (Exception e) {
            return "";
        }
    }


    public static String CompressPic(File file, Context context) throws IOException {

        File compressedImageFile = new Compressor(context).compressToFile(file);
        return compressedImageFile.getAbsolutePath();

    }


    public static LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address != null && address.size() > 0) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                return null;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return p1;
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getDateTimeSeconds() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String parseDateToFormat(String f, String dateStart) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm");
            SimpleDateFormat output = new SimpleDateFormat("HH:mm");
            Date d = sdf.parse(dateStart);
            // removing 5 hour
            long t = d.getTime();
            String formattedTime = output.format(t);
            return formattedTime;
        } catch (Exception e) {
            return null;
        }
    }

    public static  long dateTimeMilli(String date)
    {
        try
        {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return 0;
    }


    public static int calculateDistance(LatLng a , LatLng b){

        Location locationA = new Location("point A");
        locationA.setLatitude(a.latitude);
        locationA.setLongitude(a.longitude);

        Location locationB = new Location("point B");
        locationB.setLatitude(b.latitude);
        locationB.setLongitude(b.longitude);

        return (int) locationA.distanceTo(locationB) / 1000;

    }

}
