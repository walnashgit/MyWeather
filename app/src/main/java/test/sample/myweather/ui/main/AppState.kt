package test.sample.myweather.ui.main

import test.sample.myweather.base.State
import test.sample.myweather.network.ApiResult

/**
 * States for this app
 */
sealed class AppState: State {
    /**
     * App state depicting when it's checking for GPS and location permission.
     */
    object CheckGPSAndLocationPermission : AppState()

    /**
     * App state depicting when location permission is granted.
     */
    object LocationPermissionGranted: AppState()

    /**
     * App state depicting when location permission is denied.
     */
    object LocationPermissionDenied: AppState()

    /**
     * App state depicting when location could not be obtained.
     */
    object LocationNotObtained: AppState()

    /**
     * App state depicting when weather api returns a result.
     */
    data class WeatherFetchResult<T>(val apiResult: ApiResult<T>): AppState()
}
