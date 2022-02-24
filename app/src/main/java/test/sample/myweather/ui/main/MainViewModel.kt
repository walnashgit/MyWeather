package test.sample.myweather.ui.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import test.sample.myweather.DispatcherProvider
import test.sample.myweather.data.onecall.OneCall
import test.sample.myweather.network.ApiResult
import test.sample.myweather.network.WeatherRepositoryOneCallBase
import test.sample.myweather.base.BaseViewModel
import test.sample.myweather.ui.main.AppEvents.LocationFetched
import javax.inject.Inject

private const val TAG = "MainViewModel"

/**
 * ViewModel class for handling app's state and events.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: @JvmSuppressWildcards WeatherRepositoryOneCallBase,
    private val dispatcher: DispatcherProvider
) :
    BaseViewModel<AppState, AppEvents>(dispatcher) {

    private val _weatherData: MutableStateFlow<OneCall?> = MutableStateFlow(null)
    val weatherData = _weatherData.asStateFlow()

    private val _errorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _locationName: MutableStateFlow<String> = MutableStateFlow("")
    val locationName = _locationName.asStateFlow()


    override fun createInitialState(): AppState {
        return AppState.CheckGPSAndLocationPermission
    }

    override fun handleEvent(events: AppEvents) {
        when (events) {
            AppEvents.PermissionGranted -> {
                setAppState(AppState.LocationPermissionGranted)
            }
            AppEvents.PermissionDenied -> {
                setAppState(AppState.LocationPermissionDenied)
            }
            AppEvents.LocationFetchFailed -> {
                setAppState(AppState.LocationNotObtained)
                _errorMessage.value = "Location could not be determined."
            }
            is LocationFetched -> {
                getCurrentLocationWeather(events.location)
                _locationName.value = events.location.name
            }
            AppEvents.RefreshWeatherInfo -> {
                _weatherData.value = null
                _errorMessage.value = ""
                setAppState(createInitialState())
            }
        }
    }

    private fun getCurrentLocationWeather(location: LocationModel) {
        getWeatherForecast(
            location.location.latitude.toString(),
            location.location.longitude.toString()
        )
    }

    private fun getWeatherForecast(lat: String, long: String) {
        viewModelScope.launch(dispatcher.main) {
            val weatherData = repository.getWeatherData(lat, long)
            handleWeatherData(weatherData)
        }
    }

    /**
     * Handle result of weather api.
     */
    private fun <T> handleWeatherData(result: ApiResult<T>) {
        when (result) {
            is ApiResult.Error -> {
                Log.e(TAG, "Error while getting weather data: ${result.message}")
                _errorMessage.value = result.message
            }
            is ApiResult.Success -> {
                when (result.data) {
                    is OneCall -> {
                        Log.d(TAG, "Weather forecast data fetched.")
                        _weatherData.value = result.data
                    }
                }
            }
        }
        setAppState(AppState.WeatherFetchResult(result))
    }

    /**
     * Handle the refresh click event from UI.
     */
    fun onRefreshClicked() {
        setEvent(AppEvents.RefreshWeatherInfo)
    }
}