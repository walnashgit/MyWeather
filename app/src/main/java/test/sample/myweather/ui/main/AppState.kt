package test.sample.myweather.ui.main

import test.sample.myweather.network.ApiResult

sealed class AppState {
    object CheckingLocationPermission : AppState()
    object LocationPermissionGranted: AppState()
    object LocationPermissionDenied: AppState()
    object FetchingWeatherData: AppState()
    object LocationNotObtained: AppState()
    data class WeatherFetchResult<T>(val apiResult: ApiResult<T>): AppState()
}
