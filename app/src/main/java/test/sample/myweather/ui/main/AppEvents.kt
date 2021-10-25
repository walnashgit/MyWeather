package test.sample.myweather.ui.main

import android.location.Location

sealed class AppEvents {
    object PermissionGranted: AppEvents()
    object PermissionDenied: AppEvents()
    data class LocationFetched(val location: Location): AppEvents()
    object LocationFetchFailed: AppEvents()
    object RefreshWeatherInfo: AppEvents()
}
