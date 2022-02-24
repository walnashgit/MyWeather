package test.sample.myweather.ui.main

import android.location.Location
import test.sample.myweather.base.Event

/**
 * Events for this app.
 */
sealed class AppEvents: Event {
    /**
     * Event for when location permission is granted.
     */
    object PermissionGranted: AppEvents()

    /**
     * Event for when location permission is denied.
     */
    object PermissionDenied: AppEvents()

    /**
     * Event and data when current location is fetched.
     */
    data class LocationFetched(val location: LocationModel): AppEvents()

    /**
     * Event for when location fetch fails.
     */
    object LocationFetchFailed: AppEvents()

    /**
     * Event when refresh of weather data is requested.
     */
    object RefreshWeatherInfo: AppEvents()
}

/**
 * Wrapper for location data.
 */
data class LocationModel(
    /**
     * Current location information. Contains latitude and longitude info.
     */
    val location: Location,
    /**
     * Name of the current location based on latitude and longitude.
     */
    val name: String
    )