package test.sample.myweather.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import test.sample.myweather.data.onecall.OneCall

/**
 * API definition class.
 *
 * https://openweathermap.org/current
 */
interface WeatherApi : Api {

    @GET("onecall?units=metric&appid=APIKEY_HERE")
    suspend fun getWeatherForecast(
        @Query("lat") lat: String,
        @Query("lon") long: String
    ): Response<OneCall>
}