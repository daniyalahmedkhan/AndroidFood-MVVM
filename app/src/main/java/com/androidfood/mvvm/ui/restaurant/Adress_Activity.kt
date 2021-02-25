package com.androidfood.mvvm.ui.restaurant

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.androidfood.mvvm.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.androidfood.mvvm.helper.JavaHelper
import kotlinx.android.synthetic.main.address_capture_fragment.*
import org.jetbrains.anko.toast


class Adress_Activity : AppCompatActivity(), OnMapReadyCallback,
    LocationSource.OnLocationChangedListener, LocationListener {

    lateinit var currentLocation: LatLng
    private lateinit var mMapGoogleFragment: SupportMapFragment
    private lateinit var mGoogleMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.address_capture_fragment)


        IV_done.setOnClickListener {
            if (address.text.toString().isEmpty()) {
                toast("Address field is empty")
            } else {
                val resultIntent = Intent()
                resultIntent.putExtra("ADDRESS", address.text.toString())
                resultIntent.putExtra("LATLNG", currentLocation)
                resultIntent.putExtra("LAT", currentLocation.latitude)
                resultIntent.putExtra("LNG", currentLocation.longitude)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }


        address.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                JavaHelper.hideKeyboard(this)
                val loc = JavaHelper.getLocationFromAddress(this, address.text.toString().trim())
                if (loc != null) {
                    moveGoogleMap(loc)
                } else {
                    toast("Not Found, Please use widely known places")
                }

                //address.setText(JavaHelper.getAddress(this , loc.latitude , loc.longitude))
                true
            } else false
        })


        //bindGoogleFusedLocationClient()
        bindGoogleMap()
    }

    private fun bindGoogleMap() {
        mMapGoogleFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mMapGoogleFragment.getMapAsync(this)
    }


    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map!!
        configMap()

        mGoogleMap.setOnCameraIdleListener(GoogleMap.OnCameraIdleListener {
            currentLocation = mGoogleMap.getCameraPosition().target
            Log.d("LOCATION", "${currentLocation.latitude},${currentLocation.longitude}")
            address.setText(
                JavaHelper.getAddress(
                    this,
                    currentLocation.latitude,
                    currentLocation.longitude
                )
            )
        })

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()

            val location = locationManager.getLastKnownLocation(
                locationManager.getBestProvider(
                    criteria,
                    false
                )!!
            )
            if (location != null) {
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            location.latitude,
                            location.longitude
                        ), 13f
                    )
                )
                val cameraPosition = CameraPosition.Builder()
                    .target(
                        LatLng(
                            location.latitude,
                            location.longitude
                        )
                    ) // Sets the center of the map to location user
                    .zoom(14f) // Sets the zoom
                    .bearing(50f) // Sets the orientation of the camera to east
                    .tilt(20f) // Sets the tilt of the camera to 30 degrees
                    .build() // Creates a CameraPosition from the builder
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }

        }

    }

    private fun configMap() {
        //mGoogleMap.setMapStyle(MapStyling.styleMap())
        mGoogleMap.isMyLocationEnabled = true
        mGoogleMap.uiSettings.isMyLocationButtonEnabled = true

        val locationButton =
            (mMapGoogleFragment.view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(
                Integer.parseInt("2")
            )
        val rlp = locationButton.layoutParams as (RelativeLayout.LayoutParams)
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 0, 200);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mGoogleMap.isMyLocationEnabled = true
        }
    }

    private fun moveGoogleMap(latLng: LatLng) {

        currentLocation = latLng
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    override fun onLocationChanged(location: Location) {
        Log.d("loca", "${location?.latitude} , ${location?.longitude}")
    }


}