package test.sample.myweather.ui.main

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import test.sample.myweather.data.current.CurrentWeather
import test.sample.myweather.network.ApiResult
import test.sample.myweather.network.WeatherRepository
import test.sample.myweather.ui.main.AppEvents.LocationFetched
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (private val repository: WeatherRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<AppState> = MutableStateFlow(AppState.CheckingLocationPermission)
    val uiState = _uiState.asStateFlow()

    private val _event : MutableSharedFlow<AppEvents> = MutableSharedFlow()
    private val event = _event.asSharedFlow()

    init {
        subscribeEvent()
    }

    private fun getCurrentLocationWeather(location: Location?) {
        setUiState(AppState.FetchingWeatherData)
        location?.let {
            getWeatherData(it.latitude.toString(), it.longitude.toString())
        } ?: run {
            setUiState(AppState.LocationNotObtained)
        }
    }

    /**
     * Fetches the current weather data for the location passed
     */
    private fun getWeatherData(lat: String, long: String){
        viewModelScope.launch {
            val weatherData = repository.getCurrentWeatherData(lat, long)
            setUiState(AppState.WeatherFetchResult(weatherData))
        }
    }

    /**
     * Fetches the current and forecast weather data for the location passed.
     */
    private fun getWeatherForecast(lat: String, long: String) {
        viewModelScope.launch {
            val weatherData = repository.getWeatherForecast(lat, long)
            setUiState(AppState.WeatherFetchResult(weatherData))
        }
    }

    private fun setUiState(newState: AppState) {
        _uiState.value = newState
    }

    fun setEvent(newEvent: AppEvents) {
        viewModelScope.launch {
            _event.emit(newEvent)
        }
    }

    /**
     * Listen to app events.
     */
    private fun subscribeEvent() {
        viewModelScope.launch {
            event.collect {
                when (it) {
                    AppEvents.PermissionGranted -> {
                        setUiState(AppState.LocationPermissionGranted)
                    }
                    AppEvents.PermissionDenied -> {
                        setUiState(AppState.LocationPermissionDenied)
                    }
                    AppEvents.LocationFetchFailed -> {
                        setUiState(AppState.LocationNotObtained)
                    }
                    is LocationFetched -> {
                        getCurrentLocationWeather(it.location)
                    }
                    AppEvents.RefreshWeatherInfo -> {
                        setUiState(AppState.CheckingLocationPermission)
                    }
                }
            }
        }
    }
}