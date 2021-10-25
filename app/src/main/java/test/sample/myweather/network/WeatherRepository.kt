package test.sample.myweather.network

import dagger.hilt.android.scopes.ActivityRetainedScoped
import test.sample.myweather.data.current.CurrentWeather
import test.sample.myweather.data.onecall.OneCall
import javax.inject.Inject

@ActivityRetainedScoped
class WeatherRepository @Inject constructor (private val weatherApi: WeatherApi) {

    /**
     * Calls the weather api to fetch current weather data of the location determined by
     * latitude [lat] and longitude [long] passed.
     */
    suspend fun getCurrentWeatherData(lat: String, long: String): ApiResult<CurrentWeather> {
        try {
            val response = weatherApi.getCurrentWeatherData(lat, long)
            if (response.isSuccessful) {
                response.body()?.let {
                    return ApiResult.Success(it)
                }
            }
            return getErrorResult("${response.message()} : ${response.code()}")
        } catch (e: Exception) {
            return getErrorResult(e.message ?: e.toString())
        }
    }

    /**
     * Calls the weather api to fetch current and forecast weather data of the location
     * determined by latitude [lat] and longitude [long] passed.
     */
    suspend fun getWeatherForecast(lat: String, long: String): ApiResult<OneCall> {
        try {
            val response = weatherApi.getWeatherForecast(lat, long)
            if (response.isSuccessful) {
                response.body()?.let {
                    return ApiResult.Success(it)
                }
            }
            return getErrorResult("${response.message()} : ${response.code()}")
        } catch (e: Exception) {
            return getErrorResult(e.message ?: e.toString())
        }
    }

    private fun <T>getErrorResult(message: String): ApiResult<T> {
        return ApiResult.Error(message = "Call to API failed: $message")
    }
}
