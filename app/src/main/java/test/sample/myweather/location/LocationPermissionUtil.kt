package test.sample.myweather.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.scopes.ActivityScoped
import test.sample.myweather.LOCATION_PERMISSIONS_REQUEST_CODE
import test.sample.myweather.R
import test.sample.myweather.base.BaseLocationPermissionUtil
import javax.inject.Inject

private const val TAG = "LocationPermissionUtil"

/**
 * Util class for checking and requesting location permission.
 */
@ActivityScoped
open class LocationPermissionUtil @Inject constructor(private val context: Activity): BaseLocationPermissionUtil {

    override fun checkLocationPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun requestLocationPermission() {
        if (showRationale()) {
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

    override fun showRationale(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestPermission() {
        Log.d(TAG, "Request location permission")
        ActivityCompat.requestPermissions(
            context,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSIONS_REQUEST_CODE
        )
    }

    override fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun showGPSNotEnabledDialog() {
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