package test.sample.myweather.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import test.sample.myweather.base.BaseLocationUtil
import test.sample.myweather.ui.main.AppEvents
import test.sample.myweather.ui.main.LocationModel
import java.util.*
import javax.inject.Inject

private const val TAG = "LocationUtil"

/**
 * Util class for requesting current location.
 */
@ActivityScoped
open class LocationUtil @Inject constructor(@ApplicationContext private val context: Context) :
    BaseLocationUtil {

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(locationResult: (event: AppEvents) -> Unit) {

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { loc: Location? ->
            if (loc == null) {
                determineCurrentLocation(locationResult)
            } else {
                val name = getLocationName(loc.latitude, loc.longitude)
                locationResult(AppEvents.LocationFetched(LocationModel(loc, name)))
            }
        }.addOnFailureListener {
            determineCurrentLocation(locationResult)
        }
    }

    @SuppressLint("MissingPermission")
    private fun determineCurrentLocation(locationResult: (event: AppEvents) -> Unit) {
        fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { loc: Location? ->
            loc?.let {
                val name = getLocationName(it.latitude, it.longitude)
                locationResult(AppEvents.LocationFetched(LocationModel(it, name)))
            } ?: locationResult(AppEvents.LocationFetchFailed)
        }.addOnFailureListener {
            Log.e(TAG, "Location fetch failed.", it)
            locationResult(AppEvents.LocationFetchFailed)
        }
    }

    private fun getLocationName(lat: Double, long: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address = geocoder.getFromLocation(lat, long, 1)
        return address[0].subLocality
    }
}