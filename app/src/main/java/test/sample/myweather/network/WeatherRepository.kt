package test.sample.myweather.network

import dagger.hilt.android.scopes.ViewModelScoped
import test.sample.myweather.base.BaseWeatherRepository
import test.sample.myweather.data.onecall.OneCall
import javax.inject.Inject

typealias WeatherRepositoryOneCallBase = BaseWeatherRepository<ApiResult<OneCall>>

/**
 * Repository for Fetching weather related data from [WeatherApi].
 */
@ViewModelScoped
class WeatherRepository @Inject constructor (private val weatherApi: WeatherApi): WeatherRepositoryOneCallBase {

    override suspend fun getWeatherData(lat: String, long: String): ApiResult<OneCall> {
        return getWeatherForecast(lat, long)
    }

    /**
     * Calls the weather api to fetch current and forecast weather data of the location
     * determined by latitude [lat] and longitude [long] passed.
     */
    private suspend fun getWeatherForecast(lat: String, long: String): ApiResult<OneCall> {
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
