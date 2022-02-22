package test.sample.myweather.ui.main

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import test.sample.myweather.data.current.CurrentWeather
import test.sample.myweather.data.onecall.OneCall
import test.sample.myweather.network.ApiResult
import test.sample.myweather.network.WeatherRepository
import test.sample.myweather.ui.main.AppEvents.LocationFetched
import javax.inject.Inject

private const val TAG = "MainViewModel"
@HiltViewModel
class MainViewModel @Inject constructor (private val repository: WeatherRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<AppState> = MutableStateFlow(AppState.CheckingLocationPermission)
    val uiState = _uiState.asStateFlow()

    private val _event : MutableSharedFlow<AppEvents> = MutableSharedFlow()
    private val event = _event.asSharedFlow()

    private val _currentData: MutableLiveData<CurrentData> = MutableLiveData()
    val currentData: LiveData<CurrentData> = _currentData

    private val _hourlyData: MutableLiveData<HourlyData> = MutableLiveData()
    val hourlyData: LiveData<HourlyData> = _hourlyData

    private val _showError: MutableLiveData<ShowError> = MutableLiveData()
    val showError: LiveData<ShowError> = _showError

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
//            setUiState(AppState.WeatherFetchResult(weatherData))
            handleWeatherData(weatherData)
            getWeatherForecast(lat, long)
        }
    }

    /**
     * Fetches the current and forecast weather data for the location passed.
     */
    private suspend fun getWeatherForecast(lat: String, long: String) {
        val weatherData = repository.getWeatherForecast(lat, long)
        handleWeatherData(weatherData)
    }

    /**
     * Handle result of weather api.
     */
    private fun <T> handleWeatherData(result: ApiResult<T>) {
        when (result) {
            is ApiResult.Error -> {
                Log.e(TAG, "Error while getting weather data: ${result.message}")
                _showError.value = ShowError(result.message, true)
            }
            is ApiResult.Loading -> {
                Log.d(TAG, "Loading weather data")

            }
            is ApiResult.Success -> {
                when (result.data) {
                    is CurrentWeather -> {
                        Log.d(TAG, "Weather data fetched: ${result.data.weather[0].getDescription()}")
                        _currentData.value = CurrentData(result.data)
                    }
                    is OneCall -> {
                        Log.d(TAG, "Weather forecast data fetched: ${result.data.timezone}")
                        _hourlyData.value = HourlyData(result.data.getHourlyData())
                    }
                }
            }
        }
    }

//    private fun createHourDataSnapShot(oneCall: OneCall) {
//        val hourlySnapShot = oneCall.hourly
//        setUiState(AppState.WeatherFetchResult(ApiResult.Success(oneCall.hourly)))
//    }

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