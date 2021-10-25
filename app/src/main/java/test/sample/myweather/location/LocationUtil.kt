package test.sample.myweather.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.scopes.ActivityScoped
import test.sample.myweather.LOCATION_PERMISSIONS_REQUEST_CODE
import test.sample.myweather.R
import test.sample.myweather.ui.main.AppEvents
import javax.inject.Inject

private const val TAG = "LocationUtil"

/**
 * Util class for checking/requesting location permission and current location.
 */
@ActivityScoped
class LocationUtil @Inject constructor(private val context: Activity) {

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    /**
     * Checks for location permission and location (GPS) availability and gets user's current
     * location.
     */
    fun getCurrentLocation(locationResult: (event: AppEvents) -> Unit) {
        if (checkLocationPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { loc: Location? ->
                    if (loc == null) {
                        determineCurrentLocation(locationResult)
                    } else {
                        locationResult(AppEvents.LocationFetched(loc))
                    }
                }.addOnFailureListener {
                    determineCurrentLocation(locationResult)
                }
            } else {
                // show dialog to enable location
                locationResult(AppEvents.LocationFetchFailed)
                showGPSNotEnabledDialog()
            }
        } else {
            // location permission not granted, request for permission
            requestLocationPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun determineCurrentLocation(locationResult: (event: AppEvents) -> Unit) {
        fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { loc: Location? ->
            loc?.let {
                locationResult(AppEvents.LocationFetched(loc))
            } ?: locationResult(AppEvents.LocationFetchFailed)
        }.addOnFailureListener {
        }
        locationResult(AppEvents.LocationFetchFailed)
    }

    private fun checkLocationPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestLocationPermission() {
        val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (showRationale) {
            Snackbar.make(
                context.findViewById(R.id.main),
                R.string.permission_rationale,
                Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.ok) {
                requestPermission()
            }
                .show()
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        Log.d(TAG, "Request location permission")
        ActivityCompat.requestPermissions(
            context,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showGPSNotEnabledDialog() {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.enable_gps))
            .setMessage(context.getString(R.string.required_for_this_app))
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.enable_now)) { _, _ ->
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }
}