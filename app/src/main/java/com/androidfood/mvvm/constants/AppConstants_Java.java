package com.androidfood.mvvm.constants;

import android.util.Base64;


public class AppConstants_Java {

    static {
        System.loadLibrary("keys");
    }

    public native static String getBaseUAT();


    public static final String BASE_URL = new String(Base64.decode(getBaseUAT(), Base64.DEFAULT));  //..UAT

    public static String USERSDATA = "USERSDATA";
    public static String USERSRESDATA = "USERSRESDATA";


    public static double lat = 0.0;
    public static double lng = 0.0;
    public static String isLogin ;
    public static String isRestaurant;
}
